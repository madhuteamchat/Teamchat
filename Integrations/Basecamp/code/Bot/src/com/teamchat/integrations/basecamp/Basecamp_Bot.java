/**
 * 
 */
package com.teamchat.integrations.basecamp;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import com.basecamp.classes.Message;
import com.basecamp.classes.Project;
import com.basecamp.classes.Todo;
import com.basecamp.classes.Todolist;
import com.basecamp.classes.Topic;
import com.basecamp.helpers.Bool_converter;
import com.basecamp.helpers.HTTP_Response;
import com.teamchat.client.annotations.OnAlias;
import com.teamchat.client.annotations.OnKeyword;
import com.teamchat.client.sdk.Field;
import com.teamchat.client.sdk.TeamchatAPI;
import com.teamchat.client.sdk.chatlets.PrimaryChatlet;

/**
 * @author Puranjay Jain
 *
 */
public class Basecamp_Bot {
	private Basecamp_basics bb;
	private Basecamp_api_handler bah;
	// if api is initialised using keyword 'basecamp' only then shall the
	// functions work
	private Boolean api_init = false;

	// inside values to be passed
	private String projectId, topicType;
	// Array of topics
	private Topic[] topicsGlobal;

	// help commands
	@OnKeyword(value = "help")
	public void help(TeamchatAPI api) throws Exception {
		if (isBasecampnotPresent(api)) {
			return;
		}
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestionHtml("<h4>List of commands: </h4>"
								+ "<h5 style=\"color:#F00;\">Psst you can also click them</h5>"
								+ "<br/><b style=\"cursor: pointer;\" onclick=\"document.getElementById('post-message').value = 'get todolist';\">get todolist:</b> To get all todolist(s) in a particular project"
								+ "<br/><b style=\"cursor: pointer;\" onclick=\"document.getElementById('post-message').value = 'get message';\">get message:</b> To get messages from particular topic"
								+ "<br/><b style=\"cursor: pointer;\" onclick=\"document.getElementById('post-message').value = 'message';\">message:</b> To create a new message in a project"
								+ "<hr/><b style=\"cursor: pointer;\" onclick=\"document.getElementById('post-message').value = 'logout';\">logout:</b> To logout of an existing account")));
	}

	// all keywords below this must be first initiated via "basecamp" keyword
	// once !!!
	// function which checks that the above shall happen everytime
	public Boolean isBasecampnotPresent(TeamchatAPI api) throws Exception {
		// uses the api_init boolean
		// if api is not initialized display keyword
		// initiate the basecampbot
		String email = api.context().currentSender().getEmail();
		// pass to public
		Redirect_url.currentEmail = email;
		Db_handler db = new Db_handler();
		if (!api_init) {
			// check if the user email exists in the db or not
			if (db.isAuthorized(email)) {
				// init new
				// get the basic info
				bb = db.GetBasicStuff(email);
				// initiate api handler
				bah = new Basecamp_api_handler(bb);
				// set flag true
				api_init = true;
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet()
								.setQuestionHtml("Hi, you may proceed to use your <u>basecamp account</u>. "
										+ "<br />Type <b>help</b> to know more commands.")));
				// welcome message and continue
			} else {
				// display the button so the user can begin authentication
				// and tell the user to retype the keyword 'basecamp'
				api.perform(api
						.context()
						.currentRoom()
						.post(new PrimaryChatlet().setQuestionHtml("<h4 style=\"display:block;margin:0 auto;\">Login with Basecamp</h4>"
								+ "<hr/>"
								+ "<a class=\"login-button\" target=\"_blank\" href=\""
								+ Universal.NEW_CLIENT_AUTHORIZATION_URL
								+ "?client_id="
								+ Universal.CLIENT_ID
								+ "&redirect_uri="
								+ URLEncoder.encode(Universal.REDIRECT_URL,
										"UTF-8")
								+ "&type=web_server\""
								+ "style=\"display: block;box-sizing: border-box;transform: scale(0.5);width: 427px;margin: 0 auto;height: 125px;background-position: 50% 50%;background-size: 370px 83px;padding: 25px;background-repeat: no-repeat;border: 1px solid rgb(102, 204, 102);border-radius: 11px;background-color: #6C6;"
								+ "background-image: url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAXIAAABTCAYAAACCo4wuAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAytpVFh0WE1MOmNvbS5hZG9iZS54bXAAAAAAADw/eHBhY2tldCBiZWdpbj0i77u/IiBpZD0iVzVNME1wQ2VoaUh6cmVTek5UY3prYzlkIj8+IDx4OnhtcG1ldGEgeG1sbnM6eD0iYWRvYmU6bnM6bWV0YS8iIHg6eG1wdGs9IkFkb2JlIFhNUCBDb3JlIDUuNS1jMDIxIDc5LjE1NDkxMSwgMjAxMy8xMC8yOS0xMTo0NzoxNiAgICAgICAgIj4gPHJkZjpSREYgeG1sbnM6cmRmPSJodHRwOi8vd3d3LnczLm9yZy8xOTk5LzAyLzIyLXJkZi1zeW50YXgtbnMjIj4gPHJkZjpEZXNjcmlwdGlvbiByZGY6YWJvdXQ9IiIgeG1sbnM6eG1wTU09Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC9tbS8iIHhtbG5zOnN0UmVmPSJodHRwOi8vbnMuYWRvYmUuY29tL3hhcC8xLjAvc1R5cGUvUmVzb3VyY2VSZWYjIiB4bWxuczp4bXA9Imh0dHA6Ly9ucy5hZG9iZS5jb20veGFwLzEuMC8iIHhtcE1NOkRvY3VtZW50SUQ9InhtcC5kaWQ6QjEzRTUwN0E4ODI3MTFFMzlFMDY5NTkzNTdGNEY5MzIiIHhtcE1NOkluc3RhbmNlSUQ9InhtcC5paWQ6QjEzRTUwNzk4ODI3MTFFMzlFMDY5NTkzNTdGNEY5MzIiIHhtcDpDcmVhdG9yVG9vbD0iQWRvYmUgUGhvdG9zaG9wIENDIChNYWNpbnRvc2gpIj4gPHhtcE1NOkRlcml2ZWRGcm9tIHN0UmVmOmluc3RhbmNlSUQ9InhtcC5kaWQ6MzgzZjJhMjQtZGJkMi00Yzc4LTgxMjAtMDRiNTMxOTkxNDI0IiBzdFJlZjpkb2N1bWVudElEPSJ4bXAuZGlkOjM4M2YyYTI0LWRiZDItNGM3OC04MTIwLTA0YjUzMTk5MTQyNCIvPiA8L3JkZjpEZXNjcmlwdGlvbj4gPC9yZGY6UkRGPiA8L3g6eG1wbWV0YT4gPD94cGFja2V0IGVuZD0iciI/PoqVul8AAB0NSURBVHja7V0HfBVV1h9d/Wy7FgQ7Lp+7+gmCfdfC2kARdW0oLKIgxRBq6CBIr64KAQWRsjRBSugtAQIB6ZBGQgshCaGuVKUYIIH7nTM5E4fnm7l35s1L3gvn//v9fzzNm1vOnfnPeeeee6+mhQEWZ4sKwIbAaGACMAcobJhD34um6ypoDAaDwSheLMwsqAL8CrgbhLgAeAF4kSgUaHwXryuAchKpvCpsXQaDwQgS5mecuwHYApgB4nueBFxVuFWEHcs7D+VvBNbD+tjqDAaD4QHm7si7DtgVRPa/wHwPxdtO1PFF8V+sF+vnUWAwGAyXmL31dGcQ1AMkrKIEiPUegHbU4dFgMBgMB5iVfvJvwPTYHHGuhAT8EkI78qA9q4H38+gwGAyGDWJST1wN7A3CeQp4MRRE3CTmF4AnoX3teaQYDAbDD6YlHf1fEMoV6P2GkoD7EfRfoa3xwDI8agwGg0GYmnj4rdg9IpsyR0SoE9paAMyCdr/Ao8dgMC57TN5wsGvcHvFzOAi4L6HdJ6D9H/IoMhiMyxbfr9//OYjhmXAUcZOY/4L94NFkMBiXHSasyV0Qlyt+DWcRLxJz6Af0ZwCPKoPBuGywJFcsABaUBhE3CP05M/7HnH48ugwGo/SL+F4xl1ZoitJG6NvpsQmZvXmUGQxGaRbxYaVVxM1iPmZ5Ri8ebQaDUerw3dJt3UHozpRmETe4dJ84Bf1tzaPOYDBKDUbGpTcAcTt5OYi4ScxPQL9f5NFnMBhhj+GLUh5btl8cuZxE3CD0ez/0vyzfBQwGI2wxbN7m25YdEKnhJL6x6E3nFP7riZgfEBv4TmAwGGGL+ANiRjiJ+PI9QsQDv995WP+8MhcmL3MCLxfsMJzvBgaDEX4iflB0KsF9xB0TRfvfWxLFS0u7ikcWNhWPLWwtIteNE9Myj4r4wD30vMEz1/Ke5gwGI3zw5YzVlUG8joeLiK8CEW+3YY6ouKCBKDuzhrhzdg3x+qp/iQYbIkX7lF4iettCPdwSF4i3f1AcBLtUKOGh+QsOjwu2Ab4PfBVYnu9wBiO0MGrUqJeJt3hW6PJDYmO4iHgCiHj7jfPEfXNri1tnvgJ8VbySUE80S4oUkYkR4pPERqJFUpToseVrMT8rL6BQC9hlcQmP9/PAPBc8BtwP3ANMA64BdgM+xY8Qg1GiAl4beHxUIT6nz4Hv/bTikOgZTjHx/inrQMTr6J747bNeExUXviOabI4QTRObFjEi8RMQ9Caic+oAMWVXTiBifhHs06QEx/0loPCIJ4G7gJOAD/IjxWAUu4jfR8LdxUQU9sSAxLz/xCWVQKzCYktaFOMRW3cVhVNQxG+NqSlqrWkAnnjTS4TcYJPNDSHU0lP8sGtPIGJ+EOz05xIa+xc9FHIz0Ut/mx8tBqNYhfxzYhfyyA3v/BagcB1mSfhJxIbFboUgwrN3/yoeXdRUD6egiN8GrLzoXfC+/Yu4wU82NxIdIG7+w65c12IOdppcyoQcmUvlMxiM4hHyZRQX70KfUdSzzH9zXOjKw+JdEKkL4ZAnvjD7gnh5WQ9RJqa6LuIG/7WuoaU3fqlnjmLeW0zdtdetmJ/tNWZ+SYheMIVcUKjlWn7EGIxiEfIZwKYmjxy98dr0tyzgE44K7Dlq7rUgTtnhElL5YNVwCKe8UiTg6I0/FvuekoibxbxjSh/XYg4vvsQQFHIU4kU+TAb+14GYt+FHjMEoFiFvSvFwc4w8y/jXcYGrjojW4SDiK2Bys/OmxeKu2W+CeNcsEvI7Z78O3ngjZRE3x8w7pfQVUzP3uUpNBLvVCzEhHwa8z4cPA6sC3wOOVxDyQ8Cr+TFjMIrNK59BE5+3kIgfd+yNdx0+ozyI0qGQ38QKvOZh6dvEA/PqiXKQYmj2xp9eWtuxiF8q5v3EguyzbsQ8F+x3TQgJeXfJ9bcDOyqIeSV+xBiMYvXMl5ni5Pc5LuTHoyLabawa0/8wjxuJn2ODOLk5bdfP4vHFzYomNw3eAd54g41NXAu5Iea900aK+SDmTvsA9msRQkLeW6EM9LbnSMrhLXwZjHBBh68m3u0m3VDflAoYnbZdDEhJEl9sSRFjtucWibrXk5uLsi+KmvF9xC0x1S4R8XIza4rn4+vqi38CEfLCbJbGon/6BD1e7lDM94Md/yeMhBzxhqSc/vx0MBhhgtXHxWDHuwGC0E3M2Kl7sJGbeoh6azuKZ5Y0E39bHCXqrBwmhqSlijV7vfPOUVgbrh4LnvjLl4g48t65b4jGmyICFvFCFpYzeNs8xyEWsGNkmAn5DR6Vw2AwShJRA0djbPyEUxEfs3OznofdaPNHkLPdCP5tKN5cXVfcNedlcXPMS+IxyO1uuX66HmoJdOdB9O67J8aLu30mN/XFP+CNv7bqQxHpgTf+2wrQCNEssbkYtXOd/qvDQVv3gj2Lwyv3Ssg1STmDHJSDcwRPkpePIZlPgaN9GE1/w43HKnpgh4pUVmv69WDU05/+X2uy1b0e2R3718hP34z6ngzSeN9rsmu0T92fmvrpdJ7mGrJhsMbsUWqXL++2aY/RFt8+Yv1lFG1Vx09fWlPZ1wTY9r9K7o1on/uikUf3uj3WnBAdnIt4oohK7qzHlA3xw5S/FsnNxNur6+tii+GPO2f/UzRbNwUmEM+59sxxO9rotDRRcX59CKHUuETEcYLzwYVveybgvsv52yR30Vd/OvHMwZ5Nw0jIK0vKaSS5HjfewnzXwVphvB1THDFd6jQw36JM/Bvu97IOOAH4mov+16Zr11FZpy3qQW4DrgBOBXYGvhBAfdi/oxZ9O01/x3paejTOL5BtV5js6ltvvqmfOAZ9JX00xqwvfX+dizGrrdj+CdQuX/qG7MqQzeZY9DOf6o+1qbs89WkFfTffTx+yqI7XAmj7N37GaITp3vC13VGy24igveib9xl+jZO8cfROx+5MAoHrrMeS/QkgxqkfWvQOxa5r6CmCH/44wtUkKNY3bsd+fXKzjJ+QCsbG31v7saO8cafxctyXZW7WaSdt3x5GQh4tKec2m4fm38B44D4tsMVHmcDPKMwjAy5SGhZAnfhwbwXOULTPPcBRLuo7Rdf9n8vxvYf6udVlP/G6gT7jh597ejRm+6h9skVj6y2uX2v6znPAhWQz1brb+tRTn/rl5J5r67Lte03f6eBwjHDNSS3P1WDtz6KucsYIcGrmXt1LxeXtdgL4xo/1dW/ZEHP83ILCLLEOYuIxmSfFc0s6/W5y0xDxx+PeC5qIG2y8+WPRN20MtOmi8gsI7PpcGAj5uxYehMGlNtc+rHm7kvQ88AuFeP54j+r7WcE+D5ANAqnnRyrHCbyo1+DNpnJv1rxfATxe0pcEi+uO0d/x12u65m6jtyfpRYLe/ZEAynDa9gLgLeQE5bt8CX7gqRqA6CSqZozMyzojuqQO0ldCSsUPJh7vgnTAItGd9arumbdaH6Mk5iji87POi7cTvhA3zXjhdyKOLA8TnB9taBxUETd75l9vX+IkXj6/hIW8s8219+PUiFa4p4pdGc/alPGnIIgCbrNbw6bOQR7WFSOxL3rSKz2qC8u5VXFc7/ew3ot+yv8pCONW14UYIvuQmLqtF734sQG2faHLts8JsN49wMc8UYImXb98yEn+dr/0cbp3qiJ8kUlNRZXF714ivGUpzNJ6w0zbCVCMiS/IytczX3z3UCma4ITdDWusrCeaezjBKctkaZnURkzelaUaLz8P9r2zBIV8LsW3zexN8b0EBU+ir0IbcoIgCkttfgHkKXj1J0y0++7XNv26gh5w2U/zacBx9G+m5PujFOx5vUQ8nHKnnzo2BmHMtrkUQy9+xXlRxj0l0Hbkck+UYN1JMVxVxEftXK/kiZv5+qqPxG0zX/udmGPmSZsNs0GsC/Sl9nE5v3nheMLPhJ0HxbsJX+nhFN8MFX2CcyZOcL5TTAJu8sphH/NOKf2h3WeVXn5g36gSFPKz5O2YqRpy6KY4sz/Tz/W76QGYCOwH7GpiDJUva3c5h974WZoQbEATYQaxzpHUHt+H/mObfrWVtBHLrE4x5z/Rv/jfiyTecXWJPfs5EJ+NJu62+N4Uiwk83+8dtBmzifR3WZvu9kgMsW9pNn0KRhmRHrc9zcFLpnpAKtCgQ//rQGx+UgmpzN59AjJUOhXlV6uyEYRX7p7z+u+E2BDz91YOhn3EM3QBX7tP6MLeN3kNnLXZTdww/R9+RRx5F+2nElnMQm7Ey4duj1VNp8wAO19RQkLu1ht2sl3mCLoui0S9PV2PqVY3+vn+7ZQWJptoe93PtXY/ZZvbtPEqak9D8oqz6JoqFt+/UeLNf09l+kMF4Caba+Ns2olt/FVBKKKpL4+b+DLZwNw/ZC8/9bTzM2Zv2ozZjfT3LUEWw0OmvlWlPnVX+BXm64T4K+OY5LqBAbYd651kqrcqfZ6pcG1cQCqw/pSopjrB2TdtrGWGiiy88vcl7xdNevqK+Q3Tq4qnY6Mgo+VbOBx5sqi9cqi+f8qNM573K+BGSOWF5XXhyLbIYhdxg62S28FBzgeUvHKw8+NhJOT4U68meZkqeIfCNXj+5x1OonqSdnTyc80hm++r4mpq6zc2fYywqQd3jpSFy56U/HK4y+K6byQ2OUh2U+nfWBL9D/185ynKNnI6Zq8GUQyX0L3kD50dTCpbbVzX2mVigErbs6lefy/BOxRi+HhPlA1kknOiioiPy0h1JeIG661vLO6wEGXkTTEviuunPyuunfaULuz+UgzNWSr+jm8r/hBLY/3cT8VY+aAwEnKzd1FVoQ1uNwm7QtKGfn6usfOSn/HQriku4+pmJNuU8bFFbPyQxCYfOXxhRVrEfgP5hZgTBDGcrhXuzGkH2QTtMq1wPYTd/XYyCG1He8jy0W9WmD+p6Wo06jTveq3KSk7MUmmX0t1xSMVMnIz8G3jlKMK32wi6jOjV3zPnDVEXQyol6I2bFwvhS05BzLPCTMjNR75V14IHpx7Seklba2vWOe+qKEOxbKt6VH9ddbIpY4Kf7z/ncgK4uBHvsRiiJ36TQr2rNfvTrP6iUEaSx20/bBECdPorD9nF1WhsOC1eVMnhHrxt7iUrN92y/oYmuggHIuTIVxLqlWhIxTeLpT285HChkMyWYO9HSkDIccHFl36IE2CLNXnqoZGZ8XQAbcRYMi6Xxv7XotBAR4rdOhXyeZJrTlD2SCvyktwsya9pU/5xB+U8qNkf+OGLTyV9e6aYBds8ZvVMY3bIYzHspNie5ZKXQUm8hFY7sCe+rApsyprhapRAXMbLxGfm7mMgmq09ET30yqutqOvaK8frnllapxhTDdW3vB26fbHKxOfAEhDyDhbX/Q/Fad/SClPnZNksqZqzwyUw4wSXLOOWvsNIgNdp8nRA2YP1oYPrD1C9GNZqRHFhFbSXeGBt6Tsy/luzX1nqi+k23z9aDMJtjFkjspl5zM4HMGYyMeyt2D67MhI8KMNN252mDmbYlLXG8Yi99XHUlSAsx2WZKvoEZ2ITD0MRTcWjsbX0yUonIo7fx+swLh4ZQiJusHVyR/2lJxHyLSUg5KoPCa6sOycp602FcqrQT8jJkpvWrShcQ96sm/KSKe6O/fijTR8GBjlcZTU5u0azX0wULOCY1Q/imJVmIU9waOtYD8uCpNMz4gGZiE+BRS9eirixoRaGWHCy8taYV5VEvAx8D7+P1zULMW/cPPE5IH2izCsveLN+y9tCVMg1TZ6/bBejxQm1KEkc0ytRwMyJQPYIwfDISJvY/5QSEnI7wfguCAJethjHjIW8EN95KuQgKu3thHwRnEqPGRkRid5nhmBo5IP1jfQNtcpCuKSsRailHP0NV4bi95uHqIgXbRIG293GZB629co3/io+CmEh10jkrMo6bXHNMxTfU33YMUxg7ETotu21Fa6Xca/mPzVvfQgKeReP7xtcuONkvxpjV0UMtRxjIXctvj28FvIVdt74fyATIxgibhZz9LCfgrM1/zr/LV2w0fO+lYif74YFP5jp0nDTJyEv4kYGS++072T7x4wJcSFfKSnPN38aU8a2KwhBFsVcoykG+yJNpgXSdry+PwnvaZdiiotv/uFTrt3KzDPUXy/oRDCGeizi0x2OmbHPeUVJCIiF3B6TPAufvfJ+Q4yPn7Tzxjum9AmqkBti3iI5UtRa87F+WPLDi2uJyove1Ynx8OoJH+ihmGZhIOJmMcd9y22EPDvEhVy2cOFVn2yU+Zp8n49BFJf2l2/uRdsr0QTkLK1wReUxh2KOLwLzNqxDNfvtR6t5RF+s8nBSzQ7jFcZstM2YLWchdy3kdr/24h2VtOlX6/h4rL74Z4sn6YaqMXM8hCIqpTksOIrQvW8k/v9WKc1CcmJTdqJQzy3Dbb3y6rUalA1hIR8tKe9ln1i1bIWoLAfd6+PlHqKJW9w2ANMrVTf0Mqf29ZEIebCw2Kbe/R7V8ajEDpsVxiyBhdy1kNvN7Yx3drdki0/svPFOqX1LJsYMnrfBcBNwXzH/ITPXUsg35YnaISzkss3xKyqGIPZpakuOg3lOKHrZuOR7sEK/2puus1vKjWl41wdJyGWHezzgQR1fS+YMqpSAGF4uQv6gZHyjnAr5KCtvfHzG1mLzxksrUch7pX1rF17pF6JC/qhmny/su6+13Ukuqos8iuvA56oUMrCqq79PqMauXV68iK/08//qSuod5kG9WTblf6ZYBodW3An5AMn4/t2pkKf4E5iF2flwnFl/FmOPxBxPUbIQ8pgQFPLbJD/tffe1vkny3SeDJOQPavI9OazwoWJduPDpsCT8cLXLNlxPsWd/B/feKXmRntXU9r4xA9NCH1e09/OKZW5gIS/iCgfjkK3ZLzS7yqmQH/UnMCg87I17J+R99GPhgn6Wp0zIZV4WCvLTmtoJK+ZDJp6XfLeapN7rNPnuh/4erG8ppINx3HIuvHLVAzRkuxC2dfzgFW7o1Jd+yXxi8Z01knqTNLXtEtC+uHcLZqfEKQr5PyVlXkWhqqMs5EVcpVBnOU2eJfSNozvpqepvlrH6yf/djrUswh4StzawmvSEcbi2mIR8DP1k8+U/yDMcosl33EPiqk/zXjGPSL4/RDIh2Udzt7jE/ECMpgnX+0m4ZMI2xMELD4+3u6AQ06yg8KLEkBXuQmjeDXGwxfdrK9jlEJWH5d5FfbuOPqMtXiL7GimZqYpCPk2z32O9o8YLgvztfPg+3dM3me7Dq0xjL0sguKDZH6f4e8BE29tWQv751mkswB6nIs63OEUIxqFSMQk5en8H/dBp3rW/GXXZ/hsNaHIU873/Qp5kE8r8cLtKcJvFQ4wLLXCzqydMdd5Ln5/W7Bdi+KZVGohVaCOmX9ahOp4w8Wn6/7hBmb9l79NtxjRF0T5Y7hTqWw/6nGDxEjZi8r8o/NJ42DRmT9CYzdd4ZacdE2mse5C92tqMvS9jHT/1ICKtrIS8+5ZoFmCPOWlXhlV45Z1iEnKvzny8xU/dmxWuxe/Mo1jiKYf1+j5Y1yhcc9BU5zz6fErhmj/46V8lSRjB7oUpqzPVZkwxbPSzx2No7AI5VXG8jTE7GOCYXS5C7paH6D5zLOQDrIS825bBLL4eb3E7dmeylZB3DRMh32Yzm97Sg/JzHTxYVYLQv3zN/sQd7OOxINR7UTKuPTX5wdhOaDgOb3tQVjYLuSc8Rr9aNTdC/h8rIY/ethDCAU1YgD1b7NQMjoLbbyXk/UNcyI9RFktFm7r/qNlv1C8TUDzRJcLBg4WTd3kei7jKlgmYV/6Tx7ZVOaPxWw/r/YDKxBDLxgDshaL2Ggt5wPyJ7ivNrZCvtRLyBVnnYDvWDkFfmn+5ZK10Tf3S7uSgcR4J+Use3lwoknspvayNYv04SZPlsB78uT6KRKWSg0ySchR3TJSkBzppgyoa06+TQF4kx6ntbYqxXuznTJ9J2cqa8+1qsRy8Z2+mcVMdMwOrXVzjpIzVHpTRtxiEPI/Gs3FATz0IyHK75fnf7lil7+LHYhxYSKV5Ukvx/a5Mu0VBKzwS8mcpzuaWmAqJR6StpUwKN+cG4vL2lcAjkhhyNnnhdXyut2pbR4v6rqBwyHRq+z4qP1/yAOEBCbtocqmOi36WJxslU/vyFB7aI6Y622nuzso06k1TqNdc53Sbfj5EY3HIxm55ZFt/Y5btcMxmuRhnJ2XM8qCMji6E/Bf6hbPLNDa+PEV2TKZxLB/wUx9/QPSUnQrUJ20Ue+UBMVIM2vqD3d7kF2Ac2nok5LiQp24A/LPFRKZTXEVhkll0Y28hz2MjCQF6O1aLWazaVlmhXmz761T+OKrPH4fSg1rFg75eSe0bSn3bSCGmDPrXXGeER3UaffWt118/ndRZ12S3LdQHc1lW51FWdThm1QIcZ1kZ1Twoo7ILIY8zzd8YY+PLvmTHKzUvseyASLXzyudknRRdUgexmLsMqWD2j91hzGD/5Vrpxx88ekGEEyqUgjGroDGCEZv3HsMXpTy2bL/4xU7M52adguX6A/Vc6KYs6ErhFLQV2gxfhFYLgcDuR8D+f+Vng8FgIQ8YI+PSPwNhyZd75p/r2ReFgs6CbbXwB22EtrITceAZsHsDfi4YDBZyz7B0n5gpE/O4HIyZjxatktpRaiJ755d64U1026CN0FY2Ip4P9h7CzwSDwULuOZbsFXPsxBwZDwI1EvZhaZvcDfYKb8HhFvLC0RZtkj/VbRNvf+hyPtqZnwcGg4U8mGI+VybmmIGxIPu86J8+QUQldwYRa34ZCnpEUYipdXJH3RZzsn6xy04RYNtzaF9+FhgMFvLgi3muWAA8YyfmGDZYBqI1ffch0S99nC5mzZNa6eJWmjNcIkjAMb8e+9wNFvpM2ZWt28LuSDe05/gfcybxc8BgsJAXGyasyR0QlytOyPLM44oE/aD4atscCC90ES2T2hRNihaKekSpEG/MCW+RFKX/CumX/h+9z9j3OJmNcsUxtCc/AwxGqRbylSHZ4u/X768Xt0ccBhbIBB290aXklY7euVH02PK17rGiqGP82BDEpmEh7BFF4h0JnjeKN/blU8hEGbFjpb4d7VKJB64L+B5xHpgBdnyD738Go1RgpR8BL9AKt3FeHLKtnrzhYHlgHAjSaZmYFwlYTqGoz8/KE+N2pomBWyeLDim9IKOjvS6KzWDJeqHHHuEj7hHFLti/iXaE7nHjSwcPgcDsk6jkTqJP+mh4MW3Q+4Le9xIFAScRPwl2mwa8ie99BqPUALdXwP37cbMz3OMH953BFZ14EMc7Id/6qYmHo2L3iCzgWVVBj6XQC4ofiuDM3UfFqB3rxRdbY8RnqdH6plwtk9rq4o7x9eYk8MjfQhq+Qu+cESYa5aNgY51YN/5qwLZ0ThkgBqRPEsO3r4Cj7vbpbcYXUpyieOt93iPygLvBXpwjzmCUPtTSCk+DwlOxbg7LHkxLOloGODg2RxwHnlMVdH/CvrRI3I+ISRkZ4pvty8WQbQtE77SROtsld9c9eGSh2LdxRRRpoxykUf4XW2fodWLdZtFe4lC4i/oG9kC7gH16Aq/k+53BYIQ0YlJP3AocCsL1C/BsbI4z0fMVd0Pg40hIDaE3ODfrtPghM9cVY3YfvqSsJSbGmep31fYccZH6/zPaA+3CdweDwQgrzEo/WQ7YEbgXxCwPWOBW0FUF3w09b0uOyMf+Qr/Tqf9X893AYDDCHrO3nq4BnIJ7iAAxjh40US8h5lO/zkA/R2B/edQZDEapxNwdedcBPwBOAx4C4TtHInghzIT7ArX7HPYD+B316zoeZQaDcVlhfsa5Z4HdgEuAKOrnSSDRY79ILEnBNtpQQO06D+38CTgd2AJYiUeRwWAwTFiYWVAFWB/YAxgP3A0sIA/4gklYfRmISJtp1HMB66X646k92K4qPEoMBoPhAiCsLxB7EccBE0x0KuInfK4fQuW2oXoeYaszGIxwx/8DY6UOxzeG3eUAAAAASUVORK5CYII=');;\">"
								+ "</a>")));
				return true;
			}
		} else {
			// auth old
			// get the basic info
			bb = db.GetBasicStuff(email);
			// initiate api handler
			bah = new Basecamp_api_handler(bb);
		}
		return false;
	}

	// get todo list from a project
	@OnKeyword(value = "get todolist")
	public void getTodoList(TeamchatAPI api) throws Exception {
		if (isBasecampnotPresent(api)) {
			return;
		}
		// populating with project name list
		Field f = api.objects().select().name("project").label("Project");
		for (Project project : bah.getActiveProjects()) {
			f.addOption(project.getName() + " | " + project.getId());
		}
		// api.perform(api
		// .context()
		// .currentRoom()
		// .post(new PrimaryChatlet()
		// .setQuestion("Get todolists from a project")
		// .setDetailsLabel("Comments")
		// .showDetails(true)
		// .setReplyScreen(api.objects().form().addField(f))
		// .setReplyLabel("Select Project")
		// .alias("get_todo_list2")));//.setReplyLabel("Comment")
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("Get todolists from a project")
						.setReplyScreen(api.objects().form().addField(f))
						.setReplyLabel("Select Project")
						.alias("get_todo_list2")));
	}

	// part 2 of get todo
	@OnAlias(value = "get_todo_list2")
	public void getTodoList2(TeamchatAPI api) throws Exception {
		// populating with todolist name list
		// get option name
		String[] project = api.context().currentReply().getField("project")
				.split("\\|");
		// get project id from option name
		projectId = project[(project.length - 1)].trim();
		Field f = api.objects().select().name("todolist").label("Todolist");
		for (Todolist todolist : bah.getActiveTodoLists(projectId)) {
			f.addOption(todolist.getName() + " | " + todolist.getId());
		}
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("Get todos from a Todolist")
						.setReplyScreen(api.objects().form().addField(f))
						.setReplyLabel("Select Todo").alias("get_todo_list3")));
	}

	// part 3 of get todo
	@OnAlias(value = "get_todo_list3")
	public void getTodoList3(TeamchatAPI api) throws Exception {
		// using helper class
		Bool_converter boolc = new Bool_converter();
		// populating with todos name list
		// get option name
		String[] todolist = api.context().currentReply().getField("todolist")
				.split("\\|");
		// get project id from option name
		String todolistId = todolist[(todolist.length - 1)].trim();
		Todo[] todos = bah.getActiveTodos(projectId, todolistId);
		String todolistName = todos[0].getTodolist().getName();
		String htmlResponse = "<h4>" + todolistName + "</h4>"
				+ "<h5 style=\"color:#F00;\">You can't edit the values</h5>";
		for (Todo todo : todos) {
			htmlResponse += "<label><input type=\"checkbox\" value=\""
					+ todo.getId() + "\" "
					+ boolc.toBoolHTML(todo.getCompleted()) + ">&nbsp;"
					+ todo.getContent() + "</label><br />";
		}
		// show the user the current state
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(htmlResponse)));
	}

	// get message from a project
	@OnKeyword(value = "get message")
	public void getMessage(TeamchatAPI api) throws Exception {
		if (isBasecampnotPresent(api)) {
			return;
		}
		// populating with project name list
		Field f = api.objects().select().name("project").label("Project");
		for (Project project : bah.getActiveProjects()) {
			f.addOption(project.getName() + " | " + project.getId());
		}
		// set topic type to message
		topicType = "Message";
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("Get message(s) from a project")
						.setReplyScreen(api.objects().form().addField(f))
						.setReplyLabel("Select Project").alias("gettopic")));
	}

	// part 2 of get topic (e.g get message etc.
	@OnAlias(value = "gettopic")
	public void getTopic(TeamchatAPI api) throws Exception {
		// populating with message name list
		// get option name
		String[] project = api.context().currentReply().getField("project")
				.split("\\|");
		// get project id from option name
		projectId = project[(project.length - 1)].trim();
		Field f = api.objects().select().name("topic").label("Topic");
		// store topics for reference
		topicsGlobal = bah.getProjectTopics(projectId, topicType);
		for (Topic topic : topicsGlobal) {
			f.addOption(topic.getTitle() + " | " + topic.getId());
		}
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("Get " + topicType + "(s) from a project")
						.setReplyScreen(api.objects().form().addField(f))
						.setReplyLabel("Select " + topicType)
						.alias(topicType + "3")));
		// alias naming according to krrish 3 naming standards
	}

	// part 3 of getmessage
	@OnAlias(value = "Message3")
	public void getMessage3(TeamchatAPI api) throws Exception {
		// populating with message name list
		// get option name
		String[] topics = api.context().currentReply().getField("topic")
				.split("\\|");
		// get topic id from option name
		String topicId = topics[(topics.length - 1)].trim();
		Topic topic = bah.getTopic(topicsGlobal, Integer.parseInt(topicId));
		Message message = bah.getMessage(topic);
		String htmlResponse = "<img style=\"border-radius: 50%;display: inline-block;\" alt=\"avatar\" src=\""
				+ message.getCreator().getAvatar_url()
				+ "\" />"
				+ "&nbsp;<strong>"
				+ message.getCreator().getName()
				+ "</strong> said"
				+ "<h3>"
				+ message.getSubject()
				+ "</h3>"
				+ "<h4>" + message.getContent() + "</h4>";
		api.perform(api.context().currentRoom()
				.post(new PrimaryChatlet().setQuestionHtml(htmlResponse)));
	}

	// get message from a project
	@OnKeyword(value = "message")
	public void createMessage(TeamchatAPI api) throws Exception {
		if (isBasecampnotPresent(api)) {
			return;
		}
		// populating with project name list
		Field f = api.objects().select().name("project").label("Project");
		for (Project project : bah.getActiveProjects()) {
			f.addOption(project.getName() + " | " + project.getId());
		}
		// set topic type to message
		topicType = "Message";
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("Get message(s) from a project")
						.setReplyScreen(api.objects().form().addField(f))
						.setReplyLabel("Select Project")
						.alias("createmessage2")));
	}

	// part 2 of create message
	@OnAlias(value = "createmessage2")
	public void createMessage2(TeamchatAPI api) throws Exception {
		// populating with message name list
		// get option name
		String[] project = api.context().currentReply().getField("project")
				.split("\\|");
		// get topic id from option name
		projectId = project[(project.length - 1)].trim();
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("Write your message")
						.setReplyScreen(
								api.objects()
										.form()
										.addField(
												api.objects().input()
														.name("subject")
														.label("Title"))
										.addField(
												api.objects().input()
														.name("content")
														.label("Message")))
						.setReplyLabel("Send").alias("createmessage3")));
	}

	// part 3 of create message
	@OnAlias(value = "createmessage3")
	public void createMessage3(TeamchatAPI api) throws Exception {
		// populating with message name list
		// get option name
		String subject = api.context().currentReply().getField("subject"), content = api
				.context().currentReply().getField("content");
		// posting the message
		HTTP_Response httpResponse = bah.sendMessage(projectId, subject,
				content);
		if ((httpResponse.getResponseCode() == 422)
				|| (httpResponse.getResponseCode() == 404)
				|| (httpResponse.getResponseCode() == 500)) {
			api.perform(api
					.context()
					.currentRoom()
					.post(new PrimaryChatlet()
							.setQuestionHtml("<h3 style=\"color:red;\">An error occured try again after 5 minutes !</h3>")));
		} else {
			String htmlResponse = "<h3>"
					+ subject
					+ "</h3>"
					+ "<h4>"
					+ content
					+ "</h4>"
					+ "<h5 style=\"color:blue;\">This message was posted successully!</h5>";
			api.perform(api.context().currentRoom()
					.post(new PrimaryChatlet().setQuestionHtml(htmlResponse)));
		}
	}

	// get message from a project
	@OnKeyword(value = "logout")
	public void logout(TeamchatAPI api) throws Exception {
		api.perform(api
				.context()
				.currentRoom()
				.post(new PrimaryChatlet()
						.setQuestion("You have been successfully logged out of this account")));
		// log out from this email
		Db_handler db = new Db_handler();
		db.Delete(api.context().currentSender().getEmail());
	}
	// the main function is no longer required from api 1.4
	// @Deprecated
	// @SuppressWarnings("unused")
	// public static void main(String[] args)// main function
	// {
	// // init config and get data from configuration file
	// Config_handler config = new Config_handler();
	// if (config.isEmpty()) {
	// config.init_bot_Properties();
	// System.out
	// .println("Properties file has been created on the Server!");
	// }
	// // initiate api
	// TeamchatAPI api = TeamchatAPI.fromFile("teamchat.data")
	// .setEmail(config.getEmail()).setPassword(config.getPassword())
	// .startReceivingEvents(new Basecamp_Bot());
	// }
}
