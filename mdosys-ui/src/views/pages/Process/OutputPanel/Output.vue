<template>
    <terminal class="terminal" :context="context" :name="ProcessTerminal.TERMINAL_NAME" @beforeExecCmd="beforeExecCmd"
        @execCmd="execCmd" @initBefore="initBefore" @initComplete="initComplete" @tabKeyHandler="tabKeyHandler"
        @onClick="onClick" @onKeydown="onKeydown" :show-header="false"></terminal>
</template>
<script lang="ts" setup>
import terminal from "vue-web-terminal";
import { onMounted, onUnmounted, ref } from "vue";
// import { logDetail } from "@/apis/MonitorProcess/monitorProcess";
import { ApplicationCore } from "@/ApplicationCore";
import { ProcessTerminal } from "@/ApplicationCore/AppTerminal/ProcessTerminal";
const applicationCore = ApplicationCore.getInstance();
const eventCenter = applicationCore.eventCenter;
const processsTerminal: ProcessTerminal = applicationCore.processTerminal;
let context = processsTerminal.context;
let beforeExecCmd = (cmdKey: string, command: string, name: string) => {
    processsTerminal.beforeExecCmd(cmdKey, command, name)
};
let execCmd = (cmdKey: string, command: string, success: Function, failed: Function, name: string) => {
    processsTerminal.execCmd(cmdKey,command,success,failed,name);
}
let initBefore = (name: string) => {
    processsTerminal.initBefore(name);
}
let initComplete = (name: string) => {
    processsTerminal.initComplete(name);
};
let tabKeyHandler = (event: any) => {
    processsTerminal.tabKeyHandler(event);
}
let onClick = (key: string, name: string) => {
    processsTerminal.onClick(key,name);
}
let onKeydown = (event: any, name: string) => {
    processsTerminal.onKeydown(event,name);
}
onMounted(() => {
    eventCenter.registerConsumer(processsTerminal);
});
onUnmounted(() => {
    eventCenter.unregistConsumer(processsTerminal);
});
</script>

<style lang="scss" scoped>
.terminal {
    background-color: whitesmoke;
}
</style>
