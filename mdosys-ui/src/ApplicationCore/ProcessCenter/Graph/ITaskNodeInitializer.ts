import { AbsNodeInitializer } from "./GraphView/NodeInfoGenerator/AbsNodeInitializer";
import { IParamInitializer } from "./Process/TaskInfoGenerator/IParamInitializer";
export interface ITaskNodeInitializer{
    getTaskInitParam():IParamInitializer;
    getNodeInitializer():AbsNodeInitializer;
};
