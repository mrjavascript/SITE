package org.sitenv.portlets.client;

import gov.hhs.onc.pdti.ws.api.AttributeValueAssertion;
import gov.hhs.onc.pdti.ws.api.BatchRequest;
import gov.hhs.onc.pdti.ws.api.BatchResponse;
import gov.hhs.onc.pdti.ws.api.DsmlAttr;
import gov.hhs.onc.pdti.ws.api.ErrorResponse;
import gov.hhs.onc.pdti.ws.api.Filter;
import gov.hhs.onc.pdti.ws.api.ProviderInformationDirectoryService;
import gov.hhs.onc.pdti.ws.api.SearchRequest;
import gov.hhs.onc.pdti.ws.api.SearchResponse;
import gov.hhs.onc.pdti.ws.api.SearchResultEntry;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusError;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusProviderInformationDirectoryService;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusRequest;
import gov.hhs.onc.pdti.ws.api.hpdplus.HpdPlusResponse;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.UUID;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;
import javax.portlet.PortletException;
import javax.portlet.PortletSession;
import javax.xml.bind.JAXBElement;
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceException;

import org.apache.commons.lang3.ArrayUtils;

import com.liferay.util.bridges.mvc.MVCPortlet;

public class ProviderDirectoryClientPortlet extends MVCPortlet {
    private static final String OU = "ou=";
    private static final String COMMA = ",";
    private static final String SINGLE_LEVEL = "singleLevel";
    private static final String DEREF_FINDING_BASE_OBJ = "derefFindingBaseObj";
    private static final String NO_ID = "NO ID (DSML DIRECTORY)";
    private static final String HPDPLUS_RESPONSE_ITEM_TYPE_ERROR_MESSAGE_PREAMBLE = "Unknown HPDPlus response item type: ";
    private static final String JAXB_TYPE_ERROR_MESSAGE_PREAMBLE = "Unknown JAXBElement value: ";
    private static final String PERIOD = ".";

    private String baseDn;
    private String searchType;
    private String searchAttribute;
    private String searchString;
    private List<String> searchErrorMessages = new ArrayList<String>();
    private SearchResultWrapper[] searchResults = new SearchResultWrapper[0];

    private gov.hhs.onc.pdti.ws.api.ObjectFactory dsmlBasedObjectFactory = new gov.hhs.onc.pdti.ws.api.ObjectFactory();
    private gov.hhs.onc.pdti.ws.api.hpdplus.ObjectFactory hpdPlusObjectFactory =
            new gov.hhs.onc.pdti.ws.api.hpdplus.ObjectFactory();

    private void doHpdPlusSearch(ActionRequest actionRequest, URL wsdlUrl) {
        try {
            HpdPlusProviderInformationDirectoryService hpdPlusProviderInformationDirectoryService =
                    new HpdPlusProviderInformationDirectoryService(wsdlUrl, new QName ("urn:gov:hhs:onc:hpdplus:2013", "Hpd_Plus_ProviderInformationDirectory_Service"));
            HpdPlusResponse hpdPlusResponse = hpdPlusProviderInformationDirectoryService
                    .getHpdPlusProviderInformationDirectoryPortSoap()
                    .hpdPlusProviderInformationQueryRequest(buildHpdPlusRequest());
            hpdPlusResponse.setDirectoryUri(wsdlUrl.toString());
            processHpdPlusResponse(actionRequest, hpdPlusResponse);
        } catch (WebServiceException excpt) {
            searchErrorMessages.add(excpt.getMessage());
    	}
    }
    
    private void processHpdPlusResponse(ActionRequest actionRequest, HpdPlusResponse hpdPlusResponse) {
        List<HpdPlusError> hpdPlusErrors = hpdPlusResponse.getErrors();
        if(null != hpdPlusErrors && hpdPlusErrors.size() > 0) {
            for(HpdPlusError hpdPlusError : hpdPlusErrors) {
                searchErrorMessages.add(hpdPlusError.getMessage());
            }
        }
        List<Object> responseItems = hpdPlusResponse.getResponseItems();
        for(Object object : responseItems) {
            if(object instanceof BatchResponse) {
                processBatchResponse(actionRequest, hpdPlusResponse.getDirectoryId(), hpdPlusResponse.getDirectoryUri(), (BatchResponse)object);
            } else if(object instanceof HpdPlusResponse) {
                processHpdPlusResponse(actionRequest, (HpdPlusResponse)object);
            } else {
                searchErrorMessages.add(HPDPLUS_RESPONSE_ITEM_TYPE_ERROR_MESSAGE_PREAMBLE + object.getClass().getName());
            }
        }
    }

    private HpdPlusRequest buildHpdPlusRequest() {
        HpdPlusRequest hpdPlusRequest = hpdPlusObjectFactory.createHpdPlusRequest();
        //if(Boolean.TRUE.equals(isShowDetails())) {
            hpdPlusRequest.setRequestId(UUID.randomUUID().toString());
        //} else {
        //    hpdPlusRequest.setRequestId(requestId);
        //}
        hpdPlusRequest.setBatchRequest(buildBatchRequest(true));
        return hpdPlusRequest;
    }
    
    private void processBatchResponse(ActionRequest actionRequest, String directoryId, String directoryUri, BatchResponse batchResponse) {
        List<JAXBElement<?>> batchResponseJAXBElements = batchResponse.getBatchResponses();
        for (JAXBElement<?> batchResponseJAXBElement : batchResponseJAXBElements) {
            Object value = batchResponseJAXBElement.getValue();
            if (value instanceof SearchResponse) {
                SearchResponse searchResponse = (SearchResponse) value;
                if (null != searchResponse.getSearchResultDone().getErrorMessage()) {
                    searchErrorMessages.add(searchResponse.getSearchResultDone().getErrorMessage());
                } else {
                    processSearchResultEntries(actionRequest, directoryId, directoryUri, searchResponse.getSearchResultEntry());
                }
            } else if (value instanceof ErrorResponse) {
                ErrorResponse errorResponse = (ErrorResponse) value;
                searchErrorMessages.add(errorResponse.getMessage());
            } else {
                String errorMessage = JAXB_TYPE_ERROR_MESSAGE_PREAMBLE + value.getClass() + PERIOD;
                searchErrorMessages.add(errorMessage);
            }
        }
    }

    private BatchRequest buildBatchRequest(boolean isHpdPlusRequest) {
        BatchRequest batchRequest = dsmlBasedObjectFactory.createBatchRequest();
        SearchRequest searchRequest = dsmlBasedObjectFactory.createSearchRequest();
        /*if(!isHpdPlusRequest) {
            if (!StringUtils.isEmpty(requestId)) {
                batchRequest.setRequestId(requestId);
                searchRequest.setRequestId(requestId);
            }
        }*/
        searchRequest.setDn(OU + searchType + COMMA + baseDn);
        searchRequest.setScope(SINGLE_LEVEL);
        searchRequest.setDerefAliases(DEREF_FINDING_BASE_OBJ);
        Filter filter = dsmlBasedObjectFactory.createFilter();
        AttributeValueAssertion attributeValueAssertion = dsmlBasedObjectFactory.createAttributeValueAssertion();
        attributeValueAssertion.setName(searchAttribute);
        attributeValueAssertion.setValue(searchString);
        filter.setEqualityMatch(attributeValueAssertion);
        // AttributeDescriptions attributeDescriptions = dsmlBasedObjectFactory.createAttributeDescriptions();
        // for (String attribute : attributesToRetrieve) {
        // AttributeDescription attributeDescription = dsmlBasedObjectFactory.createAttributeDescription();
        // attributeDescription.setName(attribute);
        // attributeDescriptions.getAttribute().add(attributeDescription);
        // }
        // searchRequest.setAttributes(attributeDescriptions);
        searchRequest.setFilter(filter);
        batchRequest.getBatchRequests().add(searchRequest);
        return batchRequest;
    }

    private void doDsmlSearch(ActionRequest actionRequest, URL wsdlUrl) {
    	try {
            ProviderInformationDirectoryService providerInformationDirectoryService =
                    new ProviderInformationDirectoryService(wsdlUrl, new QName("urn:ihe:iti:hpd:2010", "ProviderInformationDirectory_Service"));
            BatchResponse batchResponse = providerInformationDirectoryService.getProviderInformationDirectoryPortSoap()
                    .providerInformationQueryRequest(buildBatchRequest(false));
            List<JAXBElement<?>> batchResponseJAXBElements = batchResponse.getBatchResponses();
            for (JAXBElement<?> batchResponseJAXBElement : batchResponseJAXBElements) {
                Object value = batchResponseJAXBElement.getValue();
                if (value instanceof SearchResponse) {
                    SearchResponse searchResponse = (SearchResponse) value;
                    if (null != searchResponse.getSearchResultDone().getErrorMessage()) {
                        searchErrorMessages.add(searchResponse.getSearchResultDone().getErrorMessage());
                    } else {
                        processSearchResultEntries(actionRequest, NO_ID, wsdlUrl.toExternalForm(), searchResponse.getSearchResultEntry());
                    }
                } else if (value instanceof ErrorResponse) {
                    ErrorResponse errorResponse = (ErrorResponse) value;
                    searchErrorMessages.add(errorResponse.getMessage());
                } else {
                    String errorMessage = JAXB_TYPE_ERROR_MESSAGE_PREAMBLE + value.getClass() + PERIOD;
                    searchErrorMessages.add(errorMessage);
                }
            }
        } catch (WebServiceException excpt) {
            searchErrorMessages.add(excpt.getMessage());
    	}
    }

    private void processSearchResultEntries(ActionRequest actionRequest, String directoryId, String directoryUri, List<SearchResultEntry> searchResultEntries) {
        SearchResultWrapper[] newSearchResults = new SearchResultWrapper[searchResultEntries.size()];
        int index = 0;
        for (SearchResultEntry searchResultEntry : searchResultEntries) {
            SearchResultWrapper searchResultWrapper = new SearchResultWrapper();
            searchResultWrapper.setDn(searchResultEntry.getDn());
            searchResultWrapper.setDirectoryId(directoryId);
            searchResultWrapper.setDirectoryUri(directoryUri);
            List<DsmlAttr> dsmlAttributes = searchResultEntry.getAttr();
            Map<String, List<String>> attributesMap = new TreeMap<String, List<String>>();
            for (DsmlAttr dsmlAttribute : dsmlAttributes) {
                String attributeName = dsmlAttribute.getName();
                List<String> attributeValues = new ArrayList<String>();
                for (String attributeValue : dsmlAttribute.getValue()) {
                    attributeValues.add(attributeValue);
                }
                attributesMap.put(attributeName, attributeValues);
            }
            searchResultWrapper.setAttributes(attributesMap);
            newSearchResults[index] = searchResultWrapper;
            index++;
            /*if (Boolean.TRUE.equals(showDetails)) {
                if (dn.equals(searchResultEntry.getDn())) {
                    searchResultToDetail = searchResultWrapper;
                    searchResults = null;
                    break;
                }
            }*/
        }
		
        searchResults = ArrayUtils.addAll(searchResults, newSearchResults);
    }

    @Override
    public void processAction(ActionRequest actionRequest, ActionResponse actionResponse)
    		throws IOException, PortletException {
   	

        baseDn = null;
        searchType = null;
        searchAttribute = null;
        searchString = null;
        searchErrorMessages = new ArrayList<String>();
        searchResults = new SearchResultWrapper[0];

		String wsdlUrl = actionRequest.getParameter("endpointUrl");
		if (wsdlUrl == null || wsdlUrl.equals("")) {
    		PortletSession session = actionRequest.getPortletSession();
    	    session.setAttribute("LIFERAY_SHARED_testStatus", "FAILED", PortletSession.APPLICATION_SCOPE);
    	    List<String> results = new ArrayList<String> ();
    	    results.add("You must provide a WSDL URL");
    	    session.setAttribute("LIFERAY_SHARED_whichPage", "error", PortletSession.APPLICATION_SCOPE);
    	    session.setAttribute("LIFERAY_SHARED_resultList", results, PortletSession.APPLICATION_SCOPE);
    	    return;
		}
		
		baseDn = actionRequest.getParameter("baseDn");
		if (baseDn == null || baseDn.equals("")) {
    		PortletSession session = actionRequest.getPortletSession();
    	    session.setAttribute("LIFERAY_SHARED_testStatus", "FAILED", PortletSession.APPLICATION_SCOPE);
    	    List<String> results = new ArrayList<String> ();
    	    results.add("You must provide an base DN");
    	    session.setAttribute("LIFERAY_SHARED_whichPage", "error", PortletSession.APPLICATION_SCOPE);
    	    session.setAttribute("LIFERAY_SHARED_resultList", results, PortletSession.APPLICATION_SCOPE);
    	    return;
		}
		
		searchAttribute = actionRequest.getParameter("searchAttribute");
		searchString = actionRequest.getParameter("searchString");
		
		searchType = actionRequest.getParameter("searchType");
		
		String wsdl = actionRequest.getParameter("wsdl");
		if (wsdl.equals("modSpec")) {
            doDsmlSearch(actionRequest, new URL(wsdlUrl));
		} else if (wsdl.equals("ihehpd")) {
            doHpdPlusSearch(actionRequest, new URL(wsdlUrl));
		} else {
			//TODO: add this wsdl type
		}
		
        Map<String, Map<String, String>> resultHeaders = new HashMap<String, Map<String, String>> ();
        Map<String, Map<String, List<String>>> results = new HashMap<String, Map<String, List<String>>> ();
        for (SearchResultWrapper wrapper : searchResults) {
        	Map<String, String> res = new HashMap<String, String> ();
        	res.put("directory_id", wrapper.getDirectoryId());
        	res.put("directory_uri", wrapper.getDirectoryUri());
        	res.put("dn", wrapper.getDn());
        	
        	String id = UUID.randomUUID().toString();
        	resultHeaders.put(id, res);
       	    results.put(id, wrapper.getAttributes());
        }
        
        PortletSession session = actionRequest.getPortletSession();
	    session.setAttribute("LIFERAY_SHARED_whichPage", "client", PortletSession.APPLICATION_SCOPE);
	    session.setAttribute("LIFERAY_SHARED_searchResultHeaders", resultHeaders, PortletSession.APPLICATION_SCOPE);
	    session.setAttribute("LIFERAY_SHARED_searchResults", results, PortletSession.APPLICATION_SCOPE);
	    session.setAttribute("LIFERAY_SHARED_searchErrors", searchErrorMessages, PortletSession.APPLICATION_SCOPE);
	}
}
