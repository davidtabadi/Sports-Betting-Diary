angular
  .module("myApp", [])
  // ================================ ROOT SCOPE
  .run(function ($rootScope) {
    // global method for object to string
    $rootScope.toString = function (obj) {
      console.log("casted toString()");
      var s = "{";
      for (key in obj) {
        s += key + ":" + obj[key] + ", ";
      }
      s += "}";
      return s;
    };
  })

  // ================================ SERVICE
  // service is used to share properties between controllers
  .service("clientService", function () {
    // params will be initialized by loginCtrl
    var restURL = "";
    var isloggedIn = false;
    var client = null;
    var loginFailed = false;
  })

  // ================================ LOGIN CTRL
  .controller("loginCtrl", function ($http, $scope, $rootScope, clientService) {
    $scope.isLoggedIn = function () {
      return clientService.isloggedIn;
    };

    $scope.loginFailed = function () {
      return clientService.loginFailed;
    };

    // called from HTML ng-init directive
    $scope.init = function () {
      // init shared vars via service
      clientService.restURL = "api/";
      clientService.isloggedIn = false;
      clientService.loginFailed = false;
      clientService.client = {
        name: "",
        password: "",
        clientType: ""
      };
      console.log(
        "loginCtrl.init() called, isloggedIn = " + clientService.isloggedIn
      );
    };

    $scope.login = function (name, password, clientType) {
      // var loginURL = login/
      console.log(
        "loginCTRL.login() called. with : " +
        name +
        "," +
        password +
        "," +
        clientType
      );
      switch (clientType) {
        case "PLAYER":
          loginURL = "player/";
          break;
        case "BOOKIE":
          loginURL = "bookie/";
          break;
        case "ADMIN":
          loginURL = "admin/";
          break;
      }
      console.log("clientService.restURL : " + clientService.restURL);
      console.log("loginURL : " + loginURL);

      var url =
        clientService.restURL + loginURL + "login/" + name + "/" + password;

      console.log("sending HTTP/GET : " + url);
      $http.get(url).then(
        function (response) {
          console.log(
            "HTTP/200 received, successfull login for : " +
            name +
            "," +
            password +
            "," +
            clientType
          );
          clientService.isloggedIn = true;
          if (clientService.loginFailed == true) {
            clientService.loginFailed = false;
          }

          clientService.client = response.data;
          console.log("clientService.isloggedIn = " + clientService.isloggedIn);
          console.log(
            "clientService.client = name-" +
            clientService.client.name +
            " password-" +
            clientService.client.password +
            " clientType-" +
            clientService.client.clientType
          );
        },
        function (error) {
          clientService.loginFailed = true;
          console.log(error);
          console.log("login failed  = " + clientService.loginFailed);
          alert("Login failed - try again");
        }
      );
    };
  })

  // ================================ PLAYER CTRL
  .controller("playerCtrl", function ($http, $scope, $rootScope, clientService) {
    // HTML can decide before showing Customer
    $scope.isPlayer = function () {
      return (
        clientService.isloggedIn == true &&
        clientService.client.clientType == "PLAYER"
      );
    };

    $scope.getPlayer = function () {
      return clientService.client;
    };

    $scope.init = function () {
      $scope.sportType = "ALL";
      $scope.bet = null;
      $scope.minWager = 0;
      // data display mode (Placed/Available)
      $scope.displayMode = null;
      // for highlight selected row
      $scope.selectedRow = null;
    };

    // HTML can decide if to show the 'place' button
    $scope.isPlaceButtonEnabled = function () {
      return $scope.displayMode == "Available";
    };

    // set the $scope.bet after selection, index is for
    // highlighting
    $scope.setClickedRow = function (index, bet) {
      $scope.bet = bet;
      $scope.selectedRow = index;
    };

    // show placed bets ( all / by sport / by min wager )
    $scope.getAllPlacedBets = function (sportType) {
      var url = null;
      if (sportType == null || sportType == "" || sportType == "ALL") {
        url = clientService.restURL + "player/getplayerbets";
      } else if (sportType == "MIN_WAGER") {
        url =
          clientService.restURL +
          "player/getplayerbetsmorethanwager/" +
          $scope.minWager;
      } else {
        url =
          clientService.restURL +
          "player/getplayerbetsbysport/" +
          $scope.sportType;
      }
      console.log("getting placed bets from : " + url);
      $http.get(url).then(function (response) {
        console.log("Ok. HTTP/200 - response received");
        $scope.selectedRow = null;
        $scope.bet = null;
        $scope.myBets = response.data;
        $scope.displayMode = "Placed";

        if (angular.isArray(response.data)) {
          console.log("got an array");
          $scope.myBets = response.data;
        } else {
          console.log(
            "got single bet, clearing the old myBets array, and get response to myBets[0]"
          );
          $scope.myBets = [];
          $scope.myBets[0] = response.data;
        }
        console.log("response.data.betTitle = " + response.data.betTitle);
      });
    };

    // show available bets ( all / by sport / by min wager )
    $scope.getAllAvailableBets = function (sportType) {
      var url = null;
      if (sportType == null || sportType == "" || sportType == "ALL") {
        url = clientService.restURL + "player/getavailablebets";
      } else if (sportType == "MIN_WAGER") {
        url =
          clientService.restURL +
          "player/getavailablebetsmorethanwager/" +
          $scope.minWager;
      } else {
        url =
          clientService.restURL +
          "player/getavailablebetsbysport/" +
          $scope.sportType;
      }
      console.log("getting available bets from : " + url);
      $http.get(url).then(function (response) {
        console.log("Ok. HTTP/200 - response received");
        $scope.selectedRow = null;
        $scope.bet = null;
        $scope.myBets = response.data;
        $scope.displayMode = "Available";

        if (angular.isArray(response.data)) {
          console.log("got an array");
          $scope.myBets = response.data;
        } else {
          console.log(
            "got single bet, clearing the old myBets array, and get response to myBets[0]"
          );
          $scope.myBets = [];
          $scope.myBets[0] = response.data;
        }
        console.log("response.data.betTitle = " + response.data.betTitle);
      });
    };

    // place a selected bet from table.
    $scope.placeBet = function (bet) {
      var url = clientService.restURL + "player/placebet";
      $http
        .post(url, $scope.bet, {
          headers: {
            "Content-Type": "application/json",
            Accept: "application/json"
          }
        })
        .then(
          function (response) {
            alert($scope.bet.betTitle + " : placed successfuly.");
            console.log("HTTP/200 ? response received");
            $scope.selectedRow = null;
            $scope.bet = null;
            $scope.getAllPlacedBets("ALL");
            $scope.displayMode = "Placed";
          },
          function (error) {
            $scope.operationFailed = true;

            alert("Error!! " + $scope.bet.betTitle + " bet allready placed");
          }
        );
    };
  })

  // ================================ BOOKIE CTRL
  .controller("bookieCtrl", function ($http, $scope, $rootScope, clientService) {
    $scope.isBookie = function () {
      return (
        clientService.isloggedIn == true &&
        clientService.client.clientType == "BOOKIE"
      );
    };

    $scope.getBookie = function () {
      return clientService.client;
    };

    $scope.init = function () {
      $scope.sportType = "ALL";
      $scope.bet = null;
      $scope.betId = "";
      $scope.displayMode = null;
      // for highlight selected row
      $scope.selectedRow = null;
      $scope.operationFailed = false;
    };

    $scope.setClickedRow = function (index, bet) {
      $scope.bet = bet;
      $scope.selectedRow = index;
    };

    $scope.clearBet = function () {
      $scope.bet = null;
      $scope.selectedRow = null;
      $scope.betId = "";
    };

    $scope.isOperationFailed = function () {
      return $scope.operationFailed;
    };

    // show coupons ( all / by sport / by min_wager )
    $scope.getBets = function (sportType) {
      var url = null;
      if (sportType == null || sportType == "" || sportType == "ALL") {
        url = clientService.restURL + "bookie/getbookiebets";
      } else if (sportType == "BY_ID") {
        url = clientService.restURL + "bookie/getbet/" + $scope.betId;
      } else if (sportType == "MIN_WAGER") {
        url =
          clientService.restURL +
          "bookie/getbookiebetsmorethanwager/" +
          $scope.minWager;
      } else {
        url =
          clientService.restURL + "bookie/getbookiebetsbysport/" + sportType;
      }
      console.log("getting bets from : " + url);
      $http.get(url).then(function (response) {
        console.log("Ok. HTTP/200 - response received");
        $scope.operationFailed = false;
        $scope.selectedRow = null;
        $scope.bet = null;
        if (angular.isArray(response.data)) {
          console.log("got an array");
          $scope.myBets = response.data;
        } else {
          console.log(
            "got single bet, clearing the old myBets array, and get response to myBets[0]"
          );
          $scope.myBets = [];
          $scope.myBets[0] = response.data;
        }
        console.log("response.data.betTitle = " + response.data.betTitle);
      });
    };

    $scope.addBet = function (bet) {
      var url = clientService.restURL + "bookie/addbet";
      $http.post(url, $scope.bet).then(
        function (response) {
          alert($scope.bet.betTitle + " : created successfuly.");
          console.log("HTTP/200 response received, bet created, initializing");
          $scope.selectedRow = null;
          $scope.bet = null;
          $scope.operationFailed = false;
          // refresh display
          $scope.getBets("ALL");
        },
        function (error) {
          $scope.operationFailed = true;
          console.log("creating bet failed");
        }
      );
    };

    // DELETE is different ( must use explicitly add
    // content-type
    // not working without header !
    // we do not allow deleting Bets so when the Bookie decides to cancel the Bet
	// (instead of deleting from the system) it simply makes the Bet void so no
	// wager and no odds will be available on the Bet and it will be
	// cancelled (voided)
    $scope.removeBet = function (bet) {
      var url = clientService.restURL + "bookie/removebet/" + $scope.bet.betId;
      $http({
        method: "DELETE",
        url: url,
        data: $scope.bet,
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json"
        }
      }).then(function (response) {
        alert($scope.bet.betTitle + " : cancelled  successfuly.");
        console.log("HTTP/200 response received, bet cancelled, initializing");
        $scope.operationFailed = false;
        $scope.selectedRow = null;
        $scope.bet = null;
        // refresh display
        $scope.getBets("ALL");
      });
    };

    $scope.updateBet = function (bet) {
      var url = clientService.restURL + "bookie/updatebet/" + $scope.bet.betId;
      $http.put(url, $scope.bet, $scope.betId).then(
        function (response) {
          alert($scope.bet.betTitle + " : updated successfuly.");
          console.log("HTTP/200 response received, bet updated, initializing");
          $scope.selectedRow = null;
          $scope.bet = null;
          $scope.operationFailed = false;
          // refresh display
          $scope.getBets("ALL");
        },
        function (error) {
          $scope.operationFailed = true;
          console.log("updating bet failed");
        }
      );
    };
  })

  // ================================ ADMIN CTRL
  .controller("adminCtrl", function ($http, $scope, $rootScope, clientService) {
    $scope.isAdmin = function () {
      return (
        clientService.isloggedIn == true &&
        clientService.client.clientType == "ADMIN"
      );
    };

    $scope.getAdmin = function () {
      return clientService.client;
    };

    $scope.init = function () {
      console.log("admin init() called.");
      $scope.bookiesMode = "ALL";
      $scope.bookie = null;
      $scope.bookieId = "";
      $scope.player = null;
      $scope.playerId = "";
      $scope.properties = null;
      $scope.displayMode = null;
      $scope.bookiesSelectedRow = null;
      $scope.operationFailed = false;
    };

    $scope.setBookieClickedRow = function (index, bookie) {
      $scope.bookie = bookie;
      $scope.bookiesSelectedRow = index;
    };

    $scope.clearBookie = function () {
      $scope.bookie = null;
      $scope.bookiesSelectedRow = null;
      $scope.bookieId = "";
    };

    $scope.isOperationFailed = function () {
      return $scope.operationFailed;
    };

    $scope.setPlayerClickedRow = function (index, player) {
      $scope.player = player;
      $scope.playersSelectedRow = index;
    };

    $scope.clearPlayer = function () {
      $scope.player = null;
      $scope.playersSelectedRow = null;
      $scope.playerId = "";
    };

    $scope.getBookies = function (bookiesMode) {
      var url = null;
      if (bookiesMode == null || bookiesMode == "" || bookiesMode == "ALL") {
        url = clientService.restURL + "admin/bookies";
      } else if (bookiesMode == "BY_ID") {
        url = clientService.restURL + "admin/bookies/" + $scope.bookieId;
      }
      console.log("getting bookies from : " + url);
      $http.get(url).then(function (response) {
        console.log("Ok. HTTP/200 - response received");
        $scope.operationFailed = false;
        $scope.bookiesSelectedRow = null;
        $scope.bookie = null;

        if (angular.isArray(response.data)) {
          console.log("got an array");
          $scope.bookies = response.data;
        } else {
          console.log(
            "got single bookie, clearing the old bookies array, and get response to bookies[0]"
          );
          $scope.bookies = [];
          $scope.bookies[0] = response.data;
        }
      });
    };

    // create a new bookie
    $scope.createBookie = function (bookie) {
      var url = clientService.restURL + "admin/bookies";
      $http.post(url, $scope.bookie).then(
        function (response) {
          alert($scope.bookie.bookieUserName + " : added successfuly.");
          console.log(
            "HTTP/200 response received, bookie added, initializing"
          );
          $scope.bookiesSelectedRow = null;
          $scope.bookie = null;
          $scope.operationFailed = false;
          // refresh display
          $scope.getBookies("ALL");
        },
        function (error) {
          $scope.operationFailed = true;
          console.log("create bookie failed");
        }
      );
    };

    // remove a bookie
    $scope.removeBookie = function (bookie) {
      var url =
        clientService.restURL + "admin/bookies/" + $scope.bookie.bookieId;
      $http;
      $http({
        method: "DELETE",
        url: url,
        data: $scope.bookie,
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json"
        }
      }).then(function (response) {
        alert($scope.bookie.bookieUserName + " : removed successfuly.");
        console.log("HTTP/200 response received, bookie removed, initializing");
        $scope.bookiesSelectedRow = null;
        $scope.bookie = null;
        $scope.operationFailed = false;
        // refresh display
        $scope.getBookies("ALL");
      });
    };

    // update bookie
    $scope.updateBookie = function (bookie) {
      var url =
        clientService.restURL + "admin/bookies/" + $scope.bookie.bookieId;
      $http.put(url, $scope.bookie, $scope.id).then(
        function (response) {
          alert($scope.bookie.bookieUserName + " : updated successfuly.");
          console.log(
            "HTTP/200 response received, bookie updated, initializing"
          );
          $scope.bookiesSelectedRow = null;
          $scope.bookie = null;
          $scope.operationFailed = false;
          // refresh display
          $scope.getBookies("ALL");
        },
        function (error) {
          $scope.operationFailed = true;
          console.log("updating bookie failed");
        }
      );
    };

    $scope.getPlayers = function (playersMode) {
      var url = null;
      if (playersMode == null || playersMode == "" || playersMode == "ALL") {
        url = clientService.restURL + "admin/players";
      } else if (playersMode == "BY_ID") {
        url = clientService.restURL + "admin/players/" + $scope.playerId;
      }
      console.log("getting players from : " + url);
      $http.get(url).then(function (response) {
        console.log("Ok. HTTP/200 - response received");
        $scope.operationFailed = false;
        $scope.playersSelectedRow = null;
        $scope.player = null;

        if (angular.isArray(response.data)) {
          console.log("got an array");
          $scope.players = response.data;
        } else {
          console.log(
            "got single player, clearing the old players array, and get response to players[0]"
          );
          $scope.players = [];
          $scope.players[0] = response.data;
        }
      });
    };

    // create a new player
    $scope.createPlayer = function (player) {
      var url = clientService.restURL + "admin/players";
      $http.post(url, $scope.player).then(
        function (response) {
          alert($scope.player.playerUserName + " : added successfuly.");
          console.log(
            "HTTP/200. object received : " + $rootScope.toString(response.data)
          );
          $scope.playersSelectedRow = null;
          $scope.player = null;
          $scope.operationFailed = false;
          // refresh display
          $scope.getPlayers("ALL");
        },
        function (error) {
          $scope.operationFailed = true;
          console.log("create player failed");
        }
      );
    };

    // remove a player
    $scope.removePlayer = function (player) {
      var url =
        clientService.restURL + "admin/players/" + $scope.player.playerId;
      $http({
        method: "DELETE",
        url: url,
        data: $scope.player,
        headers: {
          "Content-Type": "application/json",
          Accept: "application/json"
        }
      }).then(function (response) {
        alert($scope.player.playerUserName + " : removed successfuly.");
        console.log(
          "HTTP/200. object received : " + $rootScope.toString(response.data)
        );
        $scope.playersSelectedRow = null;
        $scope.player = null;
        $scope.operationFailed = false;
        // refresh display
        $scope.getPlayers("ALL");
      });
    };

    // update player
    $scope.updatePlayer = function (player) {
      var url =
        clientService.restURL + "admin/players/" + $scope.player.playerId;
      $http.put(url, $scope.player, $scope.id).then(
        function (response) {
          alert($scope.player.playerUserName + " : updated successfuly.");
          console.log(
            "HTTP/200. object received : " + $rootScope.toString(response.data)
          );
          $scope.playersSelectedRow = null;
          $scope.player = null;
          $scope.operationFailed = false;
          // refresh display
          $scope.getPlayers("ALL");
        },
        function (error) {
          $scope.operationFailed = true;
          console.log("updating player failed");
        }
      );
    };

    // getting system propertis from admin API
    $scope.getSystemProperties = function () {
      if ($scope.properties == null) {
        var url = clientService.restURL + "admin/getConfig";
        $http.get(url).then(function (response) {
          console.log(
            "HTTP/200. object received : " + $rootScope.toString(response.data)
          );
          $scope.properties = response.data;
        });
      }
    };
  })

  // ================================ LOGOUT CTRL
  .controller("logoutCtrl", function ($http, $scope, clientService) {
    $scope.isLoggedIn = function () {
      return clientService.isloggedIn;
    };

    $scope.logout = function () {
      var logoutURL = null;
      console.log(
        "logoutCTRL.logout() called. service clientType is = " +
        clientService.client.clientType
      );
      switch (clientService.client.clientType) {
        case "PLAYER":
          logoutURL = "player/";
          break;
        case "BOOKIE":
          logoutURL = "bookie/";
          break;
        case "ADMIN":
          logoutURL = "admin/";
          break;
      }
      console.log("clientService.restURL : " + clientService.restURL);
      console.log("logoutURL : " + logoutURL);
      var url = clientService.restURL + logoutURL + "logout";
      console.log("sending HTTP/GET : " + url);
      $http
        .get(url)
        .then(function (response) {
          console.log(
            "HTTP/200 received, successfull logout for : " +
            clientService.client.clientType
          );
          // logout and clear state.
          clientService.isloggedIn = false;
          // clientService.client=response.data;
          clientService.client = {
            name: "",
            password: "",
            clientType: ""
          };
          console.log("clientService.isloggedIn = " + clientService.isloggedIn);
          console.log(
            "logged out. client in context is now : " +
            clientService.client.name +
            "," +
            clientService.client.password +
            "," +
            clientService.client.clientType
          );
        })
        .error(function (error) {
          console.log(error);
        });
    };
  });
