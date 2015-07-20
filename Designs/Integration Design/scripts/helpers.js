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
