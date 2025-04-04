<template>
  <div class="others">
    <div class="body-content">
      <div>
        <icon
          :iconName="fileInfo.fileType == 9 ? 'zip' : 'others'"
          :width="80"
        ></icon>
      </div>
      <div class="file-name">{{ fileInfo.fileName }}</div>
      <div class="tips">该类型的文件暂不支持预览，请下载后再查看哦~</div>
      <div class="download-btn">
        <el-button type="primary" @click="download"
          >点击下载 {{ proxy.Utils.size2Str(fileInfo.fileSize) }}</el-button
        >
      </div>
    </div>
  </div>
</template>

<script setup>
import { getCurrentInstance } from "vue";
const { proxy } = getCurrentInstance();

const props = defineProps({
  createDownloadUrl: {
    type: String,
  },
  downloadUrl: {
    type: String,
  },
  fileInfo: {
    type: Object,
  },
});

//下载文件
const download = async () => {
  let result = await proxy.Request({
    url: props.createDownloadUrl,
  });
  if (!result) {
    return;
  }
  window.location.href = props.downloadUrl + "/" + result.data;
};
</script>

<style lang="scss" scoped>
.others {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 100%;
  .body-content {
    text-align: center;
    .file-name {
      font-weight: bold;
    }
    .tips {
      color: #999898;
      margin-top: 5px;
      font-size: 13px;
    }
    .download-btn {
      margin-top: 20px;
    }
  }
}
</style>