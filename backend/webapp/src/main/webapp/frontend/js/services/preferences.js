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
        service.preferences.environments = [
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
        ]

        var defaultEnvironment = "Development"
        //var defaultEnvironment = "Production"

        var environmentKey = AppConfig + ".environment"
        var environment = window.localStorage[environmentKey]

        if(!environment) {
            window.localStorage[environmentKey] = defaultEnvironment
        }

        service.preferences.environment = window.localStorage[environmentKey]



        //
        // preferences.filteringAndScrolling
        //

        var filteringAndScrollingKey = AppConfig + ".preferences.filteringAndScrolling"
        if(!window.localStorage[filteringAndScrollingKey]) {
            window.localStorage[filteringAndScrollingKey] = "ng-repeat"
        }

        service.preferences.filteringAndScrolling = {
            options: [
                { text: "Use Angular scrolling", value: "ng-repeat" }
                ,{ text: "Use Ionic scrolling (faster rendering)", value: "collection-repeat" }
              ],
              selected: window.localStorage[filteringAndScrollingKey]
          }
        if(!service.preferences.filteringAndScrolling.selected){
            services.preferences.filteringAndScrolling
        }


    }])



;
