package com.teamchat.integration.datarechargeplans;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;

import org.json.JSONArray;
import org.json.JSONObject;

import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

public class DataRechargePlansBot
{
	@OnKeyword("help")
	public void help(TeamchatAPI api)
	{
		Form f = api.objects().form();
		Field circle = api.objects().select().label("Select your circle").addRegexValidation(".+", "please select one option").name("circleName");
		Field operator = api.objects().select().label("Select your operator").addRegexValidation(".+", "please select one option").name("operatorName");
		Field type = api.objects().select().label("Select select recharge type").addRegexValidation(".+", "please select one option").name("rechargeType");

		ArrayList<String> circleName = CircleOperatorType.getCircleList();
		Collections.sort(circleName);
		
		for (int i=0;i<circleName.size();i++)
		{
			circle.addOption(circleName.get(i));
		}
		
		ArrayList<String> operatorName = CircleOperatorType.getOperatorsList();
		Collections.sort(operatorName);
		
		for (int i=0;i<operatorName.size();i++)
		{
			operator.addOption(operatorName.get(i));
		}
		
		String[] rechType = CircleOperatorType.getRechargeType();
		for (int i=0;i<rechType.length;i++)
		{
			type.addOption(rechType[i]);
		}
		
		f.addField(circle).addField(operator).addField(type);
		api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.help).setReplyScreen(f).setReplyLabel("select")
									.showDetails(true).setDetailsLabel("replies").allowComments(true).alias("rechargeInfo")));
	}
	
	@OnKeyword("plans")
	public void plans(TeamchatAPI api)
	{
		help(api);
	}
	
	@OnAlias("rechargeInfo")
	public void rechargeInfo(TeamchatAPI api) throws Exception
	{
		String circle = api.context().currentReply().getField("circleName");
		String operator = api.context().currentReply().getField("operatorName");
		String rechType = api.context().currentReply().getField("rechargeType");
		rechType = rechType.toLowerCase();

		String circleCode = CircleOperatorType.getCircleCode(circle);
		String operatorCode = CircleOperatorType.getOperatorCode(operator);
		
		System.out.println(circleCode + " " + operatorCode + " " + rechType);
		
		String url = Utility.apiUrl.replace("__CCircle", circleCode).replace("__OOperator", operatorCode).replace("__TType", rechType);
		System.out.println(url);
		URL obj = new URL(url);
		
		HttpURLConnection con = (HttpURLConnection) obj.openConnection();
		con.setRequestMethod("GET");

		// add request header
		con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
		con.setRequestProperty("X-Mashape-Key", Utility.apiKey);
		con.setRequestProperty("Accept", "text/plain");

		int responseCode = con.getResponseCode();

		if (responseCode == 200)
		{
			BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
			String inputLine;
			StringBuffer response = new StringBuffer();

			while ((inputLine = in.readLine()) != null)
			{
				response.append(inputLine);
			}
			in.close();

			System.out.println(response.toString());
			JSONArray jsonArr = new JSONArray(response.toString());
			
			if (jsonArr.length() == 0)
			{
				api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.sorry)));
			}
			else
			{
				StringBuilder resp = new StringBuilder();
				resp.append(Utility.image);
				resp.append(Utility.start + Utility.div.replace("__aa", "Recharge Plans of " + operator + " in " + circle) + Utility.startTable);
				resp.append(Utility.head.replace("__aa", "Recharge Amount").replace("__bb", "Recharge Talktime").replace("__cc", "Recharge Validity").replace("__dd", "Recharge Description"));
				
				int smoke = 0;
				for (int i=0;i<jsonArr.length();i++)
				{
					JSONObject json = jsonArr.getJSONObject(i);
					String rechAmo = json.getString("recharge_amount");
					String rechTT = json.getString("recharge_talktime");
					String rechVali = json.getString("recharge_validity");
					String rechDesc = json.getString("recharge_description");
					rechDesc = rechDesc.replace("|", "<br/>");
					
					if (smoke == 0)
					{
						resp.append(Utility.rowWhite.replace("__aa", rechAmo).replace("__bb", rechTT).replace("__cc", rechVali).replace("__dd", rechDesc));
						smoke = 1;
					}
					else
					{
						resp.append(Utility.rowSmoke.replace("__aa", rechAmo).replace("__bb", rechTT).replace("__cc", rechVali).replace("__dd", rechDesc));
						smoke = 0;
					}
				}
				
				resp.append(Utility.end);
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet().setQuestionHtml(resp.toString())));
			}
		}
		else
		{
			api.perform(api.context().currentRoom().post(new PrimaryChatlet().setQuestionHtml(Utility.sorry)));
		}
	}
}