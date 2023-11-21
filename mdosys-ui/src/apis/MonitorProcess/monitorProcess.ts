//导入axios实例

import service from "@/utils/requestUtils";
import request from "@/utils/requestUtils";
import axios from "axios";

interface monitorInfoParam {
  id: string; //流程id
  taskName: string; //任务名称
  taskType: string; //任务类型
  creator: string; //创建人
  taskProgress: number; //任务进度
  createTime: number; //创建时间
  runLog: string; //运行日志
  runTime: Date; //已运行的时长，单位(min)
  runFlag: boolean; //当前任务是否处于运行状态，true表示处于运行状态
}

//获取流程数据数目
export function getMonitorItemById(param: any) {
  return service({
    url: "/project/monitor/idItem",
    method: "get",
    params: param,
  });
}

//获取流程数据
export function getMonitorList(param: any) {
  /**
   * param:
   *      _page:          查询处于_page页的数据(下标从1开始)
   *      _limit:         每页存在_limit条数据
   */

  return service({
    url: "/project/monitor/list",
    method: "get",
    params: param,
  });
}

//获取子组件id
export function getSubNodeList(param: any) {
  /**
   * param:
   *      process_instance_id: 父流程的id
   */
  return service({
    url: "/project/monitor/nodeList",
    method: "get",
    params: param,
  });
}

//通过任务或者创建人来筛选
export function getMonitorByTaskOrCreator(param: any) {
  /**
   * param:
   *      taskName:       任务名称
   *      taskType:       任务类型
   *      creator:        创建人
   *      _page:          查询处于_page页的数据(下标从1开始)
   *      _limit:         每页存在_limit条数据
   */
  return service({
    url: "/project/monitor/list",
    method: "get",
    params: param,
  });
}

//修改一个任务(终止或者开始)
export function changeMonitorState(param: any) {
  /**
   * param:
   *      id:             流程id
   *      runFlag:        标志一个流程是否处于运行态，false为终止，true为运行
   *      taskProgress:   流程进度
   */

  return service({
    url: "/project/monitor/update",
    method: "patch",
    data: param, //对应的参数名是data不是params
  });
}

//通过输入来筛选
export function getMonitorByInput(param: any) {
  /**
   * param:
   *      q:          全局搜索的输入值
   *      _page:      查询处于_page页的数据(下标从1开始)
   *      _limit:     每页存在_limit条数据
   */
  return service({
    url: "http://localhost:3000/List",
    method: "get",
    params: param,
  });
}

//添加一条流程数据
export function addProcessData(param: any) {
  /**
   * 参数就是monitorInfoParam
   * */
  return service({
    url: "http://localhost:3000/List",
    method: "post",
    params: param,
  });
}

//删除一条流程数据
export function deleteProcessData(param: any) {
  /**
   * 参数就是流程id
   * */
  return request({
    url: "http://localhost:3000/List/" + param.id,
    method: "delete",
  });
}
// //通过创建人来筛选
// export function getMonitorByCreator(param: any) {

// }

// //通过任务类型来筛选
// export function getMonitorByTaskType(param: any) {

// }

// //终止一个任务
// export function shutDownMonitor(param: number) {

// }

// 工作流实例列表
// 参数示例：?searchVal=&pageSize=10&pageNo=1&host=&stateType=&startDate=&endDate=&executorName=&_t=0.5160550904263792
export function process_instances(
  code: string,
  param?: {
    searchVal: string;
    pageNo: number;
    pageSize: number;
    host: string;
    stateType: string;
    startDate: string;
    endDate: string;
    executorName: string;
  }
) {
  return request({
    url: "/scheduler/projects/" + code + "/process-instances/",
    method: "get",
    params: { pageNo: 1, pageSize: 10 },
  });
}

// 根据工作流实例查询任务实例
// 参数示例：?pageSize=10&pageNo=1&searchVal=&processInstanceId=&host=&stateType=&startDate=&endDate=&executorName=&processInstanceName=%E6%B5%8B%E8%AF%95%E6%B5%81%E7%A8%8B%E5%9B%BE-4-20221113220709041&_t=0.5534950240381928
export function task_instances(
  code: string,
  param: {
    processInstanceId: number;
    pageNo: number;
    pageSize: number;
  }
) {
  return request({
    url: "/scheduler/projects/" + code + "/task-instances/",
    method: "get",
    params: param,
  });
}

// 根据任务实例查询执行日志
// 参数示例：?taskInstanceId=60&skipLineNum=0&limit=1000
export function logDetail(taskInstanceId: number) {
  return request({
    url: "/scheduler/log/detail/",
    method: "get",
    params: { taskInstanceId: taskInstanceId, skipLineNum: 10, limit: 10000 },
  });
}
