export enum TerminalMessageType{
    normal="normal",
    json="json",
    code="code",
    table="table",
    html="html"
}
export enum TerminalMessageLevel{
    success="success",
    error="error",
    system="system",
    info="info",
    warning="warning"
}
export interface TerminalMessage{
    content:string|JSON|object|Array<any>;
    type?:TerminalMessageType;
    class?:TerminalMessageLevel;
    tag?:string;
}
