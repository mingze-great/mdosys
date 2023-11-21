export enum ExecuteType {


    /**
     * operation type
     * 1 repeat running
     * 2 resume pause
     * 3 resume failure
     * 4 stop
     * 5 pause
     */
    NONE, 
    REPEAT_RUNNING, 
    RECOVER_SUSPENDED_PROCESS, 
    START_FAILURE_TASK_PROCESS, 
    STOP, 
    PAUSE
}
export enum FailureStrategy {
    END,
    CONTINUE
}

export enum TaskDependType {
    TASK_ONLY,
    TASK_PRE,
    TASK_POS
}

export enum CommandType {
    /**
    * command types
    * 0 start a new process
    * 1 start a new process from current nodes
    * 2 recover tolerance fault process
    * 3 recover suspended process
    * 4 start process from failure task nodes
    * 5 complement data
    * 6 start a new process from scheduler
    * 7 repeat running a process
    * 8 pause a process
    * 9 stop a process
    * 10 recover waiting thread
    */
    START_PROCESS,
    START_CURRENT_TASK_PROCESS,
    RECOVER_TOLERANCE_FAULT_PROCESS,
    RECOVER_SUSPENDED_PROCESS,
    START_FAILURE_TASK_PROCESS,
    COMPLEMENT_DATA,
    SCHEDULER,
    REPEAT_RUNNING,
    PAUSE,
    STOP,
    RECOVER_WAITING_THREAD,
}


export enum WarningType {
    /**
  * 0 do not send warning;
  * 1 send if process success;
  * 2 send if process failed;
  * 3 send if process ending;
  */
    NONE,
    SUCCESS,
    FAILURE,
    ALL
}

export enum RunMode{
    /**
     * 0 serial run
     * 1 parallel run
     * */
    RUN_MODE_SERIAL,
    RUN_MODE_PARALLEL
}

export enum Priority{
     /**
     * 0 highest priority
     * 1 higher priority
     * 2 medium priority
     * 3 lower priority
     * 4 lowest priority
     */
     HIGHEST,
     HIGH,
     MEDIUM,
     LOW,
     LOWEST
}


export enum Flag {
    /**
     * 0 no
     * 1 yes
     */
    NO,
    YES
}


export enum TimeoutFlag {
    /**
     * 0 close
     * 1 open
     */
    CLOSE,
    OPEN
}

export enum TaskTimeoutStrategy {
    /**
     * 0 warn
     * 1 failed
     * 2 warn+failed
     */
    WARN,
    FAILED,
    WARNFAILED
}


export enum Direct {
    IN = "IN",
    OUT = "OUT",
}
export enum DataType {
    VARCHAR = "VARCHAR",
    INTEGER = "INTEGER",
    LONG = "LONG",
    FLOAT = "FLOAT",
    DOUBLE = "DOUBLE",
    DATE = "DATE",
    TIME = "TIME",
    TIMESTAMP = "TIMESTAMP",
    BOOLEAN = "BOOLEAN",
    LIST = "LIST"
}


export enum ExecutionStatus{
    SUBMITTED_SUCCESS,
    RUNNING_EXECUTION,
    READY_PAUSE,
    PAUSE,
    READY_STOP,
    STOP,
    FAILURE,
    SUCCESS,
    NEED_FAULT_TOLERANCE,
    KILL,
    WAITING_THREAD,
    WAITING_DEPEND,
    DELAY_EXECUTION,
    FORCED_SUCCESS
}
