import { async } from "@antv/x6/lib/registry/marker/async";
import axios from "axios";
import { reactive, ref } from "vue";
import {
  getMonitorList,
  getMonitorByTaskOrCreator,
  changeMonitorState,
  deleteProcessData,
  getMonitorItemById,
  getMonitorByInput,
  getSubNodeList,
} from "../../../apis/MonitorProcess/monitorProcess";
const getData = async (param: any) => {
  let w: Array<any> = [];
  await getMonitorList(param).then((res: any) => {
    w = res;
  });
  return w;
};
const getDataById = async (param: any) => {
  let w;
  await getMonitorItemById(param).then((res: any) => {
    w = res;
  });
  return w;
};
const getSearchData = async (param: any) => {
  let a: Array<any> = [];
  await getMonitorByTaskOrCreator(param).then((res: any) => {
    a = res;
    console.log("后端获取的数据为：", res);
  });
  return a;
};
const getInputSearchData = async (param: any) => {
  let w: Array<any> = [];
  await getMonitorByInput(param).then((res: any) => {
    w = res;
  });
  return w;
};

const changeState = async (param: any) => {
  // let a:Array<any> = []
  // axios.patch("http://localhost:3000/List/"+param.id, {
  //     runFlag: param.runFlag,
  //     taskProgress: param.taskProgress
  //  }).then(
  //     (res)=>{
  //         console.log("后端修改获取的数据为：",res)
  //     }
  //  );
  // axios.put("http://localhost:3000/List" + param.id, {param}).then(
  //     (res) => {
  //         //执行成功后代码处理
  //         console.log("后端获取的数据为：",res)
  //     }
  // )

  changeMonitorState(param).then((res: any) => {
    console.log("后端修改获取的数据为：", res);
  });
  // await changeMonitorState(param).then((res:any)=>{
  //     // a = res;
  //     console.log("后端获取的修改数据为：",res)
  // })
  // return a;
};
const loadChild = async (param: any) => {
  let w: Array<any> = [];
  await getSubNodeList(param).then((res: any) => {
    w = res;
    console.log("子组件获取成功", res);
  });
  return w;
};
export {
  getData,
  getSearchData,
  changeState,
  getDataById,
  getInputSearchData,
  loadChild,
};

//请求json-server的数据，异步请求方式
// const load = async()=>{
//   try{

//     getMonitorList().then((res: any) => {
//       console.log("看看res是啥",res)

//   });
//     console.log("现在的tableData：",res)
//   }catch(error){
//     console.log(error)
//   }

// }
// load()
