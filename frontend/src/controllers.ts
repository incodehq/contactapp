/// <reference path="../typings/tsd.d.ts" />
class DashCtrl {
  constructor() { }
}

class ChatsCtrl {
  public $inject = ['Chats']
  chats: any[];
  constructor(public Chats: any) {
    this.chats = Chats.all();
  }
}

class ChatDetailCtrl {
  public $inject = ['Chats', '$stateParams']
  chat: Object;
  constructor(
    public Chats: any,
    public $stateParams: ng.ui.IStateParamsService
    ) {
    this.chat = Chats.get($stateParams["instanceId"]);
  }
}
angular.module('starter.controllers', [])
  .controller('ChatsCtrl', ChatsCtrl)
  .controller('ChatDetailCtrl', ChatDetailCtrl);