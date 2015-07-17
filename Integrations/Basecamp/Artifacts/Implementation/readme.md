Implementation Manual
=======================

This is a [Basecamp](https://basecamp.com/) client integration with teamchat.

### Table of contents

  * [Implementation Manual](#implementation-manual)
      * [Table of contents](#table-of-contents)
    * [Dependencies](#dependencies)
    * [Registering your app on Basecamp](#registering-your-app-on-basecamp)
    * [Configure the code](#configure-the-code)
    * [Authorisation flow](#authorisation-flow)
    * [Rest API](#rest-api)

Dependencies
----------

You can either use Teamchat-sdk-1.5 [here](https://github.com/madhuteamchat/Teamchat/blob/master/Integrations/Basecamp/code/teamchat-client-sdk.1.5.jar) or get it from [here](http://www.teamchat.com/en/client-sdk-download/)

[Gson](https://github.com/madhuteamchat/Teamchat/blob/master/Integrations/Basecamp/code/gson-2.3.1.jar) for parsing json data

[MySql Connector](https://github.com/madhuteamchat/Teamchat/blob/master/Integrations/Basecamp/code/mysql-connector-java-5.1.35-bin.jar)

Registering your app on Basecamp
----------

1. You can create an account for basecamp [here](https://basecamp.com/start)
2. Register your app [here](https://integrate.37signals.com/) 
3. Plugin all the important values **don't worry you can change them later**.
4. After you register your app you will see this screen note this values down we'll use them later. 
 ![app registered screen](https://raw.githubusercontent.com/madhuteamchat/Teamchat/master/Integrations/Basecamp/Artifacts/Implementation/app_registered.png)


Configure the code
----------
1. Go [here](https://github.com/madhuteamchat/Teamchat/tree/master/Integrations/Basecamp/code/Bot/src/com/teamchat/integrations/basecamp)
2. You'll find Universal.java in it configure the following values:
	 - CLIENT_ID : Registered app's client id
	 - CLIENT_SECRET : Registered app's client secret
	 - REDIRECT_URL : This will be the url ( of this [servlet](https://github.com/madhuteamchat/Teamchat/blob/master/Integrations/Basecamp/code/Bot/src/com/teamchat/integrations/basecamp/Redirect_url.java) ) which will receive the access token and use it to access the api. 
	 - TOKEN_URL is already configured
	 - AUTHORIZATION_URL is already configured
	 -  NEW_CLIENT_AUTHORIZATION_URL is already configured
	 - APP_NAME : Registered app's name and Registered app's url (inside the brackets)
	 - The rest of the fields are mysql database's details. You may use [this](https://github.com/madhuteamchat/Teamchat/blob/master/Integrations/Basecamp/Artifacts/Sql%20Queries/create_auth_table) my sql query to create a table in the database.
	
Authorisation flow
----------
 Note right of Basecamp: Basecamp API
 
```sequence
Basecamp_bot.java->Basecamp: GET Request for authentication code
Basecamp-->Redirect_url.java: Returns the code
Redirect_url.java->Basecamp: POST Request for authentication token
Basecamp-->Redirect_url.java: Returns the token
Redirect_url.java->Basecamp: POST Request for authorization details
Basecamp-->Redirect_url.java: Returns the authorization details
```

The authorization details are retured in the following format if successfully done:
```json
{
		 "accounts": [
		 {
			 "product": "bcx",
			 "name": "Teamchat",
			 "id": 2965117,
			 "href": "https://basecamp.com/2965117/api/v1"
		 }
		 ],
		 "identity": {
		 "email_address": "puru1joy@gmail.com",
		 "last_name": "Jain",
		 "first_name": "Puranjay",
		 "id": 11321861
		 },
		 "expires_at": "2015-07-02T14:08:18Z"
}
```

we use gson to parse them into pojo [classes](https://github.com/madhuteamchat/Teamchat/tree/master/Integrations/Basecamp/code/Bot/src/com/basecamp/classes). You can also do the same from [here](jsonschema2pojo.org)

Rest API
----------

All the api calls are authenticated with token in the request headers as 
> Authorization: Bearer token 

There are two functions which already do so (in Request_handler.java ) :

 1. sendGet_auth : Sends authorized get request
 2. sendPost_auth : Sends ... post request

Also, the token is stored inside the basecamp_basics object and is passed for reference along with the api end-point which is different for every user.

The following api calls have been implemented (in Basecamp_api_handler.java) :

|Method|What it does| 
 ----------------- | ---------------------------- |
|sendMessage|posts a message and returns the reason if not posted|
|getMessage|returns a message from a topic|
|getTopic|returns a topic from a list of topics|
|getProjectTopics|return topics for a particular project where topictype can be "Message","Todo",etc.|
|getActiveTodos|return todos for a todolist inside a project for a particular project|
|getActiveTodoLists|return todolists list for a particular project|
|getActiveProjects|return active project list|
|getActiveProjects_names|return active project list (names)|

`Edited on stackedit: Puranjay Jain`
 



		


