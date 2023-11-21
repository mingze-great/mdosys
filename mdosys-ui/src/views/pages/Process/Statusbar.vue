<template>
    <div class='status-bar-main'>
        <div class="">

        </div>
        <div v-if="show_state" class="process_status">
            <span>
                {{ state_msg }}
            </span>
            <div class="process_bar" v-if="show_process">
                1
                <el-progress :percentage="100" :show-text="false" :stroke-width="4" :color="customColorMethod"
                    :indeterminate="true" :duration="1" />
            </div>
            <div v-else>
                <el-icon :size="13">
                    <InfoFilled />
                </el-icon>
            </div>
        </div>
    </div>
</template>

<script lang="ts" setup>
import { ref, onMounted, onUnmounted, onActivated, onDeactivated } from 'vue';
import { ElIcon, ElProgress } from 'element-plus';
import { ApplicationCore } from '@/ApplicationCore';
import { AbstractEvent, EventType } from '@/ApplicationCore/EventCenter/AbstractEvent';
import { InfoFilled } from '@element-plus/icons-vue';
const applicationCore = ApplicationCore.getInstance();
const eventCenter = applicationCore.eventCenter;

let show_process = ref(false);
let show_state = ref(true);
const state_msg = ref("");

eventCenter.registerConsumer({
    priority: -100,
    action(event: AbstractEvent): boolean {
        console.log("status", event);
        state_msg.value = event.content?.msg as string;
        switch (event.type) {
            case EventType.PROCESS_RUNNING:
                show_process.value = true;
                break;
            case EventType.PROCESS_RUN_FINISHED:
                show_process.value = false;
                break;
            case EventType.ERROR:
                show_process.value = false;
                break;
            default:
                show_process.value = false;
                break;
        }
        return false;
    }
})
onMounted(() => {
    console.log("status............");
});
const customColors = [
    { color: '#f56c6c', percentage: 20 },
    { color: '#e6a23c', percentage: 40 },
    { color: '#5cb87a', percentage: 60 },
    { color: '#1989fa', percentage: 80 },
    { color: '#6f7ad3', percentage: 100 },
]

const customColorMethod = (percentage: number) => {
    //   if (percentage < 30) {
    //     return '#909399'
    //   }
    //   if (percentage < 70) {
    //     return '#e6a23c'
    //   }
    return '#67c23a'
}

// function updateProgress(progress: number) {
//     show_process.value = true;
//     setTimeout(() => {
//         show_process.value = false;
//     }, 4000);
// }
</script>

<style lang="scss" scoped>
.status-bar-main {
    background-color: rgba(239, 239, 239, 1) !important;
    display: flex;
    justify-content: space-between;
    flex-direction: row;
    align-items: center;
    width: 100%;
    height: 100%;
    background-color: $processStatusBarBgColor;
    user-select: none;
}

.process_status {
    display: flex;
    flex-direction: row;
    align-items: center;
    justify-content: space-around;
    width: fit-content;
    line-height: 1rem;
    font-size: 12px;
    margin-right: 15px;

    span {
        color: rgb(60, 60, 60);
        margin-right: 10px;
    }

    .process_bar {
        width: 150px;
    }
}
</style>
