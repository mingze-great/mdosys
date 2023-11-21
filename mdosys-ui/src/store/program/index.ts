import {defineStore} from "pinia";
import {ProgramItem} from "@/store/program/programType";

export const useProgramStore = defineStore('programStore', {
    state: () => {
        return {
            tableData: <ProgramItem[]>[]
        }
    },
    getters: {

    },
    actions: {
        requireProgramList() {
            this.tableData = [
                {
                    id: 1,
                    name: '飞行器设计方案',
                    creator: '林政',
                    date: '2022-10-14 13:38:20',
                    remark: '已完成'
                },
                {
                    id: 2,
                    name: '飞行器设计方案-更新',
                    creator: 'lly',
                    date: '2022-10-14 13:38:20',
                    remark: '已完成'
                },
                {
                    id: 3,
                    name: '发动机设计方案',
                    creator: '白睿哲',
                    date: '2022-10-14 13:38:20',
                    remark: '已完成'
                },
                {
                    id: 4,
                    name: '气动计算设计方案',
                    creator: '张印',
                    date: '2022-10-14 13:38:20',
                    remark: '已完成'
                },
                {
                    id: 5,
                    name: '飞行器设计实验方案',
                    creator: '向铭杰',
                    date: '2022-10-14 13:38:20',
                    remark: '已完成'
                },
                {
                    id: 6,
                    name: '总体设计分析方案',
                    creator: '苏澳',
                    date: '2022-10-14 13:38:20',
                    remark: '进行中'
                },
                {
                    id: 7,
                    name: '结构设计方案',
                    creator: '张芷昕',
                    date: '2022-10-14 13:38:20',
                    remark: '-'
                },
                {
                    id: 8,
                    name: '弹道设计方案',
                    creator: '胡炜杰',
                    date: '2022-10-14 13:38:20',
                    remark: '-'
                },
                {
                    id: 9,
                    name: '特性仿真设计方案',
                    creator: '张亢',
                    date: '2022-10-14 13:38:20',
                    remark: '-'
                },

            ]
        }
    }
})