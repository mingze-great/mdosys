import { defineStore, Store } from "pinia";
import { inputParams, inputNodeParams, outputParams, dataFlowParam, lineParams, } from "@/apis/Process/dataFlow";
import { NodeDataParam, ComNodeDataParam, DataFlowInfo } from "@/apis/Process/taskNode";
import LeaderLine from '@/plugins/LEADERLINE/DivLine.js';
import { type } from "os";
import { da } from "element-plus/es/locale";

