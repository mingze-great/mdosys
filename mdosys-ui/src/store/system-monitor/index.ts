import {defineStore} from "pinia";
import {getMonitorInfo} from "@/apis/SystemMonitor";

interface ComputerStatus {
    cpuUsage: number,
    memoryUsage: number,
    loadAverage: number,
    host: string,
    lastHeartbeatTime: string,
    createTime: string,
}

export const useSystemMonitorStore = defineStore("systemMonitorStore", {
    state: () => {
        return {
            computerStatuses: <ComputerStatus[]>[]
        }
    },
    getters: {},
    actions: {
        getMonitorInfo(): Promise<unknown> {
            return new Promise<unknown>((resolve, reject) => {
                getMonitorInfo()
                    .then(response => {
                        this.computerStatuses = response.data.map((item: any) => {
                            let resInfo = JSON.parse(item.resInfo)
                            return <ComputerStatus>{
                                createTime: item.createTime,
                                lastHeartbeatTime: item.lastHeartbeatTime,
                                host: item.host,
                                cpuUsage: resInfo.cpuUsage * 100,
                                memoryUsage: resInfo.memoryUsage * 100,
                                loadAverage: resInfo.loadAverage,
                            }
                        })
                        resolve(response)
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        }
    }
})