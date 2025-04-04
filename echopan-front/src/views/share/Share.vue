<template>
  <div>
    <div class="top">
      <el-button
        type="primary"
        :disabled="selectIdList.length == 0"
        @click="cancelShareBatch"
      >
        <span class="iconfont icon-cancel"></span>取消分享
      </el-button>
    </div>
    <div class="file-list">
      <Table
        :columns="columns"
        :showPagination="true"
        :dataSource="tableData"
        :fetch="loadDataList"
        :options="tableOptions"
        @rowSelected="rowSelected"
      >
        <template #fileName="{ row }">
          <div
            class="file-item"
            @mouseenter="showOp(row)"
            @mouseleave="cancelShowOp(row)"
          >
            <template
              v-if="
                (row.fileType == 3 || row.fileType == 1) && row.status !== 0
              "
            >
              <icon :cover="row.fileCover"></icon>
            </template>
            <template v-else>
              <icon v-if="row.folderType == 0" :fileType="row.fileType"></icon>
              <icon v-if="row.folderType == 1" :fileType="0"></icon>
            </template>
            <span
              class="file-name"
              v-if="!row.showRename"
              :title="row.fileName"
            >
              <span>{{ row.fileName }}</span>
            </span>
            <span class="op">
              <template v-if="row.showOp && row.fileId">
                <span class="iconfont icon-link" @click="copy(row)"
                  >复制链接</span
                >
                <span class="iconfont icon-cancel" @click="cancelShare(row)"
                  >取消分享</span
                >
              </template>
            </span>
          </div>
        </template>
        <template #expireTime="{ row }">
          {{ row.validType == 3 ? "永久" : row.expireTime }}
        </template>
      </Table>
    </div>
  </div>
</template>

<script setup>
import useClipboard from "vue-clipboard3";
const { toClipboard } = useClipboard();
import { ref, getCurrentInstance } from "vue";
const { proxy } = getCurrentInstance();

const api = {
  loadDataList: "/share/loadShareList",
  cancelShare: "/share/cancelShare",
};

const shareUrl = ref(document.location.origin + "/share/");

//列表
const columns = [
  {
    label: "文件名",
    prop: "fileName",
    scopedSlots: "fileName",
  },
  {
    label: "分享时间",
    prop: "shareTime",
    width: 200,
  },
  {
    label: "失效时间",
    prop: "expireTime",
    scopedSlots: "expireTime",
    width: 200,
  },
  {
    label: "浏览次数",
    prop: "showCount",
    width: 200,
  },
];
//搜索
//列表
const tableData = ref({});
const tableOptions = {
  extHeight: 20,
  selectType: "checkbox",
};

const loadDataList = async () => {
  let params = {
    pageNo: tableData.value.pageNo,
    pageSize: tableData.value.pageSize,
  };
  if (params.category !== "all") {
    delete params.filePid;
  }
  let result = await proxy.Request({
    url: api.loadDataList,
    params,
  });
  if (!result) {
    return;
  }
  tableData.value = result.data;
};

//展示操作按钮
const showOp = (row) => {
  tableData.value.list.forEach((element) => {
    element.showOp = false;
  });
  row.showOp = true;
};

const cancelShowOp = (row) => {
  row.showOp = false;
};

//复制链接
const copy = async (data) => {
  await toClipboard(
    `链接:${shareUrl.value}${data.shareId} 提取码: ${data.code}`
  );
  proxy.Message.success("复制成功");
};

//多选 批量选择
const selectIdList = ref([]);
const rowSelected = (rows) => {
  selectIdList.value = [];
  rows.forEach((item) => {
    selectIdList.value.push(item.shareId);
  });
};

//取消分享
const cancelShareIdList = ref([]);
const cancelShareBatch = () => {
  if (selectIdList.value.length == 0) {
    return;
  }
  cancelShareIdList.value = selectIdList.value;
  cancelShareDone();
};

const cancelShare = (row) => {
  cancelShareIdList.value = [row.shareId];
  cancelShareDone();
};

const cancelShareDone = async () => {
  proxy.Confirm(`你确定要取消分享吗？`, async () => {
    let result = await proxy.Request({
      url: api.cancelShare,
      params: {
        shareIds: cancelShareIdList.value.join(","),
      },
    });
    if (!result) {
      return;
    }
    proxy.Message.success("取消分享成功");
    loadDataList();
  });
};
</script>

<style lang="scss" scoped>
@import "@/assets/file.list.scss";
.file-list {
  margin-top: 10px;
  .file-item {
    .file-name {
      span {
        &:hover {
          color: #494944;
        }
      }
    }
    .op {
      width: 170px;
    }
  }
}
</style>