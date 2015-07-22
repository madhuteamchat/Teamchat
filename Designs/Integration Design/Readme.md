User Docs
=======================

This is a [Teamchat](https://teamchat.com/) integration dashboard console redesign.

### Table of contents

  * [Demo](#demo)
  * [Dependencies](#dependencies)
  * [Theme and colour](#theme-and-colour)
  * [Polymer components used](#polymer-components-used)
    * [Script dependencies](#script-dependencies)
  * [HTML](#html)
    * [Inside a Section](#inside-a-section)
  * [I did'nt understand this js function](#i-didnt-understand-this-js-function)

Demo
====

[Demo](http://madhuteamchat.github.io/Teamchat/Designs/Integration%20Design/dashboard.html)

Dependencies
============

Any web server to test this page in a browser.

Use [this](https://chrome.google.com/webstore/detail/allow-control-allow-origi/nlfbmbojpeacfghkpbjhddihlkkiljbi?utm_source=chrome-ntp-icon) plugin to test this demo in google chrome.

[Polymer](https://www.polymer-project.org/1.0/) and it's [Elements](https://elements.polymer-project.org/)

Theme and colour
================

```html
<meta name="theme-color" content="#159ceb">
<!-- for theme colour in android 5+ -->
```

Font colour : #fff
Primary theme colour : #159CEB
Secondary theme colour : #0087CA

Polymer styles in
```html
<link rel="import" href="css/dashboard-styles.html">
```

Global styles in
```html
<link rel="stylesheet" href="css/dashboard-styles-global.css">
```

Polymer components used
=====

```html
  <script src="bower_components/webcomponentsjs/webcomponents-lite.min.js">
  </script>
  <link rel="import" href="bower_components/iron-icon/iron-icon.html">
  <link rel="import" href="bower_components/iron-icons/iron-icons.html">
  <link rel="import" href="bower_components/iron-form/iron-form.html">
  <link rel="import" href="bower_components/font-roboto/roboto.html">
  <link rel="import" href="bower_components/iron-media-query/iron-media-query.html">
  <link rel="import" href="bower_components/iron-collapse/iron-collapse.html">
  <link rel="import" href="bower_components/paper-styles/paper-styles.html">
  <link rel="import" href="bower_components/paper-button/paper-button.html">
  <link rel="import" href="bower_components/paper-dialog/paper-dialog.html">
  <link rel="import" href="bower_components/paper-dialog-scrollable/paper-dialog-scrollable.html">
  <link rel="import" href="bower_components/paper-fab/paper-fab.html">
  <link rel="import" href="bower_components/paper-item/paper-icon-item.html">
  <link rel="import" href="bower_components/paper-item/paper-item.html">
  <link rel="import" href="bower_components/paper-item/paper-item-body.html">
  <link rel="import" href="bower_components/paper-input/paper-input.html">
  <link rel="import" href="bower_components/paper-input/paper-input-char-counter.html">
  <link rel="import" href="bower_components/paper-input/paper-textarea.html">
  <link rel="import" href="bower_components/paper-menu/paper-menu.html">
  <link rel="import" href="bower_components/paper-item/paper-item.html">
  <link rel="import" href="bower_components/paper-item/paper-icon-item.html">
  <link rel="import" href="bower_components/paper-ripple/paper-ripple.html">
  <link rel="import" href="bower_components/paper-spinner/paper-spinner.html">
  <link rel="import" href="bower_components/paper-toast/paper-toast.html">
  <link rel="import" href="bower_components/paper-toolbar/paper-toolbar.html">
  <link rel="import" href="bower_components/paper-tabs/paper-tabs.html">
  <link rel="import" href="bower_components/paper-tabs/paper-tab.html">
  <link rel="import" href="bower_components/paper-icon-button/paper-icon-button.html">
  <link rel="import" href="bower_components/gold-email-input/gold-email-input.html">
  <link rel="import" href="bower_components/neon-animation/neon-animated-pages.html">
  <link rel="import" href="bower_components/neon-animation/neon-animatable.html">
  <link rel="import" href="bower_components/neon-animation/neon-animations.html">
```
	

Script dependencies
-------

Other scripts dependent are

[Jquery](http://jquery.com/) (both versions) 

put as below for supporting ie7+ browsers
```html
<script src="scripts/jquery-2.1.3.min.js"></script>
  <!--[if lte IE 8]>
      <script src="scripts/jquery-1.11.3.min.js"></script>
  <![endif]-->
```

[MrJsonTable](http://mrjsontable.azurewebsites.net/) and on [Github](https://github.com/MatchingRadar/Mr.JsonTable) for converting json into a table

|Script|What it does| 
 ----------------- | ---------------------------- |
|Constants|Some basic constants required for use|
|mustache|template --> html|
|helpers|some helper functions like capitalize, etc.|
|admin|client, services, etc. registration and data retrieval functions|
|util|contains variables which are populated|
|templates|used by mustache|
|registerFunctions|event handlers (e.g click events, etc.) for registration of client, etc.|
|windowend|executes at the end of page load after all polymer elements are painted|

Script below is declared in the head as an independent
```html
<script type="text/JavaScript">
	//script code is explained below
</script>
```

Script Code:

Declaring which client, services, etc. to load
```javascript
var renderRegisteredUtils = function() {

      var client = new Object();
      client.adminEmail = 'puranjay.jain@st.niituniversity.in';
      var application = new Object();
      application.adminEmail = 'puranjay.jain@st.niituniversity.in';

      var service = new Object();
      service.adminEmail = 'puranjay.jain@st.niituniversity.in';

      var workflow = new Object();

      workflow.adminEmail = 'puranjay.jain@st.niituniversity.in';

      integration.user.name = 'puranjay.jain@st.niituniversity.in';
      integration.user.email = 'puranjay.jain@st.niituniversity.in';
      integration.user.userId = '22145';
```

Triggering data fetch functions on runtime

```javascript
      admin.service.getAllServiceList(service, getAllServiceDataSuccess,
        getAllServiceDataFailure);
      admin.client.getAllClientsOfUser(client, getClientDataSuccess,
        getClientDataFailure);
      admin.application.getAllApplicationsOfUser(application,
        getApplicationDataSuccess, getApplicationDataFailure);
      admin.workflow.getAllWorkflowsOfUser(workflow, getWorkflowDataSuccess,
        getWorkflowDataFailure);
    }
```

HTML
====

```html
<body class="fullbleed layout vertical" onload="renderRegisteredUtils();">
```

|What|What it does| 
 ----------------- | ---------------------------- |
|fullbleed|body **atleast** takes 100% windows size|
|onload="renderRegisteredUtils();"|trigger data fetch on load|

The page is divided into two sections

 1. navigation bar
 
 ```html
 <paper-toolbar class="tall">
    <paper-icon-button id="home_button" icon="extension"></paper-icon-button>
    <span title>Teamchat Integration Console</span>
    <h4>Puranjay Jain</h4>
    <paper-icon-button id="logout_button" icon="exit-to-app">Logout</paper-icon-button>
    <paper-tabs id="select_tab" selected="0" class="bottom self-end" style="width: 600px;">
      <paper-tab>PLATFORM CLIENT</paper-tab>
      <paper-tab>SERVICES</paper-tab>
      <paper-tab>APPLICATIONS</paper-tab>
      <paper-tab>WORKFLOWS</paper-tab>
    </paper-tabs>
    <div class="bottom flex"></div>
  </paper-toolbar>
 ```

![Toolbar](https://raw.githubusercontent.com/madhuteamchat/Teamchat/master/Designs/Integration%20Design/docs/toolbar.png)
 
 2. main content

![content](https://raw.githubusercontent.com/madhuteamchat/Teamchat/master/Designs/Integration%20Design/docs/content.png)

It is seperated into various **sections** (4)

 1. Clients
 2. Services
 3. Applications
 4. Workflows

Each **section** is inside `<neon-animatable>`:

```html
<neon-animated-pages id="page" class="flex" selected="0" entry-animation="fade-in-animation" exit-animation="fade-out-animation">
    <neon-animatable>
	    <!--A section's code is here-->
    </neon-animatable>
</neon-animated-pages>    
```

Inside a Section
----------------
The content description is done as this
```html
<div class="content">
        <h3>
          Register Client
        </h3>
        <p>
          This section is used to create a client (customer Id) on Teamchat Integration platform , later you can use this client id to create / consume / register application / services / workflows . This will also help you maintain and manage the application ,services
          , workflows you create
        </p>
</div>
```

And a content table is added as:

```html
<table id="registeredClientsTable"></table>
```

A client button trigger ( floating button )  is declared as:
```html
<paper-fab icon="add" id="register_client_button" title="Register a new client"></paper-fab>
```

and it looks like this ![paper fab](https://raw.githubusercontent.com/madhuteamchat/Teamchat/master/Designs/Integration%20Design/docs/fab.png)

All forms which are used for client registration,etc.are declated as

```html
<form onsubmit="return false" method="post">
        <paper-dialog style="max-height:460px;min-width:350px;" id="register_client" entry-animation="scale-up-animation" exit-animation="fade-out-animation" with-backdrop>
          <h2>Register a new Client</h2>
          <!-- <paper-dialog-scrollable> -->
          <div>
            <paper-input name="reg-client-name" id="reg-client-name" required auto-validate label="Name" autofocus char-counter maxlength="128"></paper-input>
            <paper-input name="reg-client-org" id="reg-client-org" required auto-validate label="Organization" char-counter maxlength="128"></paper-input>
            <gold-email-input name="reg-client-email" id="reg-client-email" required auto-validate error-message="Please enter a valid email" label="Email"></gold-email-input>
            <paper-textarea name="reg-client-description" id="reg-client-description" label="Description" char-counter maxlength="256"></paper-textarea>
          </div>
          <!-- </paper-dialog-scrollable> -->
          <div class="buttons">
            <paper-button class="discard" dialog-dismiss raised>Discard</paper-button>
            <paper-button id="reg-client-save" onclick="registerClient()" class="submit-button" dialog-confirm raised>Create</paper-button>
          </div>
        </paper-dialog>
      </form>
```

![A form](https://raw.githubusercontent.com/madhuteamchat/Teamchat/master/Designs/Integration%20Design/docs/form.png)

**Any Error** or **success** status is shown in this:
```html
<paper-toast id="toaster" text="An Error Occured"></paper-toast>
```
declared at page end before windowend.js and after the closing tag 
```html
</neon-animated-pages>
```

![paper toast](https://raw.githubusercontent.com/madhuteamchat/Teamchat/master/Designs/Integration%20Design/docs/toast.png)
		*A sample toast*

Custom drop downs are used in lieu of paper dropdown as Polymer 1.0 doesn't have them as of today

They are declared as 
```html
  <!-- drop down clickable -->
            <div id="clientDrop" class="list drop-down">
              <div class="flex">Select Client</div>
              <iron-icon icon="arrow-drop-down"></iron-icon>
            </div>
            <!-- drop down contents -->
            <div id="clientDropBox" class="horizontal-section drop-down-data">
              <paper-menu class="list innerlist">
              </paper-menu>
            </div>
            <!-- drop down data -->
            <input type="hidden" name="reg-service-cKey" id="reg-service-cKey" value="">
```

|Element Id|Use| 
 ----------------- | ---------------------------- |
|clientDropBox|drop down button|
|clientDrop|drop down items and inner content|
|reg-service-cKey|contains the data of the drop down|

A Sample Drop Down

```html
 <!-- drop down clickable -->
            <div id="clientDrop" class="list drop-down">
              <div class="flex" title="fdf ::::: 4f599606-8098-4d69-8d01-f8d140a72182">fdf ::::: 4f599606-8098-4d69-8d01-f8d140a72182</div>
              <iron-icon icon="arrow-drop-down" class="x-scope iron-icon-0"><svg viewBox="0 0 24 24" preserveAspectRatio="xMidYMid meet" class="style-scope iron-icon" style="pointer-events: none; display: block; width: 100%; height: 100%;"><g class="style-scope iron-icon"><path d="M7 10l5 5 5-5z" class="style-scope iron-icon"></path></g></svg>
    <iron-meta id="meta" type="iconset" class="style-scope iron-icon"></iron-meta>
  </iron-icon>
            </div>
            <!-- drop down contents -->
            <div id="clientDropBox" class="horizontal-section drop-down-data">
              <paper-menu class="list innerlist x-scope paper-menu-0" role="menu" tabindex="0"><paper-item title="fdf ::::: 4f599606-8098-4d69-8d01-f8d140a72182" role="listitem" class="x-scope paper-item-0 selected-item">
    fdf ::::: 4f599606-8098-4d69-8d01-f8d140a72182
  </paper-item><paper-item title="fdf ::::: 7d2a856d-05cc-41d7-b524-cc81293072ee" role="listitem" class="x-scope paper-item-0">
    fdf ::::: 7d2a856d-05cc-41d7-b524-cc81293072ee
  </paper-item><paper-item title="fdffdsfxx ::::: 345274f6-e07d-4f5a-abde-cd1513a4e56f" role="listitem" class="x-scope paper-item-0">
    fdffdsfxx ::::: 345274f6-e07d-4f5a-abde-cd1513a4e56f
  </paper-item></paper-menu>
            </div>
            <!-- drop down data -->
            <input type="hidden" name="reg-service-cKey" id="reg-service-cKey" value="fdf ::::: 4f599606-8098-4d69-8d01-f8d140a72182">
```

In both states 

 1. Closed

	![closed](https://raw.githubusercontent.com/madhuteamchat/Teamchat/master/Designs/Integration%20Design/docs/closeddrop.png)
 
 2. Open
 
	![open](https://raw.githubusercontent.com/madhuteamchat/Teamchat/master/Designs/Integration%20Design/docs/opendrop.png)

**Notice**
Each of the drop down's item is declared as

```html
<paper-item>
	Some option
<paper-item>
```

A custom event handler is attached to a drop down as

```javascript
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
function addDropdownHandler(container, hidden, inner) {
  var elements = document.querySelectorAll('#' + container + ' .innerlist paper-item');
  Array.prototype.forEach.call(elements, function(el, i) {
    el.addEventListener("click", function() {
      document.getElementById(container).classList.toggle('open');
      //update the hidden
      document.getElementById(hidden).value = el.innerHTML.trim();
      //update the drop down
      document.querySelector('#' + inner + ' .flex').innerHTML = el.innerHTML.trim();
      document.querySelector('#' + inner + ' .flex').setAttribute('title', el.innerHTML.trim());
      //remove selected highlight from wrong item
      var elementsx = document.querySelectorAll('innerlist paper-item');
      Array.prototype.forEach.call(elements, function(el, i) {
        el.classList.remove('selected-item');
      });
      // and add it to correct item
      el.classList.add('selected-item');
    });
  });
}
```

for more of these events refer to `helpers.js`


I did'nt understand this js function
=======

```javascript
	for (var item of data)
	{
		console.log(item);
	}
```

**Q:**So what is this for...of loop ?
**Q:**Is it same as for...in loop?

**Answer** *is* **NO**

Let's compare them

```javascript
	console.log("For of loop !");
	var data = {"A","B","C"};
	for (var item of data)
	{
		console.log(item);
	}
	console.log("For in loop !");
	for (var item in data)
	{
		console.log(item);
	}
```

The console reads as
```
For of loop !
A
B
C
For in loop !
1
2
3
```

**Q:** But how can I access each item in for...in loop?
**Answer** Accessing, each item in data can also be done in for...in by using

```javascript
console.log("For in loop !");
	for (var item in data)
	{
		console.log(data[item]);
	}
```

> Written by [Puranjay Jain](https://github.com/puranjayjain).