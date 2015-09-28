package com.conflux.mpesa.reconcilation.upload.command;


public class FileCommand {
	 private String fileName;
	    private Long size;
	    private String type;
	    private String location;
	    public FileCommand(final String fileName,final Long size,final String type,final String location){
	    	this.fileName =fileName;
	    	this.size = size;
	    	this.type = type;
	    	this.location = location;
	    }
		public String getFileName() {
			return this.fileName;
		}
		public void setFileName(String fileName) {
			this.fileName = fileName;
		}
		public Long getSize() {
			return this.size;
		}
		public void setSize(Long size) {
			this.size = size;
		}
		public String getType() {
			return this.type;
		}
		public void setType(String type) {
			this.type = type;
		}
		public String getLocation() {
			return this.location;
		}
		public void setLocation(String location) {
			this.location = location;
		}
		
	    
}
