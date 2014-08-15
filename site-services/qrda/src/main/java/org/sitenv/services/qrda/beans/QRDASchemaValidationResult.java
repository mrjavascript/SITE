/**
 * 
 */
package org.sitenv.services.qrda.beans;

import java.util.ArrayList;

/**
 * @author etang
 *
 */
public class QRDASchemaValidationResult {
	public QRDASchemaValidationResult()
	{
		schemaErrors = new ArrayList<QRDASchemaError>();
	}
	
	private Boolean success;
	public Boolean getSuccess() {
		return success;
	}
	public void setSuccess(Boolean success) {
		this.success = success;
	}
	
	public ArrayList<QRDASchemaError> getSchemaErrors() {
		return schemaErrors;
	}
	public void setSchemaErrors(ArrayList<QRDASchemaError> schemaErrors) {
		this.schemaErrors = schemaErrors;
	}
	private ArrayList<QRDASchemaError> schemaErrors;
}
