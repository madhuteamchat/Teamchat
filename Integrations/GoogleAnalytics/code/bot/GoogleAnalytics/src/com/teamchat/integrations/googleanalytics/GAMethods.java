package com.teamchat.integrations.googleanalytics;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import org.json.JSONArray;
import org.json.JSONObject;

public class GAMethods {

	public String[][] getAccounts(String access_token) {
		// TODO Auto-generated method stub
		String inputline = "";
		String result = "";
		String[][] print = new String[100][2];
		String contextPath = "https://www.googleapis.com/analytics/v3/management/accounts?access_token="
				+ access_token
				+ "&client_id="
				+ PropertiesFile.getValue("client_id")
				+ "&scope="
				+ PropertiesFile.getValue("scope");
		try {
			URL urldemo = new URL(contextPath);
			HttpURLConnection yc = (HttpURLConnection) urldemo.openConnection();
			int resp_code = yc.getResponseCode();
			if (resp_code == 401) {
				GARenewToken rt = new GARenewToken();
				String refreshtoken = ManageDB.getrefreshToken(access_token);
				return getAccounts(rt.getnewaccesstoken(refreshtoken));
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while ((inputline = in.readLine()) != null) {
				result += inputline;
				System.err.println(result);
			}
			in.close();
			JSONObject jobj = new JSONObject(result);
			JSONArray jitems = jobj.getJSONArray("items");
			for (int i = 0; i < jitems.length(); i++) {
				JSONObject jacc = jitems.getJSONObject(i);
				print[i][0] = jacc.getString("name");
				print[i][1] = jacc.getString("id");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return print;
	}

	public String getAccountID(String access_token, String account_name) {
		// TODO Auto-generated method stub
		String inputline = "";
		String result = "";
		String account_id = null;
		String contextPath = "https://www.googleapis.com/analytics/v3/management/accounts?access_token="
				+ access_token
				+ "&client_id="
				+ PropertiesFile.getValue("client_id")
				+ "&scope="
				+ PropertiesFile.getValue("scope");
		try {
			URL urldemo = new URL(contextPath);
			HttpURLConnection yc = (HttpURLConnection) urldemo.openConnection();
			int resp_code = yc.getResponseCode();
			if (resp_code == 401) {
				GARenewToken rt = new GARenewToken();
				String refreshtoken = ManageDB.getrefreshToken(access_token);
				return getAccountID(rt.getnewaccesstoken(refreshtoken),
						account_name);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while ((inputline = in.readLine()) != null) {
				result += inputline;
				System.err.println(result);
			}
			in.close();
			JSONObject jobj = new JSONObject(result);
			JSONArray jitems = jobj.getJSONArray("items");
			for (int i = 0; i < jitems.length(); i++) {
				JSONObject jacc = jitems.getJSONObject(i);
				if (account_name.equals(jacc.getString("name"))) {
					account_id = jacc.getString("id");
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return account_id;
	}

	public String[][] getProperties(String access_token, String account_id) {
		// TODO Auto-generated method stub
		String inputline = "";
		String result = "";
		String[][] print = new String[100][2];
		String contextPath = "https://www.googleapis.com/analytics/v3/management/accounts/"
				+ account_id
				+ "/webproperties?access_token="
				+ access_token
				+ "&client_id="
				+ PropertiesFile.getValue("client_id")
				+ "&scope=" + PropertiesFile.getValue("scope");
		try {
			URL urldemo = new URL(contextPath);
			HttpURLConnection yc = (HttpURLConnection) urldemo.openConnection();
			int resp_code = yc.getResponseCode();
			if (resp_code == 401) {
				GARenewToken rt = new GARenewToken();
				String refreshtoken = ManageDB.getrefreshToken(access_token);
				return getProperties(rt.getnewaccesstoken(refreshtoken),
						account_id);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while ((inputline = in.readLine()) != null) {
				result += inputline;
				System.err.println(result);
			}
			in.close();
			JSONObject jobj = new JSONObject(result);
			JSONArray jitems = jobj.getJSONArray("items");
			for (int i = 0; i < jitems.length(); i++) {
				JSONObject jacc = jitems.getJSONObject(i);
				print[i][0] = jacc.getString("name");
				print[i][1] = jacc.getString("defaultProfileId");
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return print;
	}

	public String getPropertyID(String access_token, String account_id,
			String property_name) {
		// TODO Auto-generated method stub
		String inputline = "";
		String result = "";
		String property_id = null;
		// String[][] print = new String[100][2];
		String contextPath = "https://www.googleapis.com/analytics/v3/management/accounts/"
				+ account_id
				+ "/webproperties?access_token="
				+ access_token
				+ "&client_id="
				+ PropertiesFile.getValue("client_id")
				+ "&scope=" + PropertiesFile.getValue("scope");
		try {
			URL urldemo = new URL(contextPath);
			HttpURLConnection yc = (HttpURLConnection) urldemo.openConnection();
			int resp_code = yc.getResponseCode();
			if (resp_code == 401) {
				GARenewToken rt = new GARenewToken();
				String refreshtoken = ManageDB.getrefreshToken(access_token);
				return getPropertyID(rt.getnewaccesstoken(refreshtoken),
						account_id, property_name);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while ((inputline = in.readLine()) != null) {
				result += inputline;
				System.err.println(result);
			}
			in.close();
			JSONObject jobj = new JSONObject(result);
			JSONArray jitems = jobj.getJSONArray("items");
			for (int i = 0; i < jitems.length(); i++) {
				JSONObject jacc = jitems.getJSONObject(i);
				if (property_name.equals(jacc.getString("name"))) {
					property_id = jacc.getString("defaultProfileId");
					break;
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return property_id;
	}

	public String getActiveusers(String access_token, String property_id) {
		// TODO Auto-generated method stub
		String inputline = "", print = "";
		String result = "", medium1 = "", city1 = "", device1 = "", browser1 = "";
		String[] medium = new String[100];
		String[] city = new String[100];
		String[] device = new String[100];
		String[] browser = new String[100];
		String contextPath = "https://www.googleapis.com/analytics/v3/data/realtime?ids=ga:"
				+ property_id
				+ "&metrics=rt:activeUsers&dimensions=rt:medium,rt:city,rt:devicecategory,rt:browser&access_token="
				+ access_token
				+ "&client_id="
				+ PropertiesFile.getValue("client_id")
				+ "&scope="
				+ PropertiesFile.getValue("scope");
		try {
			URL urldemo = new URL(contextPath);
			HttpURLConnection yc = (HttpURLConnection) urldemo.openConnection();
			int resp_code = yc.getResponseCode();
			if (resp_code == 401) {
				GARenewToken rt = new GARenewToken();
				String refreshtoken = ManageDB.getrefreshToken(access_token);
				return getActiveusers(rt.getnewaccesstoken(refreshtoken),
						property_id);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while ((inputline = in.readLine()) != null) {
				result += inputline;
				System.err.println(result);
			}
			in.close();
			JSONObject jobj = new JSONObject(result);
			String active_users = jobj.getJSONObject("totalsForAllResults")
					.getString("rt:activeUsers");
			if (Integer.parseInt(active_users) > 0) {
				if (jobj.has("rows")) {
					JSONArray jarray = jobj.getJSONArray("rows");
					System.out.println();
					for (int i = 0; i < jobj.getInt("totalResults"); i++) {
						JSONArray jarray1 = jarray.getJSONArray(i);
						medium[i] = jarray1.get(0).toString();
						city[i] = jarray1.get(1).toString();
						device[i] = jarray1.get(2).toString();
						browser[i] = jarray1.get(3).toString();
					}
					medium = RemoveDuplicate.remove(medium);
					city = RemoveDuplicate.remove(city);
					device = RemoveDuplicate.remove(device);
					browser = RemoveDuplicate.remove(browser);
					System.out
							.println("*****************************************");
					System.out.println("no.of active users = " + active_users);
					for (int i = 0; i < medium.length; i++) {
						if (medium[i] != null) {
							System.out.println(medium[i]);
							medium1 = medium1 + ", " + medium[i];
						}
					}
					medium1 = medium1.substring(2);
					for (int i = 0; i < city.length; i++) {
						if (city[i] != null) {
							System.out.println(city[i]);
							city1 = city1 + ", " + city[i];
						}
					}
					city1 = city1.substring(2);
					for (int i = 0; i < device.length; i++) {
						if (device[i] != null) {
							System.out.println(device[i]);
							device1 = device1 + ", " + device[i];
						}
					}
					device1 = device1.substring(2);
					for (int i = 0; i < browser.length; i++) {
						if (browser[i] != null) {
							System.out.println(browser[i]);
							browser1 = browser1 + ", " + browser[i];
						}
					}
					browser1 = browser1.substring(2);
					print = print
							+ "<!DOCTYPE html><html><body><div style=\"overflow:scroll;overflow-y:scroll;overflow-x:scroll;\">"
							+ "<table border=\"1\"  style=\"width:75%\">"
							+ "<tr>"
							+ "<td >Medium</td>"
							+ "<td>"
							+ medium1
							+ "</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td>City</td>"
							+ "<td>"
							+ city1
							+ "</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td>Device</td>"
							+ "<td>"
							+ device1
							+ "</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td>Browser</td>"
							+ "<td>"
							+ browser1
							+ "</td>"
							+ "</tr>"
							+ "<tr>"
							+ "<td><div align=\"right\">Active Users Count</div></td>"
							+ "<td>" + active_users + "</td>" + "</tr>"
							+ "</table>" + "</div></body></html>";
					System.out.println("*********" + print);
				}
			} else {
				print = "<b>No current users</b>";
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("Returning........." + print
				+ " *********************************************");
		return print;
	}

	public String getPeriodicReport(String access_token, String property_id,
			String start_date, String end_date) {
		// TODO Auto-generated method stub
		String inputline = "", print = "";
		String result = "";
		String pageviews = null, sessions = null, new_users = null, users = null, avg_session_duration = null;
		String contextPath = "https://www.googleapis.com/analytics/v3/data/ga?ids=ga:"
				+ property_id
				+ "&start-date="
				+ start_date
				+ "&end-date="
				+ end_date
				+ "&metrics=ga:pageviews,ga:sessions,ga:newUsers,ga:users,ga:avgSessionDuration&dimensions=ga:browser,ga:city&access_token="
				+ access_token
				+ "&client_id="
				+ PropertiesFile.getValue("client_id")
				+ "&scope="
				+ PropertiesFile.getValue("scope");
		try {
			URL urldemo = new URL(contextPath);
			HttpURLConnection yc = (HttpURLConnection) urldemo.openConnection();
			int resp_code = yc.getResponseCode();
			if (resp_code == 401) {
				GARenewToken rt = new GARenewToken();
				String refreshtoken = ManageDB.getrefreshToken(access_token);
				return getPeriodicReport(rt.getnewaccesstoken(refreshtoken),
						property_id, start_date, end_date);
			}
			BufferedReader in = new BufferedReader(new InputStreamReader(
					yc.getInputStream()));
			while ((inputline = in.readLine()) != null) {
				result += inputline;
				System.err.println(result);
			}
			in.close();

			JSONObject jobj = new JSONObject(result);
			JSONObject jres = jobj.getJSONObject("totalsForAllResults");
			pageviews = jres.getString("ga:pageviews");
			sessions = jres.getString("ga:sessions");
			new_users = jres.getString("ga:newUsers");
			users = jres.getString("ga:users");
			avg_session_duration = jres.getString("ga:avgSessionDuration");
			print = print
					+ "<!DOCTYPE html><html><body><div style=\"overflow:scroll;overflow-y:scroll;overflow-x:scroll;\">"
					+ "<table border=\"1\"  style=\"width:75%\">" + "<tr>"
					+ "<td >Page Views</td>" + "<td>" + pageviews + "</td>"
					+ "</tr>" + "<tr>" + "<td>Sessions</td>" + "<td>"
					+ sessions + "</td>" + "</tr>" + "<tr>"
					+ "<td>New users</td>" + "<td>" + new_users + "</td>"
					+ "</tr>" + "<tr>" + "<td>Users</td>" + "<td>" + users
					+ "</td>" + "</tr>" + "<tr>"
					+ "<td>Average Session Duration</td>" + "<td>"
					+ avg_session_duration + "</td>" + "</tr>" + "</table>"
					+ "</div></body></html>";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return print;
	}
}
