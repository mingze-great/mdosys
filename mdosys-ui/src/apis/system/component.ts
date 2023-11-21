// 导入axios实例
import service from "@/utils/requestUtils";

import { handleTree, addTreeLeafs } from "@/utils/listToTree";
import { async } from "@antv/x6/lib/registry/marker/async";

// 定义接口的传参
interface componentInfoParam {
  name: string;
  classId: number;
  description: string;
}

// 获取个人组件列表(数组结构)
export function getComponentList() {
  return service({
    url: "/system/component/list",
    method: "get",
  });
}

// 获取个人组件菜单目录(数组结构)
export function getComponentMenuList() {
  return service({
    url: "/system/componentMenu/list",
    method: "get",
  });
}

// 获取个人组件信息（树形结构）（包括菜单和组件）
export async function getComponentTree() {
  var compList: any[] = [];
  var compPubList: any[] = [];
  await getComponentList().then((res) => {
    compList = res.data;
  });
  await getComponentMenuList().then((res) => {
    compPubList = handleTree(res.data, null, null, null);
  });
  return addTreeLeafs(compPubList, compList);
}

// 获取公共组件列表（全部）
export function getPubComponentList() {
  return service({
    url: "/system/component/pubList",
    method: "get",
  });
}

// 获取组件文件列表
export function getFileListByCompId(id: number) {
  return service({
    url: "/system/componentFile/fileList/" + id,
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

// 新增组件分类
export function addComponentMenu(param: any) {
  return service({
    url: "/system/componentMenu/add",
    method: "post",
    data: param,
  });
}

// 删除组件菜单
export function delComponentMenu(id: number) {
  return service({
    url: "/system/componentMenu/del/" + id,
    method: "get",
  });
}

export function getComponentInfo(id: number) {
  return service({
    url: "/system/component/info/" + id,
    method: "get",
  });
}

// 修改组件文件类型
export function updateComponentFileType(param: any) {
  return service({
    url: "/system/componentFile/updateType",
    method: "put",
    data: param,
  });
}
