var app = angular.module('bet1x2');

app.controller('Bet1x2Controller', ['$scope', function ($scope) {

    $scope.greeting = "Hello world!";

    $scope.isLoggedIn = function() {
        return true;
    };

}
]);