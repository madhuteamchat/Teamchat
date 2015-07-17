/**
 * 
 */
$(function() {

	$("#registrationGrid").jsGrid({
		width : "100%",
		filtering : true,
		editing : false,
		sorting : true,
		paging : true,
		inserting : false,
		autoload : true,
		pageSize : 100,
		fields : [ {
			name : "type",
			type : "text",
			title : "Service",
			width : 25,
			css : "myrow",
			headercss : "myrowheader"
		}, {
			name : "typeId",
			type : "text",
			title : "Service Id",
			width : 25,
			css : "myrow",
			headercss : "myrowheader"
		}, {
			name : "adminName",
			title : "Admin Name",
			type : "text",
			width : 50,
			css : "myrow",
			headercss : "myrowheader"
		}, {
			name : "apiKey",
			type : "text",
			title : "API Key",
			width : 50,
			css : "myrow",
			headercss : "myrowheader"
		}, {
			name : "clientKey",
			type : "text",
			title : "Client Key",
			width : 50,
			css : "myrow",
			headercss : "myrowheader"
		}, {
			name : "isEnabled",
			type : "checkbox",
			title : "Is Enabled",
			width : 1,
			css : "myrow",
			headercss : "myrowheader"
		} ],

		controller : {
			loadData : function(filter) {
				
				filter["operation"] = "loadRegistrationData";
				
				return $.ajax({
					type : "POST",
					url : "integrationdashboard",
					dataType : "json",
					data : filter
				});
			},

			insertItem : function(item) {
				console.log("Insert not supported");

			},

			updateItem : function(item) {
				console.log("Update not supported");
			},

			deleteItem : function(item) {
				console.log("Delete not supported");
			}

		}

	});

	$("#metadataGrid").jsGrid({
		width : "100%",
		filtering : true,
		editing : false,
		sorting : true,
		paging : true,
		inserting : false,
		autoload : true,
		pageSize : 100,
		fields : [ {
			name : "type",
			type : "text",
			title : "Service",
			width : 25,
			css : "myrow",
			headercss : "myrowheader"
		}, {
			name : "typeId",
			type : "text",
			title : "Service Id",
			width : 25,
			css : "myrow",
			headercss : "myrowheader"
		}, {
			name : "botEmail",
			title : "Bot Email",
			type : "text",
			width : 50,
			css : "myrow",
			headercss : "myrowheader"
		}, {
			name : "desc",
			type : "text",
			title : "Description",
			width : 100,
			css : "myrow",
			headercss : "myrowheader"
		} ],

		controller : {
			loadData : function(filter) {
				filter["operation"] = "loadTypeMetaData";

				return $.ajax({
					type : "POST",
					url : "integrationdashboard",
					dataType : "json",
					data : filter
				});
			},

			insertItem : function(item) {
				console.log("Insert not supported");

			},

			updateItem : function(item) {
				console.log("Update not supported");
			},

			deleteItem : function(item) {
				console.log("Delete not supported");
			}

		}

	});
});