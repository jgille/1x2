var app = angular.module('bet1x2');

app
    .directive('main', function () {
        return {
            restrict: 'E',
            templateUrl: '/view/main.html'
        };
    })
    .directive('login', function () {
        return {
            restrict: 'E',
            templateUrl: '/view/login.html'
        };
    })
;