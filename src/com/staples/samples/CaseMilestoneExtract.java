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
				   + "MilestoneType.Name, "
				   + "CreatedDate, "
				   + "StartDate, "
				   + "TargetDate, "
				   + "CompletionDate, "
				   + "IsCompleted, "
				   + "IsViolated, "
				   + "ActualElapsedTimeInDays, "
				   + "ActualElapsedTimeInHrs, "
				   + "ActualElapsedTimeInMins, "
				   + "ElapsedTimeInDays, "
				   + "ElapsedTimeInHrs, "
				   + "ElapsedTimeInMins, "
				   + "StoppedTimeInDays, "
				   + "StoppedTimeInHrs, "
				   + "StoppedTimeInMins,"
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
					cm.getCase().getCaseNumber() + "\t" +
			   		cm.getMilestoneType().getName() + "\t" +
			   		formatDate(cm.getCreatedDate()) + "\t" +
			   		formatDate(cm.getStartDate()) + "\t" +
			   		formatDate(cm.getTargetDate()) + "\t" +
			   		formatDate(cm.getCompletionDate()) + "\t" +
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
	   				formatDate(cm.getSystemModstamp());
		
		   return fileRow;
		}

}
