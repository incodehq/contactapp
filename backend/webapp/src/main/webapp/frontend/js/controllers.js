angular.module('starter.controllers', [])

    .controller('LoginCtrl',
    ['$scope','$state','$ionicPopup','AuthService',
    function($scope, $state, $ionicPopup, AuthService) {

        $scope.data = {}

        $scope.login =
            function(data) {
                var username=$scope.data.username
                var password=$scope.data.password

                AuthService.login(username, password).then(
                    function(authenticated) {
                        $scope.data = {}
                        $scope.error = undefined
                        $state.go('tab.contacts', {}, {reload: true});
                    }, function(err) {
                        $scope.data = {}
                        $scope.error = "Incorrect username or password"
                    });
        };
    }])

    .controller('ContactsCtrl',
    ['$scope','$http', '$state', 'AuthService', '$ionicFilterBar', 'AppConfig',
    function($scope, $http, $state, AuthService, $ionicFilterBar, AppConfig) {

        var ctrl = this;

        ctrl.username = AuthService.username();
        ctrl.logout = function() {
            AuthService.logout();
            $state.go('login', {}, {reload: true});
        }

        ctrl.showFilterBar = function() {
            ctrl.filterBarInstance =
                $ionicFilterBar.show({
                    items: ctrl.contacts,
                    update: function (filteredItems, filterText) {
                        ctrl.contacts = filteredItems;
                    }
                });
        }

        $http.get(
            AppConfig.baseUrl + "/restful/services/ContactViewModelRepository/actions/listAll/invoke",
            {
                headers: {
                    'Accept': 'application/json;profile=urn:org.apache.isis/v1;suppress=true'
                }
            }
        )
        .then(
            function(resp) {
                ctrl.contacts = resp.data;
            },
            function(err) {
                console.error('ERR', err); //  err.status will contain the status code
            })
    }])

    .controller('ContactDetailCtrl',
    ['$scope', '$http','$stateParams', '$state', 'AuthService', 'AppConfig',
    function($scope, $http, $stateParams, $state, AuthService, AppConfig) {

        var ctrl = this;

        ctrl.username = AuthService.username();
        ctrl.logout = function() {
            AuthService.logout();
            $state.go('login', {}, {reload: true});
        }

        $http.get(
            AppConfig.baseUrl + "/restful/objects/domainapp.app.rest.v1.contacts.ContactViewModel/" + $stateParams.instanceId,
            {
                headers: {
                    'Accept': 'application/json;profile=urn:org.apache.isis/v1'
                }
            }
        )
        .then(
            function(resp) {
                ctrl.contact = resp.data
            },
            function(err) {
                console.error('ERR', err); //  err.status will contain the status code
            })
    }])

    .controller('GroupsCtrl',
    ['$scope','$http', '$state', 'AuthService', '$ionicFilterBar', 'AppConfig',
    function($scope, $http, $state, AuthService, $ionicFilterBar, AppConfig) {

        var ctrl = this;

        ctrl.username = AuthService.username();
        ctrl.logout = function() {
            AuthService.logout();
            $state.go('login', {}, {reload: true});
        }

        ctrl.showFilterBar = function() {
            ctrl.filterBarInstance =
                $ionicFilterBar.show({
                    items: ctrl.groups,
                    update: function (filteredItems, filterText) {
                        ctrl.groups = filteredItems;
                    }
                });
        }

        $http.get(
            AppConfig.baseUrl + "/restful/services/ContactGroupViewModelRepository/actions/listAll/invoke",
            {
                headers: {
                    'Accept': 'application/json;profile=urn:org.apache.isis/v1;suppress=true'
                }
            }
        )
        .then(
            function(resp) {
                ctrl.groups = resp.data;
            },
            function(err) {
                console.error('ERR', err); //  err.status will contain the status code
            })
    }])

    .controller('GroupDetailCtrl',
        ['$scope', '$http','$stateParams', '$state', 'AuthService', 'AppConfig',
            function($scope, $http, $stateParams, $state, AuthService, AppConfig) {

                var ctrl = this;

                ctrl.username = AuthService.username();
                ctrl.logout = function() {
                    AuthService.logout();
                    $state.go('login', {}, {reload: true});
                }

                $http.get(
                      AppConfig.baseUrl + "/restful/objects/domainapp.app.rest.v1.group.ContactGroupViewModel/" + $stateParams.instanceId,
                    {
                        headers: {
                            'Accept': 'application/json;profile=urn:org.apache.isis/v1'
                        }
                    }
                    )
                    .then(
                        function(resp) {
                            ctrl.group = resp.data
                        },
                        function(err) {
                            console.error('ERR', err); //  err.status will contain the status code
                        })
            }])

    ;

