package com.cenaflixpodcast.model.dto;

public class FileUploadResponse {
    private Long id;
    private String fileName;
    private String fileType;
    private Long size;
    private String downloadUrl;
    private String message;
    
    public FileUploadResponse() {}
    
    public FileUploadResponse(String message) {
        this.message = message;
    }
    
    public FileUploadResponse(Long id, String fileName, String fileType, 
                             Long size, String downloadUrl) {
        this.id = id;
        this.fileName = fileName;
        this.fileType = fileType;
        this.size = size;
        this.downloadUrl = downloadUrl;
    }
    
    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public String getFileName() { return fileName; }
    public void setFileName(String fileName) { this.fileName = fileName; }
    
    public String getFileType() { return fileType; }
    public void setFileType(String fileType) { this.fileType = fileType; }
    
    public Long getSize() { return size; }
    public void setSize(Long size) { this.size = size; }
    
    public String getDownloadUrl() { return downloadUrl; }
    public void setDownloadUrl(String downloadUrl) { this.downloadUrl = downloadUrl; }
    
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
}
