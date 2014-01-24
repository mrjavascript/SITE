/**
 * 
 */
package org.sitenv.portlets.qrda.models;

/**
 * @author etang
 *
 */
public class QRDASchemaError {
	private int columnNumber;
	private int lineNumber;
	private String errorMessage;
	public int getColumnNumber() {
		return columnNumber;
	}
	public void setColumnNumber(int columnNumber) {
		this.columnNumber = columnNumber;
	}
	public int getLineNumber() {
		return lineNumber;
	}
	public void setLineNumber(int lineNumber) {
		this.lineNumber = lineNumber;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
