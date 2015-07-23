//helper functions
//Puranjay Jain
//find parent by type FORM,DIV,etc.
function findParentById(el, type) {
  while (el = el.parentNode)
    if (el.nodeName == type)
      return el;
  return null;
}
//makes string's first letter capitalize
//usage "stirngxyz".capitalize()
String.prototype.capitalize = function() {
  return this.charAt(0).toUpperCase() + this.slice(1);
};
//string format like c#
//usage var x = String.format("silly {0} beans are {1} good","little","very");
//x is silly little beans are very good
String.format = function() {
    // The string containing the format items (e.g. "{0}")
    // will and always has to be the first argument.
    var theString = arguments[0];

    // start with the second argument (i = 1)
    for (var i = 1; i < arguments.length; i++) {
      // "gm" = RegEx options for Global search (more than one instance)
      // and for Multiline search
      var regEx = new RegExp("\\{" + (i - 1) + "\\}", "gm");
      theString = theString.replace(regEx, arguments[i]);
    }

    return theString;
  }
  //this function creates the drop down handler for a custom created dropdown
  //select element so update the value of hidden
  // e.g
  // here clientDropBox is container,reg-service-cKey is hidden and clientDrop is inner
  // <!-- drop down clickable -->
  // <div id="clientDrop" class="list drop-down">
  //   <div class="flex">Select Client</div>
  //   <iron-icon icon="arrow-drop-down"></iron-icon>
  // </div>
  // <!-- drop down contents -->
  // <div id="clientDropBox" class="horizontal-section drop-down-data">
  //   <paper-menu class="list">
  //    <paper-item>some option</paper-item>
  //   </paper-menu>
  // </div>
  // <!-- drop down data -->
  // <input type="hidden" name="reg-service-cKey" id="reg-service-cKey" value="">
function addDropdownHandler(container, inner, hidden) { //drop down button
  //open drop down and close it
  document.getElementById(inner).onclick = function(event) {
    document.getElementById(container).classList.toggle('open');
  };
  var elements = document.querySelectorAll('#' + container + ' .innerlist paper-item');
  Array.prototype.forEach.call(elements, function(el, i) {
    el.classList.remove('selected-item');
    el.addEventListener("click", function() {
      document.getElementById(container).classList.toggle('open');
      //update the hidden
      document.getElementById(hidden).value = el.innerHTML.trim();
      //update the drop down
      document.querySelector('#' + inner + ' .flex').innerHTML = el.innerHTML.trim();
      document.querySelector('#' + inner + ' .flex').setAttribute('title', el.innerHTML.trim());
      //remove selected highlight from wrong item
      var elementsx = document.querySelectorAll('#' + container + ' .innerlist paper-item');
      Array.prototype.forEach.call(elements, function(el, i) {

      });
      // and add it to correct item
      el.classList.add('selected-item');
    });
  });
}

//update a drop down the first time
function updateDropdown(container, hidden, inner, item) {
  //get the item;s value
  var value = document.querySelector('#' + container + ' .innerlist ' + item).innerHTML.trim();
  //update the hidden
  document.getElementById(hidden).value = value.trim();
  //update the drop down
  document.querySelector('#' + inner + ' .flex').innerHTML = value.trim();
  document.querySelector('#' + inner + ' .flex').setAttribute('title', value.trim());
  //add selected to correct item
  //sorry jquery really helps here
  document.querySelector('#' + container + ' .innerlist ' + item).classList.add('selected-item');
}

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
