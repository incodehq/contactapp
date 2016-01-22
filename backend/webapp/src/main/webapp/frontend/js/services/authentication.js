angular.module(
    'ecp-contactapp.services.authentication', [])

    .factory('AuthInterceptor', 
    ['$rootScope', '$q', '$injector',
    function ($rootScope, $q, $injector) {
        return {
            responseError: function (response) {

                // whenever we get a 401 from the server.
                // * if this happens during the initial login attempt (see AuthService.login) when
                //   we are testing the provided username+password against /restful/user, then we just ignore
                // * if this happens otherwise then it means that we've lost the session, should go back to the login page
                if(response.status === 401) {
                    
                    var $state = $injector.get("$state");
                    var AuthService = $injector.get("AuthService");

                    if($state.current.name !== "login") {
                        AuthService.logout();
                        $state.go('login', {}, {reload: true});
                    }
                }
                    
                return $q.reject(response);
            }
        };
    }])
 
    // install our interceptor for any $http requests 
    .config(function ($httpProvider) {
        $httpProvider.interceptors.push('AuthInterceptor');
    })

    .service('AuthService', 
            ['$q', 'HttpService', '$http', 'Base64', '$rootScope', 'AppConfig',
        function($q, HttpService, $http, Base64, $rootScope, AppConfig ) {
            
        var LOCAL_TOKEN_KEY = AppConfig.appPrefix + ".authToken"
        var username = '';
        var isAuthenticated = false;
        var basicAuth;
        
        function loadUserCredentials() {
            var token = readUserCredentials();
            if (token) {
                useCredentials(token);
            }
        }
        
        function readUserCredentials() {
            return window.localStorage[LOCAL_TOKEN_KEY];
        }

        function storeUserCredentials(name, basicAuth) {
            var token =  name + "." + basicAuth;
            window.localStorage[LOCAL_TOKEN_KEY] = name + "." + basicAuth;
            useCredentials(token);
        }
        
        function useCredentials(token) {
            username = token.split('.')[0];
            basicAuth = token.split('.')[1];
            isAuthenticated = true;
        
            $http.defaults.headers.common['Authorization'] = 'Basic ' + basicAuth;
        }
        
        function destroyUserCredentials() {
            username = '';
            basicAuth = undefined;
            isAuthenticated = false;

            $http.defaults.headers.common.Authorization = 'Basic ';

            // we no longer do this, to allow for off-line access
            // window.localStorage.removeItem(LOCAL_TOKEN_KEY);
        }
        
        var login = function(name, pw) {
            return $q(function(resolve, reject) {
                
                // attempt to access a resource (we happen to use /restful/user) 
                // using the provided name and password
                var basicAuth = Base64.encode(name + ":" + pw);
                $http.get(AppConfig.baseUrl + "/restful/user",
                        {
                            headers: { 
                                'Authorization': 'Basic ' + basicAuth, 
                                'Cache-Control': 'no-cache',
                                'Pragma': 'no-cache',
                                'If-Modified-Since': 'Mon, 26 Jul 1997 05:00:00 GMT' // a long time ago
                            }
                        })
                    .then(
                        function() {
                            // the user/password is good, so store away in local storage, and also
                            // configure the $http service so that all subsequent calls  use the same 'Authorization' header
                            storeUserCredentials(name, basicAuth);
                            resolve('Login success.');
                        },
                        function(x) {
                            var storedCredentials = readUserCredentials(name);
                            var enteredCredentials = name + "." + basicAuth;
                            if(x.status === 0 && storedCredentials === enteredCredentials) {
                                resolve('Offline access.');
                            } else {
                                reject('Login Failed.');
                            }
                        });
            });
        };
        
        var logout = function() {
            destroyUserCredentials();
        };
        
        loadUserCredentials();
        
        return {
            login: login,
            logout: logout,
            isAuthenticated: function() {
                return isAuthenticated
            },
            username: function() {
                return username
            }
        };

    }])


    .controller('LoginCtrl',
        ['$rootScope', '$scope', '$state','$ionicPopup', 'AuthService', 'AppConfig',
        function($rootScope, $scope, $state, $ionicPopup, AuthService, AppConfig) {

        $scope.data = {}
        $scope.data.environments = [
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

;
