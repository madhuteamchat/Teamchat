<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>

<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
  <meta name="mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <title>Successfully Logged In</title>
  <!-- favicon -->
  <link rel="shortcut icon" href="https://enterprise.teamchat.com/webjim-echat/static/img/favicon.png" type="image/x-icon">
  <!-- Theme colour for android lollipop -->
  <meta name="theme-color" content="#159ceb">
  <!-- styles -->
  <link rel="stylesheet" href="http://fonts.googleapis.com/css?family=Roboto:300,400,500,700" rel="stylesheet" type="text/css">
  <link rel="stylesheet" href="css/fonts.css">
  <link rel="stylesheet" href="css/material.min.css">
  <link rel="stylesheet" href="css/success.css">
  <script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js">  	
  	  $(document).ready(
  		  function() {
  			  var myVal= 'Hello! ' + '${name}' + ' You have successfully authorized Teamchat to use your account.'
    	  	  $('#myuser').append(myVal);
  			  
				var nVer = navigator.appVersion;
				var mobile = /Mobile|mini|Fennec|Android|iP(ad|od|hone)/
						.test(nVer);
				if (mobile) {
					$('#hide').hide();
					$('#show').append("press back button");
				}
			});
  </script>
</head>

<body>
  <div class="main-container ">
    <div class="inner-container">
      <img class="logo-img" src="img/icons-04.png" alt="teamchat-logo" />
      <div class="mdl-card mdl-shadow--2dp demo-card-wide green">
        <div class="mdl-card__title font-white">
          <h2 class="mdl-card__title-text">Logged in</h2>
          <i class="icon icon-check auth_state"></i>
        </div>
        <div class="mdl-card__supporting-text font-white">
        	Hello! <%=request.getAttribute("name") %>, you have successfully authorized Teamchat to use your LinkedIn account.
        </div>
        <div id="hide" class="mdl-card__actions mdl-card--border">
          <a class="mdl-button mdl-button--colored mdl-js-button mdl-js-ripple-effect" onclick="window.close()">Continue</a>
        </div>
        <div id="show" class="mdl-card__actions mdl-card--border">
        </div>
      </div>
      <img class="logo-img" src="img/App_Logo.png" alt="logo" />
    </div>
  </div>
</body>

</html>