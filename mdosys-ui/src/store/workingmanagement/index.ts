import {defineStore} from "pinia";
import {workerGroupItem} from "@/store/workingmanagement/workerGroupType";
import {ref} from "vue-demi";

export const useWorkerManagementStore = defineStore('workerManagementStore', {
    state: () => {
        return {
            tableData: ref<workerGroupItem[]>([])
        }
    },
    getters: {

    },
    actions: {
        requireWorkerManagementList() {
            this.tableData = [
                {
                    id: 1,
                    name: 'Worker分组1',
                    workerAddress: 'unknown',
                    description: '无',
                    date: '2022/10/16'
                },
                {
                    id: 2,
                    name: 'Worker分组1',
                    workerAddress: 'unknown',
                    description: '无',
                    date: '2022/10/16'
                },
                {
                    id: 3,
                    name: 'Worker分组1',
                    workerAddress: 'unknown',
                    description: '无',
                    date: '2022/10/16'
                },
                {
                    id: 4,
                    name: 'Worker分组1',
                    workerAddress: 'unknown',
                    description: '无',
                    date: '2022/10/16'
                },
                {
                    id: 5,
                    name: 'Worker分组1',
                    workerAddress: 'unknown',
                    description: '无',
                    date: '2022/10/16'
                },
                {
                    id: 6,
                    name: 'Worker分组1',
                    workerAddress: 'unknown',
                    description: '无',
                    date: '2022/10/16'
                },
                {
                    id: 7,
                    name: 'Worker分组1',
                    workerAddress: 'unknown',
                    description: '无',
                    date: '2022/10/16'
                },
                {
                    id: 8,
                    name: 'Worker分组1',
                    workerAddress: 'unknown',
                    description: '无',
                    date: '2022/10/16'
                },
                {
                    id: 9,
                    name: 'Worker分组1',
                    workerAddress: 'unknown',
                    description: '无',
                    date: '2022/10/16'
                },
                {
                    id: 10,
                    name: 'Worker分组1',
                    workerAddress: 'unknown',
                    description: '无',
                    date: '2022/10/16'
                },
                {
                    id: 11,
                    name: 'Worker分组1',
                    workerAddress: 'unknown',
                    description: '无',
                    date: '2022/10/16'
                }
            ]
        }
    }
})