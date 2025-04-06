<template>
  <el-config-provider :locale="locale" :message="config">
    <router-view></router-view>
  </el-config-provider>
</template>

<script setup>
import { reactive } from "vue";
import zhCn from 'element-plus/dist/locale/zh-cn.mjs'
const locale = zhCn;
const config = reactive({
  max: 1,
});
</script>

<style scoped>
</style>
