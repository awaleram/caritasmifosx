package com.conflux.mpesa.reconcilation.upload.service;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;



import com.conflux.mpesa.reconcilation.upload.command.FileCommand;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import com.sun.jersey.multipart.FormDataParam;
import org.apache.http.NameValuePair;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;


import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;



@Service
public class UploadWritePlatformServiceJpaRepositoryImpl implements UploadWritePlatformService{
	
	static String convertStreamToString(java.io.InputStream is) {
	    java.util.Scanner s = new java.util.Scanner(is).useDelimiter(":");
	    return s.hasNext() ? s.next() : "";
	}
	 @Transactional
	 @Override
	    public String uploadDetails(FileCommand fileCommand,InputStream inputStream) {
		
			try {
				 BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
			        StringBuilder out = new StringBuilder();
			        String line;
			        int count = 0;
			        String destination=null,operator = null,amount,type;
			         while ((line = reader.readLine()) != null) {
			        	 line = line.replace(",,", ",null,");
		            out.append(line + "\n");
		            final String[] tokens = line.split(",");
		            
		            if(count == 1){		  
		            	String[] senderandmobilenumber = {"null","null"} ;
		            	
			        	String url = "http://localhost:9292/mpesa/transactiondetails";
 		        	HttpClient client = HttpClientBuilder.create().build();
			        	HttpPost post = new HttpPost(url);
			        	System.out.println("senderandmobilenumber  length " +tokens[9]);
			        	if(tokens[9].equals("null")){
			        		senderandmobilenumber[0] = "null";
			        		senderandmobilenumber[1] = "null";
			      
			        	}else{
			        		  senderandmobilenumber = tokens[9].split(" - ");
			        		
			        	}
			        	if(tokens[5].equals("null")){
			        		amount =tokens[6].substring(1);
			        		type = "WithDraw";
			        	
			        	}else{
			        		amount = tokens[5];
			        		type = "PaidIn";	
			        	}
			        	
			        	
			        
			        	List<NameValuePair> urlParameters = new ArrayList<NameValuePair>();
			        	urlParameters.add(new BasicNameValuePair("id", destination));
			        	urlParameters.add(new BasicNameValuePair("orig", "Mpesa"));
			        	urlParameters.add(new BasicNameValuePair("dest", destination));
			        	urlParameters.add(new BasicNameValuePair("tstamp", tokens[2]));
			        	urlParameters.add(new BasicNameValuePair("text", tokens[3]));
			        	urlParameters.add(new BasicNameValuePair("user", "caritas"));
			        	urlParameters.add(new BasicNameValuePair("pass", "nairobi"));
			        	urlParameters.add(new BasicNameValuePair("mpesa_code", tokens[0]));
			        	urlParameters.add(new BasicNameValuePair("mpesa_acc", ""));
			        	urlParameters.add(new BasicNameValuePair("mpesa_msisdn", senderandmobilenumber[0]));
			        	urlParameters.add(new BasicNameValuePair("mpesa_trx_date",tokens[2].replace('-', '/')));
			        	urlParameters.add(new BasicNameValuePair("mpesa_trx_time", tokens[2]));
			        	urlParameters.add(new BasicNameValuePair("mpesa_trx_type", type));
			        	urlParameters.add(new BasicNameValuePair("mpesa_amt", amount));
			        	urlParameters.add(new BasicNameValuePair("mpesa_sender", senderandmobilenumber[1]));
			        	
			        	post.setEntity(new UrlEncodedFormEntity(urlParameters));

			        	HttpResponse response = client.execute(post);
			        	System.out.println("Response Code : " 
			                        + response.getStatusLine().getStatusCode());

			       	BufferedReader rd = new BufferedReader(
			        	        new InputStreamReader(response.getEntity().getContent()));

			        	StringBuffer result = new StringBuffer();
			        	 line = "";
			        while ((line = rd.readLine()) != null) {
			        		result.append(line);
			        	}
			        	
			        	System.out.println("hiii"+ "hello" + result.toString() +"line " +line);  
		            }
			      
				if(tokens[0].equalsIgnoreCase("operator:")){
		            	operator = tokens[1];
		            }else if(tokens[0].equalsIgnoreCase("Short Code:")){
		            	 destination = tokens[1];
		            }else if(tokens[0].equalsIgnoreCase("Receipt No.")){
		            	count++;	
		            }
		            
		            
		        }
		      //  System.out.println(out.toString());   //Prints the string content read from input stream
		        reader.close();
			       
			
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return "hai"; 
		 
	 }

}
