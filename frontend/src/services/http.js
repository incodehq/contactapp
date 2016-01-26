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

        this.isOfflineEnabled = function() {
            return OfflineService.isOfflineEnabled()
        }

        this.get = function(cacheKey, relativeUrl, onCached, onData, onOK, onError, options) {
            var url = AppConfig.baseUrl + relativeUrl
            var headerMap = {
                'Accept': 'application/json;profile=urn:org.apache.isis/v1;suppress=true'
            }
            var localStorageKey = AppConfig.appPrefix + "." + cacheKey
            var cached = OfflineService.get(cacheKey)
            if(cached) {
                // return the data we already have stored offline
                if(onCached) {
                    onCached(cached.resp.data, cached.date)
                }
            }
            // asynchronously populate the offline cache if we can
            var showSpinner = !cached && useIonicLoading(options)
            if(showSpinner) {
                $ionicLoading.show({
                     delay: 200
                 })
            }
            $http.get(
                url,
                {
                    headers: headerMap
                }
            )
            .then(
                function(resp) {
                    // update the offline cache, and call the onOK callback once more
                    if(showSpinner) {
                        $ionicLoading.hide()
                    }
                    resp.data = onData(resp.data)
                    if(OfflineService.isOfflineEnabled()) {
                        OfflineService.put(cacheKey, resp)
                        var stored = OfflineService.get(cacheKey)
                        if(stored) {
                            onOK(stored.resp.data, stored.date)
                        }
                    } else {
                        onOK(resp.data, null) // suppress any message at end
                    }
                },
                function(err) {
                    if(showSpinner) {
                        $ionicLoading.hide()
                    }
                    if(onError && !cached) {
                        // unable to obtain any data, and wasn't previously cached
                        onError(err)
                    }
                }
            )
        }

        this.lookup = function(cacheKey) {
            return OfflineService.lookup(cacheKey)
        }

        this.isCached = function(cacheKey) {
            return OfflineService.isCached(cacheKey)
        }


    }])

;