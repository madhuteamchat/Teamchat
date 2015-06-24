package com.teamchat.integration.office365.bot;

import java.util.ArrayList;
import java.util.TimerTask;

import com.teamchat.integration.office365.database.Office365Subscribe;
import com.teamchat.integration.office365.database.SubList;
import com.teamchat.integration.office365.webhook.RenewWebhook;

public class RenewSubSchedule extends TimerTask
{

	@Override
	public void run() {
		// TODO Auto-generated method stub
		Office365Subscribe offsub=new Office365Subscribe();
		ArrayList<SubList> sublist=offsub.retreiveAll();
		for(int i=0;i<sublist.size();i++)
		{
			System.out.println(sublist.get(i).teamchat_id+"==============="+sublist.get(i).sub_id);
			new RenewWebhook().renewSubscription(sublist.get(i).teamchat_id, sublist.get(i).sub_id);
		}
	}

}
