var app = angular.module('bet1x2');

app.controller('Bet1x2Controller', ['$scope', '$cookies', function ($scope, $cookies) {

    $scope.input = {
        newRound: {
            numGames: 16,
            name: 'Omgång X',
            cutOff: '2014-09-30 20:30:00'
        }
    }

    $scope.data = {
        rounds: [
        ]
    };

    var getJwt = function () {
        return $cookies.jwt;
    }

    var isUserLoggedIn = function () {
        var jwt = getJwt();
        return typeof jwt !== 'undefined' && jwt !== '';
    };

    var decodeBase64 = function (s) {
        var e = {}, i, v = [], r = '', w = String.fromCharCode;
        var n = [
            [65, 91],
            [97, 123],
            [48, 58],
            [43, 44],
            [47, 48]
        ];

        for (z in n) {
            for (i = n[z][0]; i < n[z][1]; i++) {
                v.push(w(i));
            }
        }
        for (i = 0; i < 64; i++) {
            e[v[i]] = i;
        }

        for (i = 0; i < s.length; i += 72) {
            var b = 0, c, x, l = 0, o = s.substring(i, i + 72);
            for (x = 0; x < o.length; x++) {
                c = e[o.charAt(x)];
                b = (b << 6) + c;
                l += 6;
                while (l >= 8) {
                    r += w((b >>> (l -= 8)) % 256);
                }
            }
        }
        return r;
    };

    $scope.load = function () {
        $scope.isLoggedIn = isUserLoggedIn();

        if ($scope.isLoggedIn === true) {
            var token = getJwt();
            var json = JSON.parse(decodeBase64(token.split('.')[1]));
            $scope.greeting = 'Välkommen ' + json.username;
        } else {
            $scope.greeting = '';
        }
    }

    $scope.logout = function () {
        $cookies.jwt = '';
        $scope.load();
    };

    $scope.isAdmin = function () {
        return true;
    }

    $scope.saveRound = function (roundIndex) {
        alert(JSON.stringify($scope.data.rounds[roundIndex]));
    }

    $scope.popRound = function () {
        $scope.data.rounds.pop();
    }

    $scope.isLastRound = function (roundIndex) {
        return roundIndex === $scope.data.rounds.length - 1;
    }

    $scope.addRound = function () {
        var games = [];

        for (var i = 0; i < $scope.input.newRound.numGames; i++) {
            games.push({
                id: '' + (i + 1),
                home: 'Hemmalag',
                away: 'Bortalag'
            });
        }
        $scope.data.rounds.push(
            {
                round_id: $scope.data.rounds.length + '',
                name: $scope.input.newRound.name,
                cutOff: $scope.input.newRound.cutOff,
                games: games
            }
        );
    }

    $scope.load();
}
]);