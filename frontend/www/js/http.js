angular.module('starter')

    .service('HttpService',
            ['$http', '$ionicLoading', 'AppConfig',
            function($http, $ionicLoading, AppConfig) {

        var service = this

        var isUndefined = function (thing) {
            return thing === null || (typeof thing === "undefined");
        }
        var useIonicLoading = function(options) {
            return isUndefined(options) || !options.suppressIonicLoading
        }

        var getWithHeaders = function(cacheKey, relativeUrl, headers, onOK, onError, options) {
            var url = AppConfig.baseUrl + relativeUrl
            var localStorageKey = AppConfig.appPrefix + "." + cacheKey
            if(useIonicLoading(options)) {
                $ionicLoading.show({
                     delay: 200
                 })
            }
            $http.get(
                url,
                {
                    headers: headers
                }
            )
            .then(
                function(resp) {
                    if(useIonicLoading(options)) {
                        $ionicLoading.hide()
                    }
                    if(onOK) {
                        onOK(resp.data)
                    }
                    window.localStorage[localStorageKey] = JSON.stringify( { resp: resp, date: new Date() })
                },
                function(err) {
                    if(useIonicLoading(options)) {
                        $ionicLoading.hide()
                    }
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

        this.get = function(cacheKey, relativeUrl, onOK, onError, options) {
            var header = {
                'Accept': 'application/json;profile=urn:org.apache.isis/v1;suppress=true'
            }
            getWithHeaders(cacheKey, relativeUrl, header, onOK, onError, options)
        }

        this.lookup = function(cacheKey) {
            var localStorageKey = AppConfig.appPrefix + "." + cacheKey
            var stored = window.localStorage[localStorageKey]
            return stored
        }

        this.isCached = function(cacheKey) {
            return service.lookup(cacheKey) !== undefined
        }


    }])

;
