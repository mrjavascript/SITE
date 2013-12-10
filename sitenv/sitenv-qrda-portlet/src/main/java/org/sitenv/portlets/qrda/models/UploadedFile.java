package org.sitenv.portlets.qrda.models;

import org.springframework.web.multipart.MultipartFile;

public class UploadedFile {
	MultipartFile fileData;
	String category;

	public MultipartFile getFileData() {
		return fileData;
	}

	public void setFileData(MultipartFile fileData) {
		this.fileData = fileData;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}
}
