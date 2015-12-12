angular.module('starter.controllers', [])

    .controller('ChatsCtrl', function($scope, $http) {
          
        $http.get(
            "http://localhost:8080/restful/services/HomePageService/actions/homePage/invoke",
            {
                headers: {
//                'Authorization': 'Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='
                    'Accept': 'application/json;profile=urn:org.apache.isis/v1'
                }
            }
        )
        .then(
            function(resp) {
                $scope.chats = resp.data.objects;
            }, 
            function(err) {
                console.error('ERR', err); //  err.status will contain the status code
            })
    })
    
    .controller('ChatDetailCtrl', function($scope, $http, $stateParams) {
      
        $http.get(
            "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/" + $stateParams.instanceId,
            {
                headers: {
//                  'Authorization': 'Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='
                    'Accept': 'application/json;profile=urn:org.apache.isis/v1'
                }
            }
        )
        .then(
            function(resp) {
                $scope.chat = resp.data
            }, 
            function(err) {
                console.error('ERR', err); //  err.status will contain the status code
            })
    });