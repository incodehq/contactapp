angular.module('starter')

    .service('BackendService',
            ['HttpService', 'AppConfig',
            function(HttpService, AppConfig) {

        var listAllKey = 'listAll' // for localStorage

        var instanceIdOf = function(href) {
            var n = href.lastIndexOf('/');
            var result = href.substring(n + 1);
            return result;
        }

        var message = function(date) {
            return date ? "Data from " + $filter('date')(date, 'd MMM, HH:mm') :"No data available"
        }

        this.loadContactables = function(onOK, onError) {
            HttpService.get(
                listAllKey,
                "/restful/services/ContactableViewModelRepository/actions/listAll/invoke",
                function(respData) {
                    var trimmedData = respData.map(
                        function(contactable){
                            contactable.$$instanceId = instanceIdOf(contactable.$$href)
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
                    onOK(trimmedData)
                },
                function(err, respData, date, resp) {
                    onError(respData || {}, message(date))
                }
            )
        }

        this.loadContactable = function(instanceId, onOK, onError) {
            HttpService.get(
                instanceId,
                "/restful/objects/domainapp.app.rest.v1.contacts.ContactableViewModel/" + instanceId,
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
                            contactRole.contact.$$instanceId = instanceIdOf(contactRole.contact.href)
                            delete contactRole.contact.href
                            delete contactRole.contact.rel
                            delete contactRole.contact.method
                            delete contactRole.contact.type
                            contactRole.contactGroup.$$instanceId = instanceIdOf(contactRole.contactGroup.href)
                            delete contactRole.contactGroup.href
                            delete contactRole.contactGroup.rel
                            delete contactRole.contactGroup.method
                            delete contactRole.contactGroup.type
                            return contactRole
                        }
                    )
                    onOK(respData)
                },
                function(err, respData, date, resp) {
                    onError(respData || {}, message(date))
                }
            )
        }

    }])

;
