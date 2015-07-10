/** © Copyright 2013 FINN AS
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU Lesser General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU Lesser General Public License for more details.
 *
 *   You should have received a copy of the GNU Lesser General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.teamchat.integration.yammer;

import java.io.Closeable;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.HttpParams;
import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Document; 
import org.w3c.dom.Element;
import org.xml.sax.SAXException;

public final class PostStatus implements Closeable {

    private String authtoken="";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    private YammerConnection yc=null;
    private final CloseableHttpClient httpclient;


    /**
     *
     * @param applicationKey The key of the application registered with Yammer. See http://www.yammer.com/client_applications/new
     * @param applicationSecret The secret of the application registered with Yammer. See http://www.yammer.com/client_applications/new
     * @throws IOException
     */
    public PostStatus(String email) throws IOException {

    	 httpclient = HttpClientBuilder.create().useSystemProperties().build();
    	 yc=new YammerConnection();
    	 authtoken=yc.getAuthToken(email);

    }

/*	public void sendMessage(final String group, final String message, final String... topics) throws IOException {

        HttpPost httpPost = new HttpPost(YAMMER_API_V1_MESSAGES);
     //   httpPost.addHeader("Authorization", "Bearer " + accessAuthToken);
        httpPost.addHeader("Authorization", "Bearer " +"fdpiJV5o98k3OI3X2TpAg");

        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair(MESSAGE_BODY_PARAM_NAME, message));

        if (group != null && !group.equals("")) {
            nvps.add(new BasicNameValuePair(MESSAGE_GROUP_ID_PARAM_NAME, group));
        }

        for(int i = 0; i < topics.length; ++i) {
            nvps.add(new BasicNameValuePair(MESSAGE_TOPIC_PARAM_NAME + (i+1), topics[i]));
        }

        httpPost.setEntity(new UrlEncodedFormEntity(nvps, UTF8));
        HttpResponse response = httpclient.execute(httpPost);
        if(201 != response.getStatusLine().getStatusCode()) {
            throw new ClientProtocolException("failed to post message to yammer: " + response);
        }
	}*/
	
	public int postStatus(String status) throws IOException {
        HttpPost httppost = new HttpPost("https://www.yammer.com/api/v1/messages.json");
        httppost.addHeader("Authorization", "Bearer " +authtoken);
 
        List<NameValuePair> nvps = new ArrayList<>();
        nvps.add(new BasicNameValuePair("body",status));
        httppost.setEntity(new UrlEncodedFormEntity(nvps, UTF8));
        
        HttpResponse response = httpclient.execute(httppost);  
        if(response.getStatusLine().getStatusCode()==201)
        	System.out.println("Post successfully posted!!");
        else
        	System.out.println(response.toString());
        return response.getStatusLine().getStatusCode();
	}

    @Override
    public void close() throws IOException {
        httpclient.close();
    }

}