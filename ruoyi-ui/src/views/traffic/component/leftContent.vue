<template>
  <div>
    <dv-border-box-1 style="padding: 5px">
      <div class="colum_center">
        <h2 style="margin: 20px">路口车流量详细信息展示</h2>
        <dv-capsule-chart
          :config="config"
          style="width: 400px; height: 500px"
        />
      </div>
    </dv-border-box-1>
  </div>
</template>

<script>
import { pageIntersectionData } from "@/api/intersectionData/intersectionData";

export default {
  props: {
    message: {
      type: String,
      default: "0", // 默认值为0
    },
  },

  data() {
    return {
      config: {},
      processedMessage: this.message,
    };
  },

  watch: {
    message() {
      this.callbackFunction();
    },
  },

  created() {
    this.getList();
    // setInterval(this.getList, 3000); // 每5秒刷新一次数据
  },

  methods: {
    getList() {
      const params = { evaluationTypeId: 13 };
      pageIntersectionData(params)
        .then((response) => {
          if (response.code === 200) {
            // console.log(response);
            const { rows } = response;
            let roleList = {};
            let startIndex = Math.floor(Math.random() * rows.length); // 生成随机的起始索引值
            let randomRows = rows.slice(startIndex, startIndex + 6); // 随机截取6条记录
            roleList.data = randomRows.map((item) => ({
              name: item.intersectionName,
              value: item.value,
            }));
            this.config = roleList;
          } else {
            console.error("获取交通流量数据失败");
          }
        })
        .catch((error) => {
          console.error("获取交通流量数据失败:", error);
        });
    },

    callbackFunction() {
      this.getList();
    },
  },
};
</script>

<style lang="scss" scoped>
.colum_center {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
</style>
