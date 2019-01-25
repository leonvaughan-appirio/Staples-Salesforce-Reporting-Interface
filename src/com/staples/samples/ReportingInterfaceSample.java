package com.staples.samples;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.text.ParseException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.commons.configuration.PropertiesConfiguration;

import com.staples.samples.CaseExtract;
import com.staples.samples.EmailMessageExtract;
import com.staples.samples.CaseStatusHistoryExtract;
import com.staples.samples.TaskExtract;
import com.staples.samples.CaseMilestoneExtract;

import com.sforce.soap.enterprise.EnterpriseConnection;
import com.sforce.soap.enterprise.GetUserInfoResult;
import com.sforce.soap.enterprise.QueryResult;
import com.sforce.soap.enterprise.sobject.Case;
import com.sforce.soap.enterprise.sobject.CaseMilestone;
import com.sforce.soap.enterprise.sobject.EmailMessage;
import com.sforce.soap.enterprise.sobject.Case_Status_History__c;
import com.sforce.soap.enterprise.sobject.Task;
import com.sforce.soap.enterprise.sobject.SObject;
import com.sforce.soap.enterprise.GetUpdatedResult;
import com.sforce.ws.ConnectorConfig;
import com.sforce.ws.ConnectionException;

public class ReportingInterfaceSample {
	   public static final String CREDENTIALSFILEPATH = "salesforce_credentials.properties";
	   public static final String PROPSFILEPATH = "sample.properties";
	   public static final String OUTPUTFILEPATH = "./data/";
	   public static final int MAXNUMBOFIDS = 2000;
	   public static final String CASEFILENAME = "CASE_EXTRACT_";
	   public static final String EMAILMESSAGEFILENAME = "EMAIL_MESSAGE_EXTRACT_";
	   public static final String CASEMILESTONEFILENAME = "CASE_MILESTONE_EXTRACT_";
	   public static final String CASESTATUSHISTORYFILENAME = "CASE_STATUS_HISTORY_EXTRACT_";
	   public static final String TASKFILENAME = "TASK_EXTRACT_";
	
	   EnterpriseConnection connection;
	   String authEndPoint = "";
	   
	   // Create a logger 
	   private static final Logger LOGGER = LogManager.getLogger(ReportingInterfaceSample.class.getName());
	   
	   public static void main(String[] args) {
	      
	      //System.out.println("Usage: com.staples.samples."
	      //         + "ReportingInterfaceSample <AuthEndPoint>");
	      
	      LOGGER.info("Usage: com.staples.samples."
	               + "ReportingInterfaceSample <AuthEndPoint>");
	      
	      ReportingInterfaceSample sample = new ReportingInterfaceSample();
	      sample.run();
	   }
	   
	   public String getFilePropValue(String propFileName, String propName) {
		   String propValue = "";
		   try {
			   //FileInputStream input = new FileInputStream(propFileName);
			   //Properties props = new Properties();
			   //props.load(input);
			   //propValue = props.getProperty(propName).toString();
			   // close the properties file
			   //input.close();
			   
			   PropertiesConfiguration conf = new PropertiesConfiguration(propFileName);
			   propValue = conf.getProperty(propName).toString();
			   //props.setProperty("key", "value");
			   //conf.save();   
			   
		   }
		   catch (Exception e) {
			   LOGGER.error(e.getMessage());
		       e.printStackTrace();
		   }
		   
		   return propValue;
	   }

	   public void setFilePropValue(String propName, String propValue) {
		   //File configFile = new File(PROPSFILEPATH);
		   try {
			   //FileOutputStream output = new FileOutputStream(PROPSFILEPATH);
			   //Properties props = new Properties();
			   //props.setProperty(propName, propValue);
			   //System.out.println("Writing to property file " + PROPSFILEPATH
		       //        + " property name " + propName + " property value " + propValue);
			   
			   LOGGER.info("Writing to property file " + PROPSFILEPATH
					   + " property name " + propName + " property value " + propValue);
			   
			   PropertiesConfiguration conf = new PropertiesConfiguration(PROPSFILEPATH);
			   conf.setProperty(propName, propValue);
			   conf.save();
			   
			   //FileWriter writer = new FileWriter(configFile);
			   //props.store(output, null);
			   //output.close();
		   }
		   catch (Exception e) {
			     LOGGER.error(e.getMessage());
		         e.printStackTrace();
		   }
	   }	
	   
	   public void run() {
		   // Make a login call
		   if (login()) {
			   // Retrieve data using a query
			   queryRecsLastModified();
		
			   // Log out
			   logout();
		   }
	   }

	   // Login to Salesforce using credentials from the file
	   private boolean login() {
	      boolean success = false;

	      try {
	         ConnectorConfig config = new ConnectorConfig();
	         config.setUsername(getFilePropValue(CREDENTIALSFILEPATH, "SFDC_USERNAME"));
	         config.setPassword(getFilePropValue(CREDENTIALSFILEPATH, "SFDC_PASSWORD"));

	         //System.out.println("AuthEndPoint: " + getFilePropValue(CREDENTIALSFILEPATH, "END_POINT"));
	         LOGGER.info("AuthEndPoint: " + getFilePropValue(CREDENTIALSFILEPATH, "END_POINT"));
	         
	         config.setAuthEndpoint(getFilePropValue(CREDENTIALSFILEPATH, "END_POINT"));

	         connection = new EnterpriseConnection(config);
	         printUserInfo(config);

	         success = true;
	      }
	      catch (ConnectionException ce) {
	    	 LOGGER.error(ce.getMessage());
	         ce.printStackTrace();
	      }
	      catch (Exception e) {
	    	  LOGGER.error(e.getMessage());  
	    	  e.printStackTrace();
	       }

	      return success;
	   }
	   
	   // Output user information for reference
	   private void printUserInfo(ConnectorConfig config) {
		      try {
		         GetUserInfoResult userInfo = connection.getUserInfo();

		         LOGGER.info("Logging in");
		         LOGGER.info("UserID: " + userInfo.getUserId());
		         LOGGER.info("User Full Name: " + userInfo.getUserFullName());
		         LOGGER.info("User Email: " + userInfo.getUserEmail());
		         LOGGER.info("SessionID: " + config.getSessionId());
		         LOGGER.info("Auth End Point: " + config.getAuthEndpoint());
		         LOGGER.info("Service End Point: " + config.getServiceEndpoint());
		         
		      } catch (ConnectionException ce) {
		    	  LOGGER.error(ce.getMessage());
		          ce.printStackTrace();
		      }
	   }
	   
	   // Logout of salesforce
	   private void logout() {
		   try {
			   connection.logout();
			   LOGGER.info("Logged out.");
		   } catch (ConnectionException ce) {
			   LOGGER.error(ce.getMessage());
			   ce.printStackTrace();
		   }
	   }
	   
	   // Get the records created / updated using the Data Replication API
	   private void queryRecsLastModified() {
		   
		   try {
			   // Get an instance of a Calendar, using the current time.
			   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			   Calendar startTime = new GregorianCalendar();
			   Date startDate = dateFormat.parse(getFilePropValue(PROPSFILEPATH, "LAST_RUN_DATETIME"));
			   startTime.setTime(startDate);
			   // Set the end data
			   Calendar endTime = new GregorianCalendar();
			   
			   LOGGER.info("queryRecsLastModified replicating data modified after " + startDate.toString());
			   
			   // Get Case Ids updated since last run
			   GetUpdatedResult urCa = connection.getUpdated("Case", startTime, endTime);
			   //System.out.println("GetUpdateResult CASES: " + urCa.getIds().length);
			   LOGGER.info("GetUpdateResult CASES: " + urCa.getIds().length);
			   
			   // Get Email Message Ids updated since last run
			   GetUpdatedResult urEm = connection.getUpdated("EmailMessage", startTime, endTime);
			   //System.out.println("GetUpdateResult EMAIL MESSAGES: " + urEm.getIds().length);
			   LOGGER.info("GetUpdateResult EMAIL MESSAGES: " + urEm.getIds().length);
			   
			   // Get Case Status History records updated since last run
			   GetUpdatedResult urSta = connection.getUpdated("Case_Status_History__c", startTime, endTime);
			   //System.out.println("GetUpdateResult CASE STATUS HISTORY: " + urSta.getIds().length);
			   LOGGER.info("GetUpdateResult CASE STATUS HISTORY: " + urSta.getIds().length);
			   
			   // Get Task records created since last run
			   GetUpdatedResult urT = connection.getUpdated("Task", startTime, endTime);
			   //System.out.println("GetUpdateResult TASK: " + urT.getIds().length);
			   LOGGER.info("GetUpdateResult TASK: " + urT.getIds().length);
			   
			   // Get Case Milestone records updated since last run. CaseMilestone is not replicateable so need to use standard SOQL query
			   // to get records updated
			   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			   String[] mIds = getUpdatedRecords("CaseMilestone", df.format(startTime.getTime()), df.format(endTime.getTime()));
			   //System.out.println("GetUpdateResult CASE MILESTONE: " + mIds.length);
			   LOGGER.info("GetUpdateResult CASE MILESTONE: " + mIds.length);
			   
	           // Get the Case data and write to file
	           queryCases(urCa.getIds());
	          
	           // Get the EmailMessage data and write to file
	           queryEmailMessages(urEm.getIds());
	          
	           // Get the CaseStatusHistory data and write to file
	           queryCaseStatusHistory(urSta.getIds());
	           
	           // Get the Task data and write to file
	           queryTask(urT.getIds());
	          
	           // Get the CaseMilestone data and write to file
	           queryCaseMilestones(mIds);
		       
		       // Update the LAST_RUN_DATETIME in the properties file
		       setFilePropValue("LAST_RUN_DATETIME", dateFormat.format(endTime.getTime()));
			     
		   }
		   catch (ConnectionException ce) {
			   LOGGER.error(ExceptionUtils.getStackTrace(ce));
			   ce.printStackTrace();
		   }
		   catch (ParseException pe) {
			   LOGGER.error(ExceptionUtils.getStackTrace(pe));
			   pe.printStackTrace();
		   }
		   catch (Exception e) {
			   LOGGER.error(ExceptionUtils.getStackTrace(e));
	           e.printStackTrace();;
	       }
	   }
	   
	   // Get the records using connection.retrieve call. Needs to handle maximum of 2000 object IDs in the retrieve() call.
	   private SObject[] retrieveRecords(String fieldList, String objType, String[] ids){
		   List<SObject> mySObjects = new ArrayList<SObject>();
		   boolean hasNext = true;
		   int startIndex = 0;
		   int endIndex = ids.length;
		   int arraySize = ids.length;
		   int j = 0;
		    
		   if (ids.length > MAXNUMBOFIDS) {
		    	endIndex = MAXNUMBOFIDS;
		    	arraySize = MAXNUMBOFIDS;
		   }
		   try {
			   do {
			    	if(ids.length - startIndex > MAXNUMBOFIDS) {
				    	hasNext = true;
				    } else {
				    	hasNext = false;
				    }
			    	String[] myIds = new String[arraySize];
			    	
			    	// Iterate through the list of ids in batches of 2000 at a time and add to list of ids to call retrieve
			    	for (int i=0;i < arraySize; i++)
				    {
			    		myIds[i] = ids[j];
				    	j++;
			    	}
			    	
			    	// Call the connection.retrieve for 2000 ids at a time
			    	SObject[] sObjects = connection.retrieve(fieldList, objType, myIds);
				    
					// Iterate through the list of sObjects and add to my list
				    for (int i=0;i < sObjects.length; i++)
				    {
				    	mySObjects.add(sObjects[i]);
				    }
				    
				    // Get the next 2000 records
				    startIndex = endIndex; // Reset the StartIndex to be the end of the previous list of objects
				    if(ids.length - endIndex > MAXNUMBOFIDS) {
				    	arraySize = MAXNUMBOFIDS;
				    	endIndex = endIndex + MAXNUMBOFIDS;	
				    } else {
				    	arraySize = ids.length-endIndex;
				    	endIndex = ids.length;
				    }
				    
			    } while (hasNext);
			   
			    // convert list to array 
			    SObject[] sObjects1 = mySObjects.toArray( new SObject[] {} );
			    return sObjects1;
		   }
		   catch (Exception e) {
			    LOGGER.error(ExceptionUtils.getStackTrace(e));
	            e.printStackTrace();
	            return null;
	       }
	   }
		
	   // Handle null string fields	   
	   private String formatNull(String fieldValue) {
		   if(fieldValue == null) {
			   return "";
		   } else {
			   return fieldValue;
		   }
	   }
	   
			   
	   // Get the cases that have updated and write the details to file	   
	   private void queryCases(String[] ids) {
		   CaseExtract casExtract = new CaseExtract();
		   String fieldList = casExtract.getFieldList();
		   //CASEFILENAME = "CASE_EXTRACT_";
		   
		   //System.out.println("\nQuery retrieved "
		   //	   + ids.length + " Case records updated since last run.");
		   LOGGER.info("Query retrieved "
				   + ids.length + " Case records updated since last run.");
		   
		   try {
			   if(ids.length>0) {
				   
				   //SObject[] sObjects = connection.retrieve(fieldList, "Case", ids);
				   SObject[] sObjects = retrieveRecords(fieldList, "Case", ids);
					   
				   if (sObjects.length > 0) {
	
						   // Create the output file
						   Calendar endTime = new GregorianCalendar();
						   String fileName = OUTPUTFILEPATH 
								    + endTime.get(Calendar.YEAR) 
								    + endTime.get(Calendar.MONTH)+1
								    + endTime.get(Calendar.DAY_OF_MONTH) + "/"
								    + CASEFILENAME
								    + endTime.get(Calendar.YEAR) 
								   	+ endTime.get(Calendar.MONTH)
						   			+ endTime.get(Calendar.DAY_OF_MONTH) + "-"
						   			+ endTime.get(Calendar.HOUR)
						   			+ endTime.get(Calendar.MINUTE)
						   			+ endTime.get(Calendar.SECOND)
						   			+ ".txt";
						   
						   //FileWriter fw = new FileWriter(new File(fileName));
						   File file = new File(fileName);
						   
						   // Create file header
						   String fileHeader = casExtract.getFileHeader();
						    
						    // Write header to file
						    //fw.write(String.format(fileHeader));
						    FileUtils.writeStringToFile(file, fileHeader, (String)null, true);
						   
						    //new line
						    //fw.write(System.lineSeparator());
						    FileUtils.writeStringToFile(file, System.lineSeparator(), (String)null, true);
					   
						    for (int i = 0; i < sObjects.length; ++i) {
							   Case cas = (Case) sObjects[i];						 
							   
							   // Get the name of the sObject
							   String caseRecord = casExtract.getRecord(cas);
							   // Write to file
							   //fw.write(String.format(caseRecord));
							   FileUtils.writeStringToFile(file, String.format(caseRecord), (String)null, true);
							   //fw.write(System.lineSeparator());
							   FileUtils.writeStringToFile(file, System.lineSeparator(), (String)null, true);
					
					    	}
						   // Close the file
						   //fw.close();
						    
				   } else {
					   //System.out.println("No records found.");
					   LOGGER.info("No records found.");
				   }

			   } else {
				   //System.out.println("No CASES created since replication last ran.");
				   LOGGER.info("No CASES created since replication last ran.");
			   }
		   }
		   
		   catch (IOException ex) {
			    LOGGER.error(ExceptionUtils.getStackTrace(ex));
	            ex.printStackTrace();
	       }
		   catch (Exception e) {
			   LOGGER.error(ExceptionUtils.getStackTrace(e));
	            e.printStackTrace();
	       }

	   }
	   
	   // Export the EmailMessage records to file
	   private void queryEmailMessages(String[] ids) {
		   EmailMessageExtract emExtract = new EmailMessageExtract();
		   String fieldList = emExtract.getFieldList();
		   //OUTPUTFILENAME = "EMAILMESSAGE_EXTRACT_";
		   
		   try {
			   if(ids.length>0) {
				   
				   //SObject[] sObjects = connection.retrieve(fieldList, "EmailMessage", ids);
				   SObject[] sObjects = retrieveRecords(fieldList, "EmailMessage", ids);
				   
				   if (sObjects.length > 0) {
					   //System.out.println("\nQuery retrieved "
					   //	   + sObjects.length + " Email Messages records updated since last run.");
					   LOGGER.info("Query retrieved "
							   + sObjects.length + " Email Messages records updated since last run.");
	
						   // Create the output file
						   Calendar endTime = new GregorianCalendar();
						   String fileName = OUTPUTFILEPATH 
								    + endTime.get(Calendar.YEAR) 
								    + endTime.get(Calendar.MONTH)+1
								    + endTime.get(Calendar.DAY_OF_MONTH) + "/"
								    + EMAILMESSAGEFILENAME
								    + endTime.get(Calendar.YEAR) 
								   	+ endTime.get(Calendar.MONTH)
						   			+ endTime.get(Calendar.DAY_OF_MONTH) + "-"
						   			+ endTime.get(Calendar.HOUR)
						   			+ endTime.get(Calendar.MINUTE)
						   			+ endTime.get(Calendar.SECOND)
						   			+ ".txt";
						   
						   //FileWriter fw = new FileWriter(new File(fileName));
						   File file = new File(fileName);
						   
						   // Create file header
						   String fileHeader = emExtract.getFileHeader();
						    
						    // Write header to file
						    //fw.write(String.format(fileHeader));
						    FileUtils.writeStringToFile(file, fileHeader, (String)null, true);
						    
						    //new line
						    //fw.write(System.lineSeparator());
						    FileUtils.writeStringToFile(file, System.lineSeparator(), (String)null, true);
					   
						   for (int i = 0; i < sObjects.length; ++i) {
							   EmailMessage ema = (EmailMessage) sObjects[i];						 
							   
							   // Get the name of the sObject
							   String emailMessageRecord = emExtract.getRecord(ema);
							   // Write to file
							   //fw.write(String.format(emailMessageRecord));
							   FileUtils.writeStringToFile(file, emailMessageRecord, (String)null, true);
							   //fw.write(System.lineSeparator()); 
							   FileUtils.writeStringToFile(file, System.lineSeparator(), (String)null, true);
					
					    	}
						    // Close the file
						    //fw.close();
				   } else {
					   //System.out.println("No records found.");
					   LOGGER.info("No records found.");
				   }
			   } else {
				   //System.out.println("No EMAIL MESSAGES created since replication last ran.");
				   LOGGER.info("No EMAIL MESSAGES created since replication last ran.");
			   }
		   }
		   
		   catch (IOException ex) {
			   LOGGER.error(ExceptionUtils.getStackTrace(ex));
			   ex.printStackTrace();
	       }
		   catch (Exception e) {
			   LOGGER.error(ExceptionUtils.getStackTrace(e));
			   e.printStackTrace();
	       }
		   
	   }
	   
	   // Get the Case Status History records to file
	   private void queryCaseStatusHistory(String[] ids) {
		   CaseStatusHistoryExtract csaExtract = new CaseStatusHistoryExtract();
		   String fieldList = csaExtract.getFieldList();
		   
		   //OUTPUTFILENAME = "CASESTATUSHISTORY_EXTRACT_";
			   
		   try {
			   if(ids.length>0) {
				   
				   //SObject[] sObjects = connection.retrieve(fieldList, "Case_Status_History__c", ids);
				   SObject[] sObjects = retrieveRecords(fieldList, "Case_Status_History__c", ids);
				   
				   if (sObjects.length > 0) {
					   //System.out.println("\nQuery retrieved "
					   //	   + sObjects.length + " Case Status History records updated since last run.");
					   LOGGER.info("Query retrieved "
							   + sObjects.length + " Case Status History records updated since last run.");
					   
						   // Create the output file
						   Calendar endTime = new GregorianCalendar();
						   String fileName = OUTPUTFILEPATH 
								    + endTime.get(Calendar.YEAR) 
								    + endTime.get(Calendar.MONTH)+1
								    + endTime.get(Calendar.DAY_OF_MONTH) + "/"
								    + CASESTATUSHISTORYFILENAME
								    + endTime.get(Calendar.YEAR) 
								   	+ endTime.get(Calendar.MONTH)
						   			+ endTime.get(Calendar.DAY_OF_MONTH) + "-"
						   			+ endTime.get(Calendar.HOUR)
						   			+ endTime.get(Calendar.MINUTE)
						   			+ endTime.get(Calendar.SECOND)
						   			+ ".txt";
						   
						  //FileWriter fw = new FileWriter(new File(fileName));
						   File file = new File(fileName);
						   
						   // Create file header
						   String fileHeader = csaExtract.getFileHeader();
						    
						    // Write header to file
						    //fw.write(String.format(fileHeader));
						    FileUtils.writeStringToFile(file, fileHeader, (String)null, true);
						    
						    //new line
						    //fw.write(System.lineSeparator());
						    FileUtils.writeStringToFile(file, System.lineSeparator(), (String)null, true);
					   
						   for (int i = 0; i < sObjects.length; ++i) {
							   Case_Status_History__c csa = (Case_Status_History__c) sObjects[i];						 
							   
							   // Get the name of the sObject
							   String caseStatusHistoryRecord = csaExtract.getRecord(csa);
									   		
							   // Write to file
							   //fw.write(String.format(emailMessageRecord));
							   FileUtils.writeStringToFile(file, caseStatusHistoryRecord, (String)null, true);
							   //fw.write(System.lineSeparator());
							   FileUtils.writeStringToFile(file, System.lineSeparator(), (String)null, true);
					
					    	}
						    // Close the file
						    //fw.close();
				   } else {
					   //System.out.println("No records found.");
					   LOGGER.info("No records found.");
				   }
			   } else {
				   //System.out.println("No CASE STATUS HISTORY records created since replication last ran.");
				   LOGGER.info("No CASE STATUS HISTORY records created since replication last ran.");
			   }
		   }
		   catch (IOException ex) {
			   LOGGER.error(ExceptionUtils.getStackTrace(ex));
	            ex.printStackTrace();
	       }
		   catch (Exception e) {
			   LOGGER.error(ExceptionUtils.getStackTrace(e));
			   e.printStackTrace();
	       }
		   
	   }
	   
	   // Get the Task records to file
	   private void queryTask(String[] ids) {
		   TaskExtract tExtract = new TaskExtract();
		   String fieldList = tExtract.getFieldList();
		   
		   //OUTPUTFILENAME = "TASK_EXTRACT_";
			   
		   try {
			   if(ids.length>0) {
				   
				   //SObject[] sObjects = connection.retrieve(fieldList, "Task", ids);
				   SObject[] sObjects = retrieveRecords(fieldList, "Task", ids);
				   
				   
				   if (sObjects.length > 0) {
					    //System.out.println("\nQuery retrieved "
						//	   + sObjects.length + " Task records updated since last run.");
					    LOGGER.info("Query retrieved "
								   + sObjects.length + " Task records updated since last run.");	   
	
						   // Create the output file
						   Calendar endTime = new GregorianCalendar();
						   String fileName = OUTPUTFILEPATH 
								    + endTime.get(Calendar.YEAR) 
								    + endTime.get(Calendar.MONTH)+1
								    + endTime.get(Calendar.DAY_OF_MONTH) + "/"
								    + TASKFILENAME
								    + endTime.get(Calendar.YEAR) 
								   	+ endTime.get(Calendar.MONTH)
						   			+ endTime.get(Calendar.DAY_OF_MONTH) + "-"
						   			+ endTime.get(Calendar.HOUR)
						   			+ endTime.get(Calendar.MINUTE)
						   			+ endTime.get(Calendar.SECOND)
						   			+ ".txt";
						   
						   //FileWriter fw = new FileWriter(new File(fileName));
						   File file = new File(fileName);
						   
						   // Create file header
						   String fileHeader = tExtract.getFileHeader();
						    
						    // Write header to file
						    //fw.write(String.format(fileHeader));
						    FileUtils.writeStringToFile(file, fileHeader, (String)null, true);
						    //new line
						    //fw.write(System.lineSeparator());
						    FileUtils.writeStringToFile(file, System.lineSeparator(), (String)null, true);
					   
						   for (int i = 0; i < sObjects.length; ++i) {
							   Task t = (Task) sObjects[i];						 
							   // Get the name of the sObject
							   String taskRecord = tExtract.getRecord(t);
									        
							   // Write to file
							   //fw.write(String.format(taskRecord));
							   FileUtils.writeStringToFile(file, taskRecord, (String)null, true);
							   //fw.write(System.lineSeparator());
							   FileUtils.writeStringToFile(file, System.lineSeparator(), (String)null, true);
					
					    	}
						    // Close the file
						    //fw.close();
				   } else {
					   //System.out.println("No records found.");
					   LOGGER.info("No records found.");
				   }
			   } else {
				   //System.out.println("No TASK records created since replication last ran.");
				   LOGGER.info("No TASK records created since replication last ran.");
			   }
		   }
		   catch (IOException ex) {
			   LOGGER.error(ExceptionUtils.getStackTrace(ex));
	            ex.printStackTrace();
	       }
		   catch (Exception e) {
			   LOGGER.error(ExceptionUtils.getStackTrace(e));
			   e.printStackTrace();
	       }
		   
	   }

	   // Get the Case Milestone records to file
	   private void queryCaseMilestones(String[] ids) {
		   CaseMilestoneExtract cmExtract = new CaseMilestoneExtract();
		   String fieldList = cmExtract.getFieldList();
			      
		   try {
			   if(ids.length>0) {
					  	
				   // CaseMilestone object is not replicateable so need to query using standard SOQL query
				   //SObject[] sObjects = connection.retrieve(fieldList, "CaseMilestone", ids);
				   SObject[] sObjects = retrieveRecords(fieldList, "CaseMilestone", ids);
				   
				   if (sObjects.length > 0) {
					   //System.out.println("\nQuery retrieved "
					   //		   + sObjects.length + " Case Milestone records updated since last run.");
					   LOGGER.info("Query retrieved "
							   + sObjects.length + " Case Milestone records updated since last run.");
	
						   // Create the output file
						   Calendar endTime = new GregorianCalendar();
						   String fileName = OUTPUTFILEPATH 
								    + endTime.get(Calendar.YEAR) 
								    + endTime.get(Calendar.MONTH)+1
								    + endTime.get(Calendar.DAY_OF_MONTH) + "/"
								    + CASEMILESTONEFILENAME
								    + endTime.get(Calendar.YEAR) 
								   	+ endTime.get(Calendar.MONTH)
						   			+ endTime.get(Calendar.DAY_OF_MONTH) + "-"
						   			+ endTime.get(Calendar.HOUR)
						   			+ endTime.get(Calendar.MINUTE)
						   			+ endTime.get(Calendar.SECOND)
						   			+ ".txt";
						   
						   //FileWriter fw = new FileWriter(new File(fileName));
						   File file = new File(fileName);
						   
						   // Create file header
						   String fileHeader = cmExtract.getFileHeader();
						    
						    // Write header to file
						    //fw.write(String.format(fileHeader));
						    FileUtils.writeStringToFile(file, fileHeader, (String)null, true);
						    //new line
						    //fw.write(System.lineSeparator());
						    FileUtils.writeStringToFile(file, System.lineSeparator(), (String)null, true);
					   
						   for (int i = 0; i < sObjects.length; ++i) {
							   CaseMilestone cm = (CaseMilestone) sObjects[i];						 
							   // Get the name of the sObject
							   String caseMilestoneRecord = cmExtract.getRecord(cm);

							   // Write to file
							   //fw.write(String.format(caseMilestoneRecord));
							   FileUtils.writeStringToFile(file, caseMilestoneRecord, (String)null, true);
							   //fw.write(System.lineSeparator());
							   FileUtils.writeStringToFile(file, System.lineSeparator(), (String)null, true);
					
					    	}
						    // Close the file
						    //fw.close();
				   } else {
					   //System.out.println("No records found.");
					   LOGGER.info("No records found.");
				   }
			   } else {
				   //System.out.println("No CASE MILESTONE records created since replication last ran.");
				   LOGGER.info("No CASE MILESTONE records created since replication last ran.");
			   }
		   }
		   catch (IOException ex) {
			   LOGGER.error(ExceptionUtils.getStackTrace(ex));
			   ex.printStackTrace();
	       }
		   catch (Exception e) {
			   LOGGER.error(ExceptionUtils.getStackTrace(e));
			   e.printStackTrace();
	       }
		   
	   }
	   
	   // Get the cases that have updated and write the details to file. Had to create a new method to get the IDs of updated
	   // CaseMilestone as the standard SOAP Data Replication method isn't supported for CASEMILESTONE object
	   private String[] getUpdatedRecords(String objName, String startTime, String endTime ) {
		   
		   try {
			   String soqlQuery = "SELECT ID FROM " + objName + " WHERE SYSTEMMODSTAMP > "+ startTime + " AND SYSTEMMODSTAMP < " +endTime ;
			   
			   //System.out.println("SOQL QUERY: " + soqlQuery);
			   
			   String[] recIds = new String[]{};
			   
			   QueryResult qr = connection.query(soqlQuery);
			   
			   if(qr.getSize()>0){
				   
				   recIds = new String[qr.getSize()];
				   SObject[] recs = qr.getRecords();

				   for (int i = 0; i < recs.length; i++){
					   CaseMilestone ca = (CaseMilestone) recs[i];
					   recIds[i] = ca.getId();
				   }
				   
			   }
			   return recIds;
			   
		   } catch (Exception e) {
			   LOGGER.error(ExceptionUtils.getStackTrace(e));
			   e.printStackTrace();
		   }
		   return null;
	   }
}
