<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>
<style type="text/css">
body {
	opacity: 0.8;
}
</style>
<script>
$( document ).ready(function() {
	bodyLoad();
});
function bodyLoad() {
	$('g-hangout').click();
}
</script>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Teamchat-Hangouts</title>
</head>
<body>
<script src="https://apis.google.com/js/platform.js" async defer></script>
<div class="g-hangout" data-render="createhangout"></div>
</body>
</html>