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

    .service('Base64', function () {
        /* jshint ignore:start */
    
        var keyStr = 'ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789+/=';
    
        return {
            encode: function (input) {
                var output = "";
                var chr1, chr2, chr3 = "";
                var enc1, enc2, enc3, enc4 = "";
                var i = 0;
    
                do {
                    chr1 = input.charCodeAt(i++);
                    chr2 = input.charCodeAt(i++);
                    chr3 = input.charCodeAt(i++);
    
                    enc1 = chr1 >> 2;
                    enc2 = ((chr1 & 3) << 4) | (chr2 >> 4);
                    enc3 = ((chr2 & 15) << 2) | (chr3 >> 6);
                    enc4 = chr3 & 63;
    
                    if (isNaN(chr2)) {
                        enc3 = enc4 = 64;
                    } else if (isNaN(chr3)) {
                        enc4 = 64;
                    }
    
                    output = output +
                        keyStr.charAt(enc1) +
                        keyStr.charAt(enc2) +
                        keyStr.charAt(enc3) +
                        keyStr.charAt(enc4);
                    chr1 = chr2 = chr3 = "";
                    enc1 = enc2 = enc3 = enc4 = "";
                } while (i < input.length);
    
                return output;
            },
    
            decode: function (input) {
                var output = "";
                var chr1, chr2, chr3 = "";
                var enc1, enc2, enc3, enc4 = "";
                var i = 0;
    
                // remove all characters that are not A-Z, a-z, 0-9, +, /, or =
                var base64test = /[^A-Za-z0-9\+\/\=]/g;
                if (base64test.exec(input)) {
                    window.alert("There were invalid base64 characters in the input text.\n" +
                        "Valid base64 characters are A-Z, a-z, 0-9, '+', '/',and '='\n" +
                        "Expect errors in decoding.");
                }
                input = input.replace(/[^A-Za-z0-9\+\/\=]/g, "");
    
                do {
                    enc1 = keyStr.indexOf(input.charAt(i++));
                    enc2 = keyStr.indexOf(input.charAt(i++));
                    enc3 = keyStr.indexOf(input.charAt(i++));
                    enc4 = keyStr.indexOf(input.charAt(i++));
    
                    chr1 = (enc1 << 2) | (enc2 >> 4);
                    chr2 = ((enc2 & 15) << 4) | (enc3 >> 2);
                    chr3 = ((enc3 & 3) << 6) | enc4;
    
                    output = output + String.fromCharCode(chr1);
    
                    if (enc3 != 64) {
                        output = output + String.fromCharCode(chr2);
                    }
                    if (enc4 != 64) {
                        output = output + String.fromCharCode(chr3);
                    }
    
                    chr1 = chr2 = chr3 = "";
                    enc1 = enc2 = enc3 = enc4 = "";
    
                } while (i < input.length);
    
                return output;
            }
        };
    
        /* jshint ignore:end */
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
