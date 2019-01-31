package com.staples.samples;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.sforce.soap.enterprise.sobject.EmailMessage;

public class EmailMessageExtract {

	// Get the fields to query	   
	public String getFieldList() {
	   String fieldList;
	   
	   fieldList = 
			   "Parent.CaseNumber, "
					   + "BccAddress, "
					   + "Business_Unit__c, "
					   + "CcAddress, "
					   + "CreatedDate, "
					   + "Email_Override__c, "
					   + "Exclude_From_SLA__c, "
					   + "External_Email__c, "
					   + "FirstOpenedDate, "
					   + "ValidatedFromAddress, "
					   + "FromAddress, "
					   + "FromName, "
					   + "HasAttachment, "
					   + "IsExternallyVisible, "
					   + "Incoming, "
					   + "LastModifiedDate, "
					   + "LastOpenedDate, "
					   + "MessageDate, "
					   + "Status, "
					   + "Subject, "
					   + "ToAddress, "
					   + "SystemModStamp";
			   
	   return fieldList;
	}
		
	// Get the file header	   
	public String getFileHeader() {
	   String fileHeader =
			   		"CaseNumber" + "\t"
					   + "BccAddress" + "\t"
					   + "Business_Unit__c" + "\t"
					   + "CcAddress" + "\t"
					   + "CreatedDate" + "\t"
					   + "Email_Override__c" + "\t"
					   + "Exclude_From_SLA__c"  + "\t"
					   + "External_Email__c" + "\t"
					   + "FirstOpenedDate" + "\t"
					   + "ValidatedFromAddress" + "\t"
					   + "FromAddress" + "\t"
					   + "FromName" + "\t"
					   + "HasAttachment" + "\t"
					   + "IsExternallyVisible" + "\t"
					   + "Incoming" + "\t"
					   + "LastModifiedDate" + "\t"
					   + "LastOpenedDate" + "\t"
					   + "MessageDate" + "\t"
					   + "Status" + "\t"
					   + "Subject" + "\t"
					   + "ToAddress" + "\t"
					   + "SystemModStamp";
	
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
	public String getRecord(EmailMessage ema) {
		// Get an instance of a Calendar, using the current time.
	    String caseNum = "";
		if (ema.getParent() !=null)
			   caseNum = ema.getParent().getCaseNumber();
		   
	    String fileRow =
	    		caseNum + "\t"
	    				+ ema.getBccAddress() + "\t"
	    				+ ema.getBusiness_Unit__c() + "\t"
						+ ema.getCcAddress() + "\t"
						+ formatDate(ema.getCreatedDate()) + "\t"
						+ ema.getEmail_Override__c() + "\t"
						+ ema.getExclude_From_SLA__c()  + "\t"
						+ ema.getExternal_Email__c() + "\t"
						+ ema.getFirstOpenedDate() + "\t"
						+ ema.getValidatedFromAddress() + "\t"
						+ ema.getFromAddress() + "\t"
						+ ema.getFromName() + "\t"
						+ ema.getHasAttachment() + "\t"
						+ ema.getIsExternallyVisible() + "\t"
						+ ema.getIncoming() + "\t"
						+ formatDate(ema.getLastModifiedDate()) + "\t"
						+ formatDate(ema.getLastOpenedDate()) + "\t"
						+ formatDate(ema.getMessageDate()) + "\t"
						+ ema.getStatus() + "\t"
						+ ema.getSubject() + "\t"
						+ ema.getToAddress() + "\t"
						+ formatDate(ema.getSystemModstamp());
	
	   return fileRow;
	}
	
}
