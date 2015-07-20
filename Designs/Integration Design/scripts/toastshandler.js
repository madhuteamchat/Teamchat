//this script is a helper script used to display errors or success in toast messages
function showToast(id, text) {
  var toast = document.getElementById(id);
  //check if toast is visible
  if (toast.visible) {
    //if yes hide it
    toast.hide();
  }
  //set toast message
  toast.text = text;
  //display toast again
  toast.show();
}
