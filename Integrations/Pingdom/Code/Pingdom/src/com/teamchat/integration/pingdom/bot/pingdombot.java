package com.teamchat.integration.pingdom.bot;

import java.io.IOException;
import java.sql.SQLException;



/*
 * *
 * @author:Anuj Arora
 */
import com.google.gson.Gson;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.integration.pingdom.classes.Check;
import com.teamchat.integration.pingdom.classes.Checks;


public class pingdombot {

	String Username, App_key, roomId, url;
	String Password;
	int count = 1;

//keyword for helping the user in understanding the procedure.

	@OnKeyword("help")
	public void help(TeamchatAPI api) {

		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<h4><b>Hi, I'm Pingdom Bot.</b></h4>"
								+ "<img src=\"http://monitorama.com/2014/images/logo_pingdom.png\" height=\"90\" width=\"230\">"
								+ "<br />"
								+ "<br /><b>You can use me to check the status of your website, using following keywords:</b>"
								+ "<br />"
								+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:black\";><b>ping - </b></a1><a2 style=\"color:#359FD8\";>"
								+ "Type this command to Check the status for your website added in your pingdom account."
								+ "</a2></li><li><a3 style=\"color:black\";><b>addcheck - </b></a3><a4 style=\"color:#359FD8\";>"
								+ "Type this command to create any check i.e to add any website in your pingdom account."
								+ "</a4></li><li><a5 style=\"color:black\";><b>deletecheck - </b></a5><a6 style=\"color:#359FD8\";>"
								+ "Type this command to delete any check i.e to delete any website in your pingdom account."
								+ "</a6></li><li><a7 style=\"color:black\";><b>login - </b></a5><a6 style=\"color:#359FD8\";>"
								+ "Type this command if you are logging for the first time from this room or you want to log in from other account."
								+ "</a6></li><li><a7 style=\"color:black\";><b>logout - </b></a7><a8 style=\"color:#359FD8\";>"
								+ "Type this command to log out from your existing account."
								+ "</a8></li></ul>")));

		// list of keywords
		// 1.)ping
		// 2.)add
		// 3.)delete

	}

//For logging into the pingdom server
//If the user is logging in for the first time then only teamchat will ask him/her for his/her username,password and appkey else 
//he/she will automatically get logged into the pingdom server untill n unless he/she logs out(the session will be maintained)
// If any other user needs to log in, Then he/she should use "login" keyword.Hence,the existing user will be logged out automatically.
//Sessions are made with the help of roomId..

	@OnKeyword("login")
	public void login(TeamchatAPI api) {

		Form f = api.objects().form();
		f.addField(api.objects().input().label("Username").name("Username"));
		f.addField(api.objects().input().label("Password").name("Password"));
		f.addField(api.objects().input().label("App_key").name("App_key"));

		PrimaryChatlet prime = new PrimaryChatlet();
		prime.setQuestionHtml("<h4><b>Enter your login credentials :</b></h4>")
				.setReplyScreen(f).setReplyLabel("Enter").alias("setdata");

		api.perform(api.context().currentRoom().post(prime));

	}

//for logging out of the server.

	@OnKeyword("logout")
	public void logout(TeamchatAPI api) throws SQLException {

		String rmid;
		rmid = api.context().currentRoom().getId();
		
		DBHandler chk = new DBHandler();
		boolean flag;
		flag = chk.roomchk(rmid);
		if (flag == true) {
			DBHandler datacng = new DBHandler();
			
			datacng.stmt.executeUpdate("DELETE FROM "+datacng.configProps.getProperty("tablename").trim()+
					" WHERE roomId='" + rmid + "'");
			datacng.conn.close();
			PrimaryChatlet prime = new PrimaryChatlet();
			prime.setQuestionHtml("<h5><b>You are successfully logged out!!</b></h5>");
			api.perform(api.context().currentRoom().post(prime));
		} else {
			PrimaryChatlet prime = new PrimaryChatlet();
			prime.setQuestionHtml("<h5><b>You are not logged in!!</b></h5>");
			api.perform(api.context().currentRoom().post(prime));

		}

	}

//inserting the data into the database.
	@OnAlias("setdata")
	public void setdata(TeamchatAPI api) throws SQLException {

		String Username = api.context().currentReply().getField("Username");
		String Password = api.context().currentReply().getField("Password");
		String App_key = api.context().currentReply().getField("App_key");

		this.Username = Username;
		this.Password = Password;
		this.App_key = App_key;
		roomId = api.context().currentRoom().getId();
		DBHandler ob4 = new DBHandler();
		boolean flag;
		flag = ob4.roomchk(roomId);
		if (flag == false) {
			DBHandler ob1 = new DBHandler();
			ob1.setData(Username, Password, this.App_key, roomId);
		} else {
			DBHandler datacng = new DBHandler();
			datacng.stmt.executeUpdate("UPDATE "+datacng.configProps.getProperty("tablename").trim()
					+ " SET username ='" + Username + "'" + ", pass ='"
					+ Password + "'" + ", appkey ='" + App_key + "'"
					+ " where roomId='" + roomId + "'");
			datacng.conn.close();
			
		}
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<h5><b>You are logged in successfully!!</b></h5>")));

	}


//for finding the status of the url given by the user that whether it is up or down and its last response time.

	@OnKeyword("ping")
	public void ping(TeamchatAPI api) throws IOException, SQLException {
		String rmid;
		count=1;
//getting roomid
		rmid = api.context().currentRoom().getId();
		DBHandler ob4 = new DBHandler();
		boolean flag;
		flag = ob4.roomchk(rmid);
//checking if roomid is available in the database.If it is available then user will be logged in automatically n flag will become true else false
		if (flag == false) {
			PrimaryChatlet prime = new PrimaryChatlet();
			prime.setQuestionHtml("<br /><b>First you have to sign in!!</b>"
					+ "<br /><b>Note: </b><a1  style=\"color:#359FD8\";>For Signing in type \"login\" command</a1>");

			api.perform(api.context().currentRoom().post(prime));
		}

		else {
			DBHandler ob5 = new DBHandler();
			String[] usrpassapp = ob5.getusrpassapp(rmid);
			Username = usrpassapp[0];
			Password = usrpassapp[1];
			App_key = usrpassapp[2];
			url = "https://api.pingdom.com/api/2.0/checks";
			pingdomintegrator ob1 = new pingdomintegrator();
			String resp = ob1.getChecks(Username, Password, App_key, url);

//error if there is some issue in the authentication.

			if (resp.equals("Error")) {
				PrimaryChatlet prime = new PrimaryChatlet();
				api.perform(api
						.context()
						.currentRoom()
						.post(prime.setQuestionHtml("<br /><b>Error :</b>"
								+ "<br /><b>Status Code: 403</b>"
								+ "<br /><b>StatusDesc: Forbidden</b>"
								+ "<br /><b>Something went wrong!</b>")));

				
			}

			else {

				// parsing a json like this

				// {
				// "checks": [
				// {
				// "id": 1650784,
				// "created": 1435769579,
				// "name": "google",
				// "hostname": "www.google.com",
				// "use_legacy_notifications": true,
				// "resolution": 1,
				// "type": "http",
				// "lasttesttime": 1435806884,
				// "lastresponsetime": 167,
				// "status": "up",
				// "probe_filters": []
				// },
				// {
				// "id": 1650785,
				// "created": 1435769756,
				// "name": "Teamchat",
				// "hostname": "www.teamchat.com",
				// "use_legacy_notifications": true,
				// "resolution": 1,
				// "type": "http",
				// "lasttesttime": 1435806881,
				// "lastresponsetime": 725,
				// "status": "up",
				// "probe_filters": []
				// },
				// {
				// "id": 1650787,
				// "created": 1435769804,
				// "name": "Interns Teamchat",
				// "hostname": "interns.teamchat.com",
				// "use_legacy_notifications": true,
				// "resolution": 1,
				// "type": "http",
				// "lasterrortime": 1435806882,
				// "lasttesttime": 1435806882,
				// "lastresponsetime": 0,
				// "status": "down",
				// "probe_filters": []
				// }
				// ],
				// "counts": {
				// "total": 3,
				// "limited": 3,
				// "filtered": 3
				// }
				// }
				
				Gson gson = new Gson();
				Checks checks = gson.fromJson(resp, Checks.class);
				Form fo = api.objects().form();
			
				

				for (Check check : checks.getChecks()) {
					
			
					fo.addField(api
							.objects()
							.select()
							.name("Checks" + String.valueOf(count))
							.label("Select check " + String.valueOf(count)
									+ ":").addOption(check.getHostname())
							.addOption("none"));

					
					count++;
			}


	
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
								.setQuestionHtml("<br /><b>Select Checks from below :</b>")
								.setReplyScreen(fo).setReplyLabel("Enter")
								.alias("sendreqcheck")));

			}

		}
	}

//inserting checks into your pingdom account.

	@OnKeyword("addcheck")
	public void addcheck(TeamchatAPI api) throws IOException, SQLException {
		String rmid;
		rmid = api.context().currentRoom().getId();
		DBHandler ob4 = new DBHandler();
		boolean flag;
		flag = ob4.roomchk(rmid);

		if (flag == false) {
			PrimaryChatlet prime = new PrimaryChatlet();
			prime.setQuestionHtml("<br /><b>First you have to sign in!!</b>"
					+ "<br /><b>Note: </b><a1  style=\"color:#359FD8\";>For Signing in type \"login\" command</a1>");

			api.perform(api.context().currentRoom().post(prime));
		}

		else {
			DBHandler ob5 = new DBHandler();
			String[] usrpassapp = ob5.getusrpassapp(rmid);
			Username = usrpassapp[0];
			Password = usrpassapp[1];
			App_key = usrpassapp[2];
			url = "https://api.pingdom.com/api/2.0/checks";
			PrimaryChatlet prime = new PrimaryChatlet();
			Form f = api.objects().form();
			f.addField(api.objects().input().label("Check Name :").name("name"));
			f.addField(api.objects().input().label("Hostname :").name("host"));
			f.addField(api.objects().select().name("Protocol")
					.label("Select any protocol :").addOption("http")
					.addOption("https"));

			prime.setQuestionHtml(
					"<h5><b>Enter the Check details below:</b></h5>")
					.setReplyScreen(f).setReplyLabel("Enter")
					.alias("sendreqadd");
			api.perform(api.context().currentRoom().post(prime));
			


		}
	}

//deleting checks from your pingdom account.

	@OnKeyword("deletecheck")
	public void deletecheck(TeamchatAPI api) throws IOException, SQLException {
		String rmid;
		count=1;
		rmid = api.context().currentRoom().getId();
		DBHandler ob4 = new DBHandler();
		boolean flag;
		flag = ob4.roomchk(rmid);

		if (flag == false) {
			PrimaryChatlet prime = new PrimaryChatlet();
			prime.setQuestionHtml("<br /><b>First you have to sign in!!</b>"
					+ "<br /><b>Note: </b><a1  style=\"color:#359FD8\";>For Signing in type \"login\" command</a1>");

			api.perform(api.context().currentRoom().post(prime));
		}

		else {
			DBHandler ob5 = new DBHandler();
			String[] usrpassapp = ob5.getusrpassapp(rmid);
			Username = usrpassapp[0];
			Password = usrpassapp[1];
			App_key = usrpassapp[2];
			url = "https://api.pingdom.com/api/2.0/checks";
			pingdomintegrator ob1 = new pingdomintegrator();
			String resp = ob1.getChecks(Username, Password, App_key, url);

			if (resp.equals("Error")) {
				PrimaryChatlet prime = new PrimaryChatlet();
				api.perform(api
						.context()
						.currentRoom()
						.post(prime.setQuestionHtml("<br /><b>Error :</b>"
								+ "<br /><b>Status Code: 403</b>"
								+ "<br /><b>StatusDesc: Forbidden</b>"
								+ "<br /><b>Something went wrong!</b>")));

				
			}

			else {

				Gson gson = new Gson();
				Checks checks = gson.fromJson(resp, Checks.class);
				Form fo = api.objects().form();

				for (Check check : checks.getChecks()) {

					fo.addField(api
							.objects()
							.select()
							.name("Checks" + String.valueOf(count))
							.label("Select check " + String.valueOf(count)
									+ ":").addOption(check.getHostname()).addOption("none"));

			
					count++;

				}

				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
								.setQuestionHtml("<br /><b>Select Checks from below :</b>")
								.setReplyScreen(fo).setReplyLabel("Enter")
								.alias("sendreqdel")));

			}

		}
	}

//communicating with pingdomintegrator class
	@OnAlias("sendreqdel")
	public void sendreqdel(TeamchatAPI api) throws IOException {

		int i;
		String chks[] = new String[count-1];
		int s = 1;
		for (i = 0; i < count - 1; i++) {

			chks[i] = api.context().currentReply()
					.getField("Checks" + String.valueOf(s));
			s++;
		}

		s = 1;
		url = "https://api.pingdom.com/api/2.0/checks";
		pingdomintegrator ob1 = new pingdomintegrator();
		
		String resp = ob1.getChecks(Username, Password, App_key, url);
		Gson gson = new Gson();
		Checks checks = gson.fromJson(resp, Checks.class);

		int k = 0;

		int del[] = new int[count-1];
	
		for (Check check : checks.getChecks()) {
			if (check.getHostname().equals(chks[k])) {
			
				del[k] = check.getId();
			} else {

				del[k] = 1;
			}
           k++;
		}

		k = 1;
		
		String respdel = ob1.delChecks(Username, Password, App_key, del);

		if (respdel.equals("Error")) {
			PrimaryChatlet prime = new PrimaryChatlet();
			api.perform(api
					.context()
					.currentRoom()
					.post(prime.setQuestionHtml("<br /><b>Error :</b>"
							+ "<br /><b>Status Code: 403</b>"
							+ "<br /><b>StatusDesc: Forbidden</b>"
							+ "<br /><b>Something went wrong!</b>")));

		
		}

		else {
			PrimaryChatlet prime = new PrimaryChatlet();
			api.perform(api
					.context()
					.currentRoom()
					.post(prime.setQuestionHtml("<br /><b>" + respdel + "</b>")));
		}
	}



	@OnAlias("sendreqadd")
	public void sendreqadd(TeamchatAPI api) throws IOException {
		String name = api.context().currentReply().getField("name");
		String host = api.context().currentReply().getField("host");
		String Protocol = api.context().currentReply().getField("Protocol");

		pingdomintegrator ob1 = new pingdomintegrator();
		
		String resp = ob1.addChecks(Username, Password, App_key, host, name,Protocol);
		
		if (resp.equals("Error")) {
			PrimaryChatlet prime = new PrimaryChatlet();
			api.perform(api
					.context()
					.currentRoom()
					.post(prime.setQuestionHtml("<br /><b>Error :</b>"
							+ "<br /><b>Status Code: 403</b>"
							+ "<br /><b>StatusDesc: Forbidden</b>"
							+ "<br /><b>Something went wrong!</b>")));

			
		}

		else {

			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<br /><b>" + resp + "</b>")));

		}

	}
//communicating with pingdomintegrator class

	@OnAlias("sendreqcheck")
	public void sendreqcheck(TeamchatAPI api) throws IOException {

		int i;
		String chks[] = new String[count-1];
		int s = 1;
		for (i = 0; i < count - 1; i++) {

			chks[i] = api.context().currentReply()
					.getField("Checks" + String.valueOf(s));
			s++;
		}
		count = 1;
		s = 1;
		url = "https://api.pingdom.com/api/2.0/checks";
		pingdomintegrator ob1 = new pingdomintegrator();
		String resp = ob1.getChecks(Username, Password, App_key, url);

		Gson gson = new Gson();
		Checks checks = gson.fromJson(resp, Checks.class);

		int k = 0;
		String color;
		color = "#359FD8";
		// color="#CF0909";--red
		// color="#24970D";---green
		int len = chks.length;
		for (Check check : checks.getChecks()) {
			for(k=0;k<len;k++)
			{
			if (check.getHostname().equals(chks[k])) {
				if (check.getStatus().equals("up")) {

					color = "#24970D";

				} else if (check.getStatus().equalsIgnoreCase("down")) {
					color = "#CF0909";

				}
				PrimaryChatlet prime1 = new PrimaryChatlet();
				prime1.setQuestionHtml("<h4><b>Host Details "
						+ String.valueOf(s)
						+ ":</b></h4>"
						+ "<ul type=\"square\"; style=\"color:#359FD8\";><li><a1 style=\"color:black\";><b>Name- </b></a1><a2 style=\"color:#359FD8\";>"
						+ check.getName()
						+ "</a2></li><li><a3 style=\"color:black\";><b>Hostname- </b></a3><a4 style=\"color:#359FD8\";>"
						+ check.getHostname()
						+ "</a4></li><li><a5 style=\"color:black\";><b>Status - </b></a5><a6 style=\"color:"
						+ color
						+ "\";>"
						+ check.getStatus()
						+ "</a6></li><li><a7 style=\"color:black\";><b>Last Response Time - </b></a7><a8 style=\"color:#359FD8\";>"
						+ check.getLastresponsetime()
						+" ms"
						+ "</a8></li><li><a9 style=\"color:black\";><b>Type - </b></a9><a10 style=\"color:#359FD8\";>"
						+ check.getType() + "</a10></li></ul>");
				api.perform(api.context().currentRoom().post(prime1));

				

				s++;
				
			}
			
			}
			
		}
		
		s = 1;
	}
}
