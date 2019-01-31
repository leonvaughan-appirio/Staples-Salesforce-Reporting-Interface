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
			   + "CaseAge_Business_Hours__c, "
			   + "CaseAge_Calendar_Hours__c, "
			   + "Change_Type__c, " 
			   + "CreatedDate, "
			   + "End_Time__c, "
			   + "New_Status__c, "
			   + "Sub_Status__c, "
			   + "Old_Status__c, "
			   + "Old_Sub_Status__c, "
			   + "Owner_name__c, "
			   + "Owner_Type__c, "
			   + "Start_Time__c, "
			   + "SystemModStamp";
	   
	   return fieldList;
	}
		
	// Get the file header	   
	public String getFileHeader() {
	   String fileHeader =
			   "CaseNumber" + "\t"
					   + "CaseAge_Business_Hours__c"  + "\t"
					   + "CaseAge_Calendar_Hours__c"  + "\t"
					   + "Change_Type__c"   + "\t"
					   + "CreatedDate"  + "\t"
					   + "End_Time__c"+ "\t"
					   + "New_Status__c"+ "\t"
					   + "Sub_Status__c"+ "\t"
					   + "Old_Status__c"+ "\t"
					   + "Old_Sub_Status__c"+ "\t"
					   + "Owner_name__c"+ "\t"
					   + "Owner_Type__c"+ "\t"
					   + "Start_Time__c"+ "\t"
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
	public String getRecord(Case_Status_History__c csa) {
		String fileRow =
				csa.getCase__r().getCaseNumber() + "\t"
						   + csa.getCaseAge_Business_Hours__c()  + "\t"
						   + csa.getCaseAge_Calendar_Hours__c()  + "\t"
						   + csa.getChange_Type__c() + "\t"
						   + formatDate(csa.getCreatedDate())  + "\t"
						   + formatDate(csa.getEnd_time__c()) + "\t"
						   + csa.getNew_Status__c()+ "\t"
						   + csa.getSub_Status__c()+ "\t"
						   + csa.getOld_Status__c()+ "\t"
						   + csa.getOld_Sub_Status__c()+ "\t"
						   + csa.getOwner_name__c()+ "\t"
						   + csa.getOwner_Type__c()+ "\t"
						   + csa.getStart_time__c()+ "\t"
						   + formatDate(csa.getSystemModstamp());
	
	   return fileRow;
	}

}
