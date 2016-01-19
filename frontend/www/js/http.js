angular.module('starter')

    .service('HttpService',
            ['$http', 'AppConfig',
            function($http, AppConfig) {

        var getWithHeaders = function(relativeUrl, headers, onOK, onError) {
            var url = AppConfig.baseUrl + relativeUrl
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
                    window.localStorage[url] = JSON.stringify(resp.data)
                },
                function(err) {
                    if(onError) {
                        var stored = window.localStorage[url]
                        if(stored) {
                            onError(JSON.parse(stored), err)
                        } else {
                            onError(null, err)
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
