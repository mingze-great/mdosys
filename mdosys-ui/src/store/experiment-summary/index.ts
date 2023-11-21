import {defineStore} from "pinia";
import {
    addSummary as AddSummary,
    editSummary as EditSummary,
    getSummaryList as GetSummaryList,
    deleteSummary as DeleteSummary
} from "@/apis/Share/ExperimentSummary"
import dayjs from "dayjs";

interface Summary {
    key: string,
    editTime: string,
    experimentName: string,
    summaryContent: string,
}

export const useExperimentSummaryStore = defineStore("experimentSummaryStore", {
    state: () => {
        return {
            summaryList: <Array<Summary>>[
                {
                    key: "abcd",
                    editTime: dayjs(Date.now()).format("YYYY-MM-DD HH:mm:ss"),
                    experimentName: "hello",
                    summaryContent: "<h1>标题</h1><p>Lorem ipsum dolor sit amet, consectetur adipisicing elit. Accusantium at, commodi consequuntur dolorem ducimus, hic ipsam, iure maiores molestias nisi nobis non obcaecati officiis optio praesentium quaerat qui quidem voluptatem!</p>"
                }
            ],
        }
    },
    actions: {
        addSummary(experimentName: string): Promise<void> {
            return new Promise((resolve, reject) => {
                AddSummary(experimentName)
                    .then(() => {
                        this.getSummaryList()
                        resolve()
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        },
        deleteSummary(key: string): Promise<void> {
            return new Promise((resolve, reject) => {
                DeleteSummary(key)
                    .then(() => {
                        this.getSummaryList()
                        resolve()
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        },
        getSummaryList() {
            GetSummaryList()
                .then(res => {
                    this.summaryList = res.data
                })
        },
        editSummary(key: string): Promise<void> {
            return new Promise((resolve, reject) => {
                const summary = this.summaryList.find(item => item.key === key);
                EditSummary(key, summary!.summaryContent)
                    .then(() => {
                        this.getSummaryList()
                        resolve()
                    })
                    .catch(error => {
                        reject(error)
                    })
            })
        }
    },
    getters: {}
})