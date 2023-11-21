import { async } from '@antv/x6/lib/registry/marker/async'
import axios from 'axios'
import { reactive, ref } from 'vue'
import {getUserNum,getUser,UserFilter,addUser,deleteUser,updateUser,updateUserPwd} from '@/apis/UseManagement/UserData'

const getData =async (param:any) => {//获取用户列表
    let w : Array<any> = []
    await getUser(param).then((res: any) => {
        w = res
        console.log("res",res);
        
    }).catch((err)=>{
        console.log("打印错误信息",err);
        
    })
    return w
}
const getDataLength =async () => {//获取用户列表数目
    let w:Array<any> = []
    await getUserNum().then((res: any) => {
        w = res
    })
    return w
}

const getSearchData =async (param:any) => {//通过输入框来筛选
    let w:Array<any> = []
    await UserFilter(param).then((res: any) => {
        w = res
    })
    return w
}


console
export { getData,getSearchData,getDataLength,}








