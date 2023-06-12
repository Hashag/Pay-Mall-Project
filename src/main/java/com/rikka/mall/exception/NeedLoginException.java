package com.rikka.mall.exception;

/**
 * @author Yuno
 * @time 10:29 AM 5/31/2023
 */
public class NeedLoginException extends Exception {
    public NeedLoginException(String msg) {
        super(msg);
    }
}
