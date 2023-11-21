import service from "@/utils/requestUtils"

export function getMonitorInfo() {
    return service({
        url: "/dolphinscheduler/monitor/masters",
        method: "get",
    })
}