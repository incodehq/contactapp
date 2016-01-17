angular.module('starter.controllers', [])

    .controller('LoginCtrl',
    ['$rootScope','$scope','$state','$ionicPopup','AuthService', 'AppConfig',
    function($rootScope,$scope, $state, $ionicPopup, AuthService, AppConfig) {
        var environments = {
            Development: "http://localhost:8080",
            Test: "http://contacts-test.ecpnv.com",
            Production: "http://contacts.ecpnv.com"
        }
        $scope.data = {}
        $scope.data.environment = "Development"

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
                        $rootScope.settings = { tab: 'contacts' }
                        $state.go('tabs.contacts', {}, {reload: true})
                    }, function(err) {
                        $scope.data.username = null
                        $scope.data.password = null
                        $scope.error = "Incorrect username or password"
                    });
        };
    }])

    .controller('TabsContainerCtrl',
    ['$rootScope','$scope','$http', '$state', 'AuthService', '$ionicFilterBar', 'AppConfig', '$ionicConfig',
    function($rootScope, $scope, $http, $state, AuthService, $ionicFilterBar, AppConfig, $ionicConfig) {

        var ctrl = this;

        ctrl.goContacts = function() {
            $ionicConfig.views.transition('platform');
            $state.go('tabs.contacts', {}, {reload: true})

            //$rootScope.settings.tab = 'contacts'
        }
        ctrl.goContactGroups = function() {
            $ionicConfig.views.transition('platform');
            $state.go('tabs.groups', {}, {reload: true})

            //$rootScope.settings.tab = 'contactGroups'
        }

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


        ctrl.goContacts = function() {
            $ionicConfig.views.transition('platform');
            $state.go('tabs.contacts')

            $rootScope.settings.tab = 'contacts'
        }
        ctrl.goContactGroups = function() {
            $ionicConfig.views.transition('platform');
            $state.go('tabs.groups')

            $rootScope.settings.tab = 'contactGroups'
        }

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

        ctrl.goContacts = function() {
            $ionicConfig.views.transition('platform');
            $state.go('tabs.contacts')

            $rootScope.settings.tab = 'contacts'
        }
        ctrl.goContactGroups = function() {
            $ionicConfig.views.transition('platform');
            $state.go('tabs.groups')

            $rootScope.settings.tab = 'contactGroups'
        }

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

        ctrl.goContacts = function() {
            $ionicConfig.views.transition('platform');
            $state.go('tabs.contacts')

            $rootScope.settings.tab = 'contacts'
        }
        ctrl.goContactGroups = function() {
            $ionicConfig.views.transition('platform');
            $state.go('tabs.groups')

            $rootScope.settings.tab = 'contactGroups'
        }

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

            ctrl.goContacts = function() {
                $ionicConfig.views.transition('platform');
                $state.go('tabs.contacts')

                $rootScope.settings.tab = 'contacts'
            }
            ctrl.goContactGroups = function() {
                $ionicConfig.views.transition('platform');
                $state.go('tabs.groups')

                $rootScope.settings.tab = 'contactGroups'
            }


        }])

    ;

