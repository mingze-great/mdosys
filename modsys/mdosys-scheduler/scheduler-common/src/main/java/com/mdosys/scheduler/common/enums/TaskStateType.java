package com.mdosys.scheduler.common.enums;

/**
 * type of task state
 */
public enum TaskStateType {
    /**
     * 0 waiting running
     * 1 running
     * 2 finish
     * 3 failed
     * 4 success
     */
    WAITTING, RUNNING, FINISH, FAILED, SUCCESS;

    /**
     * convert task state to execute status integer array ;
     *
     * @param taskStateType task state type
     * @return result of execution status
     */
    public static int[] convert2ExecutStatusIntArray(TaskStateType taskStateType) {

        switch (taskStateType) {
            case SUCCESS:
                return new int[]{ExecutionStatus.SUCCESS.ordinal()};
            case FAILED:
                return new int[]{
                        ExecutionStatus.FAILURE.ordinal(),
                        ExecutionStatus.NEED_FAULT_TOLERANCE.ordinal()};
            case FINISH:
                return new int[]{
                        ExecutionStatus.PAUSE.ordinal(),
                        ExecutionStatus.STOP.ordinal()
                };
            case RUNNING:
                return new int[]{ExecutionStatus.SUBMITTED_SUCCESS.ordinal(),
                        ExecutionStatus.RUNNING_EXECUTION.ordinal(),
                        ExecutionStatus.DELAY_EXECUTION.ordinal(),
                        ExecutionStatus.READY_PAUSE.ordinal(),
                        ExecutionStatus.READY_STOP.ordinal()};
            case WAITTING:
                return new int[]{
                        ExecutionStatus.SUBMITTED_SUCCESS.ordinal()
                };
            default:
                break;
        }
        return new int[0];
    }

}
