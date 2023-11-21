<template>
    <div class="start_main">
        <div class="start_header">
            <h1>任务看板 ></h1>
            <el-input v-model="search_input" placeholder="任务名称" class="project_search">
                <template #append>
                    <el-button :icon="Search" />
                </template>
            </el-input>
        </div>
        <Ajaxdiv ref="ajax_div" class="start_content" :AjaxList="ajaxlist" @onSuccess="AjaxSuccess">
            <el-scrollbar>
                <div class="scroll_content">
                    <div class="logo-p1"
                        style="display: flex; vertical-align: middle; align-items: center; justify-content: center;"
                        @click="new_task()">
                        <img src="src/assets/icon/folder.svg" alt="new project">
                        <h1 class="new_project">任务新建</h1>
                    </div>
                    <div v-for="(item, index) in projectList" class="logo-p1" @click="skip(item.code!)"
                        v-loading="active_index == index">
                        <div class="pre-contain">
                            <img src="src/assets/imgs/preview.png">
                        </div>
                        <div class="info-box">
                            <div class="name">
                                <el-icon :size="14" style="margin-right: 5px;">
                                    <FolderOpened />
                                </el-icon>
                                <span>{{ item.name }}</span>
                            </div>
                            <div class="control">
                                <el-icon :size="16" @click="editItem($event, index)">
                                    <Edit />
                                </el-icon>
                                <el-icon :size="16" @click="delProject($event, index)">
                                    <Delete />
                                </el-icon>
                            </div>

                        </div>
                        <div class="time-box">
                            <el-icon style="margin-right: 5px;">
                                <Clock />
                            </el-icon>
                            <h2>{{ formatDate(item.updateTime) }}</h2>
                        </div>
                    </div>
                </div>
            </el-scrollbar>
        </Ajaxdiv>
        <TaskNew :obj="NewObj" @created="projectCreated" />
    </div>
</template>

<script lang="ts" setup>
import { onMounted, reactive, ref, toRefs } from 'vue'
import TaskNew from '@/views/pages/Start/StartDialog/TasktNew.vue'
import Ajaxdiv from "@/components/AjaxDiv/index.vue"
import { AjaxItem } from '@/components/AjaxDiv/interface';
import { ProjectItem } from "@/apis/Project/project"
import { formatDate } from "@/utils/dateUtils"
import { Search } from '@element-plus/icons-vue';
import { useRouter } from 'vue-router';
import { useProjectsStore } from '@/store/project';
import { ElMessage } from 'element-plus';
import { storeToRefs } from 'pinia';
const projectStore = useProjectsStore();
const ajax_div = ref();
const active_index = ref(-1);
const projectList = projectStore.projects;
const ajaxlist: Array<AjaxItem> = [
    {
        name: "UserProjectList",
        fun: projectStore.getUserProjectList,
        param: 15
    }
];
function AjaxSuccess(responseData: any) {
    // projectList.unshift(...responseData.data.list as never[]);
    console.log("data", projectList)
}
const search_input = ref('');

let NewObj = reactive({
    key: "newtask",
    dialogVisibal: false,
})

function new_task() {
    NewObj.dialogVisibal = true;
}
async function delProject(event: Event, index: number) {
    event.stopImmediatePropagation();
    active_index.value = index;
    try {
        // await processDefinitionStore.queryProcessDefinitionList(projectList[index].code);
        // await processDefinitionStore.releaseProcess(ReleaseState.OFFLINE, projectList[index].code)
        // await processDefinitionStore.delProcessDefinition(projectList[index].code);
        const res = await projectStore.deleteProject(projectList[index].code as string)
        active_index.value = -1;
        ElMessage({
            message: res.msg,
            type: "success"
        });
    } catch (error:any) {
        ElMessage({
            message: error.msg,
            type: "warning"
        });
        active_index.value = -1;
    }

    // projectStore.deleteProject(projectList[index].code).then(res => {
    //     active_index.value = -1;
    //     ElMessage({
    //         message: res.msg,
    //         type: "success"
    //     });
    // }).catch(err => {
    //     ElMessage({
    //         message: err.msg,
    //         type: "warning"
    //     });
    //     active_index.value = -1;
    // });
}

function projectCreated() {
    console.log("projectCreated");
    ajax_div.value.refresh();
}

function editItem(event: Event, index: number) {
    event.stopImmediatePropagation();
}

const router = useRouter();
const skip = (projectCode: string) => {
    router.replace({
        // name:'Process',
        path:'/process',
        query:{
            projectCode:projectCode
        }
    })
}
</script>

<style lang="scss" scoped>
$width: calc(100% - 70px);

.start_main {
    box-sizing: border-box;
    height: 100%;
    background-color: rgba(240, 240, 240, 50%);
    width: 100vw;
    display: flex;
    flex-direction: column;
    align-items: center;

}

.start_content {
    height: calc(100vh - 135px);
    border-radius: 5px;
    background-color: rgba(254, 254, 254, 0.95);
    width: $width;
}

.start_header {
    padding: 10px 40px;
    height: 45px;
    width: $width;
    font-size: 20px;
    color: #434447;
    display: flex;
    flex-direction: row;
    justify-content: space-between;
    align-items: center;


    .project_search {
        width: 35%;
    }
}

.scroll_content {
    width: calc(100% - 30px);
    height: calc(100vh - 175px);
    padding: 15px;
    display: flex;
    flex-flow: row wrap;
    justify-content: start;
}

.logo-p1 {
    $width: 180px;
    $height: 160px;
    box-sizing: border-box;
    margin: 0 7px 15px 1%;
    width: $width;
    height: $height;
    background-color: rgb(240, 240, 240);
    border-radius: $global_bd_radius;
    display: flex;
    flex-direction: column;
    z-index: 999;

    img {
        width: 35%;
    }

    .new_project {
        letter-spacing: 5px;
        font-weight: 500;
        font-size: 14px;
        color: #606266;
    }

    .pre-contain {
        align-items: center;
        display: flex;
        flex-direction: column;
        justify-content: center;
        height: $height * 0.6;

        img {
            border-radius: 5px;
            width: 90%;
            object-fit: scale-down;
        }
    }

    .info-box {
        box-sizing: border-box;
        padding-top: 5px;
        padding: 10px;
        display: flex;
        flex-direction: row;
        width: 100%;
        align-items: center;
        font-size: 14px;
        justify-content: space-between;

        .control {
            display: flex;
            flex-direction: row;
            align-items: center;
            width: 30%;
            justify-content: space-between;
            z-index: 2000;
        }

        .name {
            display: inline-flex;
            align-items: center;
            justify-content: start;
            flex-direction: row;
            font-size: 14px;
            height: fit-content;
            width: 80%;

            & span {
                // width: 50%;
                @include text_over_flow;
            }

        }
    }

    .time-box {
        padding-left: 10px;
        padding-top: 5px;
        display: inline-flex;
        flex-direction: row;
        align-items: center;
        color: #606266;
        font-size: 12px;
    }

    &:hover {
        background-color: rgb(64, 158, 255, 0.1);
        /* x 偏移量 | y 偏移量 | 阴影模糊半径 | 阴影扩散半径 | 阴影颜色 */
        color: rgb(64, 158, 255, 0.9);
        box-shadow: 1px 1px 4px rgba(0, 0, 0, 0.15);
    }
}
</style>
