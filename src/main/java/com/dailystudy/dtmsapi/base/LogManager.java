package com.dailystudy.dtmsapi.base;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by paper on 2016. 5. 25..
 */
public class LogManager {
    private static final Logger logger = LoggerFactory.getLogger(LogManager.class);
    
    public static void requestLog(Class jobClass, String jobId, Object arg) {
        logger.info("[{}] request={}", jobClass.getName() + " : " + jobId, arg);
    }
    
    public static void responseLog(Class jobClass, String jobId, Object arg) {
        logger.info("[{}] response={}", jobClass.getSimpleName() + ": "  + jobId, arg);
    }
}
