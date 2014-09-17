var app = angular.module('bet1x2');

app.controller('Bet1x2Controller', ['$scope', '$cookies', function ($scope, $cookies) {

    $scope.greeting = "Hello world!";

    $scope.isLoggedIn = function() {
        var jwt = $cookies.jwt;
        return jwt !== undefined;
    };

}
]);