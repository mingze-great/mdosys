import { defineStore } from "pinia";
// import { inputParams, inputNodeParams, outputParams, dataFlowParam, lineParams, } from "@/apis/Process/dataFlow";
import { NodeDataParam, ComNodeDataParam, DataFlowInfo, NodeData, PARAM_DIRECT, PARAM_TYPE } from "./taskNode";
import { inputParams, inputNodeParams, outputParams, dataFlowParam, lineParams, dataExcelParams, dataExcelParamsList } from "./dataFlow";
// import { NodeDataParam, ComNodeDataParam, DataFlowInfo } from "@/apis/Task//taskNode";

// import { ProcessContext } from '@/Core/Process/ProcessContext';
// import { TaskDefinitionCenter } from '@/Core/Process/DefinitionCenter/TaskDefinitionCenter';

import { TaskDefinitionData, ParamDetail } from './taskNode';
import { ComponentIndex } from "@/apis/Component/componentInterface";


// const processContext: ProcessContext = ProcessContext.getInstance();
// const taskDefinitionCenter:TaskDefinitionCenter=processContext.taskDefinitionCenter;

const tempDataOut = {
    "code": "201",
    //"msg":success,
    "defaultParams": [
        {
            "prop": "413",
            "dataFlowInfo": "dataFlowInfo",
            "direct": PARAM_DIRECT.IN,
            "type": "type",
            "value": '120',
            "rawData": {
                'id': '413',
                'fileId': 107,
                'name': '喷管入口外径',
                'value': '120',
                'iotype': '输入',
                'sign': '%single',
                'type': '输入文件',
                'unit': 'mm',
                'lowerLimit': 0,
                'upperLimit': 0,
                'columnNames': '',
                'rowNames': '',
                'enumCons': '',
                'rows': 1,
                'columns': 1,
                'remark': ''
            }
        },
        {
            "prop": "438",
            "dataFlowInfo": "dataFlowInfo",
            "direct": PARAM_DIRECT.IN,
            "type": "type",
            "value": '0   -100\r\n1   0\r\n2 100\r\n3 200',
            "rawData": {
                'id': '438',
                'fileId': 107,
                'name': '喉栓运动参数',
                'value': '0   -100\r\n1   0\r\n2 100\r\n3 200\r\n4   110\r\n1   0\r\n5   220\r\n6   320\r\n7   310\r\n8   200',
                'iotype': '输入',
                'sign': '%ComArray',
                'type': '输入文件',
                'unit': 's,mm',
                'lowerLimit': 0,
                'upperLimit': 0,
                'columnNames': '',
                'rowNames': '',
                'enumCons': '',
                'rows': 4,
                'columns': 2,
                'remark': ''
            }
        }
    ]
}

const tempDataIn = [
    {
        "code": "202",
        //"msg":success,
        "defaultParams": [
            {
                "prop": "413",
                "dataFlowInfo": "dataFlowInfo",
                "direct": PARAM_DIRECT.IN,
                "type": "type",
                "value": '120',
                "rawData": {
                    'id': '413',
                    'fileId': 107,
                    'name': '喷管入口外径',
                    'value': '120',
                    'iotype': '输入',
                    'sign': '%single',
                    'type': '输入文件',
                    'unit': 'mm',
                    'lowerLimit': 0,
                    'upperLimit': 0,
                    'columnNames': '',
                    'rowNames': '',
                    'enumCons': '',
                    'rows': 1,
                    'columns': 1,
                    'remark': ''
                }
            },
            {
                "prop": "438",
                "dataFlowInfo": "dataFlowInfo",
                "direct": PARAM_DIRECT.IN,
                "type": "type",
                "value": '0   -100\r\n1   0\r\n2 100\r\n3 200',
                "rawData": {
                    'id': '438',
                    'fileId': 107,
                    'name': '喉栓运动参数',
                    'value': '0   -100\r\n1   0\r\n2 100\r\n3 200\r\n4   110\r\n1   0\r\n5   220\r\n6   320\r\n7   310\r\n8   200',
                    'iotype': '输入',
                    'sign': '%ComArray',
                    'type': '输入文件',
                    'unit': 's,mm',
                    'lowerLimit': 0,
                    'upperLimit': 0,
                    'columnNames': '',
                    'rowNames': '',
                    'enumCons': '',
                    'rows': 4,
                    'columns': 2,
                    'remark': ''
                }
            }
        ]
    },
    {
        "code": "302",
        //"msg":success,
        "defaultParams": [
            {
                "prop": "413",
                "dataFlowInfo": "dataFlowInfo",
                "direct": PARAM_DIRECT.IN,
                "type": "type",
                "value": '120',
                "rawData": {
                    'id': '413',
                    'fileId': 107,
                    'name': '喷管入口外径',
                    'value': '120',
                    'iotype': '输入',
                    'sign': '%single',
                    'type': '输入文件',
                    'unit': 'mm',
                    'lowerLimit': 0,
                    'upperLimit': 0,
                    'columnNames': '',
                    'rowNames': '',
                    'enumCons': '',
                    'rows': 1,
                    'columns': 1,
                    'remark': ''
                }
            },
            {
                "prop": "438",
                "dataFlowInfo": "dataFlowInfo",
                "direct": PARAM_DIRECT.IN,
                "type": "type",
                "value": '0   -100\r\n1   0\r\n2 100\r\n3 200',
                "rawData": {
                    'id': '438',
                    'fileId': 107,
                    'name': '喉栓运动参数',
                    'value': '0   -100\r\n1   0\r\n2 100\r\n3 200\r\n4   110\r\n1   0\r\n5   220\r\n6   320\r\n7   310\r\n8   200',
                    'iotype': '输入',
                    'sign': '%ComArray',
                    'type': '输入文件',
                    'unit': 's,mm',
                    'lowerLimit': 0,
                    'upperLimit': 0,
                    'columnNames': '',
                    'rowNames': '',
                    'enumCons': '',
                    'rows': 4,
                    'columns': 2,
                    'remark': ''
                }
            }
        ]
    }
]
//const tempAllData: any[] = []
// tempAllData.push(tempData)
// tempAllData.push(tempData2)

export const useDataFlowParam = defineStore('useDataFlowParam', {
    state: () => {
        return {
            currentDataFlowParam: {
                code: "111",
                name: '发动机外径' as string,

                inputParamsTree: {} as inputParams,
                outputParamsTree: {} as outputParams,
                LineManageList: [] as Array<DataFlowInfo>,
            } as unknown as dataFlowParam,
        }
    },

    getters: {

    },

    actions: {

        // getStringData(data: string) {
        //     data = data.trim()
        //     data = data.replace("'", "")
        //     data = data.replace("'", "")
        //     return data
        // },

        mapToJson(map: Map<string, any>) {
            //console.log("getmap", map)
            //console.log("getmapsize", map.keys())
            if (map.size > 0) {
                let json = Object.create(null);
                for (let [key, value] of map) {
                    json[key] = value
                }
                // map.forEach((value, key) => {
                //     json[key] = value
                // });
                return json
            }
            return map
        },

        //去空
        arrayDel(data: Array<any>) {
            var newData = data.filter(i => i && i.trim())
            return newData
        },

        //value拆分
        getValueData(data: string) {
            var rowList = []
            rowList = data.split("\r\n")
            //console.log("row", rowList)
            var allList = []
            for (var i = 0; i < rowList.length; i++) {
                var tempColumnList = rowList[i].split(/[\s\n]/)
                tempColumnList = this.arrayDel(tempColumnList)
                allList.push(tempColumnList)
                //console.log("column", tempColumnList)
            }
            return allList
        },

        //unit拆分
        getUnitData(data: string) {
            var list = data.split(",")
            //console.log("danwei", list)
            return list
        },

        // async getParamByCode(list: Array<NodeData>, data: TaskDefinitionCenter) {
        //     var tempAll = []
        //     for (var i = 0; i < list.length; i++) {
        //         var tempData = await data.getTaskNodeDefaultParamGroup(list[i].code, list[i].componentIndex) //TODO
        //         tempAll.push(tempData)
        //     }
        //     return Promise.resolve(tempAll);
        // },

        //获取input树形数据(dataTask?: TaskDefinitionCenter)
        async getInputNodeData(codeList?: Array<NodeData>, dataTask?: any) {
            //const taskNodeList:Array<TaskDefinitionData> = processContext.graphManager.getPreNodeList(code);
            //console.log("codeLiat", codeList)

            //var allData = await this.getParamByCode(codeList, dataTask)
            var allData = tempDataIn

            console.log("默认参数in", allData)
            // var allData = res;   //在这里拿数据源
            var tempInputList = [] as Array<inputNodeParams>;
            for (var num = 0; num < allData.length; num++) {
                var data = allData[num];
                var dataValueList = []

                //var dataParam = data.outputDefaultParams
                var dataParam = data.defaultParams


                for (var i = 0; i < dataParam.length; i++) {
                    //var tempRawData = this.mapToJson(dataParam[i].rawData)
                    var tempRawData = dataParam[i].rawData   //用Map的时候删掉这句用上面那句！！！！！
                    //console.log("getintData", tempRawData)

                    if (tempRawData == null) {
                        //console.log("获取到一个输入空数组", i)
                        var tempSingleData: NodeDataParam = {
                            name: "defaultdata",
                            code: data.code + "-" + dataParam[i].prop,
                            type: "null-type",
                            index: dataParam[i].value,
                            units: "null-units",
                            isLeaf: true,
                        }
                        dataValueList.push(tempSingleData)
                        //console.log("节点总list", dataValueList);
                    }

                    else if (tempRawData.sign == '%single') {
                        var tempSingleData: NodeDataParam = {
                            name: tempRawData.name,
                            code: data.code + "-" + dataParam[i].prop,
                            type: tempRawData.sign,
                            index: dataParam[i].value,
                            units: tempRawData.unit,
                            isLeaf: true,
                        }
                        dataValueList.push(tempSingleData)
                    }
                    else if (tempRawData.sign == '%ComArray') {
                        var valueList = this.getValueData(dataParam[i].value)
                        //console.log("shuzhi", valueList[0][0])
                        var unitList = this.getUnitData(tempRawData.unit)
                        var valueDataParam: Array<NodeDataParam> = []
                        for (var j = 0; j < valueList.length; j++) {
                            for (var n = 0; n < tempRawData.columns; n++) {
                                var tempNodeParam: NodeDataParam = {
                                    name: tempRawData.name + "[" + String(j) + "]" + "[" + String(n) + "]",
                                    code: data.code + "-" + dataParam[i].prop + '-' + String(j * tempRawData.columns + n),
                                    type: "%single",
                                    index: valueList[j][n],
                                    units: unitList[n],
                                    isLeaf: true,
                                }
                                valueDataParam.push(tempNodeParam)
                                //console.log("dantiaoshuju", tempNodeParam)
                            }
                        }
                        var tempComplexData: ComNodeDataParam = {
                            name: tempRawData.name,
                            code: data.code + "-" + dataParam[i].prop,
                            type: tempRawData.sign,
                            column: tempRawData.columns,
                            length: tempRawData.rows,
                            children: valueDataParam,
                            isLeaf: true,
                        }
                        //console.log("整体数值", tempComplexData)
                        dataValueList.push(tempComplexData)
                    }
                }
                var tempNodeList: inputNodeParams = {
                    name: "default",
                    code: data.code,
                    NodeParams: dataValueList,
                };
                tempInputList.push(tempNodeList)
            }
            console.log("输入总list", tempInputList);
            return tempInputList
        },

        //获取output树形数据(dataTask?: TaskDefinitionCenter)
        async getOutputNodeData(code: string, comIndex: ComponentIndex, dataTask?: any) {
            var res = tempDataOut;   //在这里拿数据
            var dataParam = res.defaultParams
            console.log("getOutputNodeData", code);

            // var res = await dataTask.getTaskNodeDefaultParamGroup(code, comIndex)
            // var dataParam = res.inputDefaultParams
            
            console.log("默认参数out", res)
            console.log("获取的input的数量", dataParam);

            var dataValueList = [] as Array<any>
            if (dataParam.length == 0) {
                console.log("没有输出节点")
                var tempSingleData: NodeDataParam = {
                    name: "null",
                    code: "none-code",
                    type: "null-type",
                    index: "000",
                    units: "null-units",
                    isLeaf: true,
                }
                dataValueList.push(tempSingleData)
            }
            else {
                for (var i = 0; i < dataParam.length; i++) {
                    console.log("getoutData", dataParam[i])
                    //var tempRawData = this.mapToJson(dataParam[i].rawData)
                    var tempRawData = dataParam[i].rawData   //用Map的时候删掉这句用上面那句！！！！！
                    if (tempRawData == null) {
                        console.log("获取到一个输出空数组", i)
                        var tempSingleData: NodeDataParam = {
                            name: "null",
                            code: res.code + "-" + dataParam[i].prop,
                            type: "null-type",
                            index: dataParam[i].value,
                            units: "null-units",
                            isLeaf: true,
                        }
                        dataValueList.push(tempSingleData)

                    }

                    if (tempRawData.sign == '%single') {
                        var tempSingleData: NodeDataParam = {
                            name: tempRawData.name,
                            code: res.code + "-" + dataParam[i].prop,
                            type: tempRawData.sign,
                            index: dataParam[i].value,
                            units: tempRawData.unit,
                            isLeaf: true,
                        }
                        dataValueList.push(tempSingleData)
                    }
                    if (tempRawData.sign == '%ComArray') {
                        var valueList = this.getValueData(dataParam[i].value)
                        //console.log("shuzhi", valueList[0][0])
                        var unitList = this.getUnitData(tempRawData.unit)
                        var valueDataParam: Array<NodeDataParam> = []
                        for (var j = 0; j < valueList.length; j++) {
                            for (var n = 0; n < tempRawData.columns; n++) {
                                var tempNodeParam: NodeDataParam = {
                                    name: tempRawData.name + "[" + String(j) + "]" + "[" + String(n) + "]",
                                    code: res.code + "-" + dataParam[i].prop + '-' + String(j * tempRawData.columns + n),
                                    type: "%single",
                                    index: valueList[j][n],
                                    units: unitList[n],
                                    isLeaf: true,
                                }
                                valueDataParam.push(tempNodeParam)
                                //console.log("dantiaoshuju", tempNodeParam)
                            }
                        }
                        var tempComplexData: ComNodeDataParam = {
                            name: tempRawData.name,
                            code: res.code + "-" + dataParam[i].prop,
                            type: tempRawData.sign,
                            column: tempRawData.columns,
                            length: tempRawData.rows,
                            children: valueDataParam,
                            isLeaf: true,
                        }
                        //console.log("整体数值", tempComplexData)
                        dataValueList.push(tempComplexData)
                    }
                }
            }

            var tempNodeList: inputNodeParams = {
                name: "default",
                code: res.code,
                NodeParams: dataValueList,
            };
            return tempNodeList
        },

        getDataIndex(data: any, code: string) {
            var tempNodeCode = code.split("-")[0]
            var tempParamCode = code.split("-")[1]
            var value = []
            //console.log("liangcode", tempNodeCode, tempParamCode)
            for (var i = 0; i < data.length; i++) {
                if (String(data[i].code) == tempNodeCode) {
                    for (var j = 0; j < data[i].defaultParams.length; j++) {
                        if (data[i].defaultParams[j].prop == tempParamCode) {
                            value.push(data[i].defaultParams[j].rawData)
                            value.push(data[i].defaultParams[j].value)
                            return value
                        }
                    }
                }
            }
            return null
        },

        //展示数据的获取
        // getExcelParam(code: string) {
        //     var allList = this.getDataIndex(tempAllData, code)!
        //     var valueList = allList[0]
        //     var valueParamList = this.getValueData(allList[1])
        //     var unitNameList = this.getUnitData(valueList.rowNames)
        //     var allExcelData = []
        //     var allExcelDataList = []
        //     allExcelData.push(unitNameList)
        //     for (var i = 0; i < valueParamList.length; i++) {
        //         var tempDataList: Array<string> = []
        //         for (var j = 0; j < valueParamList[i].length; j++) {
        //             tempDataList.push(valueParamList[i][j])
        //         }
        //         var tempExcelData: dataExcelParams = {
        //             dataList: tempDataList,
        //         }
        //         allExcelDataList.push(tempExcelData)
        //     }
        //     allExcelData.push(allExcelDataList)
        //     console.log("Excel数据", allExcelData)
        //     return allExcelData
        // },


        // getLineManageParam(start: string, end: string, startCode: string, endCode: string, startParam: TaskDefinitionData, endParam: TaskDefinitionData, startRawData: Map<string, any>, endRawData: Map<string, any>) {

        //     var tempLineParam: DataFlowInfo = {
        //         startNodeCode: startCode,
        //         endNodeCode: endCode,
        //         startParamProp: start,
        //         endParamProp: end,
        //     };
        //     this.currentDataFlowParam.LineManageList.push(tempLineParam);

        //     var tempDetail: ParamDetail = {
        //         prop: start,
        //         direct: PARAM_DIRECT.OUT,  // 此处用来标识当前参数的是 输入 还是 输出 
        //         type: PARAM_TYPE.VARCHAR,  // 标识参数类型 后端以此来格式化数据
        //         value: "", // 参数值
        //         dataFlowInfo: tempLineParam,//数据流的定义信息，如果为手动输入 流的输入节点为""
        //         rawData: startRawData,
        //     }

        //     //startParam.taskParams.localParams.push(tempStartDetail)


        //     console.log("现在的线", this.currentDataFlowParam.LineManageList)

        // },

        clearLine() {
            // console.log("连接组", this.currentDataFlowParam.LineManageList)
            // const len = this.currentDataFlowParam.LineManageList.length;
            // for (let i = 0; i < len; i++) {
            //     this.currentDataFlowParam.LineManageList[i].line.remove();
            //   }
            this.currentDataFlowParam.LineManageList.length = 0;
        }

    }


});
