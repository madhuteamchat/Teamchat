
	function loadClientDataTable(data , id) {

		$(id).empty();
		$(id).mrjsontable({

			columns : [ {
				heading : KEYSTORE.DISP_NAME,
				data : KEYSTORE.CLIENT_NAME,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_CLIENT_KEY,
				data : KEYSTORE.CLIENT_KEY,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_ORG,
				data : KEYSTORE.CLIENT_ORG,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_ORG_EMAIL,
				data : KEYSTORE.CLIENT_ORG_EMAIL,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_DESC,
				data : KEYSTORE.CLIENT_DESC,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_DATE,
				data : KEYSTORE.CREATED_DATE,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}

			],

			data : data
		});

	}

	function loadApplicationDataTable(data,id) {

		$(id).empty();
		$(id).mrjsontable({

			columns : [ {
				heading : KEYSTORE.DISP_APP_NAME,
				data : KEYSTORE.APP_NAME,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_CLIENT_KEY,
				data : KEYSTORE.CLIENT_KEY,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_API_KEY,
				data : KEYSTORE.API_KEY,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_SECRET_KEY,
				data : KEYSTORE.SECRET_KEY,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_DESC,
				data : KEYSTORE.APP_DESC,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_EVENT_TYPE,
				data : KEYSTORE.APP_EVT_TYPE,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_CB_URL,
				data : KEYSTORE.APP_CB_URL,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_DATE,
				data : KEYSTORE.CREATED_DATE,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}

			],

			data : data
		});

	}

	function loadServiceDataTable(data,id) {

		$(id).empty();
		$(id).mrjsontable({

			columns : [ {
				heading : KEYSTORE.DISP_SERVICE,
				data : KEYSTORE.TYPE_ID,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_NAME,
				data : KEYSTORE.SER_NAME,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_DESC,
				data : KEYSTORE.SER_DESC,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_CLIENT_KEY,
				data : KEYSTORE.CLIENT_KEY,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_API_KEY,
				data : KEYSTORE.API_KEY,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_SECRET_KEY,
				data : KEYSTORE.SECRET_KEY,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_SERVICE_KEY,
				data : KEYSTORE.SER_ID,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_DATE,
				data : KEYSTORE.CREATED_DATE,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}

			],

			data : data
		});

	}

	function loadWorkflowDataTable(data,id) {

		$(id).empty();
		$(id).mrjsontable({
			columns : [ {
				heading : KEYSTORE.DISP_CLIENT_KEY,
				data : KEYSTORE.CLIENT_KEY,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_NAME,
				data : KEYSTORE.WF_NAME,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_BOT_EMAIL,
				data : KEYSTORE.BOT_EMAIL,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_URL,
				data : KEYSTORE.WF_CB_URL,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_FAIL_MESSAGE,
				data : KEYSTORE.FAIL_MESSAGE,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_WF_KEY,
				data : KEYSTORE.WF_KEY,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}, {
				heading : KEYSTORE.DISP_DATE,
				data : KEYSTORE.CREATED_DATE,
				type : KEYSTORE.STRING,
				sortable : false,
				starthidden : false

			}

			],

			data : data
		});

	}
