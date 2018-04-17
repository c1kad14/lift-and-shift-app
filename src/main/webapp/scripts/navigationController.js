angular.module('app').controller("navigationController", function ($scope, $location, $http, $timeout) {

	$scope.status = {
		message: "NOT PROCESSING",
		class: "status-np"
	};

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
	};

	$scope.isActive = function (location) {
		return location === $location.path();
	};

	$scope.init = function () {
		openStatusWebsocket();
	};

	function openStatusWebsocket() {
		var socket = new WebSocket("ws://localhost:8080/status");

		socket.onmessage = function (event) {
			if (event.data === "true") {
				$scope.status = {
					message: "PROCESSING",
					class: "status-p"
				};
			} else {
				$scope.status = {
					message: "NOT PROCESSING",
					class: "status-np"
				};
			}
		};

		socket.onclose = function (event) {
			openStatusWebsocket();
		};

		socket.onerror = function (error) {
			socket.close();
		};
	}
});
