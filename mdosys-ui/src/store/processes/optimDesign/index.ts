import { defineStore, Store } from "pinia";
import { optimDesignParam, targetParam, conditionParam, designParam, monitorParam } from "@/apis/Process/optimDesign";
import { NodeDataParam } from "@/apis/Task/TaskDefinition/taskNode";

export const useOptimDesinParam = defineStore('useOptimDesinParam', {
    state: () => {
        return {
            currentDesignParam: {
                code: "1111",
                name: '优化设计1' as string,

                targetParamList: [] as Array<targetParam>,
                conditionParamList: [] as Array<conditionParam>,
                designParamList: [] as Array<designParam>,
                monitorParamList: [] as Array<designParam>,
                NodeTempParam: {} as NodeDataParam,
                optimFormula: "" as string,
            } as optimDesignParam,
        }
    },
    // persist: {
    //     // key: "optimDesignParam",
    //     // paths: [""],
    //     // storage: window.sessionStorage
    // },
    getters: {
        // getConditionList() {
        //     return (code: string) => {
        //         for (let index = 0; index < this.currentDesignParam.conditionParamList.length; index++) {
        //             const item = this.currentDesignParam.conditionParamList[index];
        //             if (item.code == code) {
        //                 return item;
        //             }
        //         }
        //         return undefined;
        //     }
        // },
        // getDesignList() {
        //     return (code: string) => {
        //         for (let index = 0; index < this.currentDesignParam.designParamList.length; index++) {
        //             const item = this.currentDesignParam.designParamList[index];
        //             if (item.code == code) {
        //                 return item;
        //             }
        //         }
        //         return undefined;
        //     }
        // },
        // getMonitorList() {
        //     return (code: string) => {
        //         for (let index = 0; index < this.currentDesignParam.monitorParamList.length; index++) {
        //             const item = this.currentDesignParam.monitorParamList[index];
        //             if (item.code == code) {
        //                 return item;
        //             }
        //         }
        //         return undefined;
        //     }
        // },

    },

    actions: {
        emptySelf() {
            this.currentDesignParam.conditionParamList.length = 0;
            this.currentDesignParam.designParamList.length = 0;
            this.currentDesignParam.monitorParamList.length = 0;

            // this.currentDesignParam.targetParamList.length = 0
            
            // var temp = {
            //     name: '发动机后处理.相对均方根偏差',
            //     code: '',
            //     index: 1000.0,
            //     units: '',
            //     purpos: 'Minimum',
            // };
            // this.currentDesignParam.targetParamList.push(temp);
        },

        getTempParam(name: string, code: string, index: string, units: string) {
            var temp: NodeDataParam = {
                name: name,
                code: code,
                type: "single",
                index: index,
                units: units,
            };
            this.currentDesignParam.NodeTempParam = temp;
            console.log("huoqu", this.currentDesignParam.NodeTempParam);

        },

        addConditionList() {
            var temp: conditionParam = {
                name: this.currentDesignParam.NodeTempParam.name,
                code: this.currentDesignParam.NodeTempParam.code,
                index: this.currentDesignParam.NodeTempParam.index,
                units: this.currentDesignParam.NodeTempParam.units,
                maxIndex: 0,
                minIndex: 0,
                penalty: 0,
                maxState: false,
                minState: false,
                penState: false,
            }
            this.currentDesignParam.conditionParamList.push(temp)
            console.log("add")
        },

        addDesignList() {
            var temp: designParam = {
                name: this.currentDesignParam.NodeTempParam.name,
                code: this.currentDesignParam.NodeTempParam.code,
                index: this.currentDesignParam.NodeTempParam.index,
                units: this.currentDesignParam.NodeTempParam.units,
                maxIndex: '0',
                minIndex: '0',
                init: this.currentDesignParam.NodeTempParam.index,
                maxState: false,
                minState: false,
            }
            this.currentDesignParam.designParamList.push(temp)
        },

        addMonitorList() {
            var temp: monitorParam = {
                name: this.currentDesignParam.NodeTempParam.name,
                code: this.currentDesignParam.NodeTempParam.code,
                index: this.currentDesignParam.NodeTempParam.index,
            }
            this.currentDesignParam.monitorParamList.push(temp)
        }


    }


});
