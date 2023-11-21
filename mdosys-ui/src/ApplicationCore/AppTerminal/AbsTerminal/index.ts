import terminal from "vue-web-terminal";
import { TerminalMessage, TerminalMessageLevel, TerminalMessageType } from "./TerminalMessage";
export abstract class AbsTerminal {
    protected _terminalId: string;
    protected _terminalAPI: any = terminal.$api;
    constructor(terminalId: string) {
        this._terminalId = terminalId;
    }

    public pushMessage(...terminalmessage: Array<TerminalMessage>) {
        this._terminalAPI.pushMessage(this._terminalId, terminalmessage);
    }

    public execute(command: string) {
        this._terminalAPI.execute(this._terminalId, command);
    }

    public normal(msg: string|JSON|object|Array<any>,type?:TerminalMessageType) {
        let message: TerminalMessage = {
            content: msg,
            type:type,
        }
        this.pushMessage(message)
    }

    public info(msg: string|JSON|object|Array<any>,type?:TerminalMessageType) {
        let message: TerminalMessage = {
            content: msg,
            class:TerminalMessageLevel.info,
            type:type,
        }
        this.pushMessage(message)
    }

    public error(msg:string|JSON|object|Array<any>,type?:TerminalMessageType){
        let message: TerminalMessage = {
            content: msg,
            class:TerminalMessageLevel.error,
            type:type,
        }
        this.pushMessage(message)
    }

    public warning(msg:string|JSON|object|Array<any>,type?:TerminalMessageType){
        let message: TerminalMessage = {
            content: msg,
            class:TerminalMessageLevel.warning,
            type:type,
        }
        this.pushMessage(message)
    }

    public success(msg:string|JSON|object|Array<any>,type?:TerminalMessageType){
        let message: TerminalMessage = {
            content: msg,
            class:TerminalMessageLevel.success,
            type:type,
        }
        this.pushMessage(message)
    }

    public system(msg:string|JSON|object|Array<any>,type?:TerminalMessageType){
        let message: TerminalMessage = {
            content: msg,
            class:TerminalMessageLevel.system,
            type:type,
        }
        this.pushMessage(message)
    }

}
