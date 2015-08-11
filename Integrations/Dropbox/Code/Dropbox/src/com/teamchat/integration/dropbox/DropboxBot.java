package com.teamchat.integration.dropbox;

//import java.io.File;
//import java.io.FileInputStream;
//import java.io.FileOutputStream;
import java.io.IOException;
//import java.util.Locale;
import java.util.Properties;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONObject;

//import com.dropbox.core.DbxAppInfo;
//import com.dropbox.core.DbxAuthFinish;
//import com.dropbox.core.DbxClient;
//import com.dropbox.core.DbxEntry;
import com.dropbox.core.DbxException;
//import com.dropbox.core.DbxRequestConfig;
//import com.dropbox.core.DbxWebAuthNoRedirect;
//import com.dropbox.core.DbxWriteMode;
//import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
//import com.teamchat.client.sdk.Form;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
//import com.teamchat.client.sdk.chatlets.TextChatlet;

public class DropboxBot {
	Properties configProps;
	
	@OnKeyword("help")
	public void help(TeamchatAPI api) throws DbxException {
		try {
			configProps = loadPropertyFromClasspath("dropbox-config.properties", DropboxBot.class);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		String roomId = api.context().currentRoom().getId();
		String urls = configProps.getProperty("server").trim() + "Chooser.jsp?roomid=" + roomId + "&id=" + configProps.getProperty("dropinkey").trim();
		
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml("Click <a href='" + urls + "' target=_blank>here</a> to select and share files")));
	}
	
	/*
	DbxAppInfo appInfo;
	DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
	DbxWebAuthNoRedirect webAuth;
	Properties configProps;
	
	@OnKeyword("Help")
	public void Dropox(TeamchatAPI api) throws DbxException {
		dropbox(api);
	}

	@OnKeyword("help")
	public void dropbox(TeamchatAPI api) throws DbxException {
		try {
			DBHandler db = new DBHandler();
			try {
				configProps = loadPropertyFromClasspath(
						"dropbox-config.properties", DropboxBot.class);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			String email = api.context().currentSender().getEmail();
			String data[] = db.getAccessData(email);

			if (data == null) {
				DbxAppInfo appInfo = new DbxAppInfo(
						configProps.getProperty("apikey"),
						configProps.getProperty("secret"));

				DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0", Locale.getDefault().toString());
				webAuth = new DbxWebAuthNoRedirect(config, appInfo);
				String authorizeUrl = webAuth.start();

				Form f = api.objects().form();
				f.addField(api
						.objects()
						.input()
						.name("code")
						.label("Code:")
						.addRegexValidation("[^()]", "This field cannot be left blank"));

				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
								.setQuestionHtml("<b>Click <a href='" + authorizeUrl
												+ "' target=_blank>here</a> "
												+ "to Authenticate Teamchat</b>")
								.setReplyLabel("Enter Code").setReplyScreen(f)
								.alias("code")));
			} else {
				String accessToken = data[1];
				DbxClient client = new DbxClient(config, accessToken);

				String roomId = api.context().currentRoom().getId();
				String urls = configProps.getProperty("server").trim()
						+ "Chooser.jsp?roomid=" + roomId + "&id="
						+ configProps.getProperty("dropinkey").trim();
						
				String urlu = configProps.getProperty("server").trim()
						+ "Saver.jsp?roomid=" + roomId + "&id="
						+ configProps.getProperty("dropinkey").trim();
				
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet().setQuestionHtml("Hello! "
								+ client.getAccountInfo().displayName
								//+ "<br>'Dropbox' - To start exploring<br>"
								+ "<br><a href='" + urls + "' target=_blank>'Share'</a> - Click to select and share files")));
								//+ "<br>'Share'<a href='" + urlu + "' target=_blank>'Upload'</a> - Click to upload files")));
			}
		} catch (DbxException e) {
			DbxAppInfo appInfo = new DbxAppInfo(
					configProps.getProperty("apikey"),
					configProps.getProperty("secret"));

			DbxRequestConfig config = new DbxRequestConfig("JavaTutorial/1.0",
					Locale.getDefault().toString());
			webAuth = new DbxWebAuthNoRedirect(config, appInfo);
			String authorizeUrl = webAuth.start();

			Form f = api.objects().form();
			f.addField(api
					.objects()
					.input()
					.name("code")
					.label("Code:")
					.addRegexValidation("[^()]",
							"This field cannot be left blank"));

			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml(
									"<b>Click <a href='" + authorizeUrl
											+ "' target=_blank>here</a> "
											+ "to Authenticate Teamchat</b>")
							.setReplyLabel("Enter Code").setReplyScreen(f)
							.alias("code")));
		}
	}

	@OnAlias("code")
	public void authorize(TeamchatAPI api) {
		try {
			String email = api.context().currentReply().senderEmail();
			String code = api.context().currentReply().getField("code");

			DbxAuthFinish authFinish = webAuth.finish(code);
			String accessToken = authFinish.accessToken;

			DBHandler db = new DBHandler();
			db.setAccessData(email, accessToken);
			DbxClient client = new DbxClient(config, accessToken);

			String roomId = api.context().currentRoom().getId();
			String urls = configProps.getProperty("server").trim()
					+ "Chooser.jsp?roomid=" + roomId + "&id="
					+ configProps.getProperty("dropinkey").trim();
					
			String urlu = configProps.getProperty("server").trim()
					+ "Saver.jsp?roomid=" + roomId + "&id="
					+ configProps.getProperty("dropinkey").trim();
			
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml("Hello! "
							+ client.getAccountInfo().displayName
							//+ "<br>'Dropbox' - To start exploring<br>"
							+ "<br><a href='" + urls + "' target=_blank>'Share'</a> - Click to select and share files")));
							//+ "<br><a href='" + urlu + "' target=_blank>here</a> - Cick to upload files")));
		} catch (DbxException e) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet("Code was incorrect, try again")));
		}
	}

	@OnKeyword("Dropbox")
	public void ActionPerform(TeamchatAPI api) throws DbxException {
		actionPerform(api);
	}

	@OnKeyword("dropbox")
	public void actionPerform(TeamchatAPI api) throws DbxException {
		try {
			String email = api.context().currentSender().getEmail();
			DBHandler db = new DBHandler();
			String data[] = db.getAccessData(email);

			if (data == null)
				dropbox(api);
			else {
				Form f = api.objects().form();
				f.addField(api
						.objects()
						.select()
						.name("action")
						.label("Select")
						.addOption("View account info")
						.addOption("Upload a file")
						.addOption("Download a file")
						.addOption("List files or folders in root directory")
						.addRegexValidation("[^()]", "Choose some action first"));

				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
								.setQuestionHtml("Choose action")
								.setReplyLabel("Select").setReplyScreen(f)
								.alias("action")));
			}
		} catch (DbxException e) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet(
							"You are not authorized...authenticate first")));
		}
	}

	@OnAlias("action")
	public void action(TeamchatAPI api) throws Exception {
		try {
			String email = api.context().currentReply().senderEmail();
			String action = api.context().currentReply().getField("action");

			String data[] = new DBHandler().getAccessData(email);

			if (data != null) {
				String accessToken = data[1];
				DbxClient client = new DbxClient(config, accessToken);

				if (action.equals("View account info")) {
					api.perform(api
							.context()
							.currentRoom()
							.post(new PrimaryChatlet()
									.setQuestionHtml("<b>Name: </b>"
											+ client.getAccountInfo().displayName
											+ "<br>"
											+ "<b>Country: </b>"
											+ client.getAccountInfo().country
											+ "<br>"
											+ "<b>Referal Link: </b>"
											+ client.getAccountInfo().referralLink
											+ "<br>" + "<b>User Id: </b>"
											+ client.getAccountInfo().userId)));
				}

				if (action.equals("Upload a file")) {
					Form f = api.objects().form();
					f.addField(api.objects().input().name("file")
							.label("File Path"));
					f.addField(api.objects().input().name("target")
							.label("Target Path"));

					api.perform(api
							.context()
							.currentRoom()
							.post(new PrimaryChatlet()
									.setQuestionHtml(
											"<h5>To Upload a file give its actual path and the target path of dropbox</h5>"
													+ "<b>Example:</b><br>File Path: /home/Documents/filename.extension<br>Target Path: /documents/file.extension")
									.setReplyLabel("Enter Path")
									.setReplyScreen(f).alias("file")));
				}

				if (action.equals("List files or folders in root directory")) {
					DbxEntry.WithChildren listing = client
							.getMetadataWithChildren("/");
					StringBuffer data1 = new StringBuffer();
					for (DbxEntry child : listing.children) {
						if (child.isFile()) {
							data1.append("<img src='http://upload.wikimedia.org/wikipedia/en/0/05/File_icon.png'/>"
									+ child.name + "<br>");
						} else if (child.isFolder()) {
							data1.append("<img src='http://wiki.gigaspaces.com/wiki/download/attachments/17301573/folder_icon.gif'/>"
									+ child.name + "<br>");
						}
					}
					api.perform(api
							.context()
							.currentRoom()
							.post(new PrimaryChatlet().setQuestionHtml(data1
									.toString())));
				}

				if (action.equals("Download a file")) {
					FileOutputStream outputStream = new FileOutputStream(
							"abcd.pdf");
					try {
						DbxEntry.File downloadedFile = client.getFile(
								"//Get Started with Dropbox.pdf", null,
								outputStream);
						System.out.println("Metadata: "
								+ downloadedFile.toString());
					} finally {
						outputStream.close();
					}
				}
			} else {
				api.perform(api
						.context()
						.currentRoom()
						.post(new TextChatlet(
								"You are not authorized...authenticate first")));
				dropbox (api);
			}
		} catch (DbxException e) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new TextChatlet(
							"You are not authorized...authenticate first")));
			dropbox(api);
		}
	}

	@OnAlias("file")
	public void file(TeamchatAPI api) throws Exception {
		String email = api.context().currentReply().senderEmail();
		String file = api.context().currentReply().getField("file");
		String target = api.context().currentReply().getField("target");

		File inputFile = new File(file);
		FileInputStream inputStream = new FileInputStream(inputFile);
		String accessToken = new DBHandler().getAccessData(email)[1];
		DbxClient client = new DbxClient(config, accessToken);

		try {
			DbxEntry.File uploadedFile = client.uploadFile(target,
					DbxWriteMode.add(), inputFile.length(), inputStream);
			api.perform(api.context().currentRoom()
					.post(new TextChatlet("Uploaded: " + uploadedFile.name)));
		} finally {
			inputStream.close();
		}
	}
	*/
	
	public static String createEmbeddedLink(String url, String title, String protocol) {
		JSONObject object = new JSONObject();
		JSONObject web = new JSONObject();
		web.put("title", title);
		web.put("cancelBtnName", "Back");
		web.put("minWidth", "200");
		web.put("draggable", "true");
		web.put("newWindow", "true");
		web.put("url", url);
		object.put("web", web);
		System.out.println(object.toString());
		byte[] byteArray = Base64.encodeBase64(object.toString().getBytes());
		String encodedString = new String(byteArray);
		String fUrl = protocol + "://teamchat:data=" + encodedString;
		return fUrl;
	}

	public static Properties loadPropertyFromClasspath(String fileName,
			Class<?> type) throws IOException {
		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;
	}
}