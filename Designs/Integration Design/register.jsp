<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Teamchat Integration Console</title>

<script src="https://ajax.googleapis.com/ajax/libs/jquery/1.11.2/jquery.min.js"></script>
<script src="scripts/Constants.js"></script>
<link href="css/bootstrap.css" rel="stylesheet" />
<link href='http://fonts.googleapis.com/css?family=Roboto' rel='stylesheet' type='text/css'>
<link rel="shortcut icon" href="https://enterprise.teamchat.com/webjim-echat/static/img/favicon.png" type="image/x-icon">
<link href="css/style.css" rel="stylesheet" />
<link href="css/bootstrap.css.map" rel="stylesheet" />
<script src="scripts/bootstrap.js"></script>
<script src="scripts/mustache.js"></script>
<script src="scripts/admin.js"></script>
<script src="scripts/util.js"></script>
<script src="scripts/templates.js"></script>
<script src="scripts/mrJsonTableConfig.js"></script>
<script src="scripts/registerFunctions.js"></script>
<link rel="stylesheet" type='text/css' href="css/mrjsontable.css" />
<script type="text/javascript" src="scripts/mrjsontable.js"></script>
<script>
	$(document).ready(

			function() {
				var url = location.search;
				if (url.indexOf('errMsg=') > 0) {
					var errMsg = url.substring(url.indexOf('errMsg='),
							url.length).split('=')[1];
					console.log(errMsg);
					errMsg = unescape(errMsg);
					$('#errMsg').text(errMsg).show();
				}
			});

	function showPopUp(id) {
		$('#main-body').hide();
		$(id).css("display", "block");

	}

	function hidePopup(id) {
		$(id).css("display", "none");
		$('#main-body').show();

	}

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

		disableCallback(true);

		$("#client-form").hide()
		$("#app-form").hide()
		$("#workflow-form").hide()
	}


	var disableCallback = function(isTrue) {
		if (isTrue) {
			$("#reg-app-callbackurl").prop('disabled', true);
			document.getElementById('reg-app-eventtype').value = "pull";
		} else {
			$("#reg-app-callbackurl").prop('disabled', false);
			document.getElementById('reg-app-eventtype').value = "push";
		}
	}
</script>

</head>
<body onload="renderRegisteredUtils();">

	<div id="main-body">

		<c:choose>
			<c:when test="${not empty sessionScope.email}">


				<div class="toolbar">
					<div class="toolbar-title">
						<div class="toolbar-title-heading">Teamchat Integration Console</div>
					</div>
					<div class="toolbar-profile">
						<div>
							<c:out value="${sessionScope.profileName}"></c:out>
							<div class="logout" id="logout">
								<a class="logout" href="login?operation=logout">Logout</a>
							</div>
						</div>
					</div>
				</div>
				<div class="panel-group" id="accordion" role="tablist" aria-multiselectable="true">

					<!-- Service Registration Block Starts -->

					<div class="panel panel-default">
						<div class="panel-heading" role="tab" id="headingThree">
							<h4 class="panel-title title-click">
								<!-- <a class="collapse_1d" data-toggle="collapse_1"
									data-parent="#accordion" href="#collapse_1Three"
									aria-expanded="false" aria-controls="collapse_1Three">
									Self server Integration Services </a> -->
								Self Serve Integration Services
							</h4>
							<div class="heading">This section id used to create a client (customer Id) on Teamchat Integration platfrom , later you can use this client id to create / consume / register application
								,services , workflows . This will also help you maintain and manage the application ,services , workflows you create</div>
						</div>
						<div id="collapse_1Three" class="panel-collapse_1 collapse_1" role="tabpanel" aria-labelledby="headingThree">
							<div class="panel-body">

								<!-- Service Registration Block Starts -->
								<div id="availableServices" class="availableServices" align="left">
									<!-- Blocks of Services Available -->
								</div>

							</div>
						</div>
					</div>

					<!-- Service Registration Block Ends -->


					<!-- Client Registration Block Starts -->

					<div class="panel panel-default">
						<div class="panel-heading" role="tab" id="headingOne">
							<h4 class="panel-title title-click">
								<!-- <a data-toggle="collapse_1" data-parent="#accordion"
									href="#collapse_1One" aria-expanded="true"
									aria-controls="collapse_1One"> Register Client </a> -->
								Register Client
							</h4>
							<div class="heading">This section id used to create a client (customer Id) on Teamchat Integration platfrom , later you can use this client id to create / consume / register application
								,services , workflows . This will also help you maintain and manage the application ,services , workflows you create</div>
						</div>
						<div id="collapse_1One" class="panel-collapse_1 collapse_1 in" role="tabpanel" aria-labelledby="headingOne">
							<div class="panel-body">


								<div class="panel_1 panel-default">
									<div class="panel-heading" role="tab" id="headingFour">
										<div class="panel-title title-click">
											<!-- <a class="collapse_1d" data-toggle="collapse_1"
												data-parent="#accordion" href="#collapse_1Four"
												aria-expanded="false" aria-controls="collapse_1Four"> </a> -->
											<div align="left" id="regClientsShow">
												Registered Clients <img align="right" onclick='toggleForm("client-form","regClientsHide","regClientsShow","registeredClientsTable")' style='width: 25px; height: 25px;' src='images/add.png' />
											</div>
											<div align="left" style="display: none;" id="regClientsHide">
												Register Client <img align="right" onclick='toggleForm("regClientsShow","registeredClientsTable","client-form","regClientsHide")' style='width: 25px; height: 25px;' src='images/minus.png' />
											</div>
										</div>
									</div>
									<div id="collapse_1Four" class="panel-collapse_1 collapse_1" role="tabpanel" aria-labelledby="headingFour">
										<div class="panel-body_1">


											<table id="registeredClientsTable" border="1">

											</table>



										</div>
									</div>
								</div>



								<form onsubmit="return false" id="client-form">
									<div class="input-row">
										Name:<br /> <input type="text" name="reg-client-name" id="reg-client-name" class="reg-client-name" placeholder="Name" />
									</div>
									<div class="input-row">
										<br />Organization:<br /> <input type="text" name="reg-client-org" id="reg-client-org" class="reg-client-org" placeholder="Organization" />
									</div>
									<div class="input-row">
										<br />Organization Email:<br /> <input type="email" name="reg-client-email" id="reg-client-email" class="reg-client-email" placeholder="Organization Email" data-email="true" />
									</div>
									<div class="input-row">
										<br />Description:<br /> <input type="text" name="reg-client-description" id="reg-client-description" class="reg-client-description" placeholder="Description" />
									</div>
									<div class="button-row">
										<br /> <br />
										<button class="button disable-on-click save reg-client-save" id="reg-client-save" value="Save" onclick="registerClient()" style="">Save</button>
									</div>
								</form>





							</div>
						</div>
					</div>

					<!-- Client Registration Block Ends -->

					<!-- App Registration Block Starts -->
<!-- 
					<div class="panel panel-default">
						<div class="panel-heading" role="tab" id="headingTwo">
							<div class="panel-title title-click">
								<a class="collapse_1d" data-toggle="collapse_1"
									data-parent="#accordion" href="#collapse_1Two"
									aria-expanded="false" aria-controls="collapse_1Two">
									Register Application </a>
								Register Application


							</div>
							<div class="heading">This section id used to create a client (customer Id) on Teamchat Integration platfrom , later you can use this client id to create / consume / register application
								,services , workflows . This will also help you maintain and manage the application ,services , workflows you create</div>
						</div>
						<div id="collapse_1Two" class="panel-collapse_1 collapse_1" role="tabpanel" aria-labelledby="headingTwo">
							<div class="panel-body">


								<div class="panel_1 panel-default">
									<div class="panel-heading" role="tab" id="headingFive">
										<div class="panel-title title-click">
											<a class="collapse_1d" data-toggle="collapse_1"
												data-parent="#accordion" href="#collapse_1Five"
												aria-expanded="false" aria-controls="collapse_1Five"> </a>
											<div align="left" id="regAppsShow">
												Registered Appplications <img align="right" onclick='addApplication()' style='width: 25px; height: 25px;' src='images/add.png' />
											</div>
											<div align="left" style="display: none;" id="regAppsHide">
												Register Appplication <img align="right" onclick='toggleForm("regAppsShow","registeredApplicationsTable","app-form","regAppsHide")' style='width: 25px; height: 25px;'
													src='images/minus.png' />
											</div>
										</div>
									</div>
									<div id="collapse_1Five" class="panel-collapse_1 collapse_1" role="tabpanel" aria-labelledby="headingFive">
										<div class="panel-body">


											<table id="registeredApplicationsTable" border="1">

											</table>



										</div>
									</div>
								</div>


								<form onsubmit="return false" id="app-form">
									<div class="input-row">
										Client Key:<br /> <select id="reg-app-cKey" name="reg-app-cKey" id="reg-app-cKey" class="reg-app-cKey"></select>
									</div>
									<div class="input-row">
										<br />Name:<br /> <input type="text" name="reg-app-name" id="reg-app-name" class="reg-app-name" placeholder="Type your name here" />
									</div>
									<div class="input-row">
										<br />Description:<br /> <input type="text" name="reg-app-description" id="reg-app-description" class="reg-app-description" placeholder="Description" />
									</div>
									<div class="input-row">
										<br />Event Type:<br /> <input type="radio" name="reg-app-eventtype" id="reg-app-eventtype" class="reg-app-eventtype" value="push" onclick="disableCallback(false)">&nbsp;Push&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
										<input type="radio" name="reg-app-eventtype" id="reg-app-eventtype" class="reg-app-eventtype" value="pull" onclick="disableCallback(true)">&nbsp;Pull
									</div>
									<div class="input-row">
										<br />Callback URL:<br /> <input type="text" name="reg-app-callbackurl" id="reg-app-callbackurl" class="reg-app-callbackurl" placeholder="Call Back URL" />
									</div>
									<div class="button-row">
										<br /> <br />
										<button class="button disable-on-click save reg-app-save" id="reg-app-save" value="reg-app-save" onclick="registerApplication()" style="">Save</button>
									</div>
								</form>


							</div>
						</div>
					</div>
 -->
					<!-- App Registration Block Ends -->

					<!-- Workflow Registration Block Starts -->
<!-- 
					<div class="panel panel-default">
						<div class="panel-heading" role="tab" id="headingThreeOne">
							<h4 class="panel-title title-click">
								<a class="collapse_1d" data-toggle="collapse_1"
									data-parent="#accordion" href="#collapse_1ThreeOne"
									aria-expanded="false" aria-controls="collapse_1ThreeOne">
									Register Workflow </a>
								Register Workflow
							</h4>
							<div class="heading">This section id used to create a client (customer Id) on Teamchat Integration platfrom , later you can use this client id to create / consume / register application
								,services , workflows . This will also help you maintain and manage the application ,services , workflows you create</div>
						</div>
						<div id="collapse_1ThreeOne" class="panel-collapse_1 collapse_1" role="tabpanel" aria-labelledby="headingThreeOne">
							<div class="panel-body">


								<div class="panel_1 panel-default">
									<div class="panel-heading" role="tab" id="headingSeven">
										<div class="panel-title title-click">
											<a class="collapse_1d" data-toggle="collapse_1"
												data-parent="#accordion" href="#collapse_1Seven"
												aria-expanded="false" aria-controls="collapse_1Seven"> </a>
											<div align="left" id="regWorkflowsShow">
												Registered Workflows <img align="right" onclick='addWorkflow()' style='width: 25px; height: 25px;' src='images/add.png' />
											</div>
											<div align="left" style="display: none;" id="regWorkflowsHide">
												Register Workflow <img align="right" onclick='toggleForm("regWorkflowsShow","registeredWorkflowsTable","workflow-form","regWorkflowsHide")' style='width: 25px; height: 25px;'
													src='images/minus.png' />
											</div>
										</div>
									</div>
									<div id="collapse_1Seven" class="panel-collapse_1 collapse_1" role="tabpanel" aria-labelledby="headingSeven">
										<div class="panel-body">


											<table id="registeredWorkflowsTable" border="1">

											</table>



										</div>
									</div>
								</div>

								<form onsubmit="return false" id="workflow-form">
									<div class="input-row">
										Name:<br /> <input type="text" name="reg-workflow-name" id="reg-workflow-name" class="reg-workflow-name" placeholder="Name" />
									</div>
									<div class="input-row">
										<br />Client Key:<br /> <select id="reg-workflow-cKey" name="reg-workflow-cKey" id="reg-workflow-cKey" class="reg-workflow-cKey"></select>
									</div>
									<div class="input-row">
										<br />Bot Details:<br /> Email:&nbsp;<input type="text" name="reg-workflow-bot-email" id="reg-workflow-bot-email" class="reg-workflow-bot-email" placeholder="Email" data-email="true" />
										&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Password:&nbsp; <input type="password" name="reg-workflow-bot-password" id="reg-workflow-bot-password" class="reg-workflow-bot-password" placeholder="Password" />
									</div>
									<div class="input-row">
										<br />Callback URL:<br /> <input type="text" name="reg-workflow-callback-url" id="reg-workflow-callback-url" class="reg-workflow-callback-url" placeholder="Callback URL" />
									</div>
									<div class="input-row">
										<br />Failure Message:<br /> <input type="text" name="reg-workflow-failmessage" id="reg-workflow-failmessage" class="reg-workflow-failmessage" placeholder="Failure Message" />
									</div>

									<div class="button-row">
										<br />
										<button class="button disable-on-click save reg-workflow-save" id="reg-workflow-save" value="reg-workflow-save" onclick="registerWorkflow()" style="">Save</button>
									</div>
								</form>



							</div>
						</div> -->

						<!-- WF Registration Block Ends -->
					</div>
				</div>

			</c:when>
			<c:otherwise>
				<c:redirect url="/login.html"></c:redirect>
			</c:otherwise>
		</c:choose>

	</div>

	<!-- Service Registration POPUP Block Starts -->

	<div class="service_popup" id="service_popup" align="center">
		<div class="service_popup_container">
			<div class="service_popup_container_head" align="left">

				<!-- here the template will come and sit  -->

			</div>

			<div align="left" style="display: none; font-size: 20px;" id="regServicesHide">
				Register Service <img align="right" onclick='toggleForm("regServicesShow","registeredServicesTable","service-form","regServicesHide")' style='width: 25px; height: 25px;' src='images/minus.png' />
			</div>


			<div id="regServicesShow" style="font-size: 20px;">
				Registered Services <img align="right" onclick='addService()' style='width: 25px; height: 25px;' src='images/add.png' />
			</div>
			<table id="registeredServicesTable" border="1">
			</table>

			<div id="service-form-details" align="left">
				<form onsubmit="return false" id="service-form">
					<div class="input-row">
						Client Key:<br /> <select id="reg-service-cKey" name="reg-service-cKey" id="reg-service-cKey" class="reg-service-cKey"></select>
					</div>
					<div class="input-row">
						<br />Name:<br /> <input type="text" name="reg-service-name" id="reg-service-name" class="reg-service-name" placeholder="Name" />
					</div>
					<div class="input-row">
						<br />Description:<br /> <input type="text" name="reg-service-desc" id="reg-service-desc" class="reg-service-desc" placeholder="Description" />
					</div>
					<div class="button-row">
						<br /> <br />
						<button class="button disable-on-click save reg-service-save" id="reg-service-save" value="reg-service-save" onclick="registerService()" style="">Save</button>
					</div>
				</form>
			</div>

			<div>
				<br />
				<button class="button disable-on-click save reg-service-save" id="reg-service-save" value="Save" onclick="hidePopup('#service_popup')" style="">Close</button>
			</div>

		</div>

	</div>

	<!-- Service Registration POPUP Block Ends -->

</body>
</html>