var client = {

  register: function(client, success, error) {

    console.log("Inside Reg Client");
    var clientName = client.cName;
    var clientOrg = client.cOrg;
    var clientOrgEmail = client.cEmail;
    var clientDescription = client.cDescription;
    var adminEmail = client.adminEmail;
    var adminUserId = client.adminUserId;
    var adminName = client.adminName;

    var data = {

      operation: OPERATION.CREATE,
      cName: clientName,
      cOrg: clientOrg,
      cOrgEmail: clientOrgEmail,
      cDescription: clientDescription,
      adminEmail: adminEmail,
      adminUserId: adminUserId,
      adminName: adminName
    }

    if (clientName == "" || clientOrg == "" || clientOrgEmail == "") {
      showToast('toast_client', 'Please fill the required fields !');
      return false;
    } else {
      ajaxCall(KEYSTORE.CLIENT, success, error, KEYSTORE.JSON, HTTP.POST, JSON.stringify(data));
      return true;
    }

  },
  getAllClientsOfUser: function(client, success, error) {
    this.getData(client, OPERATION.GET_ALL_CLIENTS_OF_USER, success, error);
  },
  getData: function getData(client, operation, success, error) {
    var jsonObj = new Object();
    jsonObj.adminEmail = client.adminEmail;
    var data = {
      query: jsonObj,
      operation: operation
    }

    ajaxCall(KEYSTORE.CLIENT, success, error, KEYSTORE.JSON, HTTP.POST, JSON.stringify(data));
    return true;
  }

}

var application = {

  register: function(app, success, error) {

    console.log("Inside Reg Client");
    var clientKey = app.clientkey;
    var appName = app.appName;
    var appDesc = app.appDescription;
    var appEvtType = app.appEventtype;
    var appCbUrl = app.appCallbackurl;
    var adminEmail = app.adminEmail;
    var adminUserId = app.adminUserId;
    var adminName = app.adminName;

    var data = {

      operation: OPERATION.CREATE,
      clientKey: clientKey,
      appName: appName,
      appDesc: appDesc,
      appEvtType: appEvtType,
      adminEmail: adminEmail,
      appCbUrl: appCbUrl,
      adminUserId: adminUserId,
      adminName: adminName
    }

    if (clientKey == "" || appName == "" || appEvtType == "") {
      // alert("Please fill the required fields!");
      showToast('toaster', 'Please fill the required fields');
      return false;
    } else {
      ajaxCall(KEYSTORE.APPLICATION, success, error, KEYSTORE.JSON, HTTP.POST, JSON.stringify(data));
      return true;
    }

  },
  getAllApplicationsOfUser: function(app, success, error) {
    this.getData(app, OPERATION.GET_ALL_APPLICATIONS_OF_USER, success, error);
  },
  getData: function getData(app, operation, success, error) {
    var jsonObj = new Object();
    jsonObj.adminEmail = app.adminEmail;
    var data = {
      query: jsonObj,
      operation: operation
    }

    ajaxCall(KEYSTORE.APPLICATION, success, error, KEYSTORE.JSON, HTTP.POST, JSON.stringify(data));
    return true;
  }

}

var service = {

  register: function(service, success, error) {

    console.log("Inside Reg Service");
    var clientKey = service.clientKey;
    var serName = service.serName;
    var serDesc = service.serDesc;
    var typeId = service.typeId;
    var adminEmail = service.adminEmail;
    var adminUserId = service.adminUserId;
    var adminName = service.adminName;

    var data = {

      operation: OPERATION.CREATE,
      clientKey: clientKey,
      serName: serName,
      serDesc: serDesc,
      typeId: typeId,
      adminEmail: adminEmail,
      adminUserId: adminUserId,
      adminName: adminName
    }

    if (clientKey == null) {
      // alert("You need to register a client before you can register a service");
      showToast('toaster', 'Please register a client first !');
      return false;
    } else {
      ajaxCall(KEYSTORE.SERVICE, success, error, KEYSTORE.JSON, HTTP.POST, JSON.stringify(data));
      return true;
    }

  },
  getAllServiceList: function(service, success, error) {
    this.getData(service, OPERATION.GET_ALL_SERVICES, success, error);
  },
  getAllServiceOfUser: function(service, success, error) {
    this.getData(service, OPERATION.GET_ALL_SERVICES_OF_USER, success, error);
  },
  getData: function getData(service, operation, success, error) {
    var jsonObj = new Object();
    jsonObj.adminEmail = service.adminEmail;
    jsonObj.typeId = service.typeId;
    var data = {
      query: jsonObj,
      operation: operation
    }

    ajaxCall(KEYSTORE.SERVICE, success, error, KEYSTORE.JSON, HTTP.POST, JSON.stringify(data));
    return true;
  }

}

var workflow = {

  register: function(workflow, success, error) {

    console.log("Inside Reg Workflow");
    var clientKey = workflow.clientKey;
    var wfName = workflow.wfName;
    var botEmail = workflow.botEmail;
    var botPassword = workflow.botPassword;
    var wfCbUrl = workflow.callbackUrl;
    var failMessage = workflow.failMessage;
    var adminEmail = workflow.adminEmail;
    var adminUserId = workflow.adminUserId;
    var adminName = workflow.adminName;

    var data = {

      operation: OPERATION.CREATE,
      clientKey: clientKey,
      wfName: wfName,
      botEmail: botEmail,
      botPassword: botPassword,
      wfCbUrl: wfCbUrl,
      failMessage: failMessage,
      adminEmail: adminEmail,
      adminUserId: adminUserId,
      adminName: adminName
    }

    if (clientKey == null || wfName == "" || botEmail == "" || botPassword == "" || wfCbUrl == "") {
      // alert("Please fill the required fields!");
      showToast('toaster', 'Please fill the required fields!');
      return false;
    } else {
      ajaxCall(KEYSTORE.WORKFLOW, success, error, KEYSTORE.JSON, HTTP.POST, JSON.stringify(data));
      return true;
    }

  },
  getAllWorkflowsOfUser: function(app, success, error) {
    this.getData(app, OPERATION.GET_ALL_WORKFLOWS_OF_USER, success, error);
  },
  getData: function getData(app, operation, success, error) {
    var jsonObj = new Object();
    jsonObj.adminEmail = app.adminEmail;
    var data = {
      query: jsonObj,
      operation: operation
    }

    ajaxCall(KEYSTORE.WORKFLOW, success, error, KEYSTORE.JSON, HTTP.POST, JSON.stringify(data));
    return true;
  }

}

var admin = {

  client: client,
  service: service,
  application: application,
  workflow: workflow
}
