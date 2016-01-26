angular.module(
    'ecp-contactapp.controllers.about', [])

    .controller('AboutCtrl',
        ['$scope', 'AuthService',
        function($scope, AuthService) {

        var ctrl = this;

        ctrl.username = AuthService.username();
        ctrl.logout = function() {
            AuthService.logout();
            $state.go('login', {}, {reload: true});
        }


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

    }])

;

