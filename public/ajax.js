/**
 * Makes Synchronous AJAX call and return result as JSON On Exception returns
 * back nothing(undefined)
 * 
 * @param reqURL
 * @param data
 * @returns
 */
function ajaxSync(reqURL, data, isPost) {
	var result;
	var xmlhttp = window.XMLHttpRequest ? new XMLHttpRequest()
			: xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
	
	if(isPost) {
		xmlhttp.open("POST", reqURL, false);
		// Send the proper header information along with the request
		xmlhttp.setRequestHeader("Content-type", "application/json");
	} else {
		xmlhttp.open("GET", reqURL, false);
	}
	
	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4) {
			if (xmlhttp.status == 200) {
				result = JSON.parse(xmlhttp.responseText);
			} else {
				result = undefined;
			}
		}
	};
	xmlhttp.send(data);
	return result;
}

/**
 * Makes Asynchronous AJAX call (GET).
 * 
 * @param reqURL
 * @param callback
 *            method
 * @param errorCallback
 *            method
 */
function ajaxASync(reqURL, callback, errorCallback) {
	var xmlhttp = window.XMLHttpRequest ? new XMLHttpRequest()
			: xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");

	xmlhttp.open("GET", reqURL, true);

	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4) {
			if (xmlhttp.status == 200) {
				if (callback && (typeof callback == "function")) {
					var response = JSON.parse(xmlhttp.responseText);
					if(response.hasError) {
						errorCallback(response.errorCode);
						return;
					}
					callback(response.resultInfo);
				} else {
					console.log('Invalid callback function');
				}
			} else {
				errorCallback('status.general.ajax.call.failure');
				ajaxCallFail(reqURL, xmlhttp.status);
			}
		}
	};

	xmlhttp.send(null);
}


/**
 * Makes Asynchronous AJAX call (POST).
 * 
 * @param reqURL
 * @param callback
 *            method
 * @param errorCallback
 *            method
 */
function ajaxASyncPost(reqURL, data, callback, errorCallback) {
	var xmlhttp = window.XMLHttpRequest ? new XMLHttpRequest()
			: xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");

	xmlhttp.open("POST", reqURL, true);

	// Send the proper header information along with the request
	xmlhttp.setRequestHeader("Content-type", "application/json");
	// xmlhttp.setRequestHeader("Connection", "close");

	xmlhttp.onreadystatechange = function() {
		if (xmlhttp.readyState == 4) {
			if (xmlhttp.status == 200) {
				if (callback && (typeof callback == "function")) {
					var response = JSON.parse(xmlhttp.responseText);
					if(response.hasError) {
						errorCallback(response.errorCode);
						return;
					}
					callback(response.resultInfo);
				} else {
					console.log('Invalid callback function');
				}
			} else {
				errorCallback('status.general.ajax.call.failure');
				ajaxCallFail(reqURL, xmlhttp.status);
			}
		}
	};

	xmlhttp.send(data);
}


function ajaxCallFail(url, status) {
	console.log('Ajax Call Failed: '+ url + ", status: " + status);
}