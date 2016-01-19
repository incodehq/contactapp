angular.module('starter')

    .service('HttpService',
            ['$http', 'AppConfig',
            function($http, AppConfig) {

        var getWithHeaders = function(relativeUrl, headers, onOK, onError) {
            var url = AppConfig.baseUrl + relativeUrl
            var localStorageKey = AppConfig.appPrefix + "." + url
            $http.get(
                url,
                {
                    headers: headers
                }
            )
            .then(
                function(resp) {
                    if(onOK) {
                        onOK(resp.data)
                    }
                    window.localStorage[localStorageKey] = JSON.stringify( { resp: resp, date: new Date() })
                },
                function(err) {
                    if(onError) {
                        var stored = window.localStorage[localStorageKey]
                        if(stored) {
                            var stored = JSON.parse(stored)
                            onError(err, stored.resp.data, stored.date, stored.resp)
                        } else {
                            onError(err, null, null, null)
                        }
                    }
                })
            }

        this.get = function(relativeUrl, onOK, onError) {
            var header = {
                'Accept': 'application/json;profile=urn:org.apache.isis/v1;suppress=true'
            }
            getWithHeaders(relativeUrl, header, onOK, onError)
        }

    }])

;
