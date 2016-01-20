angular.module('starter.controllers', [])

    .controller('LoginCtrl',
    ['$scope','$state','$ionicPopup','AuthService', 'AppConfig',
    function($scope, $state, $ionicPopup, AuthService, AppConfig) {
        var environments = {
            Development: "http://localhost:8080",
            Test: "http://contacts-test.ecpnv.com",
            Production: "http://contacts.ecpnv.com"
        }
        $scope.data = {}
        $scope.data.environment = "Development"
        //$scope.data.environment = "Production"

        $scope.login =
            function(data) {
                var username=$scope.data.username
                var password=$scope.data.password

                AppConfig.baseUrl = environments[$scope.data.environment]

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
        };
    }])

    .controller('ContactablesCtrl',
    ['$scope', 'HttpService', '$state', 'AuthService', '$ionicFilterBar', '$filter',
    function($scope, HttpService, $state, AuthService, $ionicFilterBar, $filter) {

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
            "/restful/services/ContactableViewModelRepository/actions/listAll/invoke",
            function(respData) {
                ctrl.contactables = respData.map(
                    function(contactable){
                        contactable.$$instanceId = instanceId(contactable.$$href)
                        delete contactable.$$href
                        delete contactable.$$title
                        delete contactable.notes
                        delete contactable.email
                        return contactable
                    }
                )
            },
            function(err, respData, date, resp) {
                ctrl.respData = respData || {}
                if(date) {
                    ctrl.message = "Data from " + $filter('date')(date, 'd MMM, HH:mm')
                } else {
                    ctrl.message = "No data available"
                }
            }
        )

    }])

    .controller('ContactableDetailCtrl',
    ['$scope', 'HttpService', '$stateParams', '$state', 'AuthService', '$filter',
    function($scope, HttpService, $stateParams, $state, AuthService, $filter) {

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

        $scope.instanceId = function(href) {
            var n = href.lastIndexOf('/');
            var result = href.substring(n + 1);
            return result;
        }

        $scope.isUndefined = function (thing) {
            return thing === null || (typeof thing === "undefined");
        }
        $scope.isDefined = function (thing) {
            return !$scope.isUndefined(thing);
        }

    }])

    ;

