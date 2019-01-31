package com.staples.samples;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.sforce.soap.enterprise.sobject.CaseMilestone;

public class CaseMilestoneExtract {
	// Get the fields to query	   
		public String getFieldList() {
		   String fieldList;
		   
		   fieldList = 
				   "Case.CaseNumber, "
						   + "CompletionDate, "
						   + "CreatedDate, "
						   + "ElapsedTimeInDays, "
						   + "ElapsedTimeInHrs, "
						   + "ElapsedTimeInMins, "
						   + "IsCompleted, "
						   + "IsViolated, "
						   + "MilestoneType.Name, "
						   + "StartDate, "
						   + "TargetDate, "
						   + "TargetResponseInDays, "
						   + "TargetResponseInHrs, "
						   + "TargetResponseInMins, "
						   + "TimeSinceTargetInMins, "
						   + "SystemModStamp";

		   return fieldList;
		}
			
		// Get the file header	   
		public String getFileHeader() {
		   String fileHeader =
				   "CaseNumber" + "\t"
						   + "CompletionDate"+ "\t"
						   + "CreatedDate"+ "\t"
						   + "ElapsedTimeInDays"+ "\t"
						   + "ElapsedTimeInHrs"+ "\t"
						   + "ElapsedTimeInMins"+ "\t"
						   + "IsCompleted"+ "\t"
						   + "IsViolated"+ "\t"
						   + "MilestoneType.Name"+ "\t"
						   + "StartDate"+ "\t"
						   + "TargetDate"+ "\t"
						   + "TargetResponseInDays"+ "\t"
						   + "TargetResponseInHrs"+ "\t"
						   + "TargetResponseInMins"+ "\t"
						   + "TimeSinceTargetInMins"+ "\t"
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
		public String getRecord(CaseMilestone cm) {
			String fileRow =
					cm.getCase().getCaseNumber() + "\t"
							   + formatDate(cm.getCompletionDate()) + "\t"
							   + formatDate(cm.getCreatedDate()) + "\t"
							   + cm.getElapsedTimeInDays() + "\t"
							   + cm.getElapsedTimeInHrs() + "\t"
							   + cm.getElapsedTimeInMins() + "\t"
							   + cm.getIsCompleted() + "\t"
							   + cm.getIsViolated() + "\t"
							   + cm.getMilestoneType().getName() + "\t"
							   + formatDate(cm.getStartDate()) + "\t"
							   + formatDate(cm.getTargetDate()) + "\t"
							   + cm.getTargetResponseInDays() + "\t"
							   + cm.getTargetResponseInHrs() + "\t"
							   + cm.getTargetResponseInMins() + "\t"
							   + cm.getTimeSinceTargetInMins() + "\t"
							   + formatDate(cm.getSystemModstamp());
		
		   return fileRow;
		}

}
