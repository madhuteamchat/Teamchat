<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Select files or folders</title>
<script type="text/javascript" src="https://www.dropbox.com/static/api/2/dropins.js" id="dropboxjs" data-app-key="<%=request.getParameter("id")%>"></script>
<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js"></script>

<style type="text/css">
body {
	background-color: rgba(0, 0, 0, 0.66);
	opacity: 0.8;
}
</style>

</head>
<body onload="bodyLoad()">
	<script>
		function bodyLoad() {
			$('.dropin-btn-status').click();
		}

		var options = {
			// Required. Called when a user selects an item in the Chooser.
			success : function(files) {
				$.post("ChooserResponse", { data : JSON.stringify(files),
					roomid : "<%=request.getParameter("roomid")%>"
				});
				window.close();
			},

			// Optional. Called when the user closes the dialog without selecting a file
			// and does not include any parameters.
			cancel : function() {
				window.close();
			},

			// Optional. "preview" (default) is a preview link to the document for sharing,
			// "direct" is an expiring link to download the contents of the file. For more
			// information about link types, see Link types below.
			linkType : "preview", // or "direct"

			// Optional. A value of false (default) limits selection to a single file, while
			// true enables multiple file selection.
			multiselect : true, // or true

		// Optional. This is a list of file extensions. If specified, the user will
		// only be able to select files with these extensions. You may also specify
		// file types, such as "video" or "images" in the list. For more information,
		// see File types below. By default, all extensions are allowed.
		//extensions: ['.pdf', '.doc', '.docx'],
		};
		button = Dropbox.createChooseButton(options);
		document.body.appendChild(button);
	</script>
</body>
</html>
