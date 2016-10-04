var RestEndpoint = "http://localhost:8080/rbacman/ws";
var timeout = 30;

var getUsers = function(success, error){
	var url = RestEndpoint + "/users";
	$.ajax({
		  timeout: timeout,
		  type: 'GET',
		  url: url,
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		 
		});
}

var getUser = function(id, success, error){
	var url = RestEndpoint + "/users/"+id;
	$.ajax({
		 timeout: timeout,
		  type: 'GET',
		  url: url,
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var deleteUser = function(id, success, error){
	var url = RestEndpoint + "/users/"+id;
	$.ajax({
		 timeout: timeout,
		  type: 'DELETE',
		  url: url,
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var createUser = function(data, success, error){
	var url = RestEndpoint + "/users";
	var headers = [];
	headers["Content-Type"] = "application/json";	
	$.ajax({
		 timeout: timeout,
		 headers: headers,
		  type: 'POST',
		  url: url,
		  data: JSON.stringify(data),
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}


var editUser = function(id, data, success, error){
	var url = RestEndpoint + "/users/"+id;
	var headers = [];
	headers["Content-Type"] = "application/json";	
	$.ajax({
		 timeout: timeout,
		 headers: headers,
		  type: 'PUT',
		  url: url,
		  data: JSON.stringify(data),
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var createAction = function(data, success, error){
	var url = RestEndpoint + "/actions";
	var headers = [];
	headers["Content-Type"] = "application/json";	
	$.ajax({
		 timeout: timeout,
		 headers: headers,
		  type: 'POST',
		  url: url,
		  data: JSON.stringify(data),
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}


var editAction = function(id, data, success, error){
	var url = RestEndpoint + "/actions/"+id;
	var headers = [];
	headers["Content-Type"] = "application/json";	
	$.ajax({
		 timeout: timeout,
		 headers: headers,
		  type: 'PUT',
		  url: url,
		  data: JSON.stringify(data),
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var getActions = function(success, error){
	var url = RestEndpoint + "/actions";
	$.ajax({
		 timeout: timeout,
		  type: 'GET',
		  url: url,
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var getAction = function(id, success, error){
	var url = RestEndpoint + "/actions/"+id;
	$.ajax({
		 timeout: timeout,
		  type: 'GET',
		  url: url,
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var deleteAction = function(id, success, error){
	var url = RestEndpoint + "/actions/"+id;
	$.ajax({
		 timeout: timeout,
		  type: 'DELETE',
		  url: url,
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var createResource = function(data, success, error){
	var url = RestEndpoint + "/resources";
	var headers = [];
	headers["Content-Type"] = "application/json";	
	$.ajax({
		 timeout: timeout,
		 headers: headers,
		  type: 'POST',
		  url: url,
		  data: JSON.stringify(data),
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}


var editResource = function(id, data, success, error){
	var url = RestEndpoint + "/resources/"+id;
	var headers = [];
	headers["Content-Type"] = "application/json";	
	$.ajax({
		 timeout: timeout,
		 headers: headers,
		  type: 'PUT',
		  url: url,
		  data: JSON.stringify(data),
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var getResources = function(success, error){
	var url = RestEndpoint + "/resources";
	$.ajax({
		 timeout: timeout,
		  type: 'GET',
		  url: url,
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var getResource = function(id, success, error){
	var url = RestEndpoint + "/resources/"+id;
	$.ajax({
		 timeout: timeout,
		  type: 'GET',
		  url: url,
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var deleteResource = function(id, success, error){
	var url = RestEndpoint + "/resources/"+id;
	$.ajax({
		 timeout: timeout,
		  type: 'DELETE',
		  url: url,
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var createRole = function(data, success, error){
	var url = RestEndpoint + "/roles";
	var headers = [];
	headers["Content-Type"] = "application/json";	
	$.ajax({
		 timeout: timeout,
		 headers: headers,
		  type: 'POST',
		  url: url,
		  data: JSON.stringify(data),
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}


var editRole = function(id, data, success, error){
	var url = RestEndpoint + "/roles/"+id;
	var headers = [];
	headers["Content-Type"] = "application/json";	
	$.ajax({
		 timeout: timeout,
		 headers: headers,
		  type: 'PUT',
		  url: url,
		  data: JSON.stringify(data),
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var getRoles = function(success, error){
	var url = RestEndpoint + "/roles";
	$.ajax({
		 timeout: timeout,
		  type: 'GET',
		  url: url,
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var getRole = function(id, success, error){
	var url = RestEndpoint + "/roles/"+id;
	$.ajax({
		 timeout: timeout,
		  type: 'GET',
		  url: url,
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var deleteRole = function(id, success, error){
	var url = RestEndpoint + "/roles/"+id;
	$.ajax({
		 timeout: timeout,
		  type: 'DELETE',
		  url: url,
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var createRule = function(data, success, error){
	var url = RestEndpoint + "/rules";
	var headers = [];
	headers["Content-Type"] = "application/json";	
	$.ajax({
		 timeout: timeout,
		 headers: headers,
		  type: 'POST',
		  url: url,
		  data: JSON.stringify(data),
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var getRule = function(id, success, error){
	var url = RestEndpoint + "/rules/"+id;
	$.ajax({
		 timeout: timeout,
		  type: 'GET',
		  url: url,
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var deleteRule = function(id, success, error){
	var url = RestEndpoint + "/rules/"+id;
	$.ajax({
		 timeout: timeout,
		  type: 'DELETE',
		  url: url,
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var editRule = function(id, data, success, error){
	var url = RestEndpoint + "/rules/"+id;
	var headers = [];
	headers["Content-Type"] = "application/json";	
	$.ajax({
		 timeout: timeout,
		 headers: headers,
		  type: 'PUT',
		  url: url,
		  data: JSON.stringify(data),
		  crossDomain: true,
		  dataType: 'json',
		  traditional: true,
		  async:false,	  		  
		  error: error,
		  success: success		
	});
}

var getParameterByName = function(name, url) {
    if (!url) url = window.location.href;
    name = name.replace(/[\[\]]/g, "\\$&");
    var regex = new RegExp("[?&]" + name + "(=([^&#]*)|&|#|$)"),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, " "));
}