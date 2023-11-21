// 导入axios实例
import service from "@/utils/requestUtils";

// 定义接口的传参
interface fileInfoParam {
  fatherId: number;
  name: string;
  type: string;
  pageNum: number;
  pageSize: number;
  orderByColumn: string;
  isAsc: string;
}

// 获取文件列表
export function getFileList(param: fileInfoParam) {
  return service({
    url: "/system/spaceFile/list",
    method: "get",
    params: param,
  });
}

// 获取文件详细信息
export function getFileById(param: number) {
  return service({
    url: "/system/spaceFile/" + param,
    method: "get",
  });
}

// 添加文件夹
export function addFile(param: any) {
  return service({
    url: "/system/spaceFile/addDir",
    method: "post",
    data: param,
  });
}
