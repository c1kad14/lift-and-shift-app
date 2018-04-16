angular.module("app").controller("propertiesController", function ($scope, $http) {

	$scope.properties = {};
	$scope.properties.liftMethod = "dir";

	$scope.message = {};
	$scope.message.hidden = true;
	$scope.message.class = "";
	$scope.message.text = "";

	$scope.testDir = function () {
		$http({
			method: "POST",
			url: "test?value=dir&path=" + encodeURI($scope.properties.dir.path)
		}).then(function successCallback(response) {
			setMessage(response.data)
		}, function errorCallback(response) {
			$scope.message = {
				hidden: false,
				class: "message-error",
				text: "Error sending test request"
			};
		});
	};

	function setMessage(message) {
		$scope.message.hidden = false;
		$scope.message.class = "message-" + message.status.toLowerCase();
		$scope.message.text = message.message;
		hideMessage();
	}
	
	function hideMessage() {
		setTimeout(function () {
			$scope.message.hidden = true;
		}, 5000)
	}
// 	$scope.info = {};
// 	$scope.index = 0;
//
// 	$scope.init = function () {
// 		sharingDataService.getProperties().then(function (data) {
// 			$scope.info = sharingDataService.getData();
// 			console.log($scope.info);
// 			initTriggers();
// 		});
// 	};
//
// 	$scope.submit = function () {
// 		preSubmit();
// 		$http({
// 			method: 'POST',
// 			url: 'properties?json=true',
// 			headers: {
// 				'Content-Type': 'application/json'
// 			},
// 			data: JSON.stringify($scope.info)
// 		}).success(function (data) {
// 			console.log(data);
// 			$scope.info = data;
// 		}).error(function (data) {
// 			$scope.message = data.message;
// 			console.log("error saving");
// 		});
// 	};
//
// 	$scope.isVisibleProperty = function (category, property) {
// 		if (property.dependsUpon == undefined || property.dependsUpon == "") {
// 			return true;
// 		}
//
// 		var dependsUpon = property.dependsUpon.split(".");
// 		var dependsUponPropertyName = dependsUpon[0];
// 		var dependsUponPropertyValue = dependsUpon[1];
// 		if (getPropertyByName(category, dependsUponPropertyName).value !== dependsUponPropertyValue) {
// 			console.log(property.name + " is not visible because it depends on " + property.dependsUpon);
// 			return false;
// 		}
//
// 		return true;
// 	};
//
// 	var getPropertyByName = function (category, propertyName) {
// 		console.log(propertyName);
// 		var properties = category.propertyEntries;
// 		for (var i = 0; i < properties.length; i++) {
// 			var property = properties[i];
// 			if (property.name == propertyName) {
// 				return property;
// 			}
// 		}
// 	};
//
// 	$scope.addTrigger = function () {
// 		var newTrigger = {
// 			// TODO: add zone, nature, group initialization
// 			unitStatus: ""
// 		};
// 		$scope.triggers.push(newTrigger);
// 	};
//
// 	$scope.removeTrigger = function (index) {
// 		var remove = confirm("Are you sure you want to delete this row?");
// 		if (remove === true) {
// 			$scope.triggers.splice(index, 1);
// 		}
// 	};
//
// 	function initTriggers() {
// 		$scope.triggers = JSON.parse(getTriggers().value);
// 	}
//
// 	function preSubmit() {
// 		removeEmptyTriggers();
// 		setTriggersValue();
// 	}
//
// 	function removeEmptyTriggers() {
// 		$scope.triggers = $scope.triggers.filter(function (trigger) {
// 			// TODO: add zone and nature check
// 			return trigger.unitStatus !== "";
// 		});
// 	}
//
// 	function setTriggersValue() {
// 		var triggers = getTriggers();
// 		triggers.value = angular.toJson($scope.triggers);
// 	}
//
// 	function getTriggers() {
// 		var categories = $scope.info.categories;
// 		var filteredCategories = categories.filter(function (category) {
// 			return category.name === 'TriggerConfiguration';
// 		});
// 		var triggerConfiguration = filteredCategories[0];
// 		var propertyEntries  = triggerConfiguration.propertyEntries;
// 		var filteredPropertyEntries = propertyEntries.filter(function (propertyEntry) {
// 			return propertyEntry.name === 'triggers';
// 		});
// 		return filteredPropertyEntries[0];
// 	}

});
