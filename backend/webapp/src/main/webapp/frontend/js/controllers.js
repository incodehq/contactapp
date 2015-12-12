/// <reference path="../typings/tsd.d.ts" />
var DashCtrl = (function () {
    function DashCtrl() {
    }
    return DashCtrl;
})();
var ChatsCtrl = (function () {
    function ChatsCtrl(Chats) {
        this.Chats = Chats;
        this.$inject = ['Chats'];
        this.chats = Chats.all();
    }
    return ChatsCtrl;
})();
var ChatDetailCtrl = (function () {
    function ChatDetailCtrl(Chats, $stateParams) {
        this.Chats = Chats;
        this.$stateParams = $stateParams;
        this.$inject = ['Chats', '$stateParams'];
        this.chat = Chats.get($stateParams["instanceId"]);
    }
    return ChatDetailCtrl;
})();
angular.module('starter.controllers', [])
    .controller('ChatsCtrl', ChatsCtrl)
    .controller('ChatDetailCtrl', ChatDetailCtrl);
