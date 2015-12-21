angular.module('starter')

    .factory('AuthInterceptor', 
    ['$rootScope', '$q', '$injector',
    function ($rootScope, $q, $injector) {
        return {
            responseError: function (response) {

                // whenever we get a 401 from the server.
                // * if this happesn during the initial login attempt (see AuthService.login) when 
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
            ['$q','$http','Base64','$rootScope', 'AppConfig',
        function($q, $http, Base64, $rootScope, AppConfig ) {
            
        var LOCAL_TOKEN_KEY = 'contactapp';
        var username = '';
        var isAuthenticated = false;
        var basicAuth;
        
        function loadUserCredentials() {
            var token = window.localStorage.getItem(LOCAL_TOKEN_KEY);
            if (token) {
                useCredentials(token);
            }
        }
        
        function storeUserCredentials(name, basicAuth) {
            var token =  name + "." + basicAuth;
            window.localStorage.setItem(LOCAL_TOKEN_KEY, name + "." + basicAuth);
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
            window.localStorage.removeItem(LOCAL_TOKEN_KEY);
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
                        }
                    )
                    .success(function() {
                        // the user/password is good, so store away in local storage, and also   
                        // configure the $http service so that all subsequent calls  use the same 'Authorization' header
                        storeUserCredentials(name, basicAuth);
                        resolve('Login success.');        
                    })
                    .error(function(){
                        reject('Login Failed.');        
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

;
