<template>
    <div>
        <dv-border-box-1 style="padding: 5px">
            <h2 style="margin: 20px">路口饱和度详细信息展示</h2>
            <!-- <h2 v-text="config.roleList"></h2> -->
            <dv-capsule-chart :config="config" style="width: 400px; height: 500px" />
        </dv-border-box-1>
    </div>
</template>

<script>
import { pageIntersectionData } from "@/api/intersectionData/intersectionData";

export default {
    data() {
        return {
            config: {
                // roleList: [
                //     {
                //       name:'',
                //       value:''
                //     }
                // ], // 角色表格数据
            }
        };
    },

    created() {
        this.getList();
    },

    
    methods: {
        getList() {
            const params = { evaluationTypeId: 3 };
            pageIntersectionData(params)
                .then((response) => {
                    // console.log("2")
                    if (response.code === 200) {
                        console.log(response)
                        const { rows } = response;
                        // console.log(rows)
                        let roleList = {}
                        roleList.data = rows.map((item) => ({
                            name: item.intersectionName,
                            value: item.value,
                        }));
                        this.config = roleList
                        // console.log(this.config.roleList)
                        // this.config = { ...this.config }
                        //  console.log(this.config)
                        // console.log(this)
                    } else {
                        console.error("获取路口饱和度数据失败");
                    }
                })
                .catch((error) => {
                    console.error("获取路口饱和度数据失败:", error);
                });
        },
    },
};
</script>

<style>
</style>
