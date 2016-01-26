angular.module(
    'ecp-contactapp.services.http' , [])

    .service('HttpService',
            ['$http', '$ionicLoading', 'AppConfig', 'OfflineService',
            function($http, $ionicLoading, AppConfig, OfflineService) {

        var service = this

        var isUndefined = function (thing) {
            return thing === null || (typeof thing === "undefined");
        }
        var useIonicLoading = function(options) {
            return isUndefined(options) || !options.suppressIonicLoading
        }

        this.get = function(cacheKey, relativeUrl, onOK, onError, options) {
            var url = AppConfig.baseUrl + relativeUrl
            var localStorageKey = AppConfig.appPrefix + "." + cacheKey
            if(useIonicLoading(options)) {
                $ionicLoading.show({
                     delay: 200
                 })
            }
            return $http.get(
                url,
                {
                    headers: {
                        'Accept': 'application/json;profile=urn:org.apache.isis/v1;suppress=true'
                    }
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
                    OfflineService.put(cacheKey, resp)
                },
                function(err) {
                    if(useIonicLoading(options)) {
                        $ionicLoading.hide()
                    }
                    if(onError) {
                        var stored = OfflineService.get(cacheKey)
                        if(stored) {
                            onError(err, stored.resp.data, stored.date)
                        } else {
                            onError(err, null, null)
                        }
                    }
                })
        }

        this.lookup = function(cacheKey) {
            return OfflineService.lookup(cacheKey)
        }

        this.isCached = function(cacheKey) {
            return OfflineService.isCached(cacheKey)
        }


    }])

;
