import { AbstractEvent, EventType, IEventConsumer } from "@/ApplicationCore/EventCenter";
import { ProcessEvent } from "@/ApplicationCore/ProcessCenter/common";
import { Ref, ref } from "vue";
import { AbsTerminal } from "../AbsTerminal";
import { TerminalEventCallBack } from "../AbsTerminal/TerminalEventCallback";
import { TerminalMessage } from "../AbsTerminal/TerminalMessage";

export class ProcessTerminal extends AbsTerminal implements TerminalEventCallBack,IEventConsumer{
    public static readonly TERMINAL_NAME="process_bottom_terminal";
    private _context:Ref<string>;
    priority:number= 1000;
    constructor(){
        super(ProcessTerminal.TERMINAL_NAME);
        this._context=ref("MDOSys");
    }

    
    public get context() : Ref<string> {
        return this._context;
    }
    
    execCmd(cmdKey: string, command: string, success: Function, failed: Function, name: string): void {
        if (cmdKey === "fail") {
            failed("Something wrong!!!");
        } else if (cmdKey === "log") {
            success({
                type: "normal",
                class: "info",
                tag: "log",
                content: command,
            });
        } else{
            let msg:TerminalMessage={
                content:command
            }
            success(msg)
        }
    }

    beforeExecCmd(cmdKey: string, command: string, name: string): void {
        console.log("beforeExecCmd",cmdKey,command);
    }

    initBefore(name: string): void {
        
    }
    initComplete(name: string): void {
        console.log("init complete", name);
        this.info(name);
    }

    tabKeyHandler(event: any): void {
        
    }
    onClick(key: string, name: string): void {
        
    }

    onKeydown(event: any, name: string): void {
        
    }

    action(event: AbstractEvent): boolean {
        console.log("terminal,action", event);
        if (!(event instanceof ProcessEvent)) {
            return false;
        }
        let message: any;
        switch (event.type) {
            case EventType.PROCESS_MSG:
                let msg = event.content?.msg;
                if (msg?.length == 0) {
                    message = {
                        class: "warning",
                        content: "无数据",
                    };
                } else {
                    message = {
                        class: "info",
                        type: "json",
                        content: event.content?.msg,
                    };
                }

                break;
            default:
                message = {
                    type: "normal",
                    class: "info",
                    content: event.content?.msg,
                    tag: "log",
                };
                break;
        }
        this.pushMessage(message);
        //返回true表示事件不在向下传递，false表示继续由其他观察者处理
        return false;
    }

}
