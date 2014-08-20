package org.sitenv.portlets.qrda.models;

public class QRDAValidationEnhancedResult {
	private String xpath;
	private String message;
	private String navKey;

	public String getNavKey() {
		return navKey;
	}

	public void setNavKey(String navKey) {
		this.navKey = navKey;
	}

	public String getXpath() {
		return xpath;
	}

	public void setXpath(String xpath) {
		this.xpath = xpath;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
}
