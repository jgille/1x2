var app = angular.module('bet1x2');

app.controller('Bet1x2Controller', ['$scope', '$cookies', function ($scope, $cookies) {

    var isUserLoggedIn = function () {
        var jwt = $cookies.jwt;
        return jwt !== null;
    };

    $scope.load = function() {
        $scope.isLoggedIn = isUserLoggedIn();
    }

    $scope.greeting = "Hello world!";

    $scope.logout = function () {
        $cookies.jwt = null;
        $scope.isLoggedIn = false;
    };

    $scope.load();
}
]);