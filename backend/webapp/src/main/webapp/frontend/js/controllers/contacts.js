angular.module(
    'ecp-contactapp.controllers.contacts', [])


    .controller('ContactablesCtrl',
        ['BackendService', 'AuthService', 'PreferencesService', '$state', '$ionicFilterBar', '$ionicSideMenuDelegate',
        function(BackendService, AuthService, PreferencesService, $state, $ionicFilterBar, $ionicSideMenuDelegate) {

        var ctrl = this;

        ctrl.preferences = PreferencesService.preferences;
        ctrl.username = AuthService.username();
        ctrl.contactables = []

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

        ctrl.isShowLeftSideMenu = function() {
            return BackendService.isOfflineEnabled()
        }

        ctrl.showLeftSideMenu = function() {
            $ionicSideMenuDelegate.toggleLeft();
        }

        BackendService.loadContactables(
            function(contactables, messageIfAny) {
                ctrl.contactables = contactables
                ctrl.message = messageIfAny
            }
        )

/*
no longer used...
        ctrl.firstLetter = function(name) {
            return name && name.charAt(0);
        }
*/

        ctrl.cachedStateCssClass = function(contactable) {
            return contactable && contactable.$$instanceId &&
                   BackendService.isCached(contactable.$$instanceId)
                ? "cached"
                : "not-cached"
        }




    }])

    .controller('ContactableDetailCtrl',
        ['BackendService', 'PreferencesService', 'AuthService', '$stateParams', '$state',
        function(BackendService, PreferencesService, AuthService, $stateParams, $state) {

        var ctrl = this;

        ctrl.preferences = PreferencesService.preferences;
        ctrl.username = AuthService.username();
        ctrl.contactable = {}

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
            function(contactable, messageIfAny) {
                ctrl.contactable = contactable
                ctrl.message = messageIfAny
            }
        )

        var windowOpenSystem = function(href) {
            window.open(href, '_system');
        }

        ctrl.sendEmail = function(email) {
            windowOpenSystem('mailto:' + email)
        }

        ctrl.dialNumber = function(number) {
            windowOpenSystem('tel:' + number);
        }

        ctrl.cachedStateCssClass = function(instanceId) {
            return instanceId &&
                   BackendService.isCached(instanceId)
                ? "cached"
                : "not-cached"
        }


    }])

;

