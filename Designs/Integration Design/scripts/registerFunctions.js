var toggleForm = function(showForm, showImage, hideForm, hideImage) {
	$("#" + hideForm).slideUp(50);
	$("#" + hideImage).slideUp(50);
	$("#" + showForm).slideDown(500);
	$("#" + showImage).slideDown(500);
}

function handleEvents(e) {
	var target = e.currentTarget.id;
	integration.currentService = integration.services[e.currentTarget.id];
	if (target == undefined || target == null) {
		alert("Error");
	} else {
		loadDetails(target);

	}
}

var loadDetails = function(target) {

	var service = new Object();
	service.adminEmail = integration.user.email;
	service.typeId = integration.currentService.typeId;
	toggleForm("regServicesShow", "registeredServicesTable", "service-form",
			"regServicesHide");
	admin.service.getAllServiceOfUser(service, getServiceDataSuccess,
			getServiceDataFailure);
	var html = Mustache.to_html(template.serviceHead,
			integration.currentService);

	console.log(html)

	$('.service_popup_container_head').empty();
	$('#registeredServicesTable').empty();
	$('.service_popup_container_head').html(html);

	showPopUp("#service_popup");

}

var addService = function() {
	var clientKey = $('#reg-service-cKey').val();
	if (clientKey == null) {
		alert("You need to register a client before registering for a service. Click on close and go to register client section");
	} else {
		toggleForm("service-form", "regServicesHide", "regServicesShow",
				"registeredServicesTable");
	}
}

var addApplication = function() {
	var clientKey = $('#reg-app-cKey').val();
	if (clientKey == null) {
		alert("You need to register a client before registering for an application");
	} else {
		toggleForm("app-form", "regAppsHide", "regAppsShow",
				"registeredApplicationsTable");
	}
}

var addWorkflow = function() {
	var clientKey = $('#reg-workflow-cKey').val();
	if (clientKey == null) {
		alert("You need to register a client before registering for a workflow");
	} else {
		toggleForm("workflow-form", "regWorkflowsHide", "regWorkflowsShow",
				"registeredWorkflowsTable");
	}
}

var getClientDataSuccess = function(data) {
	loadClientDataTable(data, "#registeredClientsTable");
	$("#reg-app-cKey").empty();
	var select = document.getElementById("reg-app-cKey");
	for (var i = 0; i < data.length; i++) {
		var option = document.createElement('option');
		option.text = data[i].cName + ' ::::: ' + data[i].clientKey;
		option.value = data[i].clientKey;
		select.add(option, 0);
	}

	$("#reg-service-cKey").empty();
	var selectService = document.getElementById("reg-service-cKey");
	for (var i = 0; i < data.length; i++) {
		var option = document.createElement('option');
		option.text = data[i].cName + ' ::::: ' + data[i].clientKey;
		option.value = data[i].clientKey;
		selectService.add(option, 0);
	}

	$("#reg-workflow-cKey").empty();
	var selectWorkflow = document.getElementById("reg-workflow-cKey");
	for (var i = 0; i < data.length; i++) {
		var option = document.createElement('option');
		option.text = data[i].cName + ' ::::: ' + data[i].clientKey;
		option.value = data[i].clientKey;
		selectWorkflow.add(option, 0);
	}

	toggleForm("regClientsShow", "registeredClientsTable", "client-form",
			"regClientsHide");

}

var getClientDataFailure = function(data) {
	alert("getClientDataFailure: " + JSON.stringify(data));
}

var registerClientSuccess = function(data) {
	alert("Success. Client Key: " + data.clientKey);
	var client = new Object();
	client.adminEmail = integration.user.email;
	admin.client.getAllClientsOfUser(client, getClientDataSuccess,
			getClientDataFailure);
}

var registerClientFailure = function(data) {
	//alert("registerFailure: " + JSON.stringify(data));
}

var getApplicationDataSuccess = function(data) {
	toggleForm("regAppsShow", "registeredApplicationsTable", "app-form",
			"regAppsHide");
	loadApplicationDataTable(data, "#registeredApplicationsTable");
}

var getApplicationDataFailure = function(data) {
	alert("getApplicationDataFailure: " + JSON.stringify(data));
}

var getServiceDataSuccess = function(data) {
	loadServiceDataTable(data, "#registeredServicesTable");
}

var getServiceDataFailure = function(data) {
	alert("getServiceDataFailure: " + JSON.stringify(data));
}

var getAllServiceDataSuccess = function(data) {

	for (var i = 0; i < data.length; i++) {
		var service = data[i];
		integration.services[service.typeId] = service;
		var html = Mustache.to_html(template.serviceListTemplate, service);
		console.log(service.typeId);
		$('#availableServices').append(html);
		$('#' + service.typeId).bind('click', function(e) {

			handleEvents(e)
		})
	}
}

var getAllServiceDataFailure = function(data) {
	alert("getServiceDataFailure: " + JSON.stringify(data));
}

var getWorkflowDataSuccess = function(data) {
	toggleForm("regWorkflowsShow", "registeredWorkflowsTable", "workflow-form",
			"regWorkflowsHide");
	loadWorkflowDataTable(data, "#registeredWorkflowsTable");
}

var getWorkflowDataFailure = function(data) {
	alert("getWorkflowDataFailure: " + JSON.stringify(data));
}

var registerApplicationSuccess = function(data) {
	alert("Success.");
	var application = new Object();
	application.adminEmail = integration.user.email;
	admin.application.getAllApplicationsOfUser(application,
			getApplicationDataSuccess, getApplicationDataFailure);
}

var registerApplicationFailure = function(data) {
	alert("registerFailure: " + JSON.stringify(data));
}

var registerServiceSuccess = function(data) {
	alert("Success");

	var service = new Object();
	service.adminEmail = integration.user.email;
	service.typeId = integration.currentService.typeId;
	toggleForm("regServicesShow", "registeredServicesTable", "service-form",
			"regServicesHide");
	admin.service.getAllServiceOfUser(service, getServiceDataSuccess,
			getServiceDataFailure);

}

var registerServiceFailure = function(data) {
	alert("registerServiceFailure: " + JSON.stringify(data));
}

var registerWorkflowSuccess = function(data) {
	alert("Success");
	var workflow = new Object();
	workflow.adminEmail = integration.user.email;
	admin.workflow.getAllWorkflowsOfUser(workflow, getWorkflowDataSuccess,
			getWorkflowDataFailure);
}

var registerWorkflowFailure = function(data) {
	alert("registerWorkflowFailure: " + JSON.stringify(data));
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
