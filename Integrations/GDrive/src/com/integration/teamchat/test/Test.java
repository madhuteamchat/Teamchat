package com.integration.teamchat.test;

import java.io.IOException;

import java.util.List;
import java.util.Properties;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.drive.Drive;
import com.google.api.services.drive.model.File;
import com.google.api.services.drive.model.FileList;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;
import com.teamchat.integration.database.User;

/**
 * 
 * @author Mallika Gogoi
 * 
 */
public class Test {
	public static String accesstoken;
	public static TeamchatAPI api;
	// String apikey="AIzaSyC6tLQmKt3-lnqXgXXv6EPFD24tRZb1v0k";
	String client_id;
	String client_secret;
	GoogleCredential credentials;
	private HttpTransport HTTP_TRANSPORT = new NetHttpTransport();
	private JsonFactory JSON_FACTORY = new JacksonFactory();
	Properties configProps;

	public static Properties loadPropertyFromClasspath(String fileName,
			Class<?> type) throws IOException {

		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;

	}

	@OnKeyword("help")
	public void helpMethod(TeamchatAPI api) {
		System.out.println("checkpoint");
		String html = "<h4 align='center'>Hey,I am GoogleDrive Bot<br/> "
				+ "<img src='http://cdn.megaleecher.net/sites/default/files/images/google_drive_logo_trans.png'style=\"width:230px;height:65px;\"></h4>"
				+ "<p>Now Don't worry, you can get your file present in the Drive here!!</br></p>"
				+ "<p><font color='#159CEB'>Type</font></p>"
				+ "<ul><li><font color='black'>connect:</font><font color='#159CEB'> to get connected to Google Drive</li></font></li>"
				+ "<li><font color='black'>gdrive:</font><font color='#159CEB'> to get list of files in your drive</li></ul></font>";
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(html)));
	}

	// Chatlet for authentication
	@OnKeyword("connect")
	public void connectGDoc(TeamchatAPI api) {
		try {
			configProps = loadPropertyFromClasspath("gdrive.properties",
					Test.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		client_id = configProps.getProperty("client_id");
		client_secret = configProps.getProperty("client_secret");
		String host_url = configProps.getProperty("host_url");

		String sname = api.context().currentSender().getEmail();
		String email = api.context().currentSender().getEmail();

		sname = sname.replace('@', '_');
		sname = sname.replace('.', '_');
		String html = "<a href='" + host_url + "?client_id=" + client_id
				+ "&client_secret=" + client_secret + "&name=" + sname
				+ "&email=" + email + "' target=\'_blank\'> Login </a>";
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(html)));

	}

	@OnKeyword("Help")
	public void help_Method(TeamchatAPI api) {
		helpMethod(api);
	}

	@OnKeyword("Connect")
	public void Start_Setup1(TeamchatAPI api) {

		connectGDoc(api);
	}

	@OnKeyword("Gdrive")
	public void getFiles(TeamchatAPI api) {
		getList(api);
	}

	// Creating a chatlet to get/download the files present in the drive
	@OnKeyword("gdrive")
	public void getList(TeamchatAPI api) {
		String html = "";
		String html1 = "";
		String email = api.context().currentSender().getEmail();
		Drive drive;

		try {
			drive = getDrive(email);
			List<com.google.api.services.drive.model.File> results = getFiles(drive);
			int size = results.size();
			System.out.println(size);
			html1 = "<h4>List of Files:</h4><h5>click on the files to download</h5></br>";

			int i = 0;
			for (File file : results) {
				i++;
				String res = downloadFile(drive, file);
				html += "<a href='" + res + "' target=blank>" + file.getTitle()
						+ "</a><br>";
				if ((i % 5 == 0)) {
					api.perform(api
							.context()
							.currentRoom()
							.post(new PrimaryChatlet().setQuestionHtml(html1
									+ html)));
					html = "";
					html1 = "";
				}

			}
			if (!html.equalsIgnoreCase(""))
			{
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet().setQuestionHtml(html1
								+ html)));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		// api.perform(api.context().currentRoom()
		// .post(new PrimaryChatlet().setQuestionHtml(html1+html)));
	}

	// function to set google credentials
	private Drive getDrive(String email) {
		try {
			configProps = loadPropertyFromClasspath("gdrive.properties",
					Test.class);
		} catch (IOException e) {
			e.printStackTrace();
		}
		client_id = configProps.getProperty("client_id");
		client_secret = configProps.getProperty("client_secret");
		User user = new User(email);
		accesstoken = user.getAccess_token();

		System.out.println("Access Token IS:" + accesstoken);
		credentials = new GoogleCredential.Builder()
				.setClientSecrets(client_id, client_secret)
				.setJsonFactory(JSON_FACTORY).setTransport(HTTP_TRANSPORT)
				.build().setAccessToken(accesstoken);

		Drive drive = null;
		try {
			drive = getDriveService(credentials);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return drive;
	}

	public Drive getDriveService(Credential credential) throws IOException {

		return new Drive.Builder(HTTP_TRANSPORT, JSON_FACTORY, credential)
				.setApplicationName("").build();
	}

	// Function to get the list of files in the drive

	public List<com.google.api.services.drive.model.File> getFiles(Drive service)
			throws IOException {
		FileList result = service.files().list().setMaxResults(100).execute();
		List<com.google.api.services.drive.model.File> files = result
				.getItems();

		return files;
	}

	// Function to get the url of files present in the drive so that we can
	// download the file present in the file list

	private static String downloadFile(Drive service, File file) {
		if (file.getDownloadUrl() != null && file.getDownloadUrl().length() > 0) {
			try {
				service.getRequestFactory()
						.buildGetRequest(new GenericUrl(file.getDownloadUrl()))
						.execute();
				String url = file.getDownloadUrl();
				String onlyUrl = url.substring(0, url.lastIndexOf("?"));
				System.out.println(onlyUrl);
				return onlyUrl;
			} catch (IOException e) {
				e.printStackTrace();
				return null;
			}
		} else {
			return null;
		}
	}
}