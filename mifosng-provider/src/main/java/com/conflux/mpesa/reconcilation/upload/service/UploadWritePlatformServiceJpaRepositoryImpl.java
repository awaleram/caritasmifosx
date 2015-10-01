package com.conflux.mpesa.reconcilation.upload.service;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import com.conflux.mpesa.reconcilation.upload.command.FileCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.apache.http.NameValuePair;
import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

@Service
public class UploadWritePlatformServiceJpaRepositoryImpl implements UploadWritePlatformService{
	
	 @Transactional
	 @Override
	    public String uploadDetails(FileCommand fileCommand,InputStream inputStream) {
		 StringBuffer result  =null;
		
			try {
				 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			        StringBuilder out = new StringBuilder();
			        
			        String line;
			        int count = 0;
			        String destination=null,operator = null,amount,type,targetDate;
			         while ((line = reader.readLine()) != null) {
			        	 line = line.replace(",,", ", ,");
			        	 String[] removecommas = line.split("\"");
			        	 int j =removecommas.length;
			        	 if(j>=3){
			        		
			        		 while(j>=3){
			        		
			        			 removecommas[j-2] = removecommas[j-2].replace("," , "");
			        			 j=j-2;
			        		 }
			        		 for(int i=0;i<removecommas.length-1;i++){
			        		removecommas[i+1] = removecommas[i].concat(removecommas[i+1]);
			        		 }
			        		 line = removecommas[removecommas.length-1];
			        		
			        	 }
			        	 
		            out.append(line + "\n");
		            final String[] tokens = line.split(",");
		            
		            if((count == 1)&&(!tokens[1].equals(" "))){		  
		            	String[] senderandmobilenumber = {"null","null"} ;
		            	
			        	String url = "http://localhost:9292/mpesa/transactiondetails";
 		        	HttpClient client = HttpClientBuilder.create().build();
			        	HttpPost post = new HttpPost(url);
			        	if(tokens[9].equals(" ")){
			        		senderandmobilenumber[0] = " ";
			        		senderandmobilenumber[1] = " ";
			      
			        	}else{
			        		  senderandmobilenumber = tokens[9].split(" - ");
			        		
			        	}
			        	if(tokens[5].equals(" ")){
			        		amount =tokens[6].substring(1);
			        		type = "WithDraw";
			        	
			        	}else{
			        		amount = tokens[5];
			        		type = "PaidIn";	
			        	}

 
SimpleDateFormat source = new SimpleDateFormat("dd-MM-yyyy HH:mm");
SimpleDateFormat target = new SimpleDateFormat("MM/dd/yyyy");


Date date = source.parse(tokens[1]);
targetDate = target.format(date);
			        
			        	List<NameValuePair> urlParameters = new ArrayList<>();
			        	urlParameters.add(new BasicNameValuePair("id", destination));
			        	urlParameters.add(new BasicNameValuePair("orig", "Mpesa"));
			        	urlParameters.add(new BasicNameValuePair("dest", destination));
			        	urlParameters.add(new BasicNameValuePair("tstamp", tokens[1]));
			        	urlParameters.add(new BasicNameValuePair("text", tokens[3]));
			        	urlParameters.add(new BasicNameValuePair("user", "caritas"));
			        	urlParameters.add(new BasicNameValuePair("pass", "nairobi"));
			        	urlParameters.add(new BasicNameValuePair("mpesa_code", tokens[0]));
			        	urlParameters.add(new BasicNameValuePair("mpesa_acc", ""));
			        	urlParameters.add(new BasicNameValuePair("mpesa_msisdn", senderandmobilenumber[0]));
			        	urlParameters.add(new BasicNameValuePair("mpesa_trx_date",targetDate));
			        	urlParameters.add(new BasicNameValuePair("mpesa_trx_time", tokens[1]));
			        	urlParameters.add(new BasicNameValuePair("mpesa_trx_type", type));
			        	urlParameters.add(new BasicNameValuePair("mpesa_amt", amount));
			        	urlParameters.add(new BasicNameValuePair("mpesa_sender", senderandmobilenumber[1]));
			        	urlParameters.add(new BasicNameValuePair("office_Id" ,fileCommand.getOfficeId()));
			        	
			        	post.setEntity(new UrlEncodedFormEntity(urlParameters));

			        	HttpResponse response = client.execute(post);

			       	BufferedReader rd = new BufferedReader(
			        	        new InputStreamReader(response.getEntity().getContent()));

			        	 result = new StringBuffer();
			        	 line = "";
			        while ((line = rd.readLine()) != null) {
			        		result.append(line);
			        	}
			        	
		            }
			      
				if(tokens[0].equalsIgnoreCase("Short Code:")){
		            	 destination = tokens[1];
		            }else if(tokens[0].equalsIgnoreCase("Receipt No.")){
		            	count++;	
		            }
		            
		            
		        }
		     
		        reader.close();
			       
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return result.toString(); 
		 
	 }

}
