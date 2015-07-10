package com.teamchat.integration.instagram.search;


public class InstagramSearch {

	public void searchlocation(String keyword,String id)
	{
		/*try
		{
			JDBCConnection jdb=new JDBCConnection();
			String access_token=jdb.retreive(id);
			HttpGet hget=new HttpGet("");
		    HttpClient httpclient=new DefaultHttpClient();
		    HttpResponse response =httpclient.execute(hget);
		    System.out.println(response.getStatusLine());
		    BufferedReader in = new BufferedReader(new
					InputStreamReader(response.getEntity().getContent()));
					                StringBuffer sb = new StringBuffer("");
					                String line = "",js="";
					                while ((line = in.readLine()) != null) {
					                	js+=line;
					                    sb.append(line +"\n");
					                }
					                in.close();
					                System.out.println(sb);
					                JSONObject jobj=new JSONObject(js);
					                JSONArray jdata=jobj.getJSONArray("data");
					                for(int i=0;i<jdata.length();i++)
					                {
					                	
					                }
			
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}*/
	}
	
}
