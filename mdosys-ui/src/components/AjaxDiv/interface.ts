import { AxiosPromise } from "axios"

interface queryFun{
    (...param:any[]):Promise<any>
}
interface AjaxItem{
    name:String,
    fun:queryFun,
    param?:Object,
}

interface AjaxQueryData{
    name:String,
    data:Object
}

export type {AjaxItem,AjaxQueryData}
