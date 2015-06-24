<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!doctype html>
<html>

<head>
  <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0">
  <meta name="mobile-web-app-capable" content="yes">
  <meta name="apple-mobile-web-app-capable" content="yes">
  <title>Teamchat Integration Console</title>
  <link rel="shortcut icon" href="https://enterprise.teamchat.com/webjim-echat/static/img/favicon.png" type="image/x-icon">
  <meta name="theme-color" content="#159ceb">
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
  <link rel="import" href="bower_components/paper-radio-button/paper-radio-button.html">
  <link rel="import" href="bower_components/paper-radio-group/paper-radio-group.html">
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
  <link rel="stylesheet" href="css/dashboard-styles-global.css">
  <link rel="import" href="css/dashboard-styles.html">
  <script src="scripts/jquery-2.1.3.min.js"></script>
  <!--[if lte IE 8]>
      <script src="scripts/jquery-1.11.3.min.js"></script>
  <![endif]-->
  <script src="scripts/Constants.js"></script>
  <script src="scripts/mustache.js"></script>
  <script src="scripts/helpers.js"></script>
  <script src="scripts/admin.js"></script>
  <script src="scripts/util.js"></script>
  <script src="scripts/templates.js"></script>
  <script src="scripts/mrJsonTableConfig.js"></script>
  <script src="scripts/registerFunctions.js"></script>
  <script src="scripts/mrjsontable.js"></script>
  <script>
    var renderRegisteredUtils = function() {
      var client = new Object();
      client.adminEmail = '${sessionScope.email}';
      var application = new Object();
      application.adminEmail = '${sessionScope.email}';
      var service = new Object();
      service.adminEmail = '${sessionScope.email}';
      var workflow = new Object();
      workflow.adminEmail = '${sessionScope.email}';
      integration.user.name = '${sessionScope.email}';
      integration.user.email = '${sessionScope.email}';
      integration.user.userId = '${sessionScope.userId}';
      admin.service.getAllServiceList(service, getAllServiceDataSuccess,
        getAllServiceDataFailure);
      admin.client.getAllClientsOfUser(client, getClientDataSuccess,
        getClientDataFailure);
      admin.application.getAllApplicationsOfUser(application,
        getApplicationDataSuccess, getApplicationDataFailure);
      admin.workflow.getAllWorkflowsOfUser(workflow, getWorkflowDataSuccess,
        getWorkflowDataFailure);
    }
  </script>
</head>

<body class="fullbleed layout vertical" onload="renderRegisteredUtils()">
  <c:choose>
    <c:when test="${not empty sessionScope.email}">
      <paper-toolbar class="tall">
        <paper-icon-button id="home_button" icon="extension"></paper-icon-button>
        <span title>Teamchat Integration Console</span>
        <div class="right-fixed">
          <h4>Puranjay Jain</h4>
          <paper-icon-button id="logout_button" icon="exit-to-app">Logout</paper-icon-button>
        </div>
        <paper-tabs id="select_tab" selected="0" class="bottom self-end" style="width: 600px;">
          <paper-tab>PLATFORM CLIENT</paper-tab>
          <paper-tab>SERVICES</paper-tab>
          <paper-tab>APPLICATIONS</paper-tab>
          <paper-tab>WORKFLOWS</paper-tab>
        </paper-tabs>
        <div class="bottom flex"></div>
      </paper-toolbar>
      <neon-animated-pages id="page" class="flex" selected="0" entry-animation="fade-in-animation" exit-animation="fade-out-animation">
        <neon-animatable>
          <div class="content">
            <h3>
              Register Client
            </h3>
            <p>
              This section is used to create a client (customer Id) on Teamchat Integration platform , later you can use this client id to create / consume / register application / services / workflows . This will also help you maintain and manage the application ,services
              , workflows you create
            </p>
          </div>
          <table id="registeredClientsTable">
          </table>
          <paper-fab icon="add" class="corner-fab" id="register_client_button" title="Register a new client"></paper-fab>
          <form onsubmit="return false" id="client-form" method="post">
            <paper-dialog id="register_client" entry-animation="scale-up-animation" exit-animation="fade-out-animation" with-backdrop>
              <h2>Register a new Client</h2>
              <paper-dialog-scrollable>
                <paper-input name="reg-client-name" id="reg-client-name" required auto-validate label="Name" autofocus char-counter maxlength="128"></paper-input>
                <paper-input name="reg-client-org" id="reg-client-org" required auto-validate label="Organization" char-counter maxlength="128"></paper-input>
                <gold-email-input name="reg-client-email" id="reg-client-email" required auto-validate error-message="Please enter a valid email" label="Email"></gold-email-input>
                <paper-textarea name="reg-client-description" id="reg-client-description" label="Description" char-counter maxlength="256"></paper-textarea>
              </paper-dialog-scrollable>
              <div class="buttons">
                <paper-button class="discard" dialog-dismiss raised>Discard</paper-button>
                <paper-button id="reg-client-save" onclick="registerClient()" class="submit-button" dialog-confirm raised>Create</paper-button>
              </div>
            </paper-dialog>
          </form>
        </neon-animatable>
        <neon-animatable>
          <div class="content">
            <h3>
              Self Serve Integration Services
            </h3>
            <p>
              This section id used to create a client (customer Id) on Teamchat Integration platfrom , later you can use this client id to create / consume / register application ,services , workflows . This will also help you maintain and manage the application ,services
              , workflows you create
            </p>
            <div id="availableServices" class="services">
            </div>
          </div>
          <iron-collapse id="service-loader">
            <paper-spinner active></paper-spinner>
          </iron-collapse>
          <iron-collapse id="service-main">
            <div class="content">
              <h3>
                <img src="img/git.png" alt="github" />
                <span>Git</span>
                <paper-button id="register_this_button" onclick="addService()" raised>Create New</paper-button>
                <paper-button id="register_close_button" class="discard" raised>Close</paper-button>
              </h3>
              <p>
                This section is used to create a client (customer Id) on Teamchat Integration platfrom. Later you can use this client id to create / consume / register -
                <ol>
                  <li>Applications</li>
                  <li>Services</li>
                  <li>Workflows</li>
                </ol> This will also help you maintain and manage the applications ,services and workflows that you create
              </p>
            </div>
            <table id="registeredServicesTable">
            </table>
          </iron-collapse>
          <form onsubmit="return false" id="service-form" method="post">
            <paper-dialog id="register_service" with-backdrop entry-animation="scale-up-animation" exit-animation="fade-out-animation" with-backdrop>
              <h2>Register a new Service</h2>
              <paper-dialog-scrollable>
                <div class="drop-down-container">
                  <div id="clientDrop" class="list drop-down">
                    <div class="flex">Select Client</div>
                    <iron-icon icon="arrow-drop-down"></iron-icon>
                    <div id="clientDropBox" class="horizontal-section drop-down-data">
                      <paper-menu class="list innerlist">
                      </paper-menu>
                    </div>
                  </div>
                  <input type="hidden" name="reg-service-cKey" id="reg-service-cKey" value="">
                </div>
                <paper-input name="reg-service-name" id="reg-service-name" required auto-validate label="Name" char-counter maxlength="128"></paper-input>
                <paper-textarea name="reg-service-desc" id="reg-service-desc" label="Description" char-counter maxlength="256"></paper-textarea>
              </paper-dialog-scrollable>
              <div class="buttons">
                <paper-button class="discard" dialog-dismiss raised>Discard</paper-button>
                <paper-button class="submit-button" onclick="registerService()" dialog-confirm autofocus raised>Create</paper-button>
              </div>
            </paper-dialog>
          </form>
        </neon-animatable>
        <neon-animatable>
          <div class="content">
            <h3>
              Register Application
            </h3>
            <p>
              This section is used to create an application (app Id) on Teamchat Integration platfrom.
            </p>
          </div>
          <table id="registeredApplicationsTable">
          </table>
          <paper-fab icon="add" class="corner-fab" id="register_app_button" onclick="addApplication()" title="Register a new application"></paper-fab>
          <form onsubmit="return false" id="app-form" method="post">
            <paper-dialog id="register_application" entry-animation="scale-up-animation" exit-animation="fade-out-animation" with-backdrop>
              <h2>Register a new Application</h2>
              <paper-dialog-scrollable>
                <div class="drop-down-container">
                  <div id="appDrop" class="list drop-down">
                    <div class="flex">Select Client</div>
                    <iron-icon icon="arrow-drop-down"></iron-icon>
                  </div>
                  <div id="appDropBox" class="horizontal-section drop-down-data">
                    <paper-menu class="list innerlist">
                    </paper-menu>
                  </div>
                  <input type="hidden" id="reg-app-cKey" name="reg-app-cKey" value="">
                </div>
                <paper-input name="reg-app-name" id="reg-app-name" required auto-validate label="Name" autofocus char-counter maxlength="128"></paper-input>
                <paper-textarea name="reg-app-description" id="reg-app-description" label="Description" char-counter maxlength="256"></paper-textarea>
                <h4>Event Type</h4>
                <paper-radio-group id="reg-event-group" selected="push">
                  <paper-radio-button name="push">Push</paper-radio-button>
                  <paper-radio-button name="pull">Pull</paper-radio-button>
                </paper-radio-group>
                <input type="hidden" name="reg-app-eventtype" id="reg-app-eventtype" value="push">
                <paper-input name="reg-app-callbackurl" id="reg-app-callbackurl" required auto-validate label="Callback Url" char-counter maxlength="2083"></paper-input>
              </paper-dialog-scrollable>
              <div class="buttons">
                <paper-button class="discard" dialog-dismiss raised>Discard</paper-button>
                <paper-button id="reg-app-save" onclick="registerApplication()" class="submit-button" dialog-confirm raised>Create</paper-button>
              </div>
            </paper-dialog>
          </form>
        </neon-animatable>
        <neon-animatable>
          <div class="content">
            <h3>
              Register Workflows
            </h3>
            <p>
              This section is used to create a workflow (app Id) on Teamchat Integration platfrom.
            </p>
          </div>
          <table id="registeredWorkflowsTable">
          </table>
          <paper-fab icon="add" class="corner-fab" id="register_work_button" onclick="addWorkflow()" title="Register a new workflow"></paper-fab>
          <form onsubmit="return false" id="workflow-form" method="post">
            <paper-dialog id="register_workflow" entry-animation="scale-up-animation" exit-animation="fade-out-animation" with-backdrop>
              <h2>Register a new Workflow</h2>
              <paper-dialog-scrollable>
                <div class="drop-down-container">
                  <div id="workDrop" class="list drop-down">
                    <div class="flex">Select Client</div>
                    <iron-icon icon="arrow-drop-down"></iron-icon>
                  </div>
                  <div id="workDropBox" class="horizontal-section drop-down-data">
                    <paper-menu class="list innerlist">
                    </paper-menu>
                  </div>
                  <input type="hidden" id="reg-workflow-cKey" name="reg-workflow-cKey" value="">
                </div>
                <paper-input name="reg-workflow-name" id="reg-workflow-name" required auto-validate label="Name" autofocus char-counter maxlength="128"></paper-input>
                <gold-email-input name="reg-workflow-bot-email" id="reg-workflow-bot-email" required auto-validate error-message="Please enter a valid email" label="Bot Email"></gold-email-input>
                <paper-input name="reg-workflow-bot-password" id="reg-workflow-bot-password" type="password" required auto-validate label="Bot Password"></paper-input>
                <paper-input name="reg-workflow-callback-url" id="reg-workflow-callback-url" required auto-validate label="Callback Url" char-counter maxlength="2083"></paper-input>
                <paper-input name="reg-workflow-failmessage" id="reg-workflow-failmessage" label="Failure Url" char-counter maxlength="2083"></paper-input>
              </paper-dialog-scrollable>
              <div class="buttons">
                <paper-button class="discard" dialog-dismiss raised>Discard</paper-button>
                <paper-button id="reg-app-save" onclick="registerWorkflow()" class="submit-button" dialog-confirm raised>Create</paper-button>
              </div>
            </paper-dialog>
          </form>
        </neon-animatable>
      </neon-animated-pages>
  <paper-toast id="toaster" text="An Error Occured"></paper-toast>
  <script src="scripts/windowend.js"></script>
  </c:when>
  <c:otherwise>
    <c:redirect url="/login.html"></c:redirect>
  </c:otherwise>
</c:choose>
</body>

</html>
