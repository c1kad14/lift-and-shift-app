angular.module("app").controller("propertiesController", function ($scope, $http, $timeout) {

	$scope.properties = {};

	$scope.message = {};
	$scope.message.hidden = true;
	$scope.message.class = "";
	$scope.message.text = "";

	$scope.init = function () {
		$http({
			method: 'GET',
			url: '/properties'
		}).then(function successCallback(response) {
			if (response.data.liftMethod !== undefined) {
				$scope.properties = response.data;
				setMessage("Properties loaded from app", "info");
			} else {
				$scope.properties = {
					liftMethod: "dir",
					url: "",
					frequency: "60"
				};
				setMessage("Empty app properties, loaded default", "info");
			}
		}, function errorCallback(response) {
			setMessage("Error loading app properties", "error");
		});
	};

	$scope.testDir = function () {
		var errorMessage = validateDir();
		if (errorMessage !== "") {
			setMessage(errorMessage, "error");
			return;
		}
		$http({
			method: "POST",
			url: "test?value=dir&path=" + encodeURI($scope.properties.dir.path)
		}).then(function successCallback(response) {
			setMessage(response.data.message, response.data.status.toLowerCase())
		}, function errorCallback(response) {
			setMessage("Error sending test request", "error");
		});
	};

	$scope.testWs = function () {
		var errorMessage = validateWs();
		if (errorMessage !== "") {
			setMessage(errorMessage, "error");
			return;
		}
		$http({
			method: "POST",
			url: "test?value=ws&url=" + encodeURI($scope.properties.url)
		}).then(function successCallback(response) {
			setMessage(response.data.message, response.data.status.toLowerCase())
		}, function errorCallback(response) {
			setMessage("Error sending test request", "error");
		});
	};

	$scope.testProcessing = function () {
		var validDir = validateDir();
		var validFtp = validateWs();
		if (validDir !== "" || validFtp !== "") {
			setMessage(validDir + " " + validFtp, "error");
			return;
		}
		$http({
			method: "POST",
			url: "test?value=ship"
		}).then(function successCallback(response) {
			setMessage(response.data.message, response.data.status.toLowerCase())
		}, function errorCallback(response) {
			setMessage("Error sending test request", "error");
		});
	};

	$scope.stopProcessing = function () {
		$http({
			method: "DELETE",
			url: "process"
		}).then(function successCallback(response) {
			setMessage("Processing stopped", "ok")
		}, function errorCallback(response) {
			setMessage("Error sending stop processing request", "error");
		});
	};

	$scope.startProcessing = function () {
		var validDir = validateDir();
		var validFtp = validateWs();
		if (validDir !== "" || validFtp !== "") {
			setMessage(validDir + " " + validFtp, "error");
			return;
		}
		$http({
			method: "POST",
			url: "process"
		}).then(function successCallback(response) {
			setMessage("Processing started", "ok")
		}, function errorCallback(response) {
			setMessage(response.data.message, "error");
		});
	};

	$scope.updateProperties = function () {
		var validDir = validateDir();
		var validFtp = validateWs();
		if (validDir !== "" || validFtp !== "") {
			setMessage(validDir + " " + validFtp, "error");
			return;
		}
		$http({
			method: "POST",
			url: "properties",
			data: $scope.properties
		}).then(function successCallback(response) {
			setMessage("Properties updated", "ok")
		}, function errorCallback(response) {
			setMessage(response.data.message, "error");
		});
	};

	$scope.saveProperties = function () {
		var filename = "properties.json";
		var json = angular.toJson($scope.properties);
		var blob = new Blob([json], {type: "text/plain"});
		if (window.navigator && window.navigator.msSaveOrOpenBlob) {
			window.navigator.msSaveOrOpenBlob(blob, filename);
		} else {
			var e = document.createEvent("MouseEvents");
			var a = document.createElement("a");
			a.download = filename;
			a.href = window.URL.createObjectURL(blob);
			a.dataset.downloadurl = ["text/plain", a.download, a.href].join(":");
			e.initEvent("click", true, false, window,
				0, 0, 0, 0, 0, false, false, false, false, 0, null);
			a.dispatchEvent(e);
		}
	};

	function validateDir() {
		if ($scope.properties.dir === undefined
			|| $scope.properties.dir.path === undefined
			|| $scope.properties.dir.path === "") {
			return "Please enter directory path";
		} else {
			return "";
		}
	}

	function validateWs() {
		if ($scope.properties.url === undefined
			|| $scope.properties.url === "") {
			return "Please enter WebSocket URL";
		} else {
			return "";
		}
	}

	function setMessage(message, status) {
		$scope.message.hidden = false;
		$scope.message.class = "message-" + status;
		$scope.message.text = message;
		hideMessage();
	}

	function hideMessage() {
		$timeout(function () {
			$scope.message.hidden = true;
		}, 10000)
	}

	$scope.populateProperties = function($fileContent) {
		console.log($fileContent);
		if ($fileContent.indexOf("liftMethod") < 0) {
			setMessage("Wrong file", "error");
			return;
		}
		$scope.properties = JSON.parse($fileContent);
	};

	$scope.openProperties = function () {
		document.getElementById("open-file").click();
	};

});

angular.module("app").directive("onReadFile", function ($parse) {
	return {
		restrict: "A",
		scope: false,
		link: function(scope, element, attrs) {
			console.log(attrs);
			var fn = $parse(attrs.onReadFile);
			element.on("change", function(onChangeEvent) {
				var reader = new FileReader();
				reader.onload = function(onLoadEvent) {
					scope.$apply(function() {
						fn(scope, {$fileContent:onLoadEvent.target.result});
					});
				};
				reader.readAsText((onChangeEvent.srcElement || onChangeEvent.target).files[0]);
			});
		}
	};
});
