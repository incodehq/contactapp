angular.module('starter', ['ionic', 'starter.controllers', 'ngResource', 'jett.ionic.filter.bar'])

    .value('AppConfig', {
        baseUrl: "http://127.0.0.1:8080"
    })

    .run(function($ionicPlatform) {
        $ionicPlatform.ready(function() {
            // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
            // for form inputs)
            if (window.cordova && window.cordova.plugins && window.cordova.plugins.Keyboard) {
                cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
                cordova.plugins.Keyboard.disableScroll(true);
            }

            if (window.StatusBar) {
                // org.apache.cordova.statusbar required
                window.StatusBar.styleLightContent();
            }
        });
    })


    .config(function($stateProvider, $urlRouterProvider) {

    // Ionic uses AngularUI Router which uses the concept of states
    // Learn more here: https://github.com/angular-ui/ui-router
    // Set up the various states which the app can be in.
    // Each state's controller can be found in controllers.js
    $stateProvider

        .state('login', {
            url: '/login',
            templateUrl: 'templates/login.html',
            controller: 'LoginCtrl'
        })

        .state('tab', {
            cache: false,
            url: '/tab',
            abstract: true,
            templateUrl: 'templates/tabs.html'
        })

        .state('tab.contactables', {
            cache: false,
            url: '/contactables',
            views: {
                'tab-contactables': {
                templateUrl: 'templates/tab-contactables.html',
                controller: 'ContactablesCtrl as ctrl'
                }
            }
        })
        .state('tab.contactable-detail', {
            cache: false,
            url: '/contactables/:instanceId',
            views: {
                'tab-contactables': {
                    templateUrl: 'templates/contactable-detail.html',
                    controller: 'ContactableDetailCtrl as ctrl'
                }
            }
        });

        // if none of the above states are matched, use this as the fallback
        $urlRouterProvider.otherwise(function ($injector, $location) {
            var $state = $injector.get("$state");
            $state.go("login");
        });
    })

;
