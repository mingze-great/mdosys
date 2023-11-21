export class Logger{
    private constructor(){
        throw "工具类无法实例化";
    }
    public static log(message?: any, ...optionalParams: any[]){
        console.log(message, ...optionalParams)
    }
}
