<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<style type="text/css">
body {
	background-color: rgba(0, 0, 0, 0.66);
	opacity: 0.8;
}
</style>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Select files or folders</title>
    <script src="https://app.box.com/js/static/select.js"></script>
    <script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
	<script>
    	var boxSelect;
		var options = {
			clientId : "<%=request.getParameter("id")%>",
			linkType : "shared",
			multiselect : true
		};
		boxSelect = new BoxSelect(options);

		// Register a success callback handler
		boxSelect.success(function(response) {
			console.log(response);
			$.post("FilePickerResponseHandler", {data : JSON.stringify(response),
				roomid: "<%=request.getParameter("roomid")%>"});
			boxSelect.closePopup();
		});
		
		// Register a cancel callback handler
		boxSelect.cancel(function() {
			console.log("Cancel");
			boxSelect.closePopup();
		});
		
		function bodyLoad() {
			$('.but').click();
		}
	</script>
</head>
<body onload="bodyLoad()">
	<img
		src='http://www.teamchat.com/wp-content/uploads/2013/04/logoteamchat.png' width=300 height=100 />
	<br />
	<div style="text-align:center; color: #52b66e; font-size: 30px; vertical-align: 5px; padding-bottom: 10px;">
		<button onclick="boxSelect.launchPopup(); return false;" class="but">Select</button>
	</div>
</body>
</html>
