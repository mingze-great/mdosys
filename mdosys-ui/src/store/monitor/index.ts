import { defineStore } from "pinia";
import { processInstances, taskInstances } from "./monitorType";
import {
  process_instances,
  task_instances,
  logDetail,
} from "@/apis/MonitorProcess/monitorProcess";
import { ApplicationCore } from "@/ApplicationCore";
export const useMonitorStore = defineStore("monitorStore", {
  state: () => {
    return {
      // 当前流程实例列表
      processInstancesList: [] as Array<processInstances>,

      // 当前流程实例节点列表
      taskInstancesList: [] as Array<taskInstances>,
    };
  },
  actions: {
    async getProcessInstanceByDefinitionCode(processDefinitionCode: string) {
      await this.getProcessInstances();
      let proInstances = this.processInstancesList.filter(
        (instance) => instance.processDefinitionCode == processDefinitionCode
      );
      return Promise.resolve({ data: proInstances[0] });
    },
    async getProcessInstances(projectCode?: string) {
      try {
        if (projectCode == undefined) {
          projectCode =
            ApplicationCore.getInstance().processGraphManager?.parentProjectCode;
        }
        if (projectCode == undefined) {
          return Promise.reject({ msg: "项目code 为空" });
        }
        let res = await process_instances(projectCode);
        this.processInstancesList.length = 0;
        this.processInstancesList.push(...res.data.totalList);
        return Promise.resolve(res);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    // getProcessInstances(param?: any) {
    //   return new Promise((resolve: any, reject: any) => {
    //     let code =
    //      projectStore.activedProject.code;
    //     // let code = "7494642584288";
    //     if (code == undefined || code == "") {
    //       reject();
    //     } else {
    //       process_instances(code, param)
    //         .then((res) => {
    //           this.processInstancesList = res.data.totalList;
    //           resolve(res);
    //         })
    //         .catch((error) => {
    //           reject(error);
    //         });
    //     }
    //   });
    // },
    async getTaskInstances(param: any) {
      return await new Promise((resolve: any, reject: any) => {
        let code =
          ApplicationCore.getInstance().processGraphManager?.parentProjectCode;
        // let code = "7494642584288";
        task_instances(code!, param)
          .then((res) => {
            this.taskInstancesList = res.data.totalList;
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    async getTaskLog(taskInstanceId: any) {
      return await new Promise((resolve: any, reject: any) => {
        logDetail(taskInstanceId)
          .then((res) => {
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
  },
  getters: {
    getProcessInstancesList(state) {
      return state.processInstancesList;
    },
    getTaskInstancesList(state) {
      return state.taskInstancesList;
    },
  },
});
