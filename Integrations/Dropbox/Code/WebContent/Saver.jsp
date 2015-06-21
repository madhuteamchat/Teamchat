<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Select files or folders</title>
<script type="text/javascript"src="https://www.dropbox.com/static/api/2/dropins.js" id="dropboxjs" data-app-key="<%=request.getParameter("id") %>"></script>
	<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

</head>
<body>
	<script>
		var options = {
		    files: [
		        // You can specify up to 100 files.
		        {'url': 'Abcd', 'filename': 'Abcd'},
		        {'url': 'Abcd', 'filename': 'Abcd'},
		        // ...
		    ],

		    // Success is called once all files have been successfully added to the user's
		    // Dropbox, although they may not have synced to the user's devices yet.
		    success: function () {
		        // Indicate to the user that the files have been saved.
		        alert("Success! Files saved to your Dropbox.");
		        window.close();
		    },

		    // Progress is called periodically to update the application on the progress
		    // of the user's downloads. The value passed to this callback is a float
		    // between 0 and 1. The progress callback is guaranteed to be called at least
		    // once with the value 1.
		    progress: function (progress) {},

		    // Cancel is called if the user presses the Cancel button or closes the Saver.
		    cancel: function () {
		    	window.close();
		    },

		    // Error is called in the event of an unexpected response from the server
		    // hosting the files, such as not being able to find a file. This callback is
		    // also called if there is an error on Dropbox or if the user is over quota.
		    error: function (errorMessage) {}
		};
		var button = Dropbox.createSaveButton(options);
		document.body.appendChild(button);
	</script>

	<h3>File Upload:</h3>
	Select a file to upload:
	<br />
	<form action="SaverResponse" method="post"
		enctype="multipart/form-data">
		<input type="file" name="file"/> <br/>
		<input type="submit" value="Upload File"/>
	</form>
</body>
</html>
