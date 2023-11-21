// 导入axios实例
import service from "@/utils/requestUtils";
// 定义接口的传参
interface componentInfoParam {
  name: string;
  parentId: number;
  remark: string;
}

// 获取组件列表（全部）
export function getComponentList() {
  return service({
    url: "/system/component/list",
    method: "get",
  });
}

// 获取公共组件列表（全部）
export function getPubComponentList() {
  return service({
    url: "/system/component/pubList",
    method: "get",
  });
}

// 获取组件基本信息列表，只包含组件的id name code等信息
// export function getComponentMenuList() {
export function getComponentIndexList() {
  return service({
    url: "/system/component/menuList",
    method: "get",
  });
}

// 获得组件分类基本信息列表
// TODO 后端没有实现
export function getComClassIndexList() {
  return service({
    url: "/system/component/classIndex",
    method: "get",
  });
}

// 获取组件文件列表
export function getFileListByCompId(id: number) {
  return service({
    url: "/system/component/fileList/" + id,
    method: "get",
  });
}

// 新增组件，id不需要传，数据库自增长
export function addComponent(param: componentInfoParam) {
  return service({
    url: "/system/component/add",
    method: "post",
    data: param,
  });
}

// 修改组件
export function updateComponent(param: any) {
  return service({
    url: "/system/component/update",
    method: "put",
    data: param,
  });
}

// 删除组件
export function delComponent(id: number) {
  return service({
    url: "/system/component/del/" + id,
    method: "delete",
  });
}

// 删除组件文件
export function delComponentFile(param: any) {
  return service({
    url: "/system/componentFile/del",
    method: "post",
    data: param,
  });
}

// 下载组件文件
export function downloadComponentFile(param: any) {
  return service({
    url: "/system/componentFile/download",
    method: "post",
    data: param,
    responseType: "blob",
  });
}

//取消授权
export function revoke(id: number) {
  return service({
    url: "/system/component/revoke/" + id,
    method: "get",
  });
}
