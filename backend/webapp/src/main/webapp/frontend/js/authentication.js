angular.module('starter')

    .constant('AUTH_EVENTS', {
        notAuthenticated: 'auth-not-authenticated'
    })
 
    .factory('AuthInterceptor', 
    ['$rootScope', '$q', '$injector', 'AUTH_EVENTS',
    function ($rootScope, $q, $injector, AUTH_EVENTS) {
        return {
            responseError: function (response) {
                if(response.status === 401) {
                    $rootScope.$broadcast(AUTH_EVENTS.notAuthenticated. response)   
                }
                    
                /*
                // redirect back to login page if not coming *from* the login page
                // not working reliably...
                var $state = $injector.get("$state");
                var AuthService = $injector.get("AuthService");
                if($state.current.name !== "login") {
                     AuthService.logout();
                    $state.go('login', {}, {reload: true});
                }
                */
                                        
                return $q.reject(response);
            }
        };
    }])
 
    .config(function ($httpProvider) {
        $httpProvider.interceptors.push('AuthInterceptor');
    })

    .service('AuthService', 
            ['$q','$http','Base64','$rootScope' /*,'$cookieStore'*/, 
        function($q, $http, Base64, $rootScope /*,$cookieStore*/ ) {
            
        var LOCAL_TOKEN_KEY = 'yourTokenKey';
        var username = '';
        var isAuthenticated = false;
        var role = '';
        var authToken;
        
        function loadUserCredentials() {
            var token = window.localStorage.getItem(LOCAL_TOKEN_KEY);
            if (token) {
            useCredentials(token);
            }
        }
        
        function storeUserCredentials(token) {
            window.localStorage.setItem(LOCAL_TOKEN_KEY, token);
            useCredentials(token);
        }
        
        function useCredentials(token) {
            username = token.split('.')[0];
            isAuthenticated = true;
            authToken = token;
        
            // Set the token as header for your requests!
            $http.defaults.headers.common['X-Auth-Token'] = token;
        }
        
        function destroyUserCredentials() {
            authToken = undefined;
            username = '';
            isAuthenticated = false;
            $http.defaults.headers.common['X-Auth-Token'] = undefined;
            window.localStorage.removeItem(LOCAL_TOKEN_KEY);
        }
        
        var login = function(name, pw) {
            return $q(function(resolve, reject) {
                
                var basicAuth = Base64.encode(name + ":" + pw);
                $http.get("/restful/user",
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
                        storeUserCredentials(name + '.yourServerToken');
                        
                        $rootScope.globals = {
                            currentUser: {
                                username: name,
                                authdata: basicAuth
                            }
                        };
            
                        $http.defaults.headers.common['Authorization'] = 'Basic ' + basicAuth;
                        //$cookieStore.put('globals', $rootScope.globals);
                        
                        resolve('Login success.');        
                    })
                    .error(function(){
                        reject('Login Failed.');        
                    });
            });
        };
        
        var logout = function() {
            destroyUserCredentials();
            $rootScope.globals = {};
            //$cookieStore.remove('globals');
            $http.defaults.headers.common.Authorization = 'Basic ';
        };
        
        var isAuthorized = function(authorizedRoles) {
            if (!angular.isArray(authorizedRoles)) {
            authorizedRoles = [authorizedRoles];
            }
            return (isAuthenticated && authorizedRoles.indexOf(role) !== -1);
        };
        
        loadUserCredentials();
        
        return {
            login: login,
            logout: logout,
            isAuthorized: isAuthorized,
            isAuthenticated: function() {return isAuthenticated;},
            username: function() {return username;},
            role: function() {return role;}
        };

    }])

;
