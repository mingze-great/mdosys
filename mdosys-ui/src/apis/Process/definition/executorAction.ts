import service from "@/utils/requestUtils";
import { ExecuteType } from "../common";
import { ExecuteProcessInstanceConfig } from "./executorConfig";
export function startProcessInstance(projectCode:string,data:ExecuteProcessInstanceConfig) {
    return service({
        url: "/scheduler/projects/" + projectCode + "/executors/start-process-instance",
        method: "POST",
        data: data
    });
}

// export function batchStartProcessInstance(projectCode:string,data:ExecuteProcessInstanceConfig) {
//     return service({
//         url: "/scheduler/projects/" + projectCode + "/executors/start-process-instance",
//         method: "POST",
//         data: data
//     });
// }

export function execute(projectCode:string,processInstanceId:Number,executeType:ExecuteType){
    return service({
        method:"POST",
        url: "/scheduler/projects/" + projectCode + "/executors/execute",
        params:{processInstanceId:processInstanceId,executeType:executeType}
    })
}
