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
			   + "CreatedDate, "
			   + "Business_Unit__c, "
			   + "Incoming, "
			   + "FromAddress, "
			   + "ToAddress, "
			   + "CcAddress, "
			   + "Status, "
			   + "Subject, "
			   + "SystemModStamp";
	   
	   return fieldList;
	}
		
	// Get the file header	   
	public String getFileHeader() {
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
	    		caseNum + "\t" +
	    		formatDate(ema.getCreatedDate()) + "\t" +
		   		ema.getBusiness_Unit__c() + "\t" +
		   		ema.getIncoming() + "\t" +
		   		ema.getFromAddress() + "\t" +
   				ema.getToAddress() + "\t" +
   				ema.getCcAddress() + "\t" +
   				ema.getStatus() + "\t" +
			   	ema.getSubject() + "\t" +
			   	formatDate(ema.getSystemModstamp());
	
	   return fileRow;
	}
	
}
