var app = angular.module('bet1x2', ['ui.router', 'ngCookies', 'ngRoute']);

app.config(function($routeProvider) {
    $routeProvider

        .when('/', {
            templateUrl : 'view/home.html',
            controller  : 'Bet1x2Controller'
        })

        .when('/myplays', {
            templateUrl : 'view/myplays.html',
            controller  : 'Bet1x2Controller'
        })

        .when('/allplays', {
            templateUrl : 'view/allplays.html',
            controller  : 'Bet1x2Controller'
        })

        .when('/results', {
            templateUrl : 'view/results.html',
            controller  : 'Bet1x2Controller'
        });
});

app.run();