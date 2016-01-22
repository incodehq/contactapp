angular.module('ecp-contactapp')

    // stores all data under a single key, so can enumerate all.
    .service(
        'OfflineService',
        ['AppConfig',
        function(AppConfig) {

        var service = this;

        var localStorageKey = AppConfig.appPrefix + ".data"
        var numItemsKey = AppConfig.appPrefix + ".numItems"

        var internalGet = function() {
            var storedStr = window.localStorage[localStorageKey]
            if(!storedStr) {
                stored = {}
                window.localStorage[localStorageKey] = JSON.stringify(stored)
                return stored
            } else {
                return JSON.parse(storedStr)
            }
        }

        var internalPut = function(stored) {
            var numItems = Object.keys(stored).length
            window.localStorage[numItemsKey] = numItems
            window.localStorage[localStorageKey] = JSON.stringify(stored)
        }

        this.get = function(cacheKey) {
            var stored = internalGet()
            return stored[cacheKey]
        }

        this.put = function(cacheKey, resp) {
            var stored = internalGet()
            stored[cacheKey] = {
                resp: resp,
                date: new Date()
            }
            internalPut(stored)
        }

        this.lookup = function(cacheKey) {
            return service.get(cacheKey)
        }

        this.isCached = function(cacheKey) {
            var lookedUp = service.lookup(cacheKey)
            return lookedUp !== undefined
        }

        this.count = function() {
            return window.localStorage[numItemsKey]
        }

        this.clearCache = function() {
            internalPut({})
        }


    }])


;
