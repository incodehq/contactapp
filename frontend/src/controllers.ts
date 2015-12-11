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
  public remove(chat) {
    this.Chats.remove(chat)
  }
}

class ChatDetailCtrl {
  public $inject = ['Chats', '$stateParams']
  chat: Object;
  constructor(
    public Chats: any,
    public $stateParams: ng.ui.IStateParamsService
    ) {
    this.chat = Chats.get($stateParams.chatId);
  }
}
class AccountCtrl {
  settings: Object;
  constructor() {
    this.settings = {
      enableFriends: true
    }
  }
}
angular.module('starter.controllers', [])

  .controller('DashCtrl', DashCtrl)

  .controller('ChatsCtrl', ChatsCtrl)

  .controller('ChatDetailCtrl', ChatDetailCtrl)

  .controller('AccountCtrl', AccountCtrl);