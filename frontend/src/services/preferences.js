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
        var defaultEnvironment = "Development"
        //var defaultEnvironment = "Production"

        var environmentKey = AppConfig + "preferences.environment"
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
        var defaultScrolling = "ng-repeat"
        // var defaultScrolling = "collection-repeat"

        var scrollingKey = AppConfig + ".preferences.scrolling"
        if(!window.localStorage[scrollingKey]) {
            window.localStorage[scrollingKey] = defaultScrolling
        }

        service.preferences.scrolling = {
            options: [
                {
                    text: "Use Angular scrolling",
                    value: "ng-repeat"
                },
                {
                    text: "Use Ionic scrolling (faster rendering)",
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



    }])



;
