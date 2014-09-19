var app = angular.module('bet1x2', ['ui.router', 'ngCookies', 'ngRoute']);

app.config(function($routeProvider) {
    $routeProvider

        .when('/', {
            templateUrl : 'view/home.html',
            controller  : 'Bet1x2Controller'
        })

        .when('/my', {
            templateUrl : 'view/myplays.html',
            controller  : 'Bet1x2Controller'
        })

        .when('/all', {
            templateUrl : 'view/allplays.html',
            controller  : 'Bet1x2Controller'
        })

        .when('/toplist', {
            templateUrl : 'view/toplist.html',
            controller  : 'Bet1x2Controller'
        })

        .when('/results', {
            templateUrl : 'view/results.html',
            controller  : 'Bet1x2Controller'
        });
});

app.run();

$(document).on('click','.navbar-collapse.in',function(e) {
    if( $(e.target).is('a') ) {
        $(this).collapse('hide');
    }
});
