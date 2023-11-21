<template>
    <div class="select-main" :id="props.obj.code">
      <div class="select-line" style="margin-right: 5px;">
        <!-- <div>{{props.obj.code}}</div> -->
        <div style="padding: 0 3% 5px 0;" id="test1">
          <el-select v-model="value" filterable placeholder="请选择节点" style="width: 100%;" size="small">
            <el-option v-for="item in inputData" :key="item.code" :label="item.name" :value="item.name" />
          </el-select>
        </div>
        <el-scrollbar class="line-scro" @scroll="scroll">
          <el-collapse v-model="activeName" @change="change">
            <el-collapse-item v-for="(item, index) in inputData" :title="item.name" name="1" class="box-node" disabled>
              <div class="from-button-box">
                <el-tree :data="item.NodeParams" @node-click="inputNodeClick" :indent="12" @node-expand="expand"
                  @node-collapse="collapse" :expand-on-click-node="false" :highlight-current="true" default-expand-all>
                  <template #default="{ node, data }">
                    <div v-if="node.level == 1" :id="codeToId(data.code)" class="tree-child-box">
                      <span>{{ data.name }}</span>
                      <el-button v-if="data.type == '%ComArray'" size="small"
                        style="width: 18px; height: 18px; margin-right: 8px;" @click="openExcel(data.name, data.code)">
                        <el-icon>
                          <Grid />
                        </el-icon>
                      </el-button>
                    </div>
                    <div v-else-if="node.level == 2" :id="codeToId(data.code)" class="tree-child-box-leaf">{{ data.name }}</div>
                  </template>
                </el-tree>
              </div>
            </el-collapse-item>
          </el-collapse>
          <!-- <el-button @click="testbtn" size="small">测试</el-button> -->
        </el-scrollbar>
      </div>
      <el-button @click="changeclick" size="small" style="margin-top:10px;">清空</el-button>
  
      <div class="select-line" style="margin-left: 5px;">
        <div style="padding: 0 3% 5px 0;" id="test2">
          <el-input v-model="input" class="w-50 m-2" placeholder="请查找数据" :prefix-icon="Search" style="width: 100%;"
            size="small" />
        </div>
        <div>
          <el-scrollbar class="line-scro" @scroll="scroll">
            <div class="output-box">
              <!-- <div>{{outputData.name}}</div> -->
              <el-tree :data="outputData.NodeParams" @node-click="outputNodeClick" :indent="12" @node-expand="expand"
                @node-collapse="collapse" :expand-on-click-node="false" :highlight-current="true" default-expand-all>
                <template #default="{ node, data }">
                  <div v-if="node.level == 1" :id="codeToId(data.code)" class="tree-child-box">
                    <div>{{ data.name }}</div>
                    <div v-if="data.type == '%single'" class="tree-child-para" @click="editInput(data)">
                      {{ data.index }}
                    </div>
                    <!-- <div v-else-if="data.editVisible == true" class="input-group">
                      <el-input v-model="data.para" class="tree-inupt" size="small" />
                      <el-icon color="#67c23a" :size="12" style="margin:0 3px;" @click="checkInput(data)">
                        <Check />
                      </el-icon>
                    </div> -->
                  </div>
                  <div v-else-if="node.level == 2" :id="codeToId(data.code)" class="tree-child-box-leaf">
                    <div>{{ data.name }}</div>
                    <div v-if="data.type == '%single'" class="tree-child-para-leaf" @click="editInput(data)">
                      {{ data.index }}
                    </div>
                    <!-- <div v-else-if="data.editVisible == true" class="input-group">
                      <el-input v-model="data.para" class="tree-inupt" size="small" />
                      <el-icon color="#67c23a" :size="12" style="margin:0 3px;" @click="checkInput(data)">
                        <Check />
                      </el-icon>
                    </div> -->
                  </div>
                </template>
              </el-tree>
            </div>
          </el-scrollbar>
        </div>
      </div>
    </div>
    <dataExcel :obj="dataExcelObj" />
  
  </template>
  
  <script lang="ts" setup>
  import { Ref, ref, reactive, onMounted, getCurrentInstance } from 'vue'
  import { Search, Check, Grid } from '@element-plus/icons-vue'
  import LeaderLine from '@/plugins/LEADERLINE/DivLine.js';
  //import dataExcel from './DataExcel.vue';
  
  import { useDataFlowParam } from './index';
  
  // import { ProcessContext } from '@/Core/Process/ProcessContext';
  // import { TaskDefinitionCenter } from '@/Core/Process/DefinitionCenter/TaskDefinitionCenter';

  import { DataFlowInfo, ParamDetail, PARAM_DIRECT, PARAM_TYPE, TaskDefinitionData } from './taskNode';
  import { ComponentIndex } from '@/apis/Component/componentInterface';
  import { inputNodeParams } from './dataFlow';
  import { ElSelect, ElOption, ElScrollbar, ElCollapse, ElCollapseItem, ElTree, ElButton, ElIcon, ElInput } from 'element-plus';
  
  //const processContext: ProcessContext = ProcessContext.getInstance();
  //const taskDefinitionCenter: TaskDefinitionCenter = processContext.taskDefinitionCenter;
  
  const DataFlowParam = useDataFlowParam();
  
  const props = withDefaults(defineProps<{ obj: { nodeName: string, code: string, drawerVisibal: boolean, comIndex: ComponentIndex } }>(), {
    obj: () => {
      return {
        nodeName: '壳体计算',
        code: "111",
        drawerVisibal: false,
        comIndex: {} as ComponentIndex
      }
    }
  });
  
  let outputData = reactive({} as inputNodeParams);
  let inputData = reactive([] as Array<inputNodeParams>);

  const load = (node: Node, resolve: (data: Array<inputNodeParams>) => void) => {
  }
  
  onMounted(() => {
    console.log("dataflow onmounted", props);
    DataFlowParam.getOutputNodeData(props.obj.code, props.obj.comIndex).then((res: { name: string; code: string; NodeParams: any[]; }) => { outputData.name = res.name; outputData.code = res.code; outputData.NodeParams = res.NodeParams })
    DataFlowParam.getInputNodeData().then((res: any) => { inputData.push(...res); console.log("inputdata onmounted", inputData); })

    console.log("ouuuuut", outputData)
    //inputData = DataFlowParam.getInputNodeData(processContext.graphManager.getPreNodeList(props.obj.code), taskDefinitionCenter) as unknown as Array<inputNodeParams>
    //outputData = DataFlowParam.getOutputNodeData(props.obj.code, props.obj.comIndex, taskDefinitionCenter) as unknown as inputNodeParams

    //TODO 临时注释
    // DataFlowParam.getInputNodeData(processContext.graphManager.getPreNodeList(props.obj.code), taskDefinitionCenter).then(res => { inputData.push(...res); console.log("inputdata onmounted", inputData); })
    // DataFlowParam.getOutputNodeData(props.obj.code, props.obj.comIndex, taskDefinitionCenter).then(res => { outputData.name = res.name; outputData.code = res.code; outputData.NodeParams = res.NodeParams })
  
  });
  
  // let anyCode:string=taskNodeList.at(1)!.code;
  let dataExcelObj = reactive({
    dialogVisibal: false,
    titleName: "",
    code: "",
    labelList: [] as Array<any>,
    dataList: [] as Array<any>,
  });
  
  let line: LeaderLine;
  
  const value = ref('')
  const input = ref('')
  const activeName = ref('1')
  
  function openExcel(title: string, code: string) {
    // dataExcelObj.dialogVisibal = true;
    // dataExcelObj.titleName = title;
    // dataExcelObj.code = code;
    // var tempList = DataFlowParam.getExcelParam(code)
    // dataExcelObj.labelList = tempList[0];
    // dataExcelObj.dataList = tempList[1];
  
    // DataFlowParam.getExcelParam(code)
  }
  
  function changeclick() {
    console.log("连接组", lineManage)
    for (let i = 0; i < lineManage.length; i++) {
      lineManage[i].remove();
    }
    lineManage.length = 0;
  }
  
  const lineManage: any[] = []
  function creatLine(start: string, end: string) {
    // console.log("cur", test)
    // console.log("res", getStaticId(test))
    line = new LeaderLine({
      parent: document.getElementById(props.obj.code),
      start: document.getElementById(start),
      end: LeaderLine.pointAnchor(document.getElementById(end), {
        x: -25,
        y: 13
      }),
      color: '#409eff',
      startPlug: 'disc',
      endPlug: 'disc',
    });
    line.size = 1.5;
    line.setOptions({ startSocket: 'right', endSocket: 'left', path: 'fluid' });
    console.log("添加了线", line)
    lineManage.push(line)
  
    start = idToCode(start)
    end = idToCode(end)
    var startNode = start.split('-')[0]
    var endNode = end.split('-')[0]

    //TODO 临时注释
    // var getStartParam = processContext.processStore.getNode(startNode) as TaskDefinitionData
    // var getEndParam = processContext.processStore.getNode(endNode) as TaskDefinitionData

    // var startRawData = getParamName(start, PARAM_DIRECT.OUT) as unknown as Map<string, any>
    // var endRawData = getParamName(end, PARAM_DIRECT.IN) as unknown as Map<string, any>
    // getLineManageParam(start, end, startNode, endNode, getStartParam, getEndParam, startRawData, endRawData);
  
    // console.log("拿到的线start", processContext.processStore.getNode(startNode)?.taskParams.localParams)
  }
  
  function getLineManageParam(start: string, end: string, startCode: string, endCode: string, startParam: TaskDefinitionData, endParam: TaskDefinitionData, startRawData: Map<string, any>, endRawData: Map<string, any>) {
  
    var tempLineParam: DataFlowInfo = {
      startNodeCode: startCode,
      endNodeCode: endCode,
      startParamProp: start,
      endParamProp: end,
    };
    //this.currentDataFlowParam.LineManageList.push(tempLineParam);
  
    var tempDetail: ParamDetail = {
      prop: start,
      direct: PARAM_DIRECT.OUT,  // 此处用来标识当前参数的是 输入 还是 输出 
      type: PARAM_TYPE.VARCHAR,  // 标识参数类型 后端以此来格式化数据
      value: "", // 参数值
      dataFlowInfo: tempLineParam,//数据流的定义信息，如果为手动输入 流的输入节点为""
      rawData: startRawData,
    }
    //processContext.processStore.addLine(tempDetail)
  
    //console.log("现在的线", this.currentDataFlowParam.LineManageList)
  }
  
  // async function getParamName(code: string, direction: PARAM_DIRECT) {
  //   var nodeCode = code.split("-")[0]
  //   var paramCode = code.split("-")[1]
  //   var group = await taskDefinitionCenter.getTaskNodeDefaultParamGroup(nodeCode, props.obj.comIndex)
  //   if (direction == PARAM_DIRECT.IN) {
  //     var tempInputgroup = group.inputDefaultParams;
  //     for (var i = 0; i < tempInputgroup.length; i++) {
  //       if (tempInputgroup[i].prop == paramCode) {
  //         return tempInputgroup[i].rawData
  //       }
  //     }
  //   }
  //   else if (direction == PARAM_DIRECT.OUT) {
  //     var tempOutputgroup = group.outputDefaultParams;
  //     for (var i = 0; i < tempOutputgroup.length; i++) {
  //       if (tempOutputgroup[i].prop == paramCode) {
  //         return tempOutputgroup[i].rawData
  //       }
  //     }
  //   }
  // }
  
  let treeClickCountOut = 0;
  let treeClickCountIn = 0;
  
  const lineStartEnd: string[] = []
  const inputNodeClick = (data: any) => {
    // line.position();
    treeClickCountIn++;
    if (treeClickCountIn >= 2) {
      return;
    }
    window.setTimeout(() => {
      if (treeClickCountIn == 1) {
        //把次数归零
        treeClickCountIn = 0;
        //单击事件处理
        // console.log('单击事件,可在此处理对应逻辑')
  
        lineStartEnd[0] = codeToId(data.code)
        console.log('获取到', lineStartEnd);
        if (lineStartEnd.length == 2) {
          console.log('获取到', lineStartEnd);
          creatLine(lineStartEnd[0], lineStartEnd[1]);
          lineStartEnd.length = 0;
          //console.log('清空', lineStartEnd);
        }
        else if (lineStartEnd.length > 2) {
          lineStartEnd.length = 0;
          //console.log('清空', lineStartEnd);
        }
  
      } else if (treeClickCountIn > 1) {
        //把次数归零
        treeClickCountIn = 0;
        //双击事件
        console.log('双击事件,可在此处理对应逻辑');
      }
    }, 300);
  }
  
  const outputNodeClick = (data: any) => {
    // line.position();
    treeClickCountOut++;
    if (treeClickCountOut >= 2) {
      return;
    }
    window.setTimeout(() => {
      if (treeClickCountOut == 1) {
        //把次数归零
        treeClickCountOut = 0;
        //单击事件处理
        //console.log('单击事件,可在此处理对应逻辑')
  
        lineStartEnd[1] = codeToId(data.code)
        if (lineStartEnd[0] && lineStartEnd[1]) {
          console.log('获取到', lineStartEnd);
          creatLine(lineStartEnd[0], lineStartEnd[1]);
          lineStartEnd.length = 0;
          //console.log('清空', lineStartEnd);
        }
        else if (lineStartEnd.length > 2) {
          lineStartEnd.length = 0;
          //console.log('清空', lineStartEnd);
        }
  
      } else if (treeClickCountOut > 1) {
        //把次数归零
        treeClickCountOut = 0;
        //双击事件
        console.log('双击事件,可在此处理对应逻辑');
      }
    }, 300);
  
  }
  
  function scroll() {
    for (let i = 0; i < lineManage.length; i++) {
      lineManage[i].position();
    }
  }
  
  function expand() {
    for (let time = 1; time < 300; time += 1) {
      setTimeout(function () {
        for (let i = 0; i < lineManage.length; i++) {
          lineManage[i].position();
        }
      }, time);
    }
  }
  
  function collapse() {
    for (let time = 1; time < 300; time += 1) {
      setTimeout(function () {
        for (let i = 0; i < lineManage.length; i++) {
          lineManage[i].position();
        }
      }, time);
    }
  }
  
  function changeLine(code: string) {
    for (var i = 0; i < lineManage.length; i++) {
      //var tempCode = 
    }
  }
  
  function change() {
    console.log("change", activeName.value)
    for (let time = 1; time < 300; time += 1) {
      setTimeout(function () {
        for (let i = 0; i < lineManage.length; i++) {
          lineManage[i].position();
        }
      }, time);
    }
  }
  
  function editInput(data: any) {
    data.editVisible = true
    console.log(data.editVisible)
  }
  function checkInput(data: any) {
    data.editVisible = false
  }
  function testbtn() {
  
  }
  
  function codeToId(code: string) {
    return props.obj.code + "-" + code
  }
  
  function idToCode(code: string) {
    var tempCode = code.split("-")
    
    var resultCode = ''
    for(var i = 1; i < tempCode.length; i++){
      if(resultCode.length != 0) {
        resultCode += "-"
        resultCode += tempCode[i]
      }
      else {
        resultCode += tempCode[i]
      }
    }
    return resultCode
  }
  
  </script>
  
  <style lang="scss" scoped>
  .select-main {
    width: 100%;
    height: 100%;
    margin: 0;
    display: flex;
    justify-content: space-between;
  }
  
  .select-line {
    width: 40%;
    padding: 10px 0;
    text-align: center;
  
    .line-scro {
      height: calc(100vh - 210px);
    }
  
    .box-node {
      box-sizing: border-box;
      width: 97%;
      margin: 5px 0 10px 0;
  
      // max-height: 180px;
      ::v-deep .el-collapse-item__header {
        height: 25px;
        font-size: 13px;
        color: $global_text_color;
        background-color: #efefef;
        padding-left: 5px;
        border-top-right-radius: $global_bd_radius;
        border-top-left-radius: $global_bd_radius;

        //展开禁用的样式修改，禁用在主体的disabled
        cursor: pointer !important;
      }
  
      ::v-deep .el-collapse-item__content {
        padding: 5px 0 0 0;
      }
  
      .from-button-box {
        box-sizing: border-box;
        justify-content: left;
        margin: 0 0 5px 0;
        border-bottom: $global_bd_style;
        border-left: $global_bd_style;
        border-right: $global_bd_style;
        border-color: $global_bd_color;
        border-radius: $global_bd_radius;
  
        ::v-deep .el-tree-node__content {
          box-sizing: border-box;
          width: 100%;
          height: 25px;
          line-height: 25px;
          text-align: left;
          padding-left: 5px;
          color: $global_text_color;
          border-top: $global_bd_style;
          border-color: $global_bd_color;

          //展开禁用
          .el-tree-node__expand-icon{
            //background-color: aqua !important;
            pointer-events: none;
          }
  
          .el-tree-node__label {
            font-size: 12px;
            color: $global_text_color;
          }
  
          .tree-child-box {
            width: calc((100vw - 290px) * 0.40 * 0.97 - 41px);
            //width: 100%;
            display: flex;
            justify-content: space-between;
            text-align: center;
            align-items: center;
            //background-color: aqua;
          }
  
          .tree-child-box-leaf {
            width: calc(100vw * 0.55 * 0.40 * 0.97 - 53px);
            // background-color: aqua;
          }
        }
      }
    }
  
    .output-box {
      box-sizing: border-box;
      width: 97%;
      justify-content: left;
      margin: 0 0 5px 0;
      border-bottom: $global_bd_style;
      border-left: $global_bd_style;
      border-right: $global_bd_style;
      border-color: $global_bd_color;
      border-radius: $global_bd_radius;
  
      ::v-deep .el-tree-node__content {
        box-sizing: border-box;
        width: 100%;
        height: 25px;
        line-height: 25px;
        text-align: left;
        padding-left: 5px;
        color: $global_text_color;
        border-top: $global_bd_style;
        border-color: $global_bd_color;
  
        .el-tree-node__label {
          font-size: 12px;
          color: $global_text_color;
        }
  
        .tree-child-box {
          display: flex;
          justify-content: space-between;
          width: calc((100vw - 290px) * 0.40 * 0.97 - 42px);
          //width: calc(100vw * 0.55 * 0.40 * 0.97 - 42px);
          align-items: center;
  
          // background-color: aqua;
          .tree-child-para {
            margin-right: 25px;
  
          }
  
          .input-group {
            height: 25px;
  
            .tree-inupt {
              height: 20px;
              width: 50px;
            }
          }
  
        }
  
        .tree-child-box-leaf {
          display: flex;
          justify-content: space-between;
          width: calc((100vw - 290px) * 0.40 * 0.97 - 54px);
          //width: calc(100vw * 0.55 * 0.40 * 0.97 - 54px);
          align-items: center;
  
          // background-color: aqua;
          .tree-child-para-leaf {
            margin-right: 25px;
  
          }
  
          .input-group {
            height: 25px;
  
            .tree-inupt {
              height: 20px;
              width: 50px;
            }
          }
        }
      }
    }
  }
  
  .state-item {
    width: 60px;
    height: 30px;
    color: #606266;
    background: #f6f6f6;
    border: 2px solid rgba(0, 0, 0, 0.05);
    text-align: center;
    line-height: 30px;
    font-family: sans-serif;
    border-radius: 4px;
    margin-right: 60px;
  }
  
  .line-wrap {
    display: flex;
    margin-bottom: 40px;
  }
  </style>
  