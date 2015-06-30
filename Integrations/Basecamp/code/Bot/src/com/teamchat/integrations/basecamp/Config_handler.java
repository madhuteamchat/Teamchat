/**
 * 
 */
package com.teamchat.integrations.basecamp;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.Properties;

//THIS CLASS HAS BEEN DEPRACATED
/**
 * @author Puranjay Jain NOTE: Please create a config.properties file in your
 *         classpath before using it in a live project ! The classpath is the
 *         folder in which build, src, etc. folders are located
 */
public class Config_handler {
	private String email, password, sql_username, sql_password;

	Config_handler() {
		Properties configProperties = getProperties();
		email = configProperties.getProperty("email");
		password = configProperties.getProperty("password");
		sql_username = configProperties.getProperty("sql_username");
		sql_password = configProperties.getProperty("sql_password");
		client_id = configProperties.getProperty("client_id");
		client_secret = configProperties.getProperty("client_secret");
		redirect_uri = configProperties.getProperty("redirect_uri");
	}

	// setters
	public void setEmail(String value) {
		setProperty("email", value, "app_settings");
	}

	public void setPassword(String value) {
		setProperty("password", value, "app_settings");
	}

	public void setSql_username(String value) {
		setProperty("sql_username", value, "app_settings");
	}

	public void setSql_password(String value) {
		setProperty("sql_password", value, "app_settings");
	}

	// getters
	public String getEmail() {
		return email;
	}

	public String getPassword() {
		return password;
	}

	public String getSql_username() {
		return sql_username;
	}

	public String getSql_password() {
		return sql_password;
	}

	public Boolean isEmpty() {
		String[] values = { email, password, sql_username, sql_password, client_id, client_secret, redirect_uri };
		for (String value : values) {
			if (value == null) {
				return true;
			}
		}
		return false;
	}

	// methods
	// initiate a Properties file with defaults
	public void init_bot_Properties() {
		String email = "waynetech@webaroo.com", password = "rgkb4Wn", sql_username = "root", sql_password = "pappupasshogaya";
		// set local properties equal to these
		this.email = email;
		this.password = password;
		this.sql_username = sql_username;
		this.sql_password = sql_password;
		Properties configProperties = getProperties();
		File configFile = new File("config.properties");
		FileWriter writer;
		try {
			writer = new FileWriter(configFile);
			configProperties.setProperty("email", email);
			configProperties.setProperty("password", password);
			configProperties.setProperty("sql_username", sql_username);
			configProperties.setProperty("sql_password", sql_password);
			configProperties.store(writer, "app_settings");
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// for servlet
	private String client_id, client_secret, redirect_uri;

	// setters
	public void setclient_id(String value) {
		setProperty("client_id", value, "app_settings");
	}

	public void setclient_secret(String value) {
		setProperty("client_secret", value, "app_settings");
	}

	public void setredirect_uri(String value) {
		setProperty("redirect_uri", value, "app_settings");
	}

	// getters
	public String getclient_id() {
		return client_id;
	}

	public String getclient_secret() {
		return client_secret;
	}

	public String getredirect_uri() {
		return redirect_uri;
	}

	// methods
	// initiate a Properties file with defaults
	public void init_auth_Properties() {
		String client_id = "d48fa4605608e6bc3405232a71051aeb171eda35",
				client_secret = "cd7f2cc27a364efe2b7299621a265a788d17a24a",
				redirect_uri = "http://localhost:8080/Basecamp_servlet/Redirect_url";
		// set local properties equal to these
		this.client_id = client_id;
		this.client_secret = client_secret;
		this.redirect_uri = redirect_uri;
		Properties configProperties = getProperties();
		File configFile = new File("config.properties");
		FileWriter writer;
		try {
			writer = new FileWriter(configFile);
			configProperties.setProperty("client_id", client_id);
			configProperties.setProperty("client_secret", client_secret);
			configProperties.setProperty("redirect_uri", redirect_uri);
			configProperties.store(writer, "app_settings");
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// get the default properties file
	private Properties getProperties() {
		// open file reader
		FileReader reader = null;
		Properties configProperties = new Properties();
		try {
			// load configuration file
			reader = new FileReader(new File("config.properties"));
			configProperties.load(reader);
			reader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return configProperties;
	}

	// change and store the property
	private void setProperty(String property_name, String property_value,
			String property_context) {
		FileWriter writer = null;
		try {
			File configFile = new File("config.properties");
			Properties configProperties = new Properties();
			// setting values
			configProperties.setProperty(property_name, property_value);
			writer = new FileWriter(configFile);
			configProperties.store(writer, property_context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		// close
		try {
			writer.close();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
