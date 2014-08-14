package org.sitenv.services.qrda;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;



import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.SystemUtils;
import org.apache.cxf.jaxrs.ext.multipart.MultipartBody;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.sitenv.services.qrda.beans.QRDASchemaError;
import org.sitenv.services.qrda.beans.QRDASchemaValidationResult;
import org.sitenv.services.qrda.beans.QRDAValidationEnhancedResult;
import org.sitenv.services.qrda.beans.QRDAValidationResponse;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

@Path("/QRDA/")
@Produces("text/xml")
public class QRDAService {
    
	Logger logger = LogManager.getLogger(QRDAService.class.getName());
	
	public static final String DEFAULT_PROPERTIES_FILE = "environment.properties";
	
	protected Properties props;
	
	
	
	//default values.
	protected String SCHMATRON_WORKINGDIR = "C:\\Projects\\ONC\\SITE\\QRDA\\Schematron";
    protected String SCHMATRON_CCDATMP = "C:\\Projects\\ONC\\SITE\\QRDA\\Schematron\\temp";
    protected String SCHMATRON_RESULTDIR = "C:\\Projects\\ONC\\SITE\\QRDA\\Schematron\\ValidationResult";
    protected String SCHMATRON_BUILDTEMPLATEFILE_CATEGORYI = "C:\\Projects\\ONC\\SITE\\QRDA\\Schematron\\build_template_categoryI.xml";
    protected String SCHMATRON_BUILDTEMPLATEFILE_CATEGORYIII = "C:\\Projects\\ONC\\SITE\\QRDA\\Schematron\\build_template_categoryIII.xml";
    protected String QRDA_XSD_PATH = "C:\\Projects\\ONC\\SITE\\QRDA\\Schematron\\XSD\\CDA_SDTC.xsd";
    protected String LINUX_JAVA_HOME = "/usr/lib/jvm/java-7-oracle";
	protected String[] ANTCMDS = null;
    
	//mapping for directory.
	private static final Map<String, String> RESULT_CATEGORYDIR_MAPPING;
    static {
        Map<String, String> aMap = new HashMap<String, String>();
        aMap.put("categoryI", "Category_I");
        aMap.put("categoryIII", "Category_III");
        RESULT_CATEGORYDIR_MAPPING = Collections.unmodifiableMap(aMap);
    }
    
	protected void loadProperties() throws IOException {
		InputStream in = this.getClass().getClassLoader().getResourceAsStream(DEFAULT_PROPERTIES_FILE);
		
		if (in == null)
		{
			props = null;
			throw new FileNotFoundException("Environment Properties File not found in class path.");
		}
		else
		{
			props = new Properties();
			props.load(in);
		}
	}
	
	
	
	public QRDAService() throws IOException {
		//load the property file.
        loadProperties();
        //load the properties.
        SCHMATRON_WORKINGDIR = props.getProperty("schematronWrkspDir");
        SCHMATRON_CCDATMP = props.getProperty("schematronUploadedfileTmpDir");
        SCHMATRON_RESULTDIR = props.getProperty("schematronResultDir");
        SCHMATRON_BUILDTEMPLATEFILE_CATEGORYI = props.getProperty("schematronCtgryIbldtmpFile");
        SCHMATRON_BUILDTEMPLATEFILE_CATEGORYIII = props.getProperty("schematronCtgryIIIbldtmpFile");
        QRDA_XSD_PATH = props.getProperty("qrdaxsdpath");
        LINUX_JAVA_HOME = props.getProperty("linuxjavahome");
        
        ANTCMDS = props.getProperty("antCmds").split("\\|\\|");
    }

	
	private QRDASchemaValidationResult qrdaSchemaValidation(String path2Qrda)
	{
		QRDASchemaValidationResult r = new QRDASchemaValidationResult();
		final List<SAXParseException> SchemaErrors = new LinkedList<SAXParseException>();	
		try{
			URL schemaFile = new File(QRDA_XSD_PATH).toURI().toURL();
			Source xmlFile = new StreamSource(new File(path2Qrda));
			SchemaFactory schemaFactory = SchemaFactory
			    .newInstance(javax.xml.XMLConstants.W3C_XML_SCHEMA_NS_URI);
			Schema schema = schemaFactory.newSchema(schemaFile);
			Validator validator = schema.newValidator();
			
			try {
				validator.setErrorHandler(new ErrorHandler()
				  {
					@Override
				    public void warning(SAXParseException exception) throws SAXException
				    {
				      //exceptions.add(exception);
				    }
					
					
				    @Override
				    public void fatalError(SAXParseException exception) throws SAXException
				    {
				    	SchemaErrors.add(exception);
				    }
				
				    @Override
				    public void error(SAXParseException exception) throws SAXException
				    {
				    	SchemaErrors.add(exception);
				    }
			    });
				validator.validate(xmlFile);
				r.setSuccess(true);
			} catch (Exception e) {};
			
			//if there is scehma error.
			if(SchemaErrors.size()>0){
				r.setSuccess(false);
				ArrayList<QRDASchemaError> errors = new ArrayList<QRDASchemaError>();
				for(SAXParseException exp:SchemaErrors)
				{
					QRDASchemaError error = new QRDASchemaError();
					String errorMsg = "[Line number:" + exp.getLineNumber() + " Column number:" + exp.getColumnNumber() + "]";
					errorMsg += exp.getLocalizedMessage()+"\r\n";
					error.setColumnNumber(exp.getColumnNumber());
					error.setLineNumber(exp.getLineNumber());
					error.setErrorMessage(errorMsg);
					errors.add(error);
				}
				r.setSchemaErrors(errors);
			}
			
		}
		catch(Exception exp){
			r.setSuccess(false);
			if(!(exp instanceof SAXException))
			{
				ArrayList<QRDASchemaError> errors = new ArrayList<QRDASchemaError>();
				QRDASchemaError error = new QRDASchemaError();
				error.setColumnNumber(0);
				error.setLineNumber(0);
				error.setErrorMessage(exp.getMessage());
				errors.add(error);
				r.setSchemaErrors(errors);
			}
		}
		
		return r;
	}
	
	public static Element getPreviousSiblingElement(Node node) {
	      Node prevSibling = node.getPreviousSibling();
	      while (prevSibling != null) {
	          if (prevSibling.getNodeType() == Node.ELEMENT_NODE) {
	              return (Element) prevSibling;
	          }
	          prevSibling = prevSibling.getPreviousSibling();
	      }

	      return null;  
	  } 
	
    /**
     * 
     * @param Commands
     * @param WorkingDirectory
     * @return
     */
    private QRDAValidationResponse schematronCateogryIValidation(List<String> Commands, String WorkingDirectory, File ResultFilePath)
    {
    	QRDAValidationResponse validationResponse = new QRDAValidationResponse();
    	
    	try {
	    	
    		ProcessBuilder builder = new ProcessBuilder(Commands);
    		Map<String,String> enviroment = builder.environment();
    		if(!SystemUtils.IS_OS_WINDOWS){
    			//overwrite the java home.
    			enviroment.put("JAVA_HOME", LINUX_JAVA_HOME);
    		}
	        builder.directory(new File(WorkingDirectory));
	        //start a new process
	        final Process process = builder.start();
	        //output the error message if any.
	        builder.redirectErrorStream(true);
	        
	        InputStream is = process.getInputStream();
	        InputStreamReader isr = new InputStreamReader(is);
	        
	        BufferedReader br = new BufferedReader(isr);
	        BufferedReader errorBr = new BufferedReader(new InputStreamReader(process.getErrorStream()));
	        //the string builder for the success message.
	        StringBuilder resultSb = new StringBuilder();
	        //the string builder for the error message.
	        StringBuilder errorSb = new StringBuilder();
	        
	        //line buff
	        String line;
	        //if the batch runs successfully.
	        while ((line = br.readLine()) != null) {
	        	resultSb.append(line);
	        	resultSb.append(System.lineSeparator());
	        }
	        
	        //if the batch runs with errors.
	        while ((line = errorBr.readLine()) != null) {
	        	errorSb.append(line);
		    }
	        
		    int exitValue = process.waitFor();
		    
		    //if the process exit successfully, then the result file shall be generated.
		    if(exitValue == 0)
		    {
		    	
		    	if(!ResultFilePath.exists())
		    	{
		    		validationResponse.setSuccess(false);
		    		validationResponse.setErrorMessage("Process completed, but failed to locate result file:" + ResultFilePath.getAbsolutePath());
		    	}
		    	else{
		    		validationResponse.setSuccess(true);
			    	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
					factory.setNamespaceAware(true);
					DocumentBuilder xmlbuilder = factory.newDocumentBuilder();
					Document doc = xmlbuilder.parse(ResultFilePath);
					//logger.info("Result file: " + ResultFilePath);
					XPath xpath = XPathFactory.newInstance().newXPath();
					XPathExpression expr = xpath.compile("/fileset/file/*[name()='svrl:schematron-output']/*[name()='svrl:failed-assert']");
					
					NodeList nl = (NodeList) expr.evaluate(doc, XPathConstants.NODESET);
					
					//logger.info("# of nodes found: " + nl.getLength());
					//loop through all the failed assert.
					
					int seed = 0;
					HashMap<String, String> xpathMapping = new HashMap<String, String>();
					for (int i = 0; i < nl.getLength(); i++) {
						
						QRDAValidationEnhancedResult resultNode = new QRDAValidationEnhancedResult();
						
						Node childNode = nl.item(i);
						
						//figure out if this is a error or warning!!!
						Node PrevNode = getPreviousSiblingElement(childNode);
						
						while(PrevNode!=null && PrevNode.getNodeName()!="svrl:fired-rule"){
							PrevNode = getPreviousSiblingElement(PrevNode);
						}
						
						int errorType = 0;
						
						if(PrevNode!=null){
							
							logger.info("1Type:" + String.valueOf(PrevNode.getNodeType()) + " Node Name: " + PrevNode.getNodeName() + " Node value:" + PrevNode.getTextContent());
							
							String ruleName = ((Element)PrevNode).getAttribute("id");
							
							logger.info("RuleName:" + ruleName);
							
							if(ruleName.contains("-errors"))
								errorType = 0;
							else if(ruleName.contains("-warnings"))
								errorType = 1;
							else
								errorType = 0;
						}
						
						Node txt = ((Element)childNode).getElementsByTagName("svrl:text").item(0);
						logger.info("error message " + txt.getTextContent());		   
						
						//get the error message.
						resultNode.setMessage(txt.getTextContent());
						String xpathVal = childNode.getAttributes().getNamedItem("location").getNodeValue();
						String keyValue = null;
						if (!xpathMapping.containsKey(xpathVal)) {
							seed++;
							keyValue = "000" + seed;
							xpathMapping.put(xpathVal, keyValue);
						} else {
							keyValue = xpathMapping.get(xpathVal);
						}
						
						resultNode.setNavKey(keyValue);
						resultNode.setXpath(xpathVal);
						
						switch(errorType)
						{
							case 1:
								validationResponse.getSchematronWarnings().add(resultNode);
								break;
							case 0:
								validationResponse.getSchematronErrors().add(resultNode);
								break;
						}
					}
				}
		    }
		    else
		    {
		    	validationResponse.setSuccess(false);
		    	validationResponse.setErrorMessage("Validation Failed with Error Code:" + String.valueOf(exitValue));
		    	validationResponse.setNote(errorSb.toString());
		    }
		    validationResponse.setReturnCode(exitValue);
    	
    	} catch (Exception e) {
	        
	        StringWriter sw = new StringWriter();
	        PrintWriter pw = new PrintWriter(sw);
	        e.printStackTrace(pw);
	        validationResponse.setSuccess(false);
	        validationResponse.setErrorMessage(e.getMessage());
	        validationResponse.setNote(sw.toString());
	    }
    	return validationResponse;
    }
    
    
    
    public static Element getDirectChild(Element parent, String name)
    {
        for(Node child = parent.getFirstChild(); child != null; child = child.getNextSibling())
        {
            if(child instanceof Element && name.equals(child.getNodeName())) return (Element) child;
        }
        return null;
    }
    
    
    
    @GET
    @Path("/About")
    @Produces("application/xml")
    public String About(){
    	return "<h2>QRDA validator version 1.0</h2>";
    }
    
    
    @POST
    @Path("/Validate/")
    @Consumes("multipart/form-data")
    @Produces("application/json")
    public QRDAValidationResponse Validate(MultipartBody body) 
    {
    	
    	logger.info("hit post");
    	QRDAValidationResponse r = new QRDAValidationResponse();
    	OutputStream outputStream = null;
    	InputStream is = null;
    	String category = null;
    	File dir = new File(SCHMATRON_CCDATMP);
    	File buildfiledir = new File(SCHMATRON_WORKINGDIR);
    	String fileName = null;
    	
    	
    	//capture the CCDA file sent through the web service save to the local file. 
    	try {
    		category = body.getAttachmentObject("category", String.class);
    		
    		if(!category.equals("categoryI")&&!category.equals("categoryIII"))
    		{
    			r.setSuccess(false);
    			r.setErrorMessage(String.format("cateogry:%s is not recoganized.", category));
    			return r;
    		}
    		
    		fileName = category + "_" + UUID.randomUUID().toString().replaceAll("-", "");
        	File saveAsFile = new File(dir, fileName + ".xml");
    		
    		if(!saveAsFile.exists()) {
        		saveAsFile.createNewFile();
        	}
    		//parse the fields on the request.
            is = body.getAttachmentObject("doc", InputStream.class);
            
            //save to a XML file.
            outputStream = new FileOutputStream(saveAsFile);
            
            int read = 0;
    		byte[] bytes = new byte[1024];
     
    		while ((read = is.read(bytes)) != -1) {
    			outputStream.write(bytes, 0, read);
    		}
    		//validate the XSD
    		QRDASchemaValidationResult xsdrslt = qrdaSchemaValidation(saveAsFile.getPath());
    		
    		if(!xsdrslt.getSuccess())
    		{
    			//schema validation failed, then only display the schema error.
    			//TODO: this is a bit quirky to use the success flag for different purpose.
    			r.setSuccess(true);
    			r.setSchemaErrors(xsdrslt.getSchemaErrors());
    		}
    		else
    		{
    			//build the validation file and do the schematron validation.
	    		String buildfileTemplate = null;
	    		if(category.equals("categoryI")){
	    			buildfileTemplate = FileUtils.readFileToString(new File(SCHMATRON_BUILDTEMPLATEFILE_CATEGORYI));
	    		}
	    		if(category.equals("categoryIII"))
	    		{
	    			buildfileTemplate = FileUtils.readFileToString(new File(SCHMATRON_BUILDTEMPLATEFILE_CATEGORYIII));
	    		}
	    		
	    		buildfileTemplate = buildfileTemplate.replace("{filetovalidate}",saveAsFile.getPath());
	    		FileUtils.writeStringToFile(new File(buildfiledir, fileName + ".xml"), buildfileTemplate);
	    		
	    		//run the commands on a different processor.
	        	List<String> cmds = new ArrayList<String>(Arrays.asList(ANTCMDS));
	        	
	        	if(SystemUtils.IS_OS_WINDOWS){
	        		cmds.add(fileName + ".xml\"");
	        	}
	        	else
	        	{
	        		cmds.add(fileName + ".xml");
	        	}
	        	
	        	//figure out the result path.
	        	File baseResultDir = new File(SCHMATRON_RESULTDIR, RESULT_CATEGORYDIR_MAPPING.get(category));
	        	File resultFile = new File(baseResultDir,fileName + ".svrl");
	        	System.out.println(resultFile.toString());
	        	r = schematronCateogryIValidation(cmds, SCHMATRON_WORKINGDIR, resultFile);
	        	r.setNote(StringUtils.join(cmds,"||"));
	    		
    		}
    		
    	} catch (IOException e) {
    		StringWriter sw = new StringWriter();
    		PrintWriter pw = new PrintWriter(sw);
    		e.printStackTrace(pw);
    		r.setSuccess(false);
            r.setErrorMessage(e.getMessage() + sw.toString());
            return r;
        }
    	finally
    	{
    		if(is!=null)
    		{
    			try{
    				is.close();
    			}
    			catch(IOException e)
    			{
    				e.printStackTrace();
    			}
    		}
    		
    		if (outputStream != null) {
    			try {
    				outputStream.close();
    			} catch (IOException e) {
    				e.printStackTrace();
    			}
    		}
    	}
    	
        
    	return r;
    }
}
