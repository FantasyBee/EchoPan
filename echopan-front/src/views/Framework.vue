<template>
  <div class="framework">
    <div class="header">
      <div class="logo">
        <span class="iconfont icon-pan"></span>
        <span class="name">Echo云盘</span>
      </div>
      <div class="right-panel">
        <el-popover
          :width="800"
          trigger="click"
          v-model:visible="showUploader"
          :offset="20"
          transition="none"
          :hide-after="0"
          :popper-style="{ padding: '0px' }"
        >
          <template #reference>
            <span class="iconfont icon-transfer"></span>
          </template>
          <template #default>
            <Uploader
              ref="uploaderRef"
              @uploadCallback="uploadCallbackHandler"
            ></Uploader>
          </template>
        </el-popover>

        <el-dropdown>
          <div class="user-info">
            <div class="avatar">
              <Avatar
                :userId="userInfo.userId"
                :avatar="userInfo.avatar"
                :timestamp="timestamp"
                :width="46"
              ></Avatar>
            </div>
            <span class="nick-name">{{ userInfo.nickName }}</span>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item @click="updateAvatar">
                修改头像
              </el-dropdown-item>
              <el-dropdown-item @click="updatePassword">
                修改密码
              </el-dropdown-item>
              <el-dropdown-item @click="logout"> 退出 </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
      </div>
    </div>
    <div class="body">
      <div class="left-sider">
        <div class="menu-list">
          <template v-for="item in menus">
            <div
              v-if="item.allShow || (!item.allShow && userInfo.admin)"
              @click="jump(item)"
              :class="[
                'menu-item',
                item.menuCode == currentMenu.menuCode ? 'active' : '',
              ]"
            >
              <div :class="['iconfont', 'icon-' + item.icon]"></div>
              <div class="text">
                {{ item.name }}
              </div>
            </div>
          </template>
        </div>
        <div class="menu-sub-list">
          <div
            @click="jump(sub)"
            :class="['menu-item-sub', currentPath == sub.path ? 'active' : '']"
            v-for="sub in currentMenu.children"
          >
            <span
              :class="['iconfont', 'icon-' + sub.icon]"
              v-if="sub.icon"
            ></span>
            <span class="text">{{ sub.name }}</span>
          </div>
          <div class="tips" v-if="currentMenu && currentMenu.tips">
            {{ currentMenu.tips }}
          </div>
          <div class="space-info">
            <div>空间使用</div>
            <div class="percent">
              <el-progress
                :percentage="
                  Math.floor(
                    (useSpaceInfo.useSpace / useSpaceInfo.totalSpace) * 10000
                  ) / 100
                "
                color="#409eff"
              />
            </div>

            <div class="space-use">
              <div class="use">
                {{ proxy.Utils.size2Str(useSpaceInfo.useSpace) }}/
                {{ proxy.Utils.size2Str(useSpaceInfo.totalSpace) }}
              </div>
              <div class="iconfont icon-refresh" @click="getUseSpace"></div>
            </div>
          </div>
        </div>
      </div>
      <div class="body-content">
        <router-view v-slot="{ Component }">
          <component
            @addFile="addFile"
            ref="routerViewRef"
            :is="Component"
            @reload="getUseSpace"
          />
        </router-view>
      </div>
    </div>
    <!--修改头像-->
    <UpdateAvatar
      ref="updateAvatarRef"
      @updateAvatar="reloadAvatar"
    ></UpdateAvatar>
    <!--修改密码-->
    <UpdatePassword ref="updatePasswordRef"></UpdatePassword>
  </div>
</template>

<script setup>
import UpdateAvatar from "./UpdateAvatar.vue";
import UpdatePassword from "./UpdatePassword.vue";
import Uploader from "@/views/main/Uploader.vue";
import {
  ref,
  reactive,
  getCurrentInstance,
  watch,
  nextTick,
  computed,
} from "vue";
import { useRouter, useRoute } from "vue-router";
const { proxy } = getCurrentInstance();
const router = useRouter();
const route = useRoute();

const api = {
  getUseSpace: "/getUseSpace",
  logout: "/logout",
};

//显示上传窗口
const showUploader = ref(false);

//添加文件
const uploaderRef = ref();
const addFile = (data) => {
  const { file, filePid } = data;
  showUploader.value = true;
  uploaderRef.value.addFile(file, filePid);
};

//上传文件回调
const routerViewRef = ref();
const uploadCallbackHandler = () => {
  nextTick(() => {
    routerViewRef.value.reload();
    getUseSpace();
  });
};

const timestamp = ref(0);
//获取用户信息
const userInfo = ref(proxy.VueCookies.get("userInfo"));
const menus = [
  {
    icon: "cloude",
    name: "首页",
    menuCode: "main",
    path: "/main/all",
    allShow: true,
    children: [
      {
        icon: "all",
        name: "全部",
        category: "all",
        path: "/main/all",
      },
      {
        icon: "video",
        name: "视频",
        category: "video",
        path: "/main/video",
      },
      {
        icon: "music",
        name: "音频",
        category: "music",
        path: "/main/music",
      },
      {
        icon: "image",
        name: "图片",
        category: "image",
        path: "/main/image",
      },
      {
        icon: "doc",
        name: "文档",
        category: "doc",
        path: "/main/doc",
      },
      {
        icon: "more",
        name: "其他",
        category: "others",
        path: "/main/others",
      },
    ],
  },
  {
    path: "/myshare",
    icon: "share",
    name: "分享",
    menuCode: "share",
    allShow: true,
    children: [
      {
        name: "分享记录",
        path: "/myshare",
      },
    ],
  },
  {
    path: "/recycle",
    icon: "del",
    name: "回收站",
    menuCode: "recycle",
    tips: "回收站为你保存10天内删除的文件",
    allShow: true,
    children: [
      {
        name: "删除的文件",
        path: "/recycle",
      },
    ],
  },
  {
    path: "/settings/fileList",
    icon: "settings",
    name: "设置",
    menuCode: "settings",
    allShow: false,
    children: [
      {
        name: "用户文件",
        path: "/settings/fileList",
      },
      {
        name: "用户管理",
        path: "/settings/userList",
      },
      {
        path: "/settings/sysSetting",
        name: "系统设置",
      },
    ],
  },
];

const currentMenu = ref({});
const currentPath = ref();

const jump = (data) => {
  if (!data.path || data.menuCode == currentMenu.value.menuCode) {
    return;
  }
  router.push(data.path);
};

const setMenu = (menuCode, path) => {
  const menu = menus.find((item) => {
    return item.menuCode === menuCode;
  });
  currentMenu.value = menu;
  currentPath.value = path;
};

watch(
  () => route,
  (newVal, oldVal) => {
    if (newVal.meta.menuCode) {
      setMenu(newVal.meta.menuCode, newVal.path);
    }
  },
  { immediate: true, deep: true }
);

//修改头像
const updateAvatarRef = ref();
const updateAvatar = () => {
  updateAvatarRef.value.show(userInfo.value);
};
const reloadAvatar = () => {
  userInfo.value = proxy.VueCookies.get("userInfo");
  timestamp.value = new Date().getTime();
};

//修改密码
const updatePasswordRef = ref();
const updatePassword = () => {
  updatePasswordRef.value.show();
};

//退出登录
const logout = () => {
  proxy.Confirm(`你确定要删除退出吗`, async () => {
    let result = await proxy.Request({
      url: api.logout,
    });
    if (!result) {
      return;
    }
    proxy.VueCookies.remove("userInfo");
    router.push("/login");
  });
};

//使用空间
const useSpaceInfo = ref({ useSpace: 0, totalSpace: 1 });
const getUseSpace = async () => {
  let result = await proxy.Request({
    url: api.getUseSpace,
    showLoading: false,
  });
  if (!result) {
    return;
  }
  useSpaceInfo.value = result.data;
};
getUseSpace();
</script>

<style lang="scss" scoped>
.header {
  box-shadow: 0 3px 10px 0 rgb(0 0 0 / 6%);
  height: 56px;
  padding-left: 24px;
  padding-right: 24px;
  position: relative;
  z-index: 200;
  display: flex;
  align-items: center;
  justify-content: space-between;

  .logo {
    display: flex;
    align-items: center;
    .icon-pan {
      font-size: 40px;
      color: #1296db;
    }
    .name {
      font-weight: bold;
      margin-left: 5px;
      font-size: 25px;
      color: #05a1f5;
    }
  }
  .right-panel {
    display: flex;
    align-items: center;
    .icon-transfer {
      cursor: pointer;
    }
    .user-info {
      margin-right: 10px;
      display: flex;
      align-items: center;
      cursor: pointer;
      .avatar {
        margin: 0px 5px 0px 15px;
      }
      .nick-name {
        color: #05a1f5;
      }
    }
  }
}
.body {
  display: flex;
  .left-sider {
    border-right: 1px solid #f1f2f4;
    display: flex;
    .menu-list {
      height: calc(100vh - 56px);
      width: 80px;
      box-shadow: 0 3px 10px 0 rgb(0 0 0 / 6%);
      border-right: 1px solid #f1f2f4;
      .menu-item {
        text-align: center;
        font-size: 14px;
        font-weight: bold;
        padding: 20px 0px;
        cursor: pointer;
        &:hover {
          background: #f3f3f3;
        }
        .iconfont {
          font-weight: normal;
          font-size: 28px;
        }
      }
      .active {
        .iconfont {
          color: #06a7ff;
        }
        .text {
          color: #06a7ff;
        }
      }
    }
    .menu-sub-list {
      width: 200px;
      padding: 20px 10px 0px;
      position: relative;
      .menu-item-sub {
        text-align: center;
        line-height: 40px;
        border-radius: 5px;
        cursor: pointer;
        &:hover {
          background: #f3f3f3;
        }
        .iconfont {
          font-size: 14px;
          margin-right: 20px;
        }
        .text {
          font-size: 13px;
        }
      }
      .active {
        background: #eef9fe;
        .iconfont {
          color: #05a1f5;
        }
        .text {
          color: #05a1f5;
        }
      }

      .tips {
        margin-top: 10px;
        color: #888888;
        font-size: 13px;
      }

      .space-info {
        position: absolute;
        bottom: 10px;
        width: 100%;
        padding: 0px 5px;
        .percent {
          padding-right: 10px;
        }
        .space-use {
          margin-top: 5px;
          color: #7e7e7e;
          display: flex;
          justify-content: space-around;
          .use {
            flex: 1;
          }
          .iconfont {
            cursor: pointer;
            margin-right: 20px;
            color: #05a1f5;
          }
        }
      }
    }
  }
  .body-content {
    flex: 1;
    width: 0;
    padding-left: 20px;
  }
}
</style>