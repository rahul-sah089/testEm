app.controller("crudController", function($scope, $http, crudService) {
	debugger;

	$scope.UserFormContainer = false;
	$scope.itemShowCount = [ '5', '10', '20', '30' ];
	$scope.typeList = [ 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 ];
	$scope.date = new Date();
	

	GetAllUsers();

	// To Get all users list
	function GetAllUsers() {
		$http({
			method : "GET",
			url : "http://localhost:8080/read-all"
		}).then(function successCallBack(response) {
			console.log("*************");
			console.log(response);
			$scope.users = response.data;
			console.log("*******User*******");
			console.log($scope.users);
			console.log("*************");
		}, function errorCallback(response) {
			console.log(response.statusText);
		});
	}

	$scope.editUser = function(user) {
		console.log("edit user called");
		console.log(user);
		//alert("suer id==>" + user.id);
		$http({
			method : "GET",
			url : "http://localhost:8080/read",
			params : {
				employeeId : user.id
			}
		}).then(function successCallBack(response) {
			console.log("**********response******");
			console.log(response);
			console.log("*************************");
			$scope.user = response.data;
			$scope.UserId = response.data.id;
			$scope.UserFirstname = response.data.employeeFirstName;
			$scope.UserLastname = response.data.employeeLastName;
			$scope.UserType = response.data.employeeDepartment;
			$scope.Action = "Update";
			$scope.UserFormContainer = true;
		}, function errorCallback(response) {
			console.log(response.statusText);
		});

	}

	// Hide Add User Form
	$scope.addUser = function() {
		ClearFields();
		$scope.Action = "Add";
		$scope.UserFormContainer = true;
	}

	function ClearFields() {
		$scope.UserId = "";
		$scope.UserFirstname = "";
		$scope.UserLastname = "";
		$scope.UserType = "";
		$scope.UserActive = "";
	}

	// Hide Add / Update User Form
	$scope.closeFrmBtn = function() {
		$scope.UserFormContainer = false;
	}

	$scope.Cancel = function() {
		$scope.UserFormContainer = false;
	}

	// Add Update Action
	$scope.AddUpdateUser = function() {
		var user = {
			employeeFirstName : $scope.UserFirstname,
			employeeLastName : $scope.UserLastname,
			employeeDepartment : $scope.UserType
		};

		var getUserAction = $scope.Action;
		//alert("get user action==>" + getUserAction);

		if (getUserAction == "Update") {
			user.id = $scope.UserId;
			//alert("user id==>" + user.id);
			$http({
				method : "GET",
				url : "http://localhost:8080/update",
				params : user
			}).then(function successCallBack(response) {
				console.log("*************");
				console.log(response);
				$scope.users = response.data;
				alert("edited");
				GetAllUsers();
				$scope.message = "Employee Data Edited";
			}, function errorCallback(response) {
				console.log(response.statusText);
				alert("problem while editing");
			});
		} else {
			// Add Use Code Come Here
			$http({
				method : "GET",
				url : "http://localhost:8080/create",
				params : user
			}).then(function successCallBack(response) {
				console.log("*************");
				console.log(response);
				$scope.users = response.data;
				console.log("*******User*******");
				console.log($scope.users);
				console.log("*************");
				GetAllUsers();
				$scope.message = "Employee Added";
			}, function errorCallback(response) {
				console.log(response.statusText);
			});

		}
		$scope.UserFormContainer = false;

	} // end of AddUpdateUser.

	$scope.deleteUser = function(user) {
		// console.log(user.id);
		var ans = confirm('Are you sure to delete it?');
		if (ans) {
			//alert("Ans" + ans);
			alert(user.id);
			$http({
				method : "GET",
				url : "http://localhost:8080/delete",
				params : {
					employeeId : user.id
				}
			}).then(function successCallBack(response) {
				console.log("**********response******");
				console.log(response);
				console.log("*************************");
				if (response.data) {
					$scope.message = "Employee Deleted";
				} else {
					$scope.message = "Problem is deleting Employee";
				}
				GetAllUsers();
			}, function errorCallback(response) {
				console.log(response.statusText);
			});

		}

	}

	$scope.sort = function(keyname) {
		$scope.sortKey = keyname; // set the sortKey to the param passed
		$scope.reverse = !$scope.reverse; // if true make it false and vice
		// versa
	}

	$scope.activeChange = function() {
		$scope.search.active = (($scope.uActive) ? "1" : "0");
	};

	$scope.reset = function() {
		$scope.search = '';
	};

});