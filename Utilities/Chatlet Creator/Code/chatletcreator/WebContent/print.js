
var head="";
var foot="";

function print_result() {
	head = localStorage.getItem("head");
	foot = localStorage.getItem("foot");
	console.log(head + foot);

	$("#done").hide(600);
	// $("#email_text").css("visibility","visible");
	$("#email_text").show(600);
	$("#email_btn").show(600);
	console.log("0");
}
	
function postChatlet() {
	console.log("2");
	$.ajax({
		type : "POST",
		url : "createchatlet",
		data : head + foot,
		success : function() {
			alert("done");
		}
	});}


