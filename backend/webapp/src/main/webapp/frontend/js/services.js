/// <reference path="../typings/tsd.d.ts" />
angular.module('starter.services', [])
    .factory('Chats', function () {
    // Might use a resource here that returns a JSON array
    var chats = [{
            "$$href": "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/1",
            "$$title": "Bill Smith",
            "$$instanceId": "1",
            "company": "ACME",
            "email": "bill.smith@acmecompany.com",
            "name": "Bill Smith",
            "notes": null
        }, {
            "$$href": "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/2",
            "$$title": "Bob Mills",
            "$$instanceId": "2",
            "company": "ACME",
            "email": "bob.mills@acmecompany.com",
            "name": "Bob Mills",
            "notes": null
        }, {
            "$$href": "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/3",
            "$$title": "Mike Jones",
            "$$instanceId": "3",
            "company": "ACME",
            "email": "mike.jones@acmecompany.com",
            "name": "Mike Jones",
            "notes": null
        }, {
            "$$href": "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/5",
            "$$title": "Anne van Hope",
            "$$instanceId": "5",
            "company": "EuroCope Amsterdam",
            "email": "annvanhope@eurocope.com",
            "name": "Anne van Hope",
            "notes": null
        }, {
            "$$href": "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/6",
            "$$title": "Leo Gardener",
            "$$instanceId": "6",
            "company": null,
            "email": "leo.gardener@kramer-kramer.com",
            "name": "Leo Gardener",
            "notes": null
        }, {
            "$$href": "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/7",
            "$$title": "David Goodhew",
            "$$instanceId": "7",
            "company": null,
            "email": "d.goodnew@abcdef.nl",
            "name": "David Goodhew",
            "notes": null
        }, {
            "$$href": "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/9",
            "$$title": "Clarisse Bentz",
            "$$instanceId": "9",
            "company": "ACME",
            "email": "clarisse.bentz@acmecompany.fr",
            "name": "Clarisse Bentz",
            "notes": null
        }, {
            "$$href": "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/10",
            "$$title": "Zahra Martinelli",
            "$$instanceId": "10",
            "company": "ACME",
            "email": "zahra.martinelli@acmecompany.fr",
            "name": "Zahra Martinelli",
            "notes": null
        }, {
            "$$href": "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/12",
            "$$title": "Damien Grandjean",
            "$$instanceId": "12",
            "company": "ACME",
            "email": "damien.grandjean@acmecompany.fr",
            "name": "Damien Grandjean",
            "notes": null
        }, {
            "$$href": "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/13",
            "$$title": "Guillaume Maxine",
            "$$instanceId": "13",
            "company": "Noweta Property",
            "email": "gmaxine@noweta-property.fr",
            "name": "Guillaume Maxine",
            "notes": null
        }, {
            "$$href": "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/14",
            "$$title": "BenoÃ®t FourÃ©",
            "$$instanceId": "14",
            "company": "Cumba",
            "email": "benoit.foure.80@gmail.com",
            "name": "BenoÃ®t FourÃ©",
            "notes": null
        }, {
            "$$href": "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/15",
            "$$title": "Security Post",
            "$$instanceId": "15",
            "company": "Security-R-Us",
            "email": "benoit.foure.80@gmail.com",
            "name": "Security Post",
            "notes": null
        }, {
            "$$href": "http://localhost:8080/restful/objects/domainapp.dom.contacts.Contact/16",
            "$$title": "Brigitte Hollande",
            "$$instanceId": "16",
            "company": "Amiens town Hall",
            "email": "orientation.accueil@amiens-metropolis.fr",
            "name": "Brigitte Hollande",
            "notes": "Fax : +33 3 44 55 66 77"
        }];
    return {
        all: function () {
            return chats;
        },
        get: function ($$instanceId) {
            for (var i = 0; i < chats.length; i++) {
                if (chats[i]['$$instanceId'] === $$instanceId) {
                    return chats[i];
                }
            }
            return null;
        }
    };
});
