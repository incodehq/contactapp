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

        $rootScope.platform = {
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

        $rootScope.isUndefined = function (thing) {
            return thing === null || (typeof thing === "undefined");
        }
        $rootScope.isDefined = function (thing) {
            return !$rootScope.isUndefined(thing);
        }
        $rootScope.isDefinedWithLength = function (thing) {
            return $rootScope.isDefined(thing) && thing.length > 0
        }

        $scope.about = function() {
            $state.go('about', {}, {reload:true})
        }

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

        // for debugging
        $rootScope.huzzah = function() {
            $ionicPopup.alert({
                  title: 'Huzzah',
                  template: 'it worked!'
                });
        }

    }])

    .controller('ContactablesCtrl',
        ['$scope', 'HttpService', '$state', 'AuthService', '$ionicPopup', '$ionicFilterBar', '$filter',
        function($scope, HttpService, $state, AuthService, $ionicPopup, $ionicFilterBar, $filter) {

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
                    /*,
                    // an attempt to see if filtering only after 3 chars entered improves rendering speed; apparently not
                    filter: function(items, text) {
                        if(text && text.length >= 3) {
                            return items.filter(function(item) {
                                for(property in item) {
                                    if (typeof item[property] === "string" && item[property].indexOf(text) > -1) {
                                        return true
                                    }
                                }
                                return false
                            })
                        } else {
                            return items
                        }
                    }*/
                });
        }

        var instanceId = function(href) {
            var n = href.lastIndexOf('/');
            var result = href.substring(n + 1);
            return result;
        }

        HttpService.get(
            "listAll",
            "/restful/services/ContactableViewModelRepository/actions/listAll/invoke",
            function(respData) {
                var trimmedData = respData.map(
                    function(contactable){
                        contactable.$$instanceId = instanceId(contactable.$$href)
                        delete contactable.$$href
                        delete contactable.$$title
                        delete contactable.notes
                        delete contactable.email
                        return contactable
                    }
                )
                trimmedData.sort(function(a,b) {
                    return a.name.localeCompare(b.name)
                })
                ctrl.contactables = trimmedData
            },
            function(err, respData, date, resp) {
                ctrl.contactables = respData || {}
                if(date) {
                    ctrl.message = "Data from " + $filter('date')(date, 'd MMM, HH:mm')
                } else {
                    ctrl.message = "No data available"
                }
            }
        )

        this.firstLetter = function(name) {
            return name && name.charAt(0);
        }

        this.cachedStateCssClass = function(instanceId) {
            return HttpService.cached(instanceId)
                ? "cached"
                : "not-cached"
        }

    }])

    .controller('ContactableDetailCtrl',
        ['$scope', 'HttpService', '$stateParams', '$state', '$ionicPopup', 'AuthService', '$filter',
        function($scope, HttpService, $stateParams, $state, $ionicPopup, AuthService, $filter) {

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

        HttpService.get(
            $stateParams.instanceId,
            "/restful/objects/domainapp.app.rest.v1.contacts.ContactableViewModel/" + $stateParams.instanceId,
            function(respData) {
                delete respData.$$href
                delete respData.$$instanceId
                delete respData.$$title
                respData.contactNumbers = respData.contactNumbers.map(
                    function(contactNumber){
                        delete contactNumber.$$instanceId
                        delete contactNumber.$$href
                        delete contactNumber.$$title
                        return contactNumber
                    }
                )
                respData.contactRoles = respData.contactRoles.map(
                    function(contactRole){
                        delete contactRole.$$href
                        delete contactRole.$$title
                        contactRole.contact.$$instanceId = instanceId(contactRole.contact.href)
                        delete contactRole.contact.href
                        delete contactRole.contact.rel
                        delete contactRole.contact.method
                        delete contactRole.contact.type
                        contactRole.contactGroup.$$instanceId = instanceId(contactRole.contactGroup.href)
                        delete contactRole.contactGroup.href
                        delete contactRole.contactGroup.rel
                        delete contactRole.contactGroup.method
                        delete contactRole.contactGroup.type
                        return contactRole
                    }
                )
                ctrl.contactable = respData
            },
            function(err, respData, date, resp) {
                ctrl.contactable = respData || {}
                if(date) {
                    ctrl.message = "Data from " + $filter('date')(date, 'd MMM, HH:mm')
                } else {
                    ctrl.message = "No data available"
                }
            })

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

    ;

