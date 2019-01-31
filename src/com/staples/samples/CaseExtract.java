package com.staples.samples;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import com.sforce.soap.enterprise.sobject.Case;

public class CaseExtract {
	
		// Get the fields to query	   
		public String getFieldList() {
		   String fieldList;
		   
		   fieldList = 
				   "CaseNumber, "
		   		   + "CreatedDate, "
		   		   + "Status, "
		   		   + "Priority, "
		   		   + "Origin, "
		   		   + "ClosedDate, "
		   		   + "Subject, "
		   		   + "SystemModStamp, "
				   + "X0_2_hrs_Remaining_SLA__c, "
				   + "X4_2_hrs_Remaining_SLA__c, "
				   + "Account_Contact_Relationship_Validated__c, "
				   + "Account_ERP_Number__c, "
				   + "Related_Account_ERP_Number__c, "
				   + "Account_ID__c, "
				   + "Account_Validated__c, "
				   + "Approval_Type__c, "
				   + "Article_Code__c, "
				   + "Auto_Reply__c, "
				   + "Auto_Reply_on_Case_Closure__c, "
				   + "Auto_Reply_Template__c, "
				   + "Auto_Response_Email__c, "
				   + "BusinessUnit__c, "
				   + "Carrier__c, "
				   + "Case_Origin_Detail__c, "
				   + "Case_Team_Hierarchy__c, "
				   + "Closed_Today__c, "
				   + "Consignment_Number__c, "
				   + "Contact_First_Name__c, "
				   + "Contact_Last_Name__c, "
				   + "Count__c, "
				   + "Country__c, "
				   + "Credit_Amount__c, "
				   + "Current_Queue_Name__c, "
				   + "Customer_s_Latest_Email_Subject__c, "
				   + "Customer_Channel__c, "
				   + "Date_Time_Status_Last_Changed__c, "
				   + "EmailMessageId__c, "
				   + "Email_Routing_Rule__c, "
				   + "ERP_Delivery_Number__c, "
				   + "ERP_Order_Number__c, "
				   + "ERP_Returns_Number__c, "
				   + "ERP_Staples_Invoice_Number__c, "
				   + "ERP_Staples_PO_Number__c, "
				   + "First_Call_Resolution__c, "
				   + "Follow_Up_Date_Time__c, "
				   + "InContact_ContactID__c, "
				   + "isIn_Progress__c, "
				   + "Is_Reopened__c, "
				   + "Last_Status_Update__c, "
				   + "isNew__c, "
				   + "New_Email_From_Customer__c, "
				   + "Number_of_Customer_Contacts__c, "
				   + "Original_Queue_Owner__c, "
				   + "Other_Order_Number__c, "
				   + "Overdue_SLAs__c, "
				   + "Owner_Active_Time_in_Hours__c, "
				   + "Owner_Active_Time_in_Seconds__c, "
				   + "Owner_Team_Name__c, "
				   + "isPending_Advisor_Response__c, "
				   + "isPending_Customer_Response__c, "
				   + "isPending_External_Response__c, "
				   + "Reason_Detail__c, "
				   + "Reason_for_Contact__c, "
				   + "isReopened_Pending_Advisor__c, "
				   + "Root_Cause__c, "
				   + "SLA_Start__c, "
				   + "Status_Indicator__c, "
				   + "Sub_Status__c";
		   
		   return fieldList;
		}
	
		// Get the file header	   
		public String getFileHeader() {
		   String fileHeader =
				   "CaseNumber" + "\t"
				   		   + "CreatedDate" + "\t"
				   		   + "Status" + "\t"
				   		   + "Priority" + "\t"
				   		   + "Origin" + "\t"
				   		   + "ClosedDate" + "\t"
				   		   + "Subject" + "\t"
				   		   + "SystemModStamp" + "\t"
						   + "X0_2_hrs_Remaining_SLA__c" + "\t"
						   + "X4_2_hrs_Remaining_SLA__c" + "\t"
						   + "Account_Contact_Relationship_Validated__c" + "\t"
						   + "Account_ERP_Number__c" + "\t"
						   + "Related_Account_ERP_Number__c" + "\t"
						   + "Account_ID__c" + "\t"
						   + "Account_Validated__c" + "\t"
						   + "Approval_Type__c" + "\t"
						   + "Article_Code__c" + "\t"
						   + "Auto_Reply__c" + "\t"
						   + "Auto_Reply_on_Case_Closure__c" + "\t"
						   + "Auto_Reply_Template__c" + "\t"
						   + "Auto_Response_Email__c" + "\t"
						   + "BusinessUnit__c" + "\t"
						   + "Carrier__c" + "\t"
						   + "Case_Origin_Detail__c" + "\t"
						   + "Case_Team_Hierarchy__c" + "\t"
						   + "Closed_Today__c" + "\t"
						   + "Consignment_Number__c" + "\t"
						   + "Contact_First_Name__c" + "\t"
						   + "Contact_Last_Name__c" + "\t"
						   + "Count__c" + "\t"
						   + "Country__c" + "\t"
						   + "Credit_Amount__c" + "\t"
						   + "Current_Queue_Name__c" + "\t"
						   + "Customer_s_Latest_Email_Subject__c" + "\t"
						   + "Customer_Channel__c" + "\t"
						   + "Date_Time_Status_Last_Changed__c" + "\t"
						   + "EmailMessageId__c" + "\t"
						   + "Email_Routing_Rule__c" + "\t"
						   + "ERP_Delivery_Number__c" + "\t"
						   + "ERP_Order_Number__c" + "\t"
						   + "ERP_Returns_Number__c" + "\t"
						   + "ERP_Staples_Invoice_Number__c" + "\t"
						   + "ERP_Staples_PO_Number__c" + "\t"
						   + "First_Call_Resolution__c" + "\t"
						   + "Follow_Up_Date_Time__c" + "\t"
						   + "InContact_ContactID__c" + "\t"
						   + "isIn_Progress__c" + "\t"
						   + "Is_Reopened__c" + "\t"
						   + "Last_Status_Update__c" + "\t"
						   + "isNew__c" + "\t"
						   + "New_Email_From_Customer__c" + "\t"
						   + "Number_of_Customer_Contacts__c" + "\t"
						   + "Original_Queue_Owner__c" + "\t"
						   + "Other_Order_Number__c" + "\t"
						   + "Overdue_SLAs__c" + "\t"
						   + "Owner_Active_Time_in_Hours__c" + "\t"
						   + "Owner_Active_Time_in_Seconds__c" + "\t"
						   + "Owner_Team_Name__c" + "\t"
						   + "isPending_Advisor_Response__c" + "\t"
						   + "isPending_Customer_Response__c" + "\t"
						   + "isPending_External_Response__c" + "\t"
						   + "Reason_Detail__c" + "\t"
						   + "Reason_for_Contact__c" + "\t"
						   + "isReopened_Pending_Advisor__c" + "\t"
						   + "Root_Cause__c" + "\t"
						   + "SLA_Start__c" + "\t"
						   + "Status_Indicator__c" + "\t"
						   + "Sub_Status__c";
		
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
		public String getRecord(Case cas) {
			String fileRow =
				   cas.getCaseNumber() + "\t" +
				   formatDate(cas.getCreatedDate()) + "\t" +
				   cas.getStatus() + "\t" +
				   cas.getPriority() + "\t" +
				   cas.getOrigin() + "\t" +
				   formatDate(cas.getClosedDate()) + "\t" +
				   cas.getSubject() + "\t" +
				   formatDate(cas.getSystemModstamp()) + "\t" +
				   cas.getX0_2_hrs_Remaining_SLA__c() + "\t" + 
				   cas.getX4_2_hrs_Remaining_SLA__c() + "\t" + 
				   cas.getAccount_Contact_Relationship_Validated__c() + "\t" + 
				   cas.getAccount_ERP_Number__c() + "\t" + 
				   cas.getRelated_Account_ERP_Number__c() + "\t" + 
				   cas.getAccount_ID__c() + "\t" + 
				   cas.getAccount_Validated__c() + "\t" + 
				   cas.getApproval_Type__c() + "\t" + 
				   cas.getArticle_Code__c() + "\t" + 
				   cas.getAuto_Reply__c() + "\t" + 
				   cas.getAuto_Reply_on_Case_Closure__c() + "\t" + 
				   cas.getAuto_Reply_Template__c() + "\t" + 
				   cas.getAuto_Response_Email__c() + "\t" + 
				   cas.getBusinessUnit__c() + "\t" + 
				   cas.getCarrier__c() + "\t" + 
				   cas.getCase_Origin_Detail__c() + "\t" + 
				   cas.getCase_Team_Hierarchy__c() + "\t" + 
				   cas.getClosed_Today__c() + "\t" + 
				   cas.getConsignment_Number__c() + "\t" + 
				   cas.getContact_First_Name__c() + "\t" + 
				   cas.getContact_Last_Name__c() + "\t" + 
				   cas.getCount__c() + "\t" + 
				   cas.getCountry__c() + "\t" + 
				   cas.getCredit_Amount__c() + "\t" + 
				   cas.getCurrent_Queue_Name__c() + "\t" + 
				   cas.getCustomer_s_Latest_Email_Subject__c() + "\t" + 
				   cas.getCustomer_Channel__c() + "\t" + 
				   formatDate(cas.getDate_Time_Status_Last_Changed__c()) + "\t" + 
				   cas.getEmailMessageId__c() + "\t" + 
				   cas.getEmail_Routing_Rule__c() + "\t" + 
				   cas.getERP_Delivery_Number__c() + "\t" + 
				   cas.getERP_Order_Number__c() + "\t" + 
				   cas.getERP_Returns_Number__c() + "\t" + 
				   cas.getERP_Staples_Invoice_Number__c() + "\t" + 
				   cas.getERP_Staples_PO_Number__c() + "\t" + 
				   cas.getFirst_Call_Resolution__c() + "\t" + 
				   cas.getFollow_Up_Date_Time__c() + "\t" + 
				   cas.getInContact_ContactID__c() + "\t" + 
				   cas.getIsIn_Progress__c() + "\t" + 
				   cas.getIs_Reopened__c() + "\t" + 
				   cas.getLast_Status_Update__c() + "\t" + 
				   cas.getIsNew__c() + "\t" + 
				   cas.getNew_Email_From_Customer__c() + "\t" + 
				   cas.getNumber_of_Customer_Contacts__c() + "\t" + 
				   cas.getOriginal_Queue_Owner__c() + "\t" + 
				   cas.getOther_Order_Number__c() + "\t" + 
				   cas.getOverdue_SLAs__c() + "\t" + 
				   cas.getOwner_Active_Time_in_Hours__c() + "\t" + 
				   cas.getOwner_Active_Time_in_Seconds__c() + "\t" + 
				   cas.getOwner_Team_Name__c() + "\t" + 
				   cas.getIsPending_Advisor_Response__c() + "\t" + 
				   cas.getIsPending_Customer_Response__c() + "\t" + 
				   cas.getIsPending_External_Response__c() + "\t" + 
				   cas.getReason_Detail__c() + "\t" + 
				   cas.getReason_for_Contact__c() + "\t" + 
				   cas.getIsReopened_Pending_Advisor__c() + "\t" + 
				   cas.getRoot_Cause__c() + "\t" + 
				   formatDate(cas.getSLA_Start__c()) + "\t" + 
				   cas.getStatus_Indicator__c() + "\t" + 
				   cas.getSub_Status__c() ;
		
		   return fileRow;
		}

}
