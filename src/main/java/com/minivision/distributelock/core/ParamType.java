package com.minivision.distributelock.core;

public enum ParamType {
    /**
     * 将参数的hashcode加入到lock名称中
     */
    HASHCODE,
    /**
     * 将参数的TOString方法结果加入到lock名称中
     */
    TOSTRING;
}
