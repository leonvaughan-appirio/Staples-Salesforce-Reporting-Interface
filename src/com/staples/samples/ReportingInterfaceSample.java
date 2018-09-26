package com.staples.samples;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.text.ParseException;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.Error;
import com.sforce.soap.enterprise.GetUserInfoResult;
import com.sforce.soap.enterprise.LoginResult;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Case;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.enterprise.GetUpdatedResult;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.ConnectionException;

public class ReportingInterfaceSample {
	//public static final String USERNAME = "reportinginterface@staples-solutions.com.servcloud";
    //public static final String PASSWORD = "London12$CLNhwvI0uTsT3AWKrQFgChAz";
    //public static final String END_POINT = "https://test.salesforce.com/services/Soap/c/43.0/0DF6E0000004Fhr";
	public static final String CREDENTIALSFILEPATH = "salesforce_credentials.properties";
	public static final String PROPSFILEPATH = "sample.properties";
    public static final String OUTPUTFILEPATH = "data/";
    public static final String OUTPUTFILENAME = "CASE_EXTRACT_";
    //public static String SYSTEM_MODSTAMP = "";
    
	private static BufferedReader reader = new BufferedReader(
	         new InputStreamReader(System.in));

	   EnterpriseConnection connection;
	   String authEndPoint = "";

	   public static void main(String[] args) {
	      
	      System.out.println("Usage: com.staples.samples."
	               + "ReportingInterfaceSample <AuthEndPoint>");
	      
	      ReportingInterfaceSample sample = new ReportingInterfaceSample();
	      sample.run();
	   }
	   
	   public String getFilePropValue(String propFileName, String propName) {
		   String propValue = "";
		   try {
			   //FileInputStream input = new FileInputStream(PROPSFILEPATH);
			   FileInputStream input = new FileInputStream(propFileName);
			   Properties props = new Properties();
			   props.load(input);
			   
			   System.out.println("Reading property file " + propFileName
		               + " property name " + propName);
			   propValue = props.getProperty(propName).toString();
			   // close the properties file
			   input.close();
		   }
		   catch (Exception e) {
		         e.printStackTrace();
		   }
		   return propValue;
	   }

	   public void setFilePropValue(String propName, String propValue) {
		   //File configFile = new File(PROPSFILEPATH);
		   try {
			   FileOutputStream output = new FileOutputStream(PROPSFILEPATH);
			   
			   Properties props = new Properties();
			   props.setProperty(propName, propValue);
			   System.out.println("Writing to property file " + PROPSFILEPATH
		               + " property name " + propName + " property value " + propValue);
			   //FileWriter writer = new FileWriter(configFile);
			   props.store(output, null);
			   output.close();
		   }
		   catch (Exception e) {
		         e.printStackTrace();
		   }
	   }	
	   
	   public void run() {
		   // Make a login call
		   if (login()) {
			   // Retrieve some data using a query
			   queryCasesLastModified();
		
			   // Log out
			   logout();
		   }
	   }

	   private boolean login() {
	      boolean success = false;

	      try {
	         ConnectorConfig config = new ConnectorConfig();
	         config.setUsername(getFilePropValue(CREDENTIALSFILEPATH, "SFDC_USERNAME"));
	         config.setPassword(getFilePropValue(CREDENTIALSFILEPATH, "SFDC_PASSWORD"));

	         System.out.println("AuthEndPoint: " + getFilePropValue(CREDENTIALSFILEPATH, "END_POINT"));
	         config.setAuthEndpoint(getFilePropValue(CREDENTIALSFILEPATH, "END_POINT"));

	         connection = new EnterpriseConnection(config);
	         printUserInfo(config);

	         success = true;
	      } catch (ConnectionException ce) {
	         ce.printStackTrace();
	      } 

	      return success;
	   }
	   
	   private void printUserInfo(ConnectorConfig config) {
		      try {
		         GetUserInfoResult userInfo = connection.getUserInfo();

		         System.out.println("\nLogging in ...\n");
		         System.out.println("UserID: " + userInfo.getUserId());
		         System.out.println("User Full Name: " + userInfo.getUserFullName());
		         System.out.println("User Email: " + userInfo.getUserEmail());
		         System.out.println();
		         System.out.println("SessionID: " + config.getSessionId());
		         System.out.println("Auth End Point: " + config.getAuthEndpoint());
		         System.out
		               .println("Service End Point: " + config.getServiceEndpoint());
		         System.out.println();
		      } catch (ConnectionException ce) {
		         ce.printStackTrace();
		      }
	   }

	   private void logout() {
		   try {
			   connection.logout();
			   System.out.println("Logged out.");
		   } catch (ConnectionException ce) {
			   ce.printStackTrace();
		   }
	   }
	   
	   private void queryCasesLastModified() {
		   
		   try {
			   // Get an instance of a Calendar, using the current time.
			   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			   Calendar startTime = new GregorianCalendar();
			   Date startDate = dateFormat.parse(getFilePropValue(PROPSFILEPATH, "LAST_RUN_DATETIME"));
			   startTime.setTime(startDate);
			   // Set the end data
			   Calendar endTime = new GregorianCalendar();
			   
			   // Get Case Ids updated since last run
			   GetUpdatedResult ur = connection.getUpdated("Case", startTime, endTime);
			   
			   System.out.println("GetUpdateResult: " + ur.getIds().length);
			      
		       // Write the results
		       if (ur.getIds() != null && ur.getIds().length > 0) {
		          for (int i = 0; i < ur.getIds().length; i++) {
		            System.out.println(ur.getIds()[i] + " was updated between "
		                  + startTime.getTime().toString() + " and "
		                  + endTime.getTime().toString());
		          }
		          
		          // Get the Case data and write to file
		          queryCases(ur.getIds());
		          
		       } else {
		          System.out.println("No Cases updated between "
		        		  + startTime.getTime().toString() + " and "
		                  + endTime.getTime().toString());
		       }
		       
		       // Update the LAST_RUN_DATETIME in the properties file
		       setFilePropValue("LAST_RUN_DATETIME", dateFormat.format(endTime.getTime()));
			     
		   } catch (ConnectionException ce) {
			   ce.printStackTrace();
		   } catch (ParseException pe) {
			   pe.printStackTrace();
		   }
	   }
		   
	   private void queryCases(String[] ids) {
		   
		   String fieldList = "CaseNumber, CreatedDate, Status, Priority, Origin, Reason, ClosedDate, SystemModStamp";
		   try {
			   SObject[] sObjects = connection.retrieve(fieldList, "Case", ids);
			   
			   if (sObjects.length > 0) {
				   System.out.println("\nQuery retrieved "
						   + sObjects.length + " Case records updated since last run.");

					   // Create the output file
					   Calendar endTime = new GregorianCalendar();
					   String fileName = OUTPUTFILEPATH + OUTPUTFILENAME
							    + endTime.get(Calendar.YEAR) 
							   	+ endTime.get(Calendar.MONTH)
					   			+ endTime.get(Calendar.DAY_OF_MONTH)
					   			+ endTime.get(Calendar.HOUR)
					   			+ endTime.get(Calendar.MINUTE)
					   			+ endTime.get(Calendar.SECOND)
					   			+ ".txt";
					   
					   FileWriter fw = new FileWriter(new File(fileName));
					   
					   // Create file header
					   String fileHeader = "CASENUMBER" + "\t" +
					    		"CREATEDDATE" + "\t" +
					    		"STATUS"+ "\t" +
					    		"PRIORITY" + "\t" +
					    		"ORIGIN" + "\t" +
					    		"REASON" + "\t" +
					    		"CLOSEDDATE" + "\t" +
					    		"SYSTEMMODSTAMP";
					    
					    // Write header to file
					    fw.write(String.format(fileHeader));
					    //new line
					    fw.write(System.lineSeparator());
				   
					   for (int i = 0; i < sObjects.length; ++i) {
						   Case cas = (Case) sObjects[i];
						   
						   // Get an instance of a Calendar, using the current time.
						   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
						   //Calendar calendar = Calendar.getInstance();
						   //System.out.println(dateFormat.format(calendar.getTime()));
						   String closedDate = "";
						   if (cas.getClosedDate() != null)
							   closedDate = dateFormat.format(cas.getClosedDate().getTime());
						   
						   // Get the name of the sObject
						   String caseRecord = cas.getCaseNumber() + "\t" +
								   dateFormat.format(cas.getCreatedDate().getTime()) + "\t" +
								   cas.getStatus() + "\t" +
								   cas.getPriority() + "\t" +
								   cas.getOrigin() + "\t" +
								   cas.getReason() + "\t" +
								   closedDate + "\t" +
								   dateFormat.format(cas.getSystemModstamp().getTime());
						   // Write to file
						   fw.write(String.format(caseRecord));
						   fw.write(System.lineSeparator()); 
				
				    	}
					    // Close the file
					    fw.close();
			   } else {
				   System.out.println("No records found.");
			   }
		   } catch (ConnectionException ce) {
			   ce.printStackTrace();
		   }
		   catch (IOException ex) {
	            ex.printStackTrace();
	       }
	   }

}
