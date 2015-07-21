function ajaxCall(url, success, error, dataType, method, data) {

	console.log("Inside call : data " + data)
	$.ajax({
		url : url,
		data : data,
		success : success,
		error : error,
		type : method,
		dataType : "json",
		timeout:10000,
		contentType : "application/x-www-form-urlencoded; charset=UTF-8"
	});
}

integration = {};
integration.services = {};
integration.currentService;
integration.user = {};
integration.user.name;
integration.user.email;
integration.user.userId;

HTTP = {};
HTTP.POST = "post";

OPERATION = {};
OPERATION.CREATE = "create";
OPERATION.GET_ALL_CLIENTS_OF_USER = "getAllClientsOfUser";
OPERATION.GET_ALL_APPLICATIONS_OF_USER = "getAllApplicationsOfUser";
OPERATION.GET_ALL_SERVICES = "getAllService";
OPERATION.GET_ALL_SERVICES_OF_USER = "getAllServiceOfUser";
OPERATION.GET_ALL_WORKFLOWS_OF_USER = "getAllWorkflowsOfUser";

KEYSTORE = {};
KEYSTORE.CLIENT = "http://stag-solutions.teamchat.com/TeamchatIntegrationServer/client";
KEYSTORE.APPLICATION = "http://stag-solutions.teamchat.com/TeamchatIntegrationServer/application";
KEYSTORE.SERVICE = "http://stag-solutions.teamchat.com/TeamchatIntegrationServer/service";
KEYSTORE.WORKFLOW = "http://stag-solutions.teamchat.com/TeamchatIntegrationServer/workflow";
KEYSTORE.JSON = "http://stag-solutions.teamchat.com/TeamchatIntegrationServer/json";

KEYSTORE.DISP_NAME = "Name";
KEYSTORE.DISP_CLIENT_KEY = "Client Key";
KEYSTORE.DISP_ORG = "Organization";
KEYSTORE.DISP_ORG_EMAIL = "Org Email";
KEYSTORE.DISP_DESC = "Description";
KEYSTORE.DISP_DATE = "Date";

KEYSTORE.DISP_APP_NAME = "App Name";
KEYSTORE.DISP_API_KEY = "API Key";
KEYSTORE.DISP_SECRET_KEY = "Secret Key";
KEYSTORE.DISP_EVENT_TYPE = "Event Type";
KEYSTORE.DISP_CB_URL = "Callback URL";

KEYSTORE.DISP_SERVICE = "Service";
KEYSTORE.DISP_SERVICE_KEY = "Service Key";
KEYSTORE.DISP_BOT_EMAIL = "Bot Email";
KEYSTORE.DISP_URL = "URL";
KEYSTORE.DISP_FAIL_MESSAGE = "Failure Message";
KEYSTORE.DISP_WF_KEY = "Workflow Key";

KEYSTORE.CLIENT_NAME = "cName";
KEYSTORE.CLIENT_KEY = "clientKey";
KEYSTORE.CLIENT_ORG = "cOrg";
KEYSTORE.CLIENT_ORG_EMAIL = "cOrgEmail";
KEYSTORE.CLIENT_DESC = "cDescription";
KEYSTORE.CREATED_DATE = "createdDate";

KEYSTORE.APP_NAME = "appName";
KEYSTORE.API_KEY = "apiKey";
KEYSTORE.SECRET_KEY = "secretKey";
KEYSTORE.APP_DESC = "appDesc";
KEYSTORE.APP_EVT_TYPE = "appEvtType";
KEYSTORE.APP_CB_URL = "appCbUrl";

KEYSTORE.TYPE_ID = "typeId";
KEYSTORE.SER_NAME = "serName";
KEYSTORE.SER_DESC = "serDesc";
KEYSTORE.SER_ID = "serviceId";

KEYSTORE.WF_NAME = "wfName";
KEYSTORE.BOT_EMAIL = "botEmail";
KEYSTORE.WF_CB_URL = "wfCbUrl";
KEYSTORE.FAIL_MESSAGE = "failMessage";
KEYSTORE.WF_KEY = "workflowKey";

KEYSTORE.STRING = "string";
