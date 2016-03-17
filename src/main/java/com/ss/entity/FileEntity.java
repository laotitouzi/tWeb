package com.ss.entity;

import org.springframework.web.multipart.MultipartFile;

public class FileEntity {
	public MultipartFile  csvTradeFile;

	public MultipartFile getCsvTradeFile() {
		return csvTradeFile;
	}

	public void setCsvTradeFile(MultipartFile csvTradeFile) {
		this.csvTradeFile = csvTradeFile;
	}
	
	
}
