package com.mdosys.scheduler.common.utils;

/**
 * tri function function interface
 */
@FunctionalInterface
public interface TriFunction<IN1, IN2, IN3, OUT1> {

    OUT1 apply(IN1 in1, IN2 in2, IN3 in3);

}
