import { defineStore } from "pinia";
import {
  ComponentType,
  ComponentDetail,
  ComponentIndex,
  ComponentInfoDetail,
} from "@/apis/Component/componentInterface";
import {
  componentClassIndex,
  componentClassDetail,
} from "@/apis/Component/componentClassInterface";
import {
  getComponentMenuList,
  getComponentList,
  getComponentInfo,
} from "@/apis/system/component";
import {
  getWrapperList,
  getWrapperDetail,
  getFileList,
  getInputList,
  getOutputList,
  BasicInfo,
  IOParam,
  File,
} from "@/apis/system/pluginWrapper";
import { handleTree, addTreeLeafs } from "@/utils/listToTree";
export const useCompStore = defineStore("compStore", {
  state: () => ({
    comClassIndex: [] as Array<componentClassIndex>,
    comIndex: [] as Array<ComponentIndex>,
    componentDetails: [] as Array<ComponentDetail>,
    comTree: [] as Array<any>,
    comInfoDetails: [] as Array<ComponentInfoDetail>,
    wrappers: [] as Array<BasicInfo>,
  }),
  getters: {
    getComponentInfoByComIndex() {
      return async (comIndex: ComponentIndex): Promise<ComponentInfoDetail> => {
        for (let index = 0; index < this.comInfoDetails.length; index++) {
          const comInfoDetail = this.comInfoDetails[index];
          if (comInfoDetail.id == comIndex.id) {
            return Promise.resolve(comInfoDetail);
          }
        }

        // 封装器组件
        for (let index = 0; index < this.wrappers.length; index++) {
          const basicInfo = this.wrappers[index];
          if (basicInfo.id == comIndex.code) {
            let res = await getWrapperDetail(String(comIndex.id));
            let comInfoDetails = {
              resourceIds: [],
              inputDefaultParams: [],
              outputDefaultParams: [],
              code: res.data.code,
              runScript: "",
              id: 0,
              name: res.data.name,
              classId: "",
              createTime: "",
              updateTime: "",
              icon: "",
              userId: "",
              publicComp: "",
              param: "",
              description: "",
              type: ComponentType.WRAPPER,
            };
            this.comInfoDetails.push(comInfoDetails);
            return Promise.resolve(comInfoDetails);
          }
        }

        // 专业组件
        let res = await getComponentInfo(comIndex.id);
        this.comInfoDetails.push(res.data);
        return Promise.resolve(res.data);
      };
    },
  },
  actions: {
    getComponentInfoById(id: number) {
      return new Promise((resolve: any, reject: any) => {
        getComponentInfo(id).then((res) => {
          this.comInfoDetails.push(...res.data);
        });
      });
    },
    queryComIndexs() {
      this.comIndex.length = 0;
      return new Promise((resolve: any, reject: any) => {
        getComponentList()
          .then((res) => {
            this.comIndex.push(...res.data);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    queryComClassIndexs() {
      this.comClassIndex.length = 0;
      return new Promise((resolve: any, reject: any) => {
        getComponentMenuList()
          .then((res) => {
            this.comClassIndex.push(...handleTree(res.data, null, null, null));
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
    // 获取组件目录完整结构
    async queryComTree() {
      if (this.comTree == undefined || this.comTree.length == 0) {
        if (this.comClassIndex == undefined || this.comClassIndex.length == 0) {
          await this.queryComClassIndexs();
        }
        if (this.comIndex == undefined || this.comIndex.length == 0) {
          await this.queryComIndexs();
        }
        this.comTree = addTreeLeafs(this.comClassIndex, this.comIndex);
        if (this.wrappers == undefined || this.wrappers.length == 0) {
          await this.getWrapperList();
        }
        let i = 0;
        for (let t of this.comTree) {
          if (t.name == "软件接口") {
            let j = 0;
            for (let t of this.wrappers) {
              this.wrappers[j].type = ComponentType.WRAPPER;
              j++;
            }
            this.comTree[i].children.push(...this.wrappers);
            break;
          }
          i++;
        }
        await this.queryComClassIndexs();
      }
    },
    async updateComTree() {
      this.comTree.length = 0;
      await this.queryComClassIndexs();
      await this.queryComIndexs();
      this.comTree = addTreeLeafs(this.comClassIndex, this.comIndex);
      await this.queryComClassIndexs();
    },
    async updateComClassTree() {
      await this.queryComClassIndexs();
    },
    async getWrapperList() {
      return new Promise((resolve, reject) => {
        getWrapperList()
          .then((res) => {
            this.wrappers.push(...res.data);
            resolve(res);
          })
          .catch((error) => {
            reject(error);
          });
      });
    },
  },
});

export const useWrapperStore = defineStore("wrapperStore", {
  state: () => {
    return {
      wrapperTree: [
        {
          name: "封装器模块",
          children: [] as Array<BasicInfo>,
        },
      ],
      wrappers: [] as Array<BasicInfo>,
      basicInfo: {} as BasicInfo,
      files: [] as Array<File>,
      inputs: [] as Array<IOParam>,
      outputs: [] as Array<IOParam>,
    };
  },
  actions: {
    async getWrapperList() {
      try {
        this.wrappers.splice(0, this.wrappers.length);
        const res = await getWrapperList();
        this.wrappers.unshift(...res.data);
        this.wrapperTree[0].children.splice(
          0,
          this.wrapperTree[0].children.length
        );
        this.wrapperTree[0].children.unshift(...res.data);
        return Promise.resolve(res);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    async getBasicInfo(pluginId: string) {
      try {
        const res = await getWrapperDetail(pluginId);
        this.basicInfo = res.data;
        return Promise.resolve(res);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    async getFileList(pluginId: string) {
      try {
        this.files.splice(0, this.files.length);
        const res = await getFileList(pluginId);
        this.files.unshift(...res.data);
        return Promise.resolve(res);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    async getInputList(pluginId: string) {
      try {
        this.inputs.splice(0, this.inputs.length);
        const res = await getInputList(pluginId);
        this.inputs.unshift(...res.data);
        return Promise.resolve(res);
      } catch (error) {
        return Promise.reject(error);
      }
    },
    async getOutputList(pluginId: string) {
      try {
        this.outputs.splice(0, this.outputs.length);
        const res = await getOutputList(pluginId);
        this.outputs.unshift(...res.data);
        return Promise.resolve(res);
      } catch (error) {
        return Promise.reject(error);
      }
    },
  },
  getters: {},
});
