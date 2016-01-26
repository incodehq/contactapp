angular.module(
    'ecp-contactapp.controllers.options', [])

    .controller('OptionsCtrl',
        ['$scope', 'BackendService', 'OfflineService', 'AuthService', 'PreferencesService', '$state', '$timeout',
        function($scope, BackendService, OfflineService, AuthService, PreferencesService, $state, $timeout) {

        var ctrl = this;

        ctrl.username = AuthService.username();
        ctrl.logout = function() {
            AuthService.logout();
            $state.go('login', {}, {reload: true});
        }

        ctrl.preferences = PreferencesService.preferences;

        ctrl.scrolling = PreferencesService.preferences.scrolling.selected
        ctrl.nameOrder = PreferencesService.preferences.nameOrder.selected

        $scope.updateScrolling = function() {
            PreferencesService.updateScrolling(ctrl.scrolling)
        }

        $scope.updateNameOrder = function() {
            PreferencesService.updateNameOrder(ctrl.nameOrder)
        }

        ctrl.downloadContacts = function() {
            OfflineService.clearCache()

            BackendService.loadContactables(
                function(contactables, messageIfAny){
                    ctrl.message = "Downloading..."
                    ctrl.numberOfContacts = contactables.length
                    for (var i = 0; i < contactables.length; i++) {
                        var j = i+1
                        var contactable = contactables[i]
                        var instanceId = contactable.$$instanceId
                        BackendService.loadContactable(
                            instanceId,
                            function(contactData){
                                $timeout(function() {
                                    ctrl.downloadCount = j
                                    // Angular doesn't seem able to keep up...
                                    // ctrl.message = contactData.name
                                })
                            },
                            {
                                suppressIonicLoading: true
                            }
                        )
                    }
                }
            )
        }

        ctrl.numberOfDownloadedContacts = function() {
            var count = OfflineService.count() - 1 // one for "listAll"
            return count > 0 ? count : 0
        }

        ctrl.removeDownloadedContacts = function() {
            OfflineService.clearCache()
            ctrl.message = null
        }

    }])

;

