import { createApp } from 'vue'

import App from '@/App.vue'
import router from '@/router'
//引入cookies
import VueCookies from 'vue-cookies'
//引入element plus
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
//图标 图标在附件中
import '@/assets/icon/iconfont.css'
import '@/assets/base.scss'

//引入代码高亮
import HljsVuePlugin from '@highlightjs/vue-plugin'
import "highlight.js/styles/atom-one-light.css";
import 'highlight.js/lib/common'


import Request from '@/utils/Request';
import Message from '@/utils/Message'
import Confirm from '@/utils/Confirm'
import Verify from '@/utils/Verify'
import Utils from '@/utils/Utils'

//自定义组件
import Icon from "@/components/Icon.vue"
import Table from '@/components/Table.vue'
import Dialog from '@/components/Dialog.vue'
import NoData from '@/components/NoData.vue'
import Window from '@/components/Window.vue'
import Preview from '@/components/preview/Preview.vue'
import Navigation from '@/components/Navigation.vue'
import FolderSelect from '@/components/FolderSelect.vue'
import Avatar from '@/components/Avatar.vue'

const app = createApp(App)
app.use(ElementPlus);
app.use(HljsVuePlugin);
app.use(router)

app.component("Icon", Icon);
app.component("Table", Table);
app.component("Dialog", Dialog);
app.component("NoData", NoData);
app.component("Window", Window);
app.component("Preview", Preview);
app.component("Navigation", Navigation);
app.component("FolderSelect", FolderSelect);
app.component("Avatar", Avatar);


//配置全局变量
app.config.globalProperties.Request = Request;
app.config.globalProperties.Message = Message;
app.config.globalProperties.Confirm = Confirm;
app.config.globalProperties.Verify = Verify;
app.config.globalProperties.Utils = Utils;

app.config.globalProperties.VueCookies = VueCookies;
app.config.globalProperties.globalInfo = {
    avatarUrl: "/api/getAvatar/",
    imageUrl: "/api/file/getImage/"
}
app.mount('#app')
