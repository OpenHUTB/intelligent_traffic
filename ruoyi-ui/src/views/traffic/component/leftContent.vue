<template>
  <div class="content">
    <dv-border-box-10>
      <div style="padding: 5px">
        <dv-scroll-board :config="road" style="height: 600px" />
      </div>
    </dv-border-box-10>
  </div>
</template>

<script>
import { pageIntersectionData } from "@/api/intersectionData/intersectionData";

export default {
  data() {
    return {
      road: {
        header: ["路口", "车流量"],
        data: [
          ["渝北区", "——"],
          ["万州区", "——"],
          ["巴南区", "O3"],
          ["江北区", "PM2.5"],
          ["渝北区", "——"],
          ["万州区", "——"],
          ["巴南区", "O3"],
          ["江北区", "PM2.5"],
        ],
        align: ["left"],
        columnWidth: ["300"],
      },
    };
  },

  created() {
    this.getList();
  },

  watch: {
    road: {
      immediate: true,
      deep: true,
      handler() {
        console.log("isHot被修改了");
      },
    },
  },

  methods: {
    getList() {
      const params = { evaluationTypeId: 13 };
      pageIntersectionData(params)
        .then((response) => {
          if (response.code === 200) {
            // console.log(response);
            const { rows } = response;
            let roleList = [];
            // roleList.data = rows.map((item) => ({
            //   name: item.intersectionName,
            //   value: item.value,
            // }));
            rows.forEach((item) => {
                let list = [item.intersectionName, item.value]
                roleList.push(list)
            })
            // console.log(roleList);
            console.log(roleList.data);
            this.road.data = roleList;

            this.road = { ...this.road };
          } else {
            console.error("获取路口车流量数据失败");
          }
        })
        .catch((error) => {
          console.error("获取路口车流量数据失败:", error);
        });
    },
  },
};
</script>

<style>
.content {
  width: 30vw;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}
h2 {
  margin: 10px;
}
</style>
