import { defineStore } from "pinia";

export const useGlobalConfigure = defineStore('useGlobalConfigure', {
    state: () => {
        return {
            isfullScreen:false,
            currentPage:'',
            processPage: {
                OutputPanel: {
                    isClose: true,
                    isfullHeight:false
                },
                ResourseTreePanel: {
                    isClose: false
                },
                componentPanel:{
                    isClose:false
                },
            },
        }
    },
    getters: {
     
    },
    actions: {
    }
});
