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
    ['$scope','$http', '$state', 'AuthService', '$ionicFilterBar',
    function($scope, $http, $state, AuthService, $ionicFilterBar) {

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
            "http://localhost:8080/restful/services/HomePageService/actions/homePage/invoke",
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
    ['$scope', '$http','$stateParams', '$state', 'AuthService',
    function($scope, $http, $stateParams, $state, AuthService) {

        var ctrl = this;

        ctrl.username = AuthService.username();
        ctrl.logout = function() {
            AuthService.logout();
            $state.go('login', {}, {reload: true});
        }

        $http.get(
            "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/" + $stateParams.instanceId,
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
    
