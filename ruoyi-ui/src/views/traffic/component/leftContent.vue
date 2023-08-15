<template>
  <div>
    <dv-border-box-1 style="padding: 5px"
      >
      <h2 style="margin: 20px">各区县车流量</h2>
      <dv-capsule-chart :config="config" style="width: 400px; height: 500px"
    /></dv-border-box-1>
  </div>
</template>

<script>
export default {
  data() {
    return {
      config: {
        data: [
        {
            name: "湘江新区",
            value: 179,
          },          
          {
            name: "岳麓区",
            value: 167,
          },
          {
            name: "芙蓉区",
            value: 67,
          },
          {
            name: "开福区",
            value: 123,
          },
          {
            name: "天心区",
            value: 55,
          },
          {
            name: "望城区",
            value: 98,
          },
          {
            name: "长沙县",
            value: 55,
          },
          {
            name: "浏阳市",
            value: 85,
          },
        ],
      },
    };
  },
};
</script>

<style></style>
