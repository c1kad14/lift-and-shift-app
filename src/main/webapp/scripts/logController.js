angular.module('app').controller("logController", function ($scope, $http) {

	$scope.followToggle = function () {
		console.log("111111");
	};

	$scope.clearLog = function () {
		console.log("111111");
		$http({
			method: "POST",
			url: "shutdown"
		}).then(
			function successCallback(response) {
			}, function errorCallback(response) {
			}
		);
	}
	//
	// var contextPath = window.location.pathname.substring(0, window.location.pathname.indexOf("/", 2));
	// $scope.info = {};
	// $scope.log = '';
	// $scope.logLocation = '';
	// $scope.init = function () {
	//
	// 	$http({
	// 		method: 'GET',
	// 		url: 'properties?json=true'
	// 	}).then(function successCallback(data) {
	// 		$scope.info = data.data;
	// 	}, function errorCallback(data) {
	// 		$scope.info = 'User not found';
	// 		console.log($scope.info);
	// 	});
	//
	// 	$http({
	// 		method: 'GET',
	// 		url: contextPath + '/logFile'
	// 	}).then(function successCallback(data) {
	// 		$scope.log = data.data.logFileContent;
	// 		$scope.logLocation = data.data.logFileLocation;
	// 	}, function errorCallback(data) {
	// 		$scope.log = 'Failed to load log file';
	// 		$scope.logLocation = 'Failed to find location of log file';
	// 	});
	// }
});