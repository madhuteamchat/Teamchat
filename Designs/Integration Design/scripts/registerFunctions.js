var toggleForm = function(showForm, showImage, hideForm, hideImage) {
  $("#" + hideForm).slideUp(50);
  $("#" + hideImage).slideUp(50);
  $("#" + showForm).slideDown(500);
  $("#" + showImage).slideDown(500);
}

function handleEvents(e) {
  var el = e.currentTarget;
  integration.currentService = integration.services[e.currentTarget.id];
  if (el.id == undefined || el.id == null) {
    // alert("Error");
    showToast('toaster', 'An Error Occurred');
  } else {
    if (document.getElementById('service-main').opened) {
      //close the existing service
      document.getElementById('service-main').toggle();
    }
    //toggle open the spinner
    document.getElementById('service-loader').toggle();
    //set properties inside collapse
    var strigger = el.classList[1].replace("-service", "");
    setTimeout(function() {
      //change text description of service
      document.querySelector('#service-main .content h3 span').innerHTML = strigger.capitalize();
      //change service icon
      document.querySelector('#service-main .content h3 img').setAttribute('src', el.querySelector('img').getAttribute('src'));
      //toggle open the spinner
      document.getElementById('service-loader').toggle();
      //toggle iron collapse
      document.getElementById('service-main').toggle();
    }, 1500);
    loadDetails(el.id);
  }
}

var loadDetails = function(target) {

  var service = new Object();
  service.adminEmail = integration.user.email;
  service.typeId = integration.currentService.typeId;
  // toggleForm("regServicesShow", "registeredServicesTable", "service-form",
  //   "regServicesHide");
  admin.service.getAllServiceOfUser(service, getServiceDataSuccess,
    getServiceDataFailure);
  var html = Mustache.to_html(template.serviceHead,
    integration.currentService);

  console.log(html)

  $('.service_popup_container_head').empty();
  $('#registeredServicesTable').empty();
  $('.service_popup_container_head').html(html);
}

var addService = function() {
  var clientKey = $('#reg-service-cKey').val();
  if (clientKey == null) {
    // alert("You need to register a client before registering for a service. Click on close and go to register client section");
    showToast('toaster', 'Please register a client first !');
  } else {
    // toggleForm("service-form", "regServicesHide", "regServicesShow",
    //   "registeredServicesTable");
  }
}

var addApplication = function() {
  var clientKey = $('#reg-app-cKey').val();
  if (clientKey == null) {
    // alert("You need to register a client before registering for an application");
    showToast('toaster', 'Please register a client first !');
  } else {
    // toggleForm("app-form", "regAppsHide", "regAppsShow",
    //   "registeredApplicationsTable");
  }
}

var addWorkflow = function() {
  var clientKey = $('#reg-workflow-cKey').val();
  if (clientKey == null) {
    // alert("You need to register a client before registering for a workflow");
    showToast('toaster', 'Please register a client first !');
  } else {
    toggleForm("workflow-form", "regWorkflowsHide", "regWorkflowsShow",
      "registeredWorkflowsTable");
  }
}

var getClientDataSuccess = function(data) {
  loadClientDataTable(data, "#registeredClientsTable");
  //set client key drop down
  $("#reg-app-cKey").empty();
  var select = document.getElementById("reg-app-cKey");
  // for (var i = 0; i < data.length; i++) {
  //   var option = document.createElement('option');
  //   option.text = data[i].cName + ' ::::: ' + data[i].clientKey;
  //   option.value = data[i].clientKey;
  //   select.add(option, 0);
  // }

  $("#reg-service-cKey").empty();
  var selectService = document.getElementById("reg-service-cKey");
  for (client of data) {
    $("<paper-item title=\"" + client.cName + ' ::::: ' + client.clientKey + "\">" + client.cName + ' ::::: ' + client.clientKey + "</paper-item>").appendTo('#clientDropBox .list');
  }
  //add event handler for this drop down
  addDropdownHandler('clientDropBox', 'reg-service-cKey', 'clientDrop');
  //set a default value for the hidden
  updateDropdown('clientDropBox', 'reg-service-cKey', 'clientDrop', 'paper-item:first-of-type');

  $("#reg-workflow-cKey").empty();
  var selectWorkflow = document.getElementById("reg-workflow-cKey");
  for (var i = 0; i < data.length; i++) {
    var option = document.createElement('option');
    option.text = data[i].cName + ' ::::: ' + data[i].clientKey;
    option.value = data[i].clientKey;
    selectWorkflow.add(option, 0);
  }
  // toggleForm("regClientsShow", "registeredClientsTable", "client-form",
  //   "regClientsHide");
}

var getClientDataFailure = function(data) {
  // alert("getClientDataFailure: " + JSON.stringify(data));
  showToast('toaster', 'Error retrieving clients');
}

var registerClientSuccess = function(data) {
  // alert("Success. Client Key: " + data.clientKey);
  showToast('toaster', 'Client has been registered successfully');
  var client = new Object();
  client.adminEmail = integration.user.email;
  admin.client.getAllClientsOfUser(client, getClientDataSuccess,
    getClientDataFailure);
}

var registerClientFailure = function(data) {
  //alert("registerFailure: " + JSON.stringify(data));
  showToast('toaster', 'Couldn\'t register client');
}

var getApplicationDataSuccess = function(data) {
  toggleForm("regAppsShow", "registeredApplicationsTable", "app-form",
    "regAppsHide");
  loadApplicationDataTable(data, "#registeredApplicationsTable");
}

var getApplicationDataFailure = function(data) {
  // alert("getApplicationDataFailure: " + JSON.stringify(data));
  showToast('toaster', 'Error retirieving applications');
}

var getServiceDataSuccess = function(data) {
  loadServiceDataTable(data, "#registeredServicesTable");
}

var getServiceDataFailure = function(data) {
  // alert("getServiceDataFailure: " + JSON.stringify(data));
  showToast('toaster', 'Error retrieving services');
}

var getAllServiceDataSuccess = function(data) {
  for (service of data) {
    //TODO if services are present
    //store them in count
    var count = 0,
      countHtml = "";
    //if there is some count add it
    if (count > 0) {
      countHtml = String.format('<span class="badge">{0}</span>', count);
    }
    //store services in var for reference
    integration.services[service.typeId] = service;
    //populate services list
    var serviceHtml = String.format('<div id="{0}" class="service {1}-service" title="{2}"><img src="{3}" alt="{4}" />{5}</div>',
      service.typeId,
      service.typeId.toLowerCase(),
      service.typeId.toLowerCase().capitalize(),
      service.imgUrl,
      service.typeId,
      countHtml);
    $('#availableServices').append(serviceHtml);
    //add click event to that service
    $('#' + service.typeId).bind('click', function(e) {
      handleEvents(e);
    });
  }
  // for (var i = 0; i < data.length; i++) {
  // 	var service = data[i];
  // 	integration.services[service.typeId] = service;
  // 	var html = Mustache.to_html(template.serviceListTemplate, service);
  // 	console.log(service.typeId);
  // 	$('#availableServices').append(html);
  // 	$('#' + service.typeId).bind('click', function(e) {
  // 		handleEvents(e);
  // 	});
  // }
}

var getAllServiceDataFailure = function(data) {
  // alert("getServiceDataFailure: " + JSON.stringify(data));
  showToast('toaster', 'Error retrieving services');
}

var getWorkflowDataSuccess = function(data) {
  toggleForm("regWorkflowsShow", "registeredWorkflowsTable", "workflow-form",
    "regWorkflowsHide");
  loadWorkflowDataTable(data, "#registeredWorkflowsTable");
}

var getWorkflowDataFailure = function(data) {
  // alert("getWorkflowDataFailure: " + JSON.stringify(data));
  showToast('toaster', 'Error retirieving workflows');
}

var registerApplicationSuccess = function(data) {
  // alert("Success.");
  showToast('toaster', 'Successfully created application');
  var application = new Object();
  application.adminEmail = integration.user.email;
  admin.application.getAllApplicationsOfUser(application,
    getApplicationDataSuccess, getApplicationDataFailure);
}

var registerApplicationFailure = function(data) {
  // alert("registerFailure: " + JSON.stringify(data));
  showToast('toaster', 'Error retirieving applications');
}

var registerServiceSuccess = function(data) {
  // alert("Success");
  showToast('toaster', 'Successfully registered a new service !');

  var service = new Object();
  service.adminEmail = integration.user.email;
  service.typeId = integration.currentService.typeId;
  // toggleForm("regServicesShow", "registeredServicesTable", "service-form", "regServicesHide");
  admin.service.getAllServiceOfUser(service, getServiceDataSuccess,
    getServiceDataFailure);
}

var registerServiceFailure = function(data) {
  // alert("registerServiceFailure: " + JSON.stringify(data));
  showToast('toaster', 'Error registering the service');
}

var registerWorkflowSuccess = function(data) {
  // alert("Success");
  showToast('toaster', 'Successfully registered workflows');
  var workflow = new Object();
  workflow.adminEmail = integration.user.email;
  admin.workflow.getAllWorkflowsOfUser(workflow, getWorkflowDataSuccess,
    getWorkflowDataFailure);
}

var registerWorkflowFailure = function(data) {
  // alert("registerWorkflowFailure: " + JSON.stringify(data));
  showToast('toaster', 'Error retirieving workflows');
}

var registerClient = function() {

  var client = new Object();
  client.cName = $('#reg-client-name').val();
  client.cOrg = $('#reg-client-org').val();
  client.cEmail = $('#reg-client-email').val();
  client.cDescription = $('#reg-client-description').val();
  client.adminEmail = integration.user.email;
  client.adminUserId = integration.user.userId;
  client.adminName = integration.user.name;
  admin.client.register(client, registerClientSuccess, registerClientFailure);

}

var registerApplication = function() {

  var application = new Object();
  application.appName = $('#reg-app-name').val();
  application.appDescription = $('#reg-app-description').val();
  application.appEventtype = $('#reg-app-eventtype').val();
  application.appCallbackurl = $('#reg-app-callbackurl').val();
  application.clientkey = $('#reg-app-cKey').val();
  application.adminEmail = integration.user.email;
  application.adminUserId = integration.user.userId;
  application.adminName = integration.user.name;
  admin.application.register(application, registerApplicationSuccess,
    registerApplicationFailure);

}

var registerService = function() {

  var service = new Object();
  service.clientKey = $('#reg-service-cKey').val();
  service.serName = $('#reg-service-name').val();
  service.serDesc = $('#reg-service-desc').val();
  service.typeId = integration.currentService.typeId;
  service.adminEmail = integration.user.email;
  service.adminUserId = integration.user.userId;
  service.adminName = integration.user.name;
  admin.service.register(service, registerServiceSuccess,
    registerServiceFailure);
}

var registerWorkflow = function() {

  var workflow = new Object();
  workflow.wfName = $('#reg-workflow-name').val();
  workflow.clientKey = $('#reg-workflow-cKey').val();
  workflow.botEmail = $('#reg-workflow-bot-email').val();
  workflow.botPassword = $('#reg-workflow-bot-password').val();
  workflow.callbackUrl = $('#reg-workflow-callback-url').val();
  workflow.failMessage = $('#reg-workflow-failmessage').val();
  workflow.adminEmail = integration.user.email;
  workflow.adminUserId = integration.user.userId;
  workflow.adminName = integration.user.name;
  admin.workflow.register(workflow, registerWorkflowSuccess,
    registerWorkflowFailure);

}
