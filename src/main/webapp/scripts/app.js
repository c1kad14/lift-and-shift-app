var app = angular.module('app', ["ngRoute"]);

app.config(function ($routeProvider) {
	$routeProvider.when("/", {
		templateUrl: "properties.html"
	}).when("/properties", {
		templateUrl: "properties.html"
	}).when("/logs", {
		templateUrl: "logs.html"
	}).otherwise({
		redirectTo: '/'
	});
});