<template>
    <el-dialog v-model="show" append-to-body top="35vh" :show-close="false" :modal="false" width="30%"
        class="dialog_class">
        <template #header>
            loading...
        </template>
        <div class="process_creater_main" style="margin-bottom: 20px;">
            <span>
                {{ msg }}
            </span>
            <el-progress :percentage="100" status="warning" :indeterminate="true" :duration="1" />
        </div>
    </el-dialog>
</template>
<script lang="ts" setup>
import { AbstractEvent, EventCenter, EventType, IEventConsumer } from '@/ApplicationCore/EventCenter';
import { ref, onMounted } from 'vue';
const msg = ref('');
const show = ref(false);
let eventCenter: EventCenter = EventCenter.getInstance();
const eventConsumer: IEventConsumer = {
    priority: 1000,
    action(event: AbstractEvent): boolean {
        console.log("tip dialog", event);
        switch (event.type) {
            case EventType.INITIALIZE:
                msg.value = event.content?.msg as string;
                show.value = true;
                return true;
            case EventType.INITIALIZING:
                msg.value = event.content?.msg as string;
                break;
            case EventType.INITIALIZED:
                msg.value = event.content?.msg as string;
                show.value = false;
                return false;
            case EventType.ERROR:
                msg.value = event.content?.msg as string;
                show.value = false;
                return false;
            default:
                break;
        }
        return false;
    }
}
onMounted(() => {
    eventCenter.registerConsumer(eventConsumer);
});
</script>
<style lang="scss" scoped>
.inputer {
    width: 70%;
}

.process_creater_main {
    width: 100%;
    height: fit-content;
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: flex-start;

    &>* {
        width: 100%;
    }
}

.dialog_class {
    border: 1px solid rgb(255, 1, 1);
    color: red;
}

.header_main {
    width: 100%;
    height: 100%;
    display: inline-block;
    font-size: 16px;
    color: rgb(131, 131, 131);
    text-align: center;
    letter-spacing: 3px;
}
</style>
