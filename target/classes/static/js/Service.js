app.service("crudService", function ($http) {
	var serviceUrl = "http://localhost:8080/";
	//get All Users
    this.getUsers = function () {
    	alert("get Users called");
    	serviceUrl = serviceUrl + "read-all";
    	alert("serviceUrl=>"+serviceUrl);
        var result =  $http.get(serviceUrl);
        console.log(result);
        return result;
    };

	this.getUser = function(userId) {
		var response = $http({
			method	: "POST",
			url		: serviceUrl + "getUser.php",
			params 	: {
					id : userId
			}
		});
		return response;
	};

	this.updateUser = function (user) {
		var response = $http({
			method : "POST",
			url : serviceUrl + "updateUser.php",
			params : user
		});

		return response;
	};

	this.addUser = function (user) {
		var response = $http({
			method  : "POST",
			url		: serviceUrl + "createUser.php",
			params : user
		});
		return response;
	};

	this.deleteUser = function (id) {
		var response = $http({
			method  : "POST",
			url		: serviceUrl + "deleteUser.php",
			params : {userId : id}
		});
		return response;
	};

});