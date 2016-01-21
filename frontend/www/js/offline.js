angular.module('starter')

    .service(
        'OfflineService',
        ['AppConfig',
        function(AppConfig) {

        var service = this;

        var localStorageKeyFor = function(cacheKey) {
            return AppConfig.appPrefix + "." + cacheKey
        }

        this.put = function(cacheKey, resp) {
            var localStorageKey = localStorageKeyFor(cacheKey)
            window.localStorage[localStorageKey] =
                JSON.stringify(
                {
                    resp: resp,
                    date: new Date()
                })
        }

        this.get = function(cacheKey) {
            var localStorageKey = localStorageKeyFor(cacheKey)
            var stored = window.localStorage[localStorageKey]
            return stored? JSON.parse(stored): null
        }

        this.lookup = function(cacheKey) {
            var localStorageKey = localStorageKeyFor(cacheKey)
            var stored = window.localStorage[localStorageKey]
            return stored
        }

        this.isCached = function(cacheKey) {
            return service.lookup(cacheKey) !== undefined
        }

    }])


;
