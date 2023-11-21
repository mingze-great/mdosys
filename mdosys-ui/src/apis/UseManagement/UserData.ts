import service from "@/utils/requestUtils";
import request from "@/utils/requestUtils";
import axios from "axios";
import { TimeList } from "element-plus";

interface User {
    id: number
    deptId:number
    roleId: number
    userName: string
    nickName: string
    userType: string
    email: string
    phoneNumber: string
    sex: string
    avatar: string
    passWord: string
    status: string 
    delFlag:string
    loginIp:string
    loginDate: string
    createBy: string
    createTime: string
    updateBy: string
    updateTime: string
    remark: string
    code: string
}




//获取用户列表
export function getUser(param?: any) {
    //  param:
    //      pageNum:          查询处于_page页的数据(下标从1开始)
    //      pageSize:         每页存在_limit条数据
    return service({
        url: "/user/list",
        method: "get",
        params: param,
})
}


//获取用户列表数目
export function getUserNum() {
    return service({
        url: "",
        method: "get",
    });
}


//通过输入框来筛选
export function UserFilter(param: any) {
    //   param:
    //       _input:     全局搜索的输入值
    //       _page:      查询处于_page页的数据(下标从1开始)
    //       _limit:     每页存在_limit条数据
    return service({
        url: "",
        method: "get",
        params: param,
    });
}


//新增用户
export function addUser(data: any) {
    //   参数就是User 
    return service({
        url: "",
        method: "post",
        data: data
    })
}


//删除一条用户数据
export function deleteUser(param: any) {
    // 参数就是用户id
    return service({
        url: "" + param.id,
        method: "delete"
    })
}

// 修改用户个人信息
export function updateUser(data: any) {
    return service({
      url: '',
      method: 'put',
      data: data
    })
  }

// 用户密码重置
export function updateUserPwd(oldPassword: any, newPassword: any) {
    const data = {
      oldPassword,
      newPassword
    }
    return service({
      url: '',
      method: 'put',
      params: data
    })
  }