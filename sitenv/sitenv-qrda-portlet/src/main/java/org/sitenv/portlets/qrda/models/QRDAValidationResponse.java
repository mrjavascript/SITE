package org.sitenv.portlets.qrda.models;

import java.io.Serializable;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author etang
 * 
 */

@XmlRootElement(name = "QRDAValidationResponse")
public class QRDAValidationResponse implements Serializable {
	private int returnCode;
	private boolean success;
	private String validationResult;
	private String errorMessage;
	private String[] validationResults;
	private ArrayList<QRDAValidationEnhancedResult> enhancedResults;

	private String orgXml;
	private String note;

	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	public String getOrgXml() {
		return orgXml;
	}

	public void setOrgXml(String orgXml) {
		this.orgXml = orgXml;
	}

	public QRDAValidationResponse() {
		// validationResults = new JsonArray();
	}

	public String[] getValidationResults() {
		return validationResults;
	}

	public ArrayList<QRDAValidationEnhancedResult> getEnhancedResults() {
		return enhancedResults;
	}

	public void parse() {
		validationResults = this.validationResult.split("\\r?\\n");

		enhancedResults = new ArrayList<QRDAValidationEnhancedResult>();
		for (String result : validationResults) {
			System.out.println("Raw validation result string:" + result);
			if (result.startsWith("[schematron] [assert] ")
					&& result.contains("] - ")) {
				String[] parts = result.split("\\] \\-");
				// System.out.println("first:" + parts[0] + " second:" +
				// parts[1]);
				QRDAValidationEnhancedResult temp = new QRDAValidationEnhancedResult();
				temp.setMessage(parts[1]);
				temp.setXpath(parts[0].replace("[schematron] [assert] ", ""));
				enhancedResults.add(temp);
			}
		}
	}

	public void setValidationResult(String validationResult) {
		this.validationResult = validationResult;
		/*
		 * String[] strs = validationResult.split("\\r?\\n"); for (String str :
		 * strs) { this.validationResults.add(new JsonPrimitive(str)); }
		 */
	}

	public int getReturnCode() {
		return returnCode;
	}

	public String getValidationResult() {
		return validationResult;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public void setReturnCode(int returnCode) {
		this.returnCode = returnCode;
	}

	public boolean getSuccess() {
		return success;
	}

	public void setSuccess(boolean isSuccess) {
		this.success = isSuccess;
	}
}
