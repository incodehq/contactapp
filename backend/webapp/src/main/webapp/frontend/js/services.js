angular.module('starter.services', [])
    .factory('Chats', function ($resource) {
    return $resource("http://localhost:8080/restful/services/HomePageService/actions/homePage/invoke", {}, {
        get: {
            method: "GET",
            headers: {
                //            'Authorization': 'Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='
                'Accept': 'application/json;profile=urn:org.apache.isis/v1'
            }
        }
    });
})
    .factory('Chat', function ($resource) {
    return $resource("http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/:instanceId", {}, {
        get: {
            method: "GET",
            headers: {
                //            'Authorization': 'Basic QWxhZGRpbjpvcGVuIHNlc2FtZQ=='
                'Accept': 'application/json;profile=urn:org.apache.isis/v1'
            }
        }
    });
});
