

var serviceListTemplate = "<div id='{{typeId}}' style='float:left;'>" + '<center><div style="padding:30px;">' + '<div>' + '<div style="height:80px;width:70px;">'
		+ '<img style="height:80px;width:90px;border: 5px solid rgba(7, 161, 216, 0.29);border-radius: 71px;" src="{{imgUrl}}" />' + '</div>' + '<div style="padding-top:10px;text-align:center;color:#159ceb">' + '<div>{{typeDisp}}</div>' + '</div>' + '</div>'
		+ '</center>' + '</div>';



var serviceHeder = "<div id='{{typeId}}' style=''>" + '<center><div style="padding:10px;">' + '<div>' + '<div style="height:80px;width:70px;">'
		+ '<img style="height:80px;width:70px;" src="{{imgUrl}}" />' + '</div>' + '<div style="padding-top:10px;text-align:center;color:#159ceb">' + '<div>{{typeDisp}}</div>' + '</div>' + '</div>'
		+ '</center>' + '</div>';


var serviceHead = serviceHeder + '<div>{{{desc}}}</div>';



template = {};
template.serviceHead = serviceHead;
template.serviceListTemplate = serviceListTemplate;