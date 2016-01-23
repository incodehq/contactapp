angular.module(
    'ecp-contactapp.services.preferences', [])


    .service(
        'PreferencesService',
        ['AppConfig', '$rootScope',
        function(AppConfig, $rootScope) {

        var service = this;

        service.preferences = {}

        //
        // preferences.environment
        //
        var environmentKey = AppConfig.appPrefix + ".preferences.environment"
        var defaultEnvironment = "Development"
        if(!window.localStorage[environmentKey]) {
            window.localStorage[environmentKey] = defaultEnvironment
        }

        service.preferences.environment = {
            options: [
                {
                    name: "Development",
                    url: "http://localhost:8080"
                },
                {
                    name: "Test",
                    url: "http://10.0.0.5:8080"
                },
                {
                    name: "Production",
                    url: "http://contacts.ecpnv.com"
                }
            ],
            selected: window.localStorage[environmentKey]
        }


        service.urlForSelectedEnvironment = function() {
            return service.preferences.environment.options.find(
                    function(element) {
                        return element.name === service.preferences.environment.selected
                    }).url
        }

        service.updateEnvironment = function(environmentName) {
            service.preferences.environment.selected = environmentName
            window.localStorage[environmentKey] = environmentName
        }




        //
        // preferences.scrolling
        //
        var scrollingKey = AppConfig.appPrefix + ".preferences.scrolling"
        var defaultScrolling = "ng-repeat"
        // var defaultScrolling = "collection-repeat"

        if(!window.localStorage[scrollingKey]) {
            window.localStorage[scrollingKey] = defaultScrolling
        }
        service.preferences.scrolling = {
            options: [
                {
                    text: "With dividers, but slower to display",
                    value: "ng-repeat"
                },
                {
                    text: "No dividers, but faster to display",
                    value: "collection-repeat"
                }
            ],
            selected: window.localStorage[scrollingKey]
        }

        service.valueForSelectedScrolling = function() {
            return service.preferences.environment.options.find(
                    function(element) {
                        return element.text === service.preferences.scrolling.selected
                    }).value
        }

        service.updateScrolling = function(text) {
            service.preferences.scrolling.selected = text
            window.localStorage[scrollingKey] = text
        }



        //
        // preferences.nameOrder
        //
        var nameOrderKey = AppConfig.appPrefix + ".preferences.nameOrder"
        var defaultNameOrder = "first-last"
        // var defaultNameOrder = "last-first"
        service.preferences.nameOrder = {
            options: [
                {
                    text: "First Last",
                    value: "first-last"
                },
                {
                    text: "Last, First",
                    value: "last-first"
                }
            ],
            selected: window.localStorage[nameOrderKey]
        }

        if(!window.localStorage[nameOrderKey]) {
            window.localStorage[nameOrderKey] = defaultNameOrder
        }

        service.valueForSelectedNameOrder = function() {
            return service.preferences.environment.options.find(
                    function(element) {
                        return element.text === service.preferences.nameOrder.selected
                    }).value
        }

        service.updateNameOrder = function(text) {
            service.preferences.nameOrder.selected = text
            window.localStorage[nameOrderKey] = text
        }



    }])



;
