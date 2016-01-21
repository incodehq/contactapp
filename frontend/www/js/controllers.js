angular.module('starter.controllers', [])

    .controller('LoginCtrl',
        ['$rootScope', '$scope', '$state','$ionicPopup','AuthService', 'AppConfig',
        function($rootScope, $scope, $state, $ionicPopup, AuthService, AppConfig) {

        $scope.data = {}
        $scope.data.environments = [
            {
                name: "Development",
                url: "http://localhost:8080"
            },
            {
                name: "Production",
                url: "http://contacts.ecpnv.com"
            }
        ]

        $scope.data.environment = "Development"
        //$scope.data.environment = "Production"

        $scope.login =
            function(data) {
                var username=$scope.data.username
                var password=$scope.data.password

                AppConfig.baseUrl = $scope.data.environments.find(function(element) { return element.name === $scope.data.environment}).url

                AuthService.login(username, password).then(
                    function(authenticated) {
                        $scope.data.username = null
                        $scope.data.password = null
                        $scope.error = undefined
                        $state.go('tab.contactables', {}, {reload: true});
                    }, function(err) {
                        $scope.data.username = null
                        $scope.data.password = null
                        $scope.error = "Incorrect username or password"
                    });
        }

        $scope.about = function() {
            $state.go('about', {}, {reload:true})
        }



        // global utility variables and functions (TODO: create a service instead?)
        $rootScope.platform = {
            onDevice: ionic.Platform.isWebView() // true if on a mobile device (as opposed to via web browser)
        }

        $rootScope.isUndefined = function (thing) {
            return thing === null || (typeof thing === "undefined");
        }
        $rootScope.isDefined = function (thing) {
            return !$rootScope.isUndefined(thing);
        }
        $rootScope.isDefinedWithLength = function (thing) {
            return $rootScope.isDefined(thing) && thing.length > 0
        }

        // for debugging
        $rootScope.huzzah = function() {
            $ionicPopup.alert({
                  title: 'Huzzah',
                  template: 'it worked!'
                });
        }


    }])

    .controller('ContactablesCtrl',
        ['$scope', 'BackendService', 'HttpService', '$state', 'AuthService', '$ionicPopup', '$ionicFilterBar', '$filter',
        function($scope, BackendService, HttpService, $state, AuthService, $ionicPopup, $ionicFilterBar, $filter) {

        var ctrl = this;

        ctrl.username = AuthService.username();
        ctrl.logout = function() {
            AuthService.logout();
            $state.go('login', {}, {reload: true});
        }

        ctrl.showFilterBar = function() {
            ctrl.filterBarInstance =
                $ionicFilterBar.show({
                    items: ctrl.contactables,
                    update: function (filteredItems, filterText) {
                        ctrl.contactables = filteredItems;
                    }
                });
        }

        BackendService.loadContactables(
            function(contactables){
                ctrl.contactables = contactables
            },
            function(contactables, message) {
                ctrl.contactables = contactables
                ctrl.message = message
            }
        )

        ctrl.firstLetter = function(name) {
            return name && name.charAt(0);
        }

        ctrl.cachedStateCssClass = function(instanceId) {
            return HttpService.isCached(instanceId)
                ? "cached"
                : "not-cached"
        }

    }])

    .controller('ContactableDetailCtrl',
        ['$scope', 'BackendService', 'HttpService', '$stateParams', '$state', '$ionicPopup', 'AuthService', '$filter',
        function($scope, BackendService, HttpService, $stateParams, $state, $ionicPopup, AuthService, $filter) {

        var ctrl = this;

        ctrl.username = AuthService.username();
        ctrl.logout = function() {
            AuthService.logout();
            $state.go('login', {}, {reload: true});
        }

        var instanceId = function(href) {
            var n = href.lastIndexOf('/');
            var result = href.substring(n + 1);
            return result;
        }

        BackendService.loadContactable(
            $stateParams.instanceId,
            function(contactable) {
                ctrl.contactable = contactable
            },
            function(contactable, message) {
                ctrl.contactable = contactable
                ctrl.message = message
            }
        )

        ctrl.sendEmail = function() {
            if(window.plugins && window.plugins.emailComposer) {
                window.plugins.emailComposer.showEmailComposerWithCallback(
                    function(result) {
                        console.log("Response -> " + result);
                    },
                "",                     // Subject
                "",                     // Body
                [contactable.email],    // To
                null,                   // CC
                null,                   // BCC
                false,                  // isHTML
                null,                   // Attachments
                null);                  // Attachment Data
            }
        }

    }])

    .controller('AboutCtrl',
        ['$scope', 'HttpService', 'AppConfig',
        function($scope, HttpService, AppConfig) {

        var ctrl = this;

        ctrl.platform = {
            deviceInformation: ionic.Platform.device(),
            isWebView: ionic.Platform.isWebView(), // ie running in Cordova
            isIPad: ionic.Platform.isIPad(),
            isIOS: ionic.Platform.isIOS(),
            isAndroid: ionic.Platform.isAndroid(),
            isWindowsPhone: ionic.Platform.isWindowsPhone(),
            platform: ionic.Platform.platform(),
            platformVersion: ionic.Platform.version(),

            onDevice: ionic.Platform.isWebView() // true if on a device
        }

        ctrl.downloadContacts = function() {
            var contactables = HttpService.lookup(
                AppConfig.listAllKey
            )
            if(!contactables) {
                return
            }
            for(contactable in contactables) {

            }
        }


    }])

;

