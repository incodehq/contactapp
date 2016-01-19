angular.module('starter.controllers', [])

    .controller('LoginCtrl',
    ['$scope','$state','$ionicPopup','AuthService', 'AppConfig',
    function($scope, $state, $ionicPopup, AuthService, AppConfig) {
        var environments = {
            Development: "http://localhost:8080",
            Test: "http://contacts-test.ecpnv.com",
            Production: "http://contacts.ecpnv.com"
        }
        $scope.data = {}
        //$scope.data.environment = "Development"
        $scope.data.environment = "Production"

        $scope.login =
            function(data) {
                var username=$scope.data.username
                var password=$scope.data.password

                AppConfig.baseUrl = environments[$scope.data.environment]

                AuthService.login(username, password).then(
                    function(authenticated) {
                        $scope.data.username = null
                        $scope.data.password = null
                        $scope.error = undefined
                        $state.go('tab.contactables', {}, {reload: true});
                    }, function(err) {
                        $scope.data.username = null
                        $scope.data.password = null
                        $scope.error = "Incorrect username or password"
                    });
        };
    }])

    .controller('ContactablesCtrl',
    ['$scope', 'HttpService', '$state', 'AuthService', '$ionicFilterBar',
    function($scope, HttpService, $state, AuthService, $ionicFilterBar) {

        var ctrl = this;

        ctrl.username = AuthService.username();
        ctrl.logout = function() {
            AuthService.logout();
            $state.go('login', {}, {reload: true});
        }

        ctrl.showFilterBar = function() {
            ctrl.filterBarInstance =
                $ionicFilterBar.show({
                    items: ctrl.contactables,
                    update: function (filteredItems, filterText) {
                        ctrl.contactables = filteredItems;
                    }
                });
        }

        HttpService.get(
            "/restful/services/ContactableViewModelRepository/actions/listAll/invoke",
            function(respData) {
                ctrl.contactables = respData
            },
            function(respData, err) {
                ctrl.contactables = respData || []
                console.error('ERR', err); //  err.status will contain the status code
            }
            )

    }])

    .controller('ContactableDetailCtrl',
    ['$scope', 'HttpService', '$http','$stateParams', '$state', 'AuthService', 'AppConfig',
    function($scope, HttpService, $http, $stateParams, $state, AuthService, AppConfig) {

        var ctrl = this;

        ctrl.username = AuthService.username();
        ctrl.logout = function() {
            AuthService.logout();
            $state.go('login', {}, {reload: true});
        }

        $http.get(
            AppConfig.baseUrl + "/restful/objects/domainapp.app.rest.v1.contacts.ContactableViewModel/" + $stateParams.instanceId,
            {
                headers: {
                    'Accept': 'application/json;profile=urn:org.apache.isis/v1'
                }
            }
        )
        .then(
            function(resp) {
                ctrl.contactable = resp.data
            },
            function(err) {
                console.error('ERR', err); //  err.status will contain the status code
            })

        $scope.instanceId = function(href) {
            var n = href.lastIndexOf('/');
            var result = href.substring(n + 1);
            return result;
        }

        $scope.isUndefined = function (thing) {
            return thing === null || (typeof thing === "undefined");
        }
        $scope.isDefined = function (thing) {
            return !$scope.isUndefined(thing);
        }

    }])

    ;

