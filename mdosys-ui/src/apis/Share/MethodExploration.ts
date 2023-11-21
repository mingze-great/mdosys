import service from "@/utils/requestUtils";

export function save(key: string, noteContent: string, editTime: string) {
    return service({
        url: "",
        method: "post",
        data: {
            key,
            noteContent,
            editTime
        }
    })
}

export function changeNoteName(key: string, noteName: string) {
    return service({
        url: "",
        method: "post",
        data: {
            key,
            noteName
        }
    })
}

export function deleteNotePage(key: string) {
    return service({
        url: "",
        method: "post",
        data: {
            key
        }
    })
}

export function getNoteList() {
    return service({
        url: "",
        method: "get",
    })
}

export function addNotePage(key: string, noteName: string, editTime: string) {
    return service({
        url: "",
        method: "post",
        data: {
            key,
            noteName,
            editTime
        }
    })
}

export function exportAsPDF(htmlStr: string) {
    return service({
        url: "",
        method: "post",
        data: {
            htmlStr
        }
    })
}