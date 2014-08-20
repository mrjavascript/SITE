package org.sitenv.portlets.qrda.models;

import java.util.List;

public class QRDAAjaxResponse {

	private QRDAValidationResponse body;
	private List<QRDAFileResponse> files;
	
	
	public QRDAValidationResponse getBody() {
		return body;
	}
	public void setBody(QRDAValidationResponse body) {
		this.body = body;
	}
	
	public List<QRDAFileResponse> getFiles() {
		return files;
	}
	public void setFiles(List<QRDAFileResponse> files) {
		this.files = files;
	}
	
	
	
}
