var app = angular.module('bet1x2');

app.controller('Bet1x2Controller', ['$scope', '$cookies', '$http', function ($scope, $cookies, $http) {

    $scope.maxNumRows = 72;

    $scope.input = {
        newRound: {
            numGames: 16,
            name: 'Omgång X',
            cut_off: '2014-09-30 20:30:00'
        }
    };

    $scope.data = {
        rounds: [
        ]
    };

    var getJwt = function () {
        return $cookies.jwt;
    };

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

            $scope.loadRounds();
            $scope.loadMyPlays();
            $scope.loadToplist();
            $scope.loadAllPlays();
        } else {
            $scope.greeting = '';
        }
    };

    $scope.logout = function () {
        $cookies.jwt = '';
        $scope.load();
    };

    $scope.isAdmin = function () {
        return true;
    };

    $scope.saveRounds = function () {
        $http.post('/api/rounds', $scope.data.rounds).success(function () {
        }).error(function () {
                alert("Something went wrong!");
            });
    };

    $scope.loadRounds = function () {
        $http.get('/api/rounds').success(function (data) {
            $scope.data.rounds = data;
            var hasCompletedRounds = false;
            for (var i = 0; i < data.length; i++) {
                hasCompletedRounds = data[i].completed || hasCompletedRounds;
            }
            $scope.data.hasCompletedRounds = hasCompletedRounds;
        });
    };

    $scope.loadMyPlays = function () {
        $http.get('/api/myplays').success(function (data) {
            $scope.data.myplays = data;
        });
    };

    $scope.loadToplist = function () {
        $http.get('/api/toplist').success(function (data) {
            $scope.data.toplist = data;
        });
    };

    $scope.loadAllPlays = function () {
        $http.get('/api/allplays').success(function (data) {
            $scope.data.allplays = data;
            $scope.selectRound(Math.max(0, $scope.selectedRound));
        });
    };

    $scope.popRound = function () {
        $scope.data.rounds.pop();
    };

    $scope.isLastRound = function (roundIndex) {
        return roundIndex === $scope.data.rounds.length - 1;
    };

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
                cut_off: $scope.input.newRound.cut_off,
                games: games
            }
        );
    };

    $scope.savePlay = function (playIndex) {
        $http.put('/api/myplays', $scope.data.myplays[playIndex])
            .success(function () {
            })
            .error(function () {
                alert("Something went wrong!");
            });
    };

    $scope.mayNotSubmitPlay = function (playIndex) {
        var numPlayedRows = $scope.numPlayedRows(playIndex);
        return !$scope.data.myplays[playIndex].may_submit_play ||
            numPlayedRows == 0 || numPlayedRows > $scope.maxNumRows;
    };

    $scope.selectRound = function (index) {
        $scope.data.selectedRound = index;
    }

    $scope.numPlayedRows = function (playIndex) {
        var myplay = $scope.data.myplays[playIndex];

        var total = 1;
        for (var index = 0; index < myplay.plays.length; index++) {
            var play = myplay.plays[index];
            var num = 0;
            if (play.one) {
                num++;
            }
            if (play.x) {
                num++;
            }
            if (play.two) {
                num++;
            }
            total *= num;
        }
        return total;
    };

    $scope.load();
}
]);


app.factory('authInterceptor', function ($rootScope, $q, $window, $cookies) {
    return {
        request: function (config) {
            config.headers = config.headers || {};
            config.headers.Authorization = 'Bearer ' + $cookies.jwt;
            return config;
        }
    };
});

app.config(function ($httpProvider) {
    $httpProvider.interceptors.push('authInterceptor');
});



