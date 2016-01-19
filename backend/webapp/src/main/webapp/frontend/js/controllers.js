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
    ['$scope', 'HttpService', '$state', 'AuthService', '$ionicFilterBar', '$filter',
    function($scope, HttpService, $state, AuthService, $ionicFilterBar, $filter) {

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
            function(err, respData, date, resp) {
                ctrl.contactables = respData || []
                if(date) {
                    ctrl.message = "Data from " + $filter('date')(date, 'd MMM, HH:mm')
                } else {
                    ctrl.message = "No data available"
                }
            }
        )

    }])

    .controller('ContactableDetailCtrl',
    ['$scope', 'HttpService', '$stateParams', '$state', 'AuthService', '$filter',
    function($scope, HttpService, $stateParams, $state, AuthService, $filter) {

        var ctrl = this;

        ctrl.username = AuthService.username();
        ctrl.logout = function() {
            AuthService.logout();
            $state.go('login', {}, {reload: true});
        }

        HttpService.get(
            "/restful/objects/domainapp.app.rest.v1.contacts.ContactableViewModel/" + $stateParams.instanceId,
            function(respData) {
                ctrl.contactable = respData
            },
            function(err, respData, date, resp) {
                ctrl.contactable = respData || {}
                if(date) {
                    ctrl.message = "Data from " + $filter('date')(date, 'd MMM, HH:mm')
                } else {
                    ctrl.message = "No data available"
                }
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

