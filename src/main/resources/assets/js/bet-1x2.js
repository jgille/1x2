var app = angular.module('bet1x2', ['ui.router', 'ngCookies', 'ngRoute']);

app.config(function($routeProvider) {
    $routeProvider

        // route for the home page
        .when('/', {
            templateUrl : 'view/home.html',
            controller  : 'Bet1x2Controller'
        })

        // route for the my plays page
        .when('/myplays', {
            templateUrl : 'view/myplays.html',
            controller  : 'Bet1x2Controller'
        })

        // route for the results page
        .when('/results', {
            templateUrl : 'view/results.html',
            controller  : 'Bet1x2Controller'
        });
});

app.run();