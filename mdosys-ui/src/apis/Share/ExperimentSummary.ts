import service from "@/utils/requestUtils";

export function addSummary(experimentName: string) {
    return service({
        url: "",
        method: "post",
        data: {
            experimentName
        }
    })
}

export function deleteSummary(key: string) {
    return service({
        url: "",
        method: "post",
        data: {
            key
        }
    })
}

export function getSummaryList() {
    return service({
        url: "",
        method: "get",
    })
}

export function editSummary(key: string, summaryContent: string) {
    return service({
        url: "",
        method: "post",
        data: {
            key,
            summaryContent,
        }
    })
}