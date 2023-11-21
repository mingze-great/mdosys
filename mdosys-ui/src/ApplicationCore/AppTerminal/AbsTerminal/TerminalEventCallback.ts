export interface TerminalEventCallBack{
    /**
     * 执行自定义命令时触发。
     * success和failed为回调函数，必须调用两个回调其中之一才会回显！
     * 
     * 特别说明：execCmd的success回调参数支持多种数据类型，不同数据类型执行逻辑也会不同：
     *      不传任何参数，立即结束本次执行
     *      传入一个消息对象，将会向记录中追加一条消息，并立即结束本次执行
     *      传入一个消息对象数组，将会向记录中追加多条消息，并立即结束本次执行
     *      传入一个Terminal.$Flash对象，将会进入实时回显处理逻辑，本次执行并不会结束，直到调用finish()
     *      传入一个Terminal.$Ask对象，将会进入用户询问输入处理逻辑，本次执行并不会结束，直到调用finish()
    */
    execCmd?(cmdKey:string, command:string, success:Function, failed:Function, name:string):void;

    /**
     * 用户敲下回车之后执行命令之前触发
     */
    beforeExecCmd?(cmdKey:string, command:string, name:string):void;

    /**
     * 当获取命令输入光标焦点时，按下任意键触发	(event, name)
     */
    onKeydown?(event:any,name:string):void;	

    /**
     * 用户点击按钮时触发，参数key为按钮唯一识别，已有按钮：close、minScreen、fullScreen、title	(key, name)
     */
    onClick?(key:string, name:string):void;

    /**
     * 生命周期函数，插件初始化之前触发	(name)
     */
    initBefore?(name:string):void;
    
    /**
     * 生命周期函数，插件初始化完成之后触发	(name)
     */
    initComplete?(name:string):void;	

    /**
     * 用户键入Tab键时的逻辑处理方法，可配合helpCmd这个slot使用	(event)
     */
    tabKeyHandler?(event:any):void;	

}
