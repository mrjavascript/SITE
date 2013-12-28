package org.sitenv.portlets.qrda.models;

import static com.google.common.collect.Iterators.transform;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.portlet.filter.ActionRequestWrapper;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.portlet.multipart.MultipartActionRequest;

import com.google.common.base.Function;
import com.liferay.portal.kernel.util.WebKeys;

public class LiferayDefaultMultipartActionRequest extends ActionRequestWrapper
		implements MultipartActionRequest {

	private final MultipartActionRequest multipartActionRequest;
	private final String portletNamespace;

	public LiferayDefaultMultipartActionRequest(
			MultipartActionRequest multipartActionRequest) {

		super(multipartActionRequest);
		this.multipartActionRequest = multipartActionRequest;
		this.portletNamespace = "_"
				+ (String) multipartActionRequest
						.getAttribute(WebKeys.PORTLET_ID) + "_";
	}

	@Override
	public String getParameter(String name) {

		String parameter;
		if (multipartActionRequest.getParameter(name) != null) {
			parameter = multipartActionRequest.getParameter(name);
		} else {
			parameter = multipartActionRequest.getParameter(portletNamespace
					+ name);
		}
		return parameter;
	}

	@Override
	public Map<String, String[]> getParameterMap() {

		Map<String, String[]> newMap = new LinkedHashMap<String, String[]>();
		Map<String, String[]> oldMap = multipartActionRequest.getParameterMap();
		for (Map.Entry<String, String[]> entry : oldMap.entrySet()) {
			String newKey;
			if (entry.getKey() != null
					&& entry.getKey().startsWith(portletNamespace)) {
				newKey = entry.getKey().substring(portletNamespace.length());
			} else {
				newKey = entry.getKey();
			}
			newMap.put(newKey, entry.getValue());
		}
		return newMap;
	}

	@Override
	public Enumeration<String> getParameterNames() {

		Set<String> parameterNames = new HashSet<String>();
		Enumeration<String> paramEnum = multipartActionRequest
				.getParameterNames();
		while (paramEnum.hasMoreElements()) {
			String string = paramEnum.nextElement();
			if (string != null && string.startsWith(portletNamespace)) {
				parameterNames.add(string.substring(portletNamespace.length()));
			} else {
				parameterNames.add(string);
			}
		}
		parameterNames.addAll(getFileMap().keySet());
		return Collections.enumeration(parameterNames);
	}

	@Override
	public String[] getParameterValues(String name) {

		String[] parameterValues;
		if (multipartActionRequest.getParameterValues(name) != null) {
			parameterValues = multipartActionRequest.getParameterValues(name);
		} else {
			parameterValues = multipartActionRequest
					.getParameterValues(portletNamespace + name);
		}
		return parameterValues;
	}

	@Override
	public MultipartFile getFile(String name) {
		return multipartActionRequest.getFile(portletNamespace + name);
	}

	@Override
	public Map<String, MultipartFile> getFileMap() {

		Map<String, MultipartFile> newMap = new LinkedHashMap<String, MultipartFile>();
		Map<String, MultipartFile> oldMap = multipartActionRequest.getFileMap();
		for (Map.Entry<String, MultipartFile> entry : oldMap.entrySet()) {
			String newKey;
			if (entry.getKey() != null
					&& entry.getKey().startsWith(portletNamespace)) {
				newKey = entry.getKey().substring(portletNamespace.length());
			} else {
				newKey = entry.getKey();
			}
			newMap.put(newKey, entry.getValue());
		}
		return newMap;
	}

	@Override
	public Iterator<String> getFileNames() {

		return transform(multipartActionRequest.getFileNames(),
				new Function<String, String>() {
					@Override
					public String apply(String fileName) {
						if (fileName != null
								&& fileName.startsWith(portletNamespace)) {
							return fileName.substring(portletNamespace.length());
						}
						return null;
					}
				});
	}

	@Override
	public List<MultipartFile> getFiles(String name) {
		return multipartActionRequest.getFiles(portletNamespace + name);
	}

	@Override
	public MultiValueMap<String, MultipartFile> getMultiFileMap() {

		LinkedMultiValueMap<String, MultipartFile> newMap = new LinkedMultiValueMap<String, MultipartFile>();
		MultiValueMap<String, MultipartFile> oldMap = multipartActionRequest
				.getMultiFileMap();
		for (Map.Entry<String, List<MultipartFile>> entry : oldMap.entrySet()) {
			String newKey;
			if (entry.getKey() != null
					&& entry.getKey().startsWith(portletNamespace)) {
				newKey = entry.getKey().substring(portletNamespace.length());
			} else {
				newKey = entry.getKey();
			}
			newMap.put(newKey, entry.getValue());
		}
		return newMap;
	}

	@Override
	public String getMultipartContentType(String arg0) {
		// TODO Auto-generated method stub
		return null;
	}
}
