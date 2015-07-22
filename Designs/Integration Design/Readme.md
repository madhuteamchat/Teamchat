User Docs
=======================

This is a [Teamchat](https://teamchat.com/) integration dashboard console redesign.

### Table of contents

  use ghmd toc to generate toc

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

and it looks like ![paper fab](https://raw.githubusercontent.com/madhuteamchat/Teamchat/master/Designs/Integration%20Design/docs/fab.png)

> Written by [Puranjay Jain](https://github.com/puranjayjain).