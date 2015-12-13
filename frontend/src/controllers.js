angular.module('starter.controllers', [])


    .controller('ContactsCtrl', 
    ['$scope','$http', '$state', 'Base64', 'AuthService',
    function($scope, $http, $state, Base64, AuthService) {

        $scope.logout = function() {
            AuthService.logout();
            $state.go('login');
        }
        
        var ctrl = this;
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
    ['$scope','$http','$stateParams', '$state', 'Base64', 'AuthService',
    function($scope, $http, $stateParams, $state, Base64, AuthService) {

        $scope.logout = function() {
            AuthService.logout();
            $state.go('login');
        }
       
        var ctrl = this;
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
    
    .controller('LoginCtrl', 
    ['$scope','$state','$ionicPopup','AuthService',
    function($scope, $state, $ionicPopup, AuthService) {
        
        $scope.data = {};
 
        $scope.login = 
            function(data) {
                
                AuthService.login($scope.data.username, $scope.data.password).then(
                    function(authenticated) {
                        $state.go('tab.contacts', {}, {reload: true});
                        //$scope.setCurrentUsername($scope.data.username);
                    }, function(err) {
                        var alertPopup = $ionicPopup.alert({
                        title: 'Login failed!',
                        template: 'Please check your credentials!'
                    });
                });
                
                //$state.go('tab.contacts', {}, {reload: true});
        };
    }])
    
    ;
    
