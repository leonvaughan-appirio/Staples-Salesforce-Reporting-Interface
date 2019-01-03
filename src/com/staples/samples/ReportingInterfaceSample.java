package com.staples.samples;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.io.File;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.Properties;
import java.text.ParseException;

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
	   public static final String OUTPUTFILEPATH = "data/";
	   public static final int MAXNUMBEROFIDS = 2000;
	   public static String OUTPUTFILENAME;
	   //public static String SYSTEM_MODSTAMP = "";
	
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

	         System.out.println("AuthEndPoint: " + getFilePropValue(CREDENTIALSFILEPATH, "END_POINT"));
	         config.setAuthEndpoint(getFilePropValue(CREDENTIALSFILEPATH, "END_POINT"));

	         connection = new EnterpriseConnection(config);
	         printUserInfo(config);

	         success = true;
	      }
	      catch (ConnectionException ce) {
	         ce.printStackTrace();
	      }
	      catch (Exception e) {
	            e.printStackTrace();
	       }

	      return success;
	   }
	   
	   // Output user information for reference
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
	   
	   // Logout of salesforce
	   private void logout() {
		   try {
			   connection.logout();
			   System.out.println("Logged out.");
		   } catch (ConnectionException ce) {
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
			   
			   // Get Case Ids updated since last run
			   GetUpdatedResult urCa = connection.getUpdated("Case", startTime, endTime);
			   System.out.println("GetUpdateResult CASES: " + urCa.getIds().length);
			   
			   // Get Email Message Ids updated since last run
			   GetUpdatedResult urEm = connection.getUpdated("EmailMessage", startTime, endTime);
			   System.out.println("GetUpdateResult EMAIL MESSAGES: " + urEm.getIds().length);
			   
			   // Get Case Status History records updated since last run
			   GetUpdatedResult urSta = connection.getUpdated("Case_Status_History__c", startTime, endTime);
			   System.out.println("GetUpdateResult CASE STATUS HISTORY: " + urSta.getIds().length);
			   
			   // Get Task records created since last run
			   GetUpdatedResult urT = connection.getUpdated("Task", startTime, endTime);
			   System.out.println("GetUpdateResult TASK: " + urT.getIds().length);
			   
			   // Get Case Milestone records updated since last run. CaseMilestone is not replicateable so need to use standard SOQL query
			   // to get records updated
			   SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
			   String[] mIds = getUpdatedRecords("CaseMilestone", df.format(startTime.getTime()), df.format(endTime.getTime()));
			   System.out.println("GetUpdateResult CASE MILESTONE: " + mIds.length);
			   
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
			   ce.printStackTrace();
		   }
		   catch (ParseException pe) {
			   pe.printStackTrace();
		   }
		   catch (Exception e) {
	            e.printStackTrace();
	       }
	   }
	   
	   // Get the cases that have updated and write the details to file	   
	   private void queryCases(String[] ids) {
		   String fieldList = "CaseNumber, CreatedDate, Status, Priority, Origin, ClosedDate, SystemModStamp";
		   OUTPUTFILENAME = "CASE_EXTRACT_";
		   
		   System.out.println("\nQuery retrieved "
				   + ids.length + " Case records updated since last run.");
		   
		   try {
			   if(ids.length>0) {
				   
				   SObject[] sObjects = connection.retrieve(fieldList, "Case", ids);
					   
				   if (sObjects.length > 0) {
	
						   // Create the output file
						   Calendar endTime = new GregorianCalendar();
						   String fileName = OUTPUTFILEPATH + OUTPUTFILENAME
								    + endTime.get(Calendar.YEAR) 
								   	+ endTime.get(Calendar.MONTH)
						   			+ endTime.get(Calendar.DAY_OF_MONTH) + "-"
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

			   } else {
				   System.out.println("No CASES created since replication last ran.");
			   }
		   }
			   catch (ConnectionException ce) {
			   ce.printStackTrace();
		   }
		   catch (IOException ex) {
	            ex.printStackTrace();
	       }
		   catch (Exception e) {
	            e.printStackTrace();
	       }

	   }
	   
	   // Export the EmailMessage records to file
	   private void queryEmailMessages(String[] ids) {
		   String fieldList = "Parent.CaseNumber, CreatedDate, Business_Unit__c, Incoming, "
		   		+ "FromAddress, ToAddress, CcAddress, Status, Subject, SystemModStamp";
		   OUTPUTFILENAME = "EMAILMESSAGE_EXTRACT_";
		   
		   try {
			   if(ids.length>0) {
				   
				   SObject[] sObjects = connection.retrieve(fieldList, "EmailMessage", ids);
				   
				   if (sObjects.length > 0) {
					   System.out.println("\nQuery retrieved "
							   + sObjects.length + " Email Messages records updated since last run.");
	
						   // Create the output file
						   Calendar endTime = new GregorianCalendar();
						   String fileName = OUTPUTFILEPATH + OUTPUTFILENAME
								    + endTime.get(Calendar.YEAR) 
								   	+ endTime.get(Calendar.MONTH)
						   			+ endTime.get(Calendar.DAY_OF_MONTH) + "-"
						   			+ endTime.get(Calendar.HOUR)
						   			+ endTime.get(Calendar.MINUTE)
						   			+ endTime.get(Calendar.SECOND)
						   			+ ".txt";
						   
						   FileWriter fw = new FileWriter(new File(fileName));
						   
						   // Create file header
						   String fileHeader = 
								    "CASENUMBER" + "\t" +
								    "CREATEDDATE" + "\t" +
								    "BUSINESSUNIT" + "\t" +
								    "INCOMING" + "\t" +
								    "FROMADDRESS"+ "\t" +
						    		"TOADDRESS" + "\t" +
						    		"CCADDRESS" + "\t" +
						    		"STATUS" + "\t" +
						    		"SUBJECT" + "\t" +
						    		"SYSTEMMODSTAMP";
						    
						    // Write header to file
						    fw.write(String.format(fileHeader));
						    //new line
						    fw.write(System.lineSeparator());
					   
						   for (int i = 0; i < sObjects.length; ++i) {
							   EmailMessage ema = (EmailMessage) sObjects[i];						 
							   // Get an instance of a Calendar, using the current time.
							   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							   
							   String caseNum = "";
							   if (ema.getParent() !=null)
								   caseNum = ema.getParent().getCaseNumber();
							   
							   // Get the name of the sObject
							   String emailMessageRecord = 
									   		caseNum + "\t" +
									   		dateFormat.format(ema.getCreatedDate().getTime()) + "\t" +
									   		ema.getBusiness_Unit__c() + "\t" +
									   		ema.getIncoming() + "\t" +
									   		ema.getFromAddress() + "\t" +
							   				ema.getToAddress() + "\t" +
							   				ema.getCcAddress() + "\t" +
							   				ema.getStatus() + "\t" +
										   	ema.getSubject() + "\t" +
										   	dateFormat.format(ema.getSystemModstamp().getTime());
							   // Write to file
							   fw.write(String.format(emailMessageRecord));
							   fw.write(System.lineSeparator()); 
					
					    	}
						    // Close the file
						    fw.close();
				   } else {
					   System.out.println("No records found.");
				   }
			   } else {
				   System.out.println("No EMAIL MESSAGES created since replication last ran.");
			   }
		   }
		   catch (ConnectionException ce) {
			   ce.printStackTrace();
		   }
		   catch (IOException ex) {
	            ex.printStackTrace();
	       }
		   catch (Exception e) {
	            e.printStackTrace();
	       }
		   
	   }
	   
	   // Get the Case Status History records to file
	   private void queryCaseStatusHistory(String[] ids) {
		   String fieldList = "Case__r.CaseNumber, CreatedDate, New_Status__c, Sub_Status__c, Old_Status__c, Old_Sub_Status__c, Start_Time__c, End_Time__c, "
			   		+ "CaseAge_Business_Hours__c, CaseAge_Calendar_Hours__c, SystemModStamp";
			   OUTPUTFILENAME = "CASESTATUSHISTORY_EXTRACT_";
			   
		   try {
			   if(ids.length>0) {
				   
				   SObject[] sObjects = connection.retrieve(fieldList, "Case_Status_History__c", ids);
				   
				   if (sObjects.length > 0) {
					   System.out.println("\nQuery retrieved "
							   + sObjects.length + " Case Status History records updated since last run.");
	
						   // Create the output file
						   Calendar endTime = new GregorianCalendar();
						   String fileName = OUTPUTFILEPATH + OUTPUTFILENAME
								    + endTime.get(Calendar.YEAR) 
								   	+ endTime.get(Calendar.MONTH)
						   			+ endTime.get(Calendar.DAY_OF_MONTH) + "-"
						   			+ endTime.get(Calendar.HOUR)
						   			+ endTime.get(Calendar.MINUTE)
						   			+ endTime.get(Calendar.SECOND)
						   			+ ".txt";
						   
						   FileWriter fw = new FileWriter(new File(fileName));
						   
						   // Create file header
						   String fileHeader = 
								    "CASENUMBER" + "\t" +
								    "CREATEDDATE" + "\t" +
						    		"NEWSTATUS"+ "\t" +
						    		"NEWSUBSTATUS"+ "\t" +
						    		"OLDSTATUS" + "\t" +
						    		"OLDSUBSTATUS" + "\t" +
						    		"STARTTIME" + "\t" +
						    		"ENDTIME" + "\t" +
						    		"CASEAGEBUSINESSHOURS" + "\t" +
						    		"CASEAGECALENDARHOURS" + "\t" +
						    		"SYSTEMMODSTAMP";
						    
						    // Write header to file
						    fw.write(String.format(fileHeader));
						    //new line
						    fw.write(System.lineSeparator());
					   
						   for (int i = 0; i < sObjects.length; ++i) {
							   Case_Status_History__c csa = (Case_Status_History__c) sObjects[i];						 
							   // Get an instance of a Calendar, using the current time.
							   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							   String startDate = "";
							   // Check if the date fields is null, if so return a blank string
							   if (csa.getStart_time__c() != null)
								   startDate = dateFormat.format(csa.getStart_time__c().getTime());
							   // Check if the date fields is null, if so return a blank string
							   String endDate = "";
							   if (csa.getEnd_time__c() != null)
								   endDate = dateFormat.format(csa.getEnd_time__c().getTime());
							   
							   // Get the name of the sObject
							   String emailMessageRecord = 
									   		csa.getCase__r().getCaseNumber() + "\t" +
									   		dateFormat.format(csa.getCreatedDate().getTime()) + "\t" +
							   				csa.getNew_Status__c() + "\t" +
							   				csa.getSub_Status__c() + "\t" +
							   				csa.getOld_Status__c() + "\t" +
							   				csa.getOld_Sub_Status__c() + "\t" +
							   				startDate + "\t" +
							   				endDate + "\t" +
							   				csa.getCaseAge_Business_Hours__c() + "\t" +
							   				csa.getCaseAge_Calendar_Hours__c() + "\t" +
										   	dateFormat.format(csa.getSystemModstamp().getTime());
							   // Write to file
							   fw.write(String.format(emailMessageRecord));
							   fw.write(System.lineSeparator()); 
					
					    	}
						    // Close the file
						    fw.close();
				   } else {
					   System.out.println("No records found.");
				   }
			   } else {
				   System.out.println("No CASE STATUS HISTORY records created since replication last ran.");
			   }
		   }
		   catch (ConnectionException ce) {
			   ce.printStackTrace();
		   }
		   catch (IOException ex) {
	            ex.printStackTrace();
	       }
		   catch (Exception e) {
	            e.printStackTrace();
	       }
		   
	   }
	   
	   // Get the Task records to file
	   private void queryTask(String[] ids) {
		   String fieldList = "Id, What.Type, What.Id, Who.Type, Who.Id, ActivityDate, Activity_Date__c, Activity_Open_Time__c, Business_Type__c, "
		   		+ "CallDisposition, CallDurationInSeconds, CallObject, CallType, CreatedDate, IsClosed, Priority, Status, Status__c, Subject, "
		   		+ "Type, TaskSubType, Task_Type__c, Task_Type_2__c, SystemModStamp";
			   OUTPUTFILENAME = "TASK_EXTRACT_";
			   
		   try {
			   if(ids.length>0) {
				   
				   SObject[] sObjects = connection.retrieve(fieldList, "Task", ids);
				   
				   if (sObjects.length > 0) {
					   System.out.println("\nQuery retrieved "
							   + sObjects.length + " Task records updated since last run.");
	
						   // Create the output file
						   Calendar endTime = new GregorianCalendar();
						   String fileName = OUTPUTFILEPATH + OUTPUTFILENAME
								    + endTime.get(Calendar.YEAR) 
								   	+ endTime.get(Calendar.MONTH)
						   			+ endTime.get(Calendar.DAY_OF_MONTH) + "-"
						   			+ endTime.get(Calendar.HOUR)
						   			+ endTime.get(Calendar.MINUTE)
						   			+ endTime.get(Calendar.SECOND)
						   			+ ".txt";
						   
						   FileWriter fw = new FileWriter(new File(fileName));
						   
						   // Create file header
						   String fileHeader = 
								    "ID" + "\t" +
								    "WHATTYPE" + "\t" +
								    "WHATID" + "\t" +
						    		"WHOTYPE"+ "\t" +
						    		"WHOID"+ "\t" +
						    		"ACTIVITYDATE" + "\t" +
						    		"ACTIVITY_DATE__c" + "\t" +
						    		"ACTIVITY_OPEN_TIME__c" + "\t" +
						    		"BUSINESS_TYPE__c" + "\t" +
						    		"CALLDISPOSITION" + "\t" +
						    		"CALLOBJECT" + "\t" +
						    		"CALLDURATIONINSECS" + "\t" +
						    		"CALLTYPE" + "\t" +
						    		"CREATEDDATE" + "\t" +
						    		"ISCLOSED" + "\t" +
						    		"PRIORITY" + "\t" +
						    		"STATUS" + "\t" +
						    		"STATUS__c" + "\t" +
						    		"SUBJECT" + "\t" +
						    		"TYPE" + "\t" +
						    		"TASKSUBTYPE" + "\t" +
						    		"TASK_TYPE__c" + "\t" +
						    		"TASK_TYPE_2__c" + "\t" +
						    		"SYSTEMMODSTAMP";
						    
						    // Write header to file
						    fw.write(String.format(fileHeader));
						    //new line
						    fw.write(System.lineSeparator());
					   
						   for (int i = 0; i < sObjects.length; ++i) {
							   Task t = (Task) sObjects[i];						 
							   // Get an instance of a Calendar, using the current time.
							   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							   String actDate = "";
							   // Check if the date fields is null, if so return a blank string
							   if (t.getActivityDate() != null)
								   actDate = dateFormat.format(t.getActivityDate().getTime());
							   
							   String actDate2 = "";
							   // Check if the date fields is null, if so return a blank string
							   if (t.getActivity_Date__c() != null)
								   actDate2 = dateFormat.format(t.getActivity_Date__c().getTime());
							   
							   String createdDate = "";
							   // Check if the date fields is null, if so return a blank string
							   if (t.getCreatedDate() != null)
								   createdDate = dateFormat.format(t.getCreatedDate().getTime());
							   
							   String whatType = "";
							   // Check if the fields is null, if so return a blank string
							   if (t.getWhatId() != null)
								   whatType = t.getWhat().getType();
							   
							   String whoType = "";
							   // Check if the fields is null, if so return a blank string
							   if (t.getWhoId() != null)
								   whoType = t.getWho().getType();
							   
							   
							   // Get the name of the sObject
							   String taskRecord = 
									        t.getId() + "\t" +
											whatType + "\t" +
									   		t.getWhatId() + "\t" +
									   		whoType + "\t" +
									   		t.getWhoId() + "\t" +
									   		actDate + "\t" +
									   		actDate2 + "\t" +
									   		t.getActivity_Open_Time__c() + "\t" +
									   		t.getBusiness_Type__c() + "\t" +
									   		t.getCallDisposition() + "\t" +
									   		t.getCallDurationInSeconds() + "\t" +
									   		t.getCallObject() + "\t" +
									   		t.getCallType() + "\t" +
									   		createdDate + "\t" +
							   				t.getIsClosed() + "\t" +
							   				t.getPriority() + "\t" +
							   				t.getStatus() + "\t" +
							   				t.getStatus__c() + "\t" +
							   				t.getSubject() + "\t" +
							   				t.getType() + "\t" +
							   				t.getTaskSubtype() + "\t" +
							   				t.getTask_Type__c() + "\t" +
							   				t.getTask_Type_2__c() + "\t" +
							   				dateFormat.format(t.getSystemModstamp().getTime());
							   // Write to file
							   fw.write(String.format(taskRecord));
							   fw.write(System.lineSeparator()); 
					
					    	}
						    // Close the file
						    fw.close();
				   } else {
					   System.out.println("No records found.");
				   }
			   } else {
				   System.out.println("No TASK records created since replication last ran.");
			   }
		   }
		   catch (ConnectionException ce) {
			   ce.printStackTrace();
		   }
		   catch (IOException ex) {
	            ex.printStackTrace();
	       }
		   catch (Exception e) {
	            e.printStackTrace();
	       }
		   
	   }

	   // Get the Case Milestone records to file
	   private void queryCaseMilestones(String[] ids) {
		   String fieldList = "Case.CaseNumber, MilestoneType.Name, CreatedDate, StartDate, TargetDate, CompletionDate, IsCompleted, "
			   		+ "IsViolated, ActualElapsedTimeInDays, ActualElapsedTimeInHrs, ActualElapsedTimeInMins, "
			   		+ "ElapsedTimeInDays, ElapsedTimeInHrs, ElapsedTimeInMins, StoppedTimeInDays, StoppedTimeInHrs, StoppedTimeInMins,"
			   		+ "TargetResponseInDays, TargetResponseInHrs, TargetResponseInMins, TimeSinceTargetInMins, SystemModStamp";
			   OUTPUTFILENAME = "CASEMILESTONE_EXTRACT_";
			      
		   try {
			   if(ids.length>0) {
					  	
				   // CaseMilestone object is not replicateable so need to query using standard SOQL query
				   SObject[] sObjects = connection.retrieve(fieldList, "CaseMilestone", ids);
				   
				   if (sObjects.length > 0) {
					   System.out.println("\nQuery retrieved "
							   + sObjects.length + " Case Milestone records updated since last run.");
	
						   // Create the output file
						   Calendar endTime = new GregorianCalendar();
						   String fileName = OUTPUTFILEPATH + OUTPUTFILENAME
								    + endTime.get(Calendar.YEAR) 
								   	+ endTime.get(Calendar.MONTH)
						   			+ endTime.get(Calendar.DAY_OF_MONTH) + "-"
						   			+ endTime.get(Calendar.HOUR)
						   			+ endTime.get(Calendar.MINUTE)
						   			+ endTime.get(Calendar.SECOND)
						   			+ ".txt";
						   
						   FileWriter fw = new FileWriter(new File(fileName));
						   
						   // Create file header
						   String fileHeader = 
								    "CASENUMBER" + "\t" +
								    "MILESTONENAME" + "\t" +
						    		"CREATEDDATE"+ "\t" +
						    		"STARTDATE"+ "\t" +
						    		"TARGETDATE" + "\t" +
						    		"COMPLETIONDATE" + "\t" +
						    		"ISCOMPLETED" + "\t" +
						    		"ISVIOLATED" + "\t" +
						    		"ACTUALELAPSEDTIMEINDAYS" + "\t" +
						    		"ACTUALELAPSEDTIMEINHRS" + "\t" +
						    		"ACTUALELAPSEDTIMEINMINS" + "\t" +
						    		"ELAPSEDTIMEINDAYS" + "\t" +
						    		"ELAPSEDTIMEINHRS" + "\t" +
						    		"ELAPSEDTIMEINMINS" + "\t" +
						    		"STOPPEDELAPSEDTIMEINDAYS" + "\t" +
						    		"STOPPEDELAPSEDTIMEINHRS" + "\t" +
						    		"STOPPEDELAPSEDTIMEINMINS" + "\t" +
						    		"TARGETELAPSEDTIMEINDAYS" + "\t" +
						    		"TARGETELAPSEDTIMEINHRS" + "\t" +
						    		"TARGETELAPSEDTIMEINMINS" + "\t" +
						    		"TIMESINCETARGETINMINS" + "\t" +
						    		"SYSTEMMODSTAMP";
						    
						    // Write header to file
						    fw.write(String.format(fileHeader));
						    //new line
						    fw.write(System.lineSeparator());
					   
						   for (int i = 0; i < sObjects.length; ++i) {
							   CaseMilestone cm = (CaseMilestone) sObjects[i];						 
							   // Get an instance of a Calendar, using the current time.
							   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							   String startDate = "";
							   // Check if the date fields is null, if so return a blank string
							   if (cm.getStartDate() != null)
								   startDate = dateFormat.format(cm.getStartDate().getTime());
							   // Check if the date fields is null, if so return a blank string
							   String targetDate = "";
							   if (cm.getTargetDate() != null)
								   targetDate = dateFormat.format(cm.getTargetDate().getTime());
							   // Check if the date fields is null, if so return a blank string
							   String completionDate = "";
							   if (cm.getCompletionDate() != null)
								   completionDate = dateFormat.format(cm.getCompletionDate().getTime());
							   
							   // Get the name of the sObject
							   String caseMilestoneRecord = 
									   		cm.getCase().getCaseNumber() + "\t" +
									   		cm.getMilestoneType().getName() + "\t" +
									   		dateFormat.format(cm.getCreatedDate().getTime()) + "\t" +
							   				startDate + "\t" +
							   				targetDate + "\t" +
							   				completionDate + "\t" +
									   		cm.getIsCompleted() + "\t" +
									   		cm.getIsViolated() + "\t" +
							   				cm.getActualElapsedTimeInDays() + "\t" +
							   				cm.getActualElapsedTimeInHrs() + "\t" +
							   				cm.getActualElapsedTimeInMins() + "\t" +
							   				cm.getElapsedTimeInDays() + "\t" +
							   				cm.getElapsedTimeInHrs() + "\t" +
							   				cm.getElapsedTimeInMins() + "\t" +
							   				cm.getStoppedTimeInDays() + "\t" +
							   				cm.getStoppedTimeInHrs() + "\t" +
							   				cm.getStoppedTimeInMins() + "\t" +
							   				cm.getTargetResponseInDays() + "\t" +
							   				cm.getTargetResponseInHrs() + "\t" +
							   				cm.getTargetResponseInMins() + "\t" +
							   				cm.getTimeSinceTargetInMins() + "\t" +
										   	dateFormat.format(cm.getSystemModstamp().getTime());
							   // Write to file
							   fw.write(String.format(caseMilestoneRecord));
							   fw.write(System.lineSeparator()); 
					
					    	}
						    // Close the file
						    fw.close();
				   } else {
					   System.out.println("No records found.");
				   }
			   } else {
				   System.out.println("No CASE MILESTONE records created since replication last ran.");
			   }
		   }
		   catch (ConnectionException ce) {
			   ce.printStackTrace();
		   }
		   catch (IOException ex) {
	            ex.printStackTrace();
	       }
		   catch (Exception e) {
	            e.printStackTrace();
	       }
		   
	   }
	   
	   // Get the cases that have updated and write the details to file. Had to create a new method to get the IDs of updated
	   // CaseMilestone as the standard SOAP Data Replication method isn't supported for CASEMILESTONE object
	   private String[] getUpdatedRecords(String objName, String startTime, String endTime ) {
		   
		   try {
			   String soqlQuery = "SELECT ID FROM " + objName + " WHERE SYSTEMMODSTAMP > "+ startTime + " AND SYSTEMMODSTAMP < " +endTime ;
			   
			   System.out.println("SOQL QUERY: " + soqlQuery);
			   
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
			   e.printStackTrace();
		   }
		   return null;
	   }
}
