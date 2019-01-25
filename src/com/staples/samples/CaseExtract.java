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
				       "CASENUMBER" + "\t" +
					   "CREATEDDATE" + "\t" +
					   "STATUS"+ "\t" +
					   "PRIORITY" + "\t" +
					   "ORIGIN" + "\t" +
					   "CLOSEDDATE" + "\t" +
					   "SYSTEMMODSTAMP"+ "\t" + 
				   	   "0-2 HRS REMAINING SLA" + "\t" + 
					   "4-2 HRS REMAINING SLA" + "\t" + 
					   "ACCOUNT CONTACT RELATIONSHIP VALIDATED" + "\t" + 
					   "ACCOUNT ERP NUMBER" + "\t" + 
					   "ACCOUNT ERP NUMBER" + "\t" + 
					   "ACCOUNT ID" + "\t" + 
					   "ACCOUNT VALIDATED" + "\t" + 
					   "APPROVAL TYPE" + "\t" + 
					   "ARTICLE CODE" + "\t" + 
					   "AUTO-REPLY" + "\t" + 
					   "AUTO-REPLY ON CASE CLOSURE" + "\t" + 
					   "AUTO-REPLY TEMPLATE" + "\t" + 
					   "AUTO-RESPONSE EMAIL" + "\t" + 
					   "BUSINESS UNIT" + "\t" + 
					   "CARRIER" + "\t" + 
					   "CASE ORIGIN DETAIL" + "\t" + 
					   "CASE TEAM HIERARCHY" + "\t" + 
					   "CLOSED TODAY" + "\t" + 
					   "CONSIGNMENT NUMBER" + "\t" + 
					   "CONTACT FIRST NAME" + "\t" + 
					   "CONTACT LAST NAME" + "\t" + 
					   "COUNT" + "\t" + 
					   "COUNTRY" + "\t" + 
					   "CREDIT AMOUNT" + "\t" + 
					   "CURRENT QUEUE" + "\t" + 
					   "CUSTOMER'S LATEST EMAIL SUBJECT" + "\t" + 
					   "CUSTOMER CHANNEL" + "\t" + 
					   "DATE/TIME STATUS LAST CHANGED" + "\t" + 
					   "EMAILMESSAGEID" + "\t" + 
					   "EMAIL ROUTING RULE" + "\t" + 
					   "ERP DELIVERY NUMBER" + "\t" + 
					   "ERP ORDER NUMBER" + "\t" + 
					   "ERP RETURNS NUMBER" + "\t" + 
					   "ERP STAPLES INVOICE NUMBER" + "\t" + 
					   "ERP STAPLES PO NUMBER" + "\t" + 
					   "FIRST CONTACT RESOLUTION" + "\t" + 
					   "FOLLOW-UP DATE/TIME" + "\t" + 
					   "INCONTACT CONTACTID" + "\t" + 
					   "IN PROGRESS" + "\t" + 
					   "IS REOPENED" + "\t" + 
					   "LAST STATUS UPDATE" + "\t" + 
					   "NEW" + "\t" + 
					   "NEW EMAIL FROM CUSTOMER?" + "\t" + 
					   "NUMBER OF CUSTOMER CONTACTS" + "\t" + 
					   "ORIGINAL QUEUE OWNER" + "\t" + 
					   "OTHER ORDER NUMBER" + "\t" + 
					   "OVERDUE SLAS" + "\t" + 
					   "OWNER ACTIVE TIME IN HOURS" + "\t" + 
					   "OWNER ACTIVE TIME IN SECONDS" + "\t" + 
					   "OWNER TEAM NAME" + "\t" + 
					   "PENDING ADVISOR RESPONSE" + "\t" + 
					   "PENDING CUSTOMER RESPONSE" + "\t" + 
					   "PENDING EXTERNAL RESPONSE" + "\t" + 
					   "REASON DETAIL" + "\t" + 
					   "REASON FOR CONTACT" + "\t" + 
					   "REOPENED - PENDING ADVISOR" + "\t" + 
					   "ROOT CAUSE" + "\t" + 
					   "SLA START" + "\t" + 
					   "STATUS INDICATOR" + "\t" + 
					   "SUB-STATUS";
		
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
