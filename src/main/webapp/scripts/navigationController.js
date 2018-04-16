angular.module('app').controller("navigationController", function ($scope, $location, $http) {

	$scope.shutdown = function () {
		console.log("test");
		$http({
			method: "POST",
			url: "shutdown"
		}).then(
			function successCallback(response) {
			}, function errorCallback(response) {
			}
		);
	};

	$scope.changeRoute = function (event) {
		if (event.currentTarget.id === "propertiesButton") {
			$location.path("/properties");
		}
		if (event.currentTarget.id === "logsButton") {
			$location.path("/logs");
		}
		if (event.currentTarget.id === "propertiesManagementButton") {
			$location.path("/propertiesManagement");
		}
	};

	$scope.isActive = function (location) {
		return location === $location.path();
	};

});
