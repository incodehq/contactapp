angular.module('starter.controllers', [])

    .controller('LoginCtrl', 
    ['$scope','$state','$ionicPopup','AuthService',
    function($scope, $state, $ionicPopup, AuthService) {
        
        $scope.data = {};
 
        $scope.login = 
            function(data) {
                var username=$scope.data.username
                var password=$scope.data.password
                
                AuthService.login(username, password).then(
                    function(authenticated) {
                        $scope.data.username = ""
                        $scope.data.password = ""
                        $state.go('tab.contacts', {}, {reload: true});
                        //$scope.setCurrentUsername($scope.data.username);
                    }, function(err) {
                        
                        var alertPopup = $ionicPopup.alert({
                            title: 'Login failed!',
                            template: 'Please check your credentials!'
                        }).then(function() {
                            $scope.data.username = ""
                            $scope.data.password = ""
                        });
                        
                    });
        };
    }])
 
    .controller('AppCtrl', 
    ['$scope', '$state', '$ionicPopup', 'AuthService', 'AUTH_EVENTS',
    function($scope, $state, $ionicPopup, AuthService, AUTH_EVENTS) {
    
        $scope.username = AuthService.username();
        
        $scope.$on(AUTH_EVENTS.notAuthorized, function(event) {
            var alertPopup = $ionicPopup.alert({
            title: 'Unauthorized!',
            template: 'You are not allowed to access this resource.'
            });
        });
        
        $scope.$on(AUTH_EVENTS.notAuthenticated, function(event) {
            AuthService.logout();
            $state.go('login');
            var alertPopup = $ionicPopup.alert({
            title: 'Session Lost!',
            template: 'Sorry, You have to login again.'
            });
        });
        
        $scope.setCurrentUsername = function(name) {
            $scope.username = name;
        };
    
    }])

    .controller('ContactsCtrl', 
    ['$scope','$http', '$state', 'AuthService',
    function($scope, $http, $state, AuthService) {

        var ctrl = this;

        ctrl.logout = function() {
            AuthService.logout();
            $state.go('login', {}, {reload: true});
        }

        $http.get(
            "/restful/services/HomePageService/actions/homePage/invoke",
            {
                headers: {
                    'Accept': 'application/json;profile=urn:org.apache.isis/v1'
                }
            }
        )
        .then(
            function(resp) {
                ctrl.contacts = resp.data.objects;
            }, 
            function(err) {
                console.error('ERR', err); //  err.status will contain the status code
            })
    }])
    
    .controller('ContactDetailCtrl', 
    ['$http','$stateParams', '$state', 'AuthService',
    function($http, $stateParams, $state, AuthService) {

        var ctrl = this;

        ctrl.logout = function() {
            AuthService.logout();
            $state.go('login', {}, {reload: true});
        }

        $http.get(
            "/restful/objects/domainapp.dom.contacts.Contact/" + $stateParams.instanceId,
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
    
   
    ;
    
