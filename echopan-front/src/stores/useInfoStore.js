import { defineStore } from 'pinia'

export const userStore = defineStore('userInfo', {
  state: () => {
    return {
      userInfo: {},
    }
  },
  getters: {
    getUserInfo() {
      return this.userInfo;
    }
  },
  actions: {
    saveUserInfo(userInfo) {
      this.userInfo = userInfo
    }
  }
})
