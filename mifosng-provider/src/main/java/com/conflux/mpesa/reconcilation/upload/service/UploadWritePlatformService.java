package com.conflux.mpesa.reconcilation.upload.service;

import java.io.InputStream;
import com.conflux.mpesa.reconcilation.upload.command.FileCommand;



public interface UploadWritePlatformService {
	
		    
String uploadDetails(FileCommand fileCommand, InputStream inputStream);


}
