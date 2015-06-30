<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Select files or folders</title>
    <script src="https://app.box.com/js/static/select.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script>
    	var boxSelect;
    	String id = getServletContext().getInitParameter("client-id");
		var options = {
			clientId : id,
			linkType : "shared",
			multiselect : true
		};
		boxSelect = new BoxSelect(options);

		// Register a success callback handler
		boxSelect.success(function(response) {
			console.log(response);
			$.post('AuthHandler', {data : JSON.stringify(response),
				roomid: "<%=request.getParameter("roomid")%>"});
			boxSelect.closePopup();
			window.close();
		});
		
		// Register a cancel callback handler
		boxSelect.cancel(function() {
			console.log("Cancel");
			boxSelect.closePopup();
			window.close();
		});
	</script>
</head>
<body>
	<img
		src='http://www.teamchat.com/wp-content/uploads/2013/04/logoteamchat.png' width=300 height=100 />
	<br />
	<div style="text-align:center; color: #52b66e; font-size: 30px; vertical-align: 5px; padding-bottom: 10px;">
		<button onclick="boxSelect.launchPopup(); return false;">Select</button>
	</div>
</body>
</html>
