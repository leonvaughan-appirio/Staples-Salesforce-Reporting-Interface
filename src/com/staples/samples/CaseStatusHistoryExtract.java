package com.staples.samples;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.sforce.soap.enterprise.sobject.Case_Status_History__c;

public class CaseStatusHistoryExtract {
	
	// Get the fields to query	   
	public String getFieldList() {
	   String fieldList;
	   
	   fieldList = 
			   "Case__r.CaseNumber, "
			   + "CreatedDate, "
			   + "New_Status__c, "
			   + "Sub_Status__c, "
			   + "Old_Status__c, "
			   + "Old_Sub_Status__c, "
			   + "Start_Time__c, "
			   + "End_Time__c, "
			   + "CaseAge_Business_Hours__c, "
			   + "CaseAge_Calendar_Hours__c, "
			   + "SystemModStamp";
	   
	   return fieldList;
	}
		
	// Get the file header	   
	public String getFileHeader() {
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
	public String getRecord(Case_Status_History__c csa) {
		String fileRow =
	    		csa.getCase__r().getCaseNumber() + "\t" +
	    		formatDate(csa.getCreatedDate()) + "\t" +
   				csa.getNew_Status__c() + "\t" +
   				csa.getSub_Status__c() + "\t" +
   				csa.getOld_Status__c() + "\t" +
   				csa.getOld_Sub_Status__c() + "\t" +
   				formatDate(csa.getStart_time__c()) + "\t" +
   				formatDate(csa.getEnd_time__c()) + "\t" +
   				csa.getCaseAge_Business_Hours__c() + "\t" +
   				csa.getCaseAge_Calendar_Hours__c() + "\t" +
   				formatDate(csa.getSystemModstamp());
	
	   return fileRow;
	}

}
