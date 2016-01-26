angular.module(
    'ecp-contactapp.services.backend', [])

    .service(
        'BackendService',
        ['PreferencesService', 'HttpService', 'AppConfig', '$filter',
        function(PreferencesService, HttpService, AppConfig, $filter) {

        var listAllKey = 'listAll' // for localStorage

        var instanceIdOf = function(href) {
            var n = href.lastIndexOf('/');
            var result = href.substring(n + 1);
            return result;
        }

        var dataProvenanceMessage = function(date) {
            return date ? "Data from " + $filter('date')(date, 'd MMM, HH:mm:ss') :"No data available"
        }

        this.loadContactables = function(onComplete, options) {

            var sort = function(respData) {
                respData.sort(function(a,b) {
                    if(PreferencesService.preferences.nameOrder.selected === "first-last") {
                        return a.name.localeCompare(b.name)
                    } else {
                        return a.lastName.localeCompare(b.lastName)
                    }
                })
                return respData
            }

            HttpService.get(
                listAllKey,
                "/restful/services/ContactableViewModelRepository/actions/listAll/invoke",
                function(cachedData, date) {
                    onComplete(sort(cachedData), dataProvenanceMessage(date))
                },
                function(respData, date) {
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
                    onComplete(sort(trimmedData), dataProvenanceMessage(date))
                },
                function(err) {
                    onComplete([], dataProvenanceMessage(null))
                },
                options
            )
        }

        this.loadContactable = function(instanceId, onComplete, options) {
            HttpService.get(
                instanceId,
                "/restful/objects/domainapp.app.rest.v1.contacts.ContactableViewModel/" + instanceId,
                function(cachedData, date) {
                    onComplete(cachedData, dataProvenanceMessage(date))
                },
                function(respData, date) {
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
                    onComplete(respData, dataProvenanceMessage(date))
                },
                function(err, respData, date, resp) {
                    onComplete({}, dataProvenanceMessage(null))
                },
                options
            )
        }

        this.isCached = function(cacheKey) {
            return HttpService.isCached(cacheKey)
        }


    }])

;
