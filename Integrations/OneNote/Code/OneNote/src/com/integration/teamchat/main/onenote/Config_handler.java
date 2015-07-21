package com.integration.teamchat.main.onenote;

import java.io.IOException;
import java.util.Properties;

public class Config_handler
{
	String fileName = "com/integration/teamchat/main/onenote/onenote-config.properties";
	public Properties configProps;

	public static Properties loadPropertyFromClasspath(String fileName, Class<?> type) throws IOException
	{

		Properties prop = new Properties();
		prop.load(type.getClassLoader().getResourceAsStream(fileName));
		return prop;

	}

	public Config_handler()
	{
		try
		{
			configProps = loadPropertyFromClasspath(fileName, this.getClass());
		} catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
