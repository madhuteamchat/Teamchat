/**
 * performs validations
 */
function validateUpload()
{
	var success = false;
	var room =$('#roomId').val();
	var subject =$('#subject').val();
	var comment =$('#comment').val();
	if(room.length > 0)
	{
		success = true;
	}
	else
	{
		alert("Please select a room");
		return false;
	}
	
	if(subject.length > 0)
	{
		success = true;
	}
	else
	{
		alert("Please enter subject");
		return false;
	}
	
	if(comment.length > 0)
	{
		success = true;
	}
	else
	{
		alert("Please enter comment");
		return false;
	}
	
	return success;
}