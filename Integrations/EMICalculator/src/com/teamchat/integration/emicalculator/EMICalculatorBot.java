package com.teamchat.integration.emicalculator;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.client.sdk.chatlets.TextChatlet;

public class EMICalculatorBot
{
	@OnKeyword("help")
	public void help(TeamchatAPI api)
	{
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.help)));
	}

	@OnKeyword("emi")
	public void emi(TeamchatAPI api) throws Exception
	{
		Form f = api.objects().form();
		f.addField(api.objects().input().label("Principal").name("principal"));
		f.addField(api.objects().input().label("Rate of interest").name("rate"));
		f.addField(api.objects().input().label("Period (in months)").name("period"));

		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("Enter Principal, Rate of Interest and Period(in months)").setReplyScreen(f).setReplyLabel("Enter").alias("emi")));
	}

	@OnAlias("emi")
	public void calculate(TeamchatAPI api)
	{
		try
		{
			double principal = Double.parseDouble(api.context().currentReply().getField("principal"));
			double rate = Double.parseDouble(api.context().currentReply().getField("rate"));
			double period = Double.parseDouble(api.context().currentReply().getField("period"));

			EMCalc eCalc = new EMCalc();
			eCalc.calcEmiAllMonths(principal, rate, period, api);
		}
		catch (Exception e)
		{
			api.perform(api.context().currentRoom().post(new TextChatlet("please enter correct values")));
		}
	}

	public class EMCalc
	{
		public Double calcEmi(double p, double r, double n)
		{
			double R = (r / 12) / 100;
			double e = (p * R * (Math.pow((1 + R), n)) / ((Math.pow((1 + R), n)) - 1));

			return e;
		}

		public void calcEmiAllMonths(double p, double r, double n, TeamchatAPI api)
		{
			double R = (r / 12) / 100;
			double P = p;
			double e = calcEmi(P, r, n);
			double totalInt = Math.round((e * n) - p);
			double totalAmt = Math.round((e * n));
			System.out.println("***************************");
			System.out.println(" Principal-> " + P);
			System.out.println(" Rate of Interest-> " + r);
			System.out.println(" Number of Months-> " + n);
			System.out.println(" EMI -> " + Math.round(e));
			System.out.println(" Total Interest -> " + totalInt);
			System.out.println(" Total Amount -> " + totalAmt);
			System.out.println("***************************");
			double intPerMonth = Math.round(totalInt / n);
			
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml("<center><img src='http://integration.teamchat.com/sol/bot-images/emi.jpg' width='150' /></center><p style='padding-right:5px; padding-left:5px; margin-top:5px'><b>Principal: </b>" + P + "<br/><b>Rate of Interest: </b>" + r + "<br/><b>Number of Months: </b>" + (int) n
					+ "<br/><b>EMI: </b>" + Math.round(e)
					+ "<br/><b>Total Interest: </b>" + totalInt
					+ "<br/><b>Total Amount: </b>" + totalAmt
					+ "</p>")));

			StringBuilder emi = new StringBuilder();

			emi.append(Utility.start);
			emi.append(Utility.div.replace("__aa", "Per Month"));
			emi.append(Utility.startTable);
			emi.append(Utility.head.replace("__aa", "Month").replace("__bb", "Interest per month").replace("__cc", "Principal per month").replace("__dd", "Balance Principal"));
			
			int smoke = 0;
			for (double i = 1; i <= n; i++)
			{
				intPerMonth = (P * R);
				P = ((P) - ((e) - (intPerMonth)));
				System.out.println(" Month -> " + (int) i);
				System.out.println(" Interest per month -> " + Math.round(intPerMonth));
				System.out.println(" Principal per month -> " + Math.round((e) - intPerMonth));
				System.out.println(" Balance Principal -> " + Math.round(P));
				System.out.println("***************************");

				if (smoke == 0)
				{
					emi.append(Utility.rowWhite.replace("__aa", (int) i + "").replace("__bb", Math.round(intPerMonth) + "").replace("__cc", Math.round((e) - intPerMonth) + "").replace("__dd", Math.round(P) + ""));
					smoke = 1;
				}
				else
				{
					emi.append(Utility.rowSmoke.replace("__aa", (int) i + "").replace("__bb", Math.round(intPerMonth) + "").replace("__cc", Math.round((e) - intPerMonth) + "").replace("__dd", Math.round(P) + ""));
					smoke = 0;
				}
			}
			
			emi.append(Utility.end);
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(emi.toString())));
		}
	}
}