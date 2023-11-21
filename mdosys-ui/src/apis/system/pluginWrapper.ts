import service from "@/utils/requestUtils";

interface BasicInfo {
    id: string
    name: string
    type: string
    command: string
    path: string
    remark: string
}
interface File {
    id: number
    pluginId: string
    name: string
    type: string
    path: string
}
interface TempFile{
    pluginId: string
    name: string
    string: string
}
interface IOParam {
    id: number
    pluginId: string
    name: string
    defaultValue: string
    unit: string
    mode: string
    file: string
    ioType: string
    paramType: string
    valueType: string
    beginRow: string
    endRow: string
    beginColumn: string
    endColumn: string
}
export type { BasicInfo, File, TempFile, IOParam };

export function addBasicInfo(param: BasicInfo) {
    return service({
        url: "/plugin/add",
        method: "post",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        },
        data: param
    })
}

export function addIOParam(param: IOParam) {
    return service({
        url: "/plugin/io/add",
        method: "post",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        },
        data: param
    })
}

export function addFile(param: File) {
    return service({
        url: "/plugin/file/add",
        method: "post",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        },
        data: param
    })
}

export function getFileString(tempFile: TempFile) {
    return service({
        url: "/plugin/file/fileString",
        method: "post",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        },
        data: tempFile
    })
}

export function editFile(tempFile: TempFile) {
    return service({
        url: "/plugin/file/stringToFile",
        method: "post",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        },
        data: tempFile
    })
}

export function getWrapperList(){
    return service({
        url: "/plugin/list",
        method: "get",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        }
    })
}

export function getInputList(pluginId: string) {
    return service({
        url: "/plugin/io/inputlist/"+pluginId,
        method: "get",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        }
    })
}

export function getOutputList(pluginId: string) {
    return service({
        url: "/plugin/io/outputlist/"+pluginId,
        method: "get",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        }
    })
}

export function getFileList(pluginId: string) {
    return service({
        url: "/plugin/file/list/"+pluginId,
        method: "get",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        }
    })
}

export function getWrapperDetail(pluginId: string) {
    return service({
        url: "/plugin/detail/"+pluginId,
        method: "get",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        }
    })
}


export function editIOParam(param: IOParam) {
    return service({
        url: "/plugin/io/edit",
        method: "put",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        },
        data: param
    })
}

export function editFileInfo(file: File) {
    return service({
        url: "/plugin/file/edit",
        method: "put",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        },
        data: file
    })
}

export function editBasicInfo(basicInfo: BasicInfo) {
    return service({
        url: "/plugin/edit",
        method: "put",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        },
        data: basicInfo
    })
}

export function removeWrapper(pluginId: string) {
    return service({
        url: "/plugin/remove/"+pluginId,
        method: "delete",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        },
    })
}

export function removeAllFile(pluginId: string) {
    return service({
        url: "/plugin/file/removeByPluginId/"+pluginId,
        method: "delete",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        },
    })
}

export function removeAllParam(pluginId: string) {
    return service({
        url: "/plugin/io/removeByPluginId/"+pluginId,
        method: "delete",
        headers: {
            "content-type": "application/json;charset=utf-8",
            'Plugin':'test'
        },
    })
}