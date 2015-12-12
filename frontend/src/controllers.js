angular.module('starter.controllers', [])

    .controller('ChatsCtrl', function($scope, $http) {
        var ctrl = this;  
        $http.get(
            "/restful/services/HomePageService/actions/homePage/invoke",
            {
                headers: {
//                'Authorization': 'Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='
                    'Accept': 'application/json;profile=urn:org.apache.isis/v1'
                }
            }
        )
        .then(
            function(resp) {
                ctrl.chats = resp.data.objects;
            }, 
            function(err) {
                console.error('ERR', err); //  err.status will contain the status code
            })
    })
    
    .controller('ChatDetailCtrl', function($scope, $http, $stateParams) {
      
        var ctrl = this;
        $http.get(
            "/restful/objects/domainapp.dom.contacts.Contact/" + $stateParams.instanceId,
            {
                headers: {
//                  'Authorization': 'Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='
                    'Accept': 'application/json;profile=urn:org.apache.isis/v1'
                }
            }
        )
        .then(
            function(resp) {
                ctrl.chat = resp.data
            }, 
            function(err) {
                console.error('ERR', err); //  err.status will contain the status code
            })
    });