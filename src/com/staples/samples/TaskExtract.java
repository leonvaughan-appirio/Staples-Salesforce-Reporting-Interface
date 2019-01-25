package com.staples.samples;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.sforce.soap.enterprise.sobject.Task;

public class TaskExtract {
	// Get the fields to query	   
		public String getFieldList() {
		   String fieldList;
		   
		   fieldList = 
				   "Id, "
				   + "What.Type, "
				   + "What.Id, "
				   + "Who.Type, "
				   + "Who.Id, "
				   + "ActivityDate, "
				   + "Activity_Date__c, "
				   + "Activity_Open_Time__c, "
				   + "Business_Type__c, "
				   + "CallDisposition, "
				   + "CallDurationInSeconds, "
				   + "CallObject, "
				   + "CallType, "
				   + "CreatedDate, "
				   + "IsClosed, "
				   + "Priority, "
				   + "Status, "
				   + "Status__c, "
				   + "Subject, "
				   + "Type, "
				   + "TaskSubType, "
				   + "Task_Type__c, "
				   + "Task_Type_2__c, "
				   + "SystemModStamp";
		   
		   return fieldList;
		}
			
		// Get the file header	   
		public String getFileHeader() {
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
		
		   return fileHeader;
		}
		
		// Format date fields	   
	    private String formatDate(Calendar dateValue) {
	    	SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	    	String sDate = "";
		    if (dateValue != null)
		    	sDate = dateFormat.format(dateValue.getTime());
	    	return sDate;
	    }

		// Get the record	   
		public String getRecord(Task t) {
			String whatType = "";
		   // Check if the fields is null, if so return a blank string
		   if (t.getWhatId() != null)
			   whatType = t.getWhat().getType();
		   
		   String whoType = "";
		   // Check if the fields is null, if so return a blank string
		   if (t.getWhoId() != null)
			   whoType = t.getWho().getType();
		   
			String fileRow =
					t.getId() + "\t" +
					whatType + "\t" +
			   		t.getWhatId() + "\t" +
			   		whoType + "\t" +
			   		t.getWhoId() + "\t" +
			   		formatDate(t.getActivityDate()) + "\t" +
			   		formatDate(t.getActivity_Date__c()) + "\t" +
			   		t.getActivity_Open_Time__c() + "\t" +
			   		t.getBusiness_Type__c() + "\t" +
			   		t.getCallDisposition() + "\t" +
			   		t.getCallDurationInSeconds() + "\t" +
			   		t.getCallObject() + "\t" +
			   		t.getCallType() + "\t" +
			   		formatDate(t.getCreatedDate()) + "\t" +
	   				t.getIsClosed() + "\t" +
	   				t.getPriority() + "\t" +
	   				t.getStatus() + "\t" +
	   				t.getStatus__c() + "\t" +
	   				t.getSubject() + "\t" +
	   				t.getType() + "\t" +
	   				t.getTaskSubtype() + "\t" +
	   				t.getTask_Type__c() + "\t" +
	   				t.getTask_Type_2__c() + "\t" +
	   				formatDate(t.getSystemModstamp());
		
		   return fileRow;
		}

}
