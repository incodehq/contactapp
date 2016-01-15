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

        // setup an abstract state for the tabs directive
        .state('tab', {
            cache: false,
            url: '/tab',
            abstract: true,
            templateUrl: 'templates/tabs.html'
        })

        // Each tab has its own nav history stack:
        .state('tab.contacts', {
            cache: false,
            url: '/contacts',
            views: {
                'tab-contacts': {
                templateUrl: 'templates/tab-contacts.html',
                controller: 'ContactsCtrl as ctrl'
                }
            }
        })
        .state('tab.contact-detail', {
            cache: false,
            url: '/contacts/:instanceId',
            views: {
                'tab-contacts': {
                    templateUrl: 'templates/contact-detail.html',
                    controller: 'ContactDetailCtrl as ctrl'
                }
            }
        })
        .state('tab.groups', {
            cache: false,
            url: '/groups',
            views: {
                'tab-groups': {
                    templateUrl: 'templates/tab-groups.html',
                    controller: 'GroupsCtrl as ctrl'
                }
            }
        })
        .state('tab.group-detail', {
            cache: false,
            url: '/groups/:instanceId',
            views: {
                'tab-groups': {
                    templateUrl: 'templates/group-detail.html',
                    controller: 'GroupDetailCtrl as ctrl'
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
