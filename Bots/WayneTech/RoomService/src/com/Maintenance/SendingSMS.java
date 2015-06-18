package com.Maintenance;

import com.tc.sol.server.util.sms.SMSUtil;

public class SendingSMS extends mainmaintenance
{
public void sendmessage(String Person, String Name, String Roomno, String Message, int Secret){
	SMSUtil.sendSMS(Person, "Name:- " + Name + " Room no:- " + Roomno + " problem:- " + Message + " secret:- " + Secret);
	}
}
