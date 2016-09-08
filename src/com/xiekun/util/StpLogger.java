package com.xiekun.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.ResourceBundle;

import org.apache.log4j.Category;
import org.apache.log4j.Priority;
import org.apache.log4j.PropertyConfigurator;

/**
 * 日志工具类
 */
public class StpLogger {
 
    public static final String noModuleModule = "Report";  // set to null for previo us behavior
    private static Map logModuleMap = new HashMap();
    private static String callingClass = "com.xiekun.util.StpLogger";
    private static String DEFAULT_PATH = GetPath.getConfigPath();
	private static String DEFAULT_FILE = "log4j.properties";
 
    static {
        //initialize Log4J
    	Properties properties = new Properties();
    	
    	synchronized (StpLogger.class) {
    		try {
				InputStream in = new FileInputStream(DEFAULT_PATH + DEFAULT_FILE);
				properties.load(in);
				in.close();
			} catch (Exception e) {
				
				e.printStackTrace();
				throw new RuntimeException(e);
			}
    	}
    	
    	PropertyConfigurator.configure(properties);
    }
 
    public static void main(String[] args){
    	
    }
    public static Category getLogger(String module) {
        if (module != null && module.length() > 0) {
            return Category.getInstance(module);
        } else {
            return Category.getInstance(noModuleModule);
        }
    }
    
    /**
     * 以当前线程的ID为KEY设置日志模块名
     * @param logModuleName
     */
    public static void setLogModuleName(String logModuleName)
    {
    	if (logModuleName == null || "".equals(logModuleName)){
    		return;
    	}
    	//设置logModuleMap的size为50,如果检查有超过50立即强行清除
    	if (logModuleMap.size()>50)
    	{
    		logModuleMap.clear();
    	}
    	String key = String.valueOf(Thread.currentThread().hashCode());
    	logModuleMap.put(key,logModuleName);
    }
    
    /**
     * 获取当前线程的的日志模块名，默认为noModuleModule值
     * @return
     */
    public static String getLogModuleName()
    {
    	String logModuleName = null;
    	String key = String.valueOf(Thread.currentThread().hashCode());
    	logModuleName = (String)logModuleMap.get(key);
    	return (logModuleName==null || "null".equals(logModuleName))?noModuleModule:logModuleName;
    }
    
    /**
     * 删除当前线程保存的模块名
     * 注意：在调用了setLogModuleName后一定要记得调用removeLogModuleName
     */
    public static void removeLogModuleName()
    {
    	String key = String.valueOf(Thread.currentThread().hashCode());
    	if (logModuleMap.get(key) != null){
    		logModuleMap.remove(key);
    	}
    }
    
    public static void log(Priority level, Throwable t, String msg, String module) {
    	Category logger = getLogger(module);
    	logger.log(callingClass, level, msg, t);
    }    
    
    /**
     * 打印异常信息
     * @param msg 说明信息
     * @param t 异常
     */
    public static void error(Object msg,Throwable t)
    {
    	if(msg!=null){
	    	String module = getLogModuleName();
			Category logger = getLogger(module);
			logger.error(msg.toString(),t);
    	}
    }
 
    /**
     * 分模块打印异常信息
     * @param msg 说明信息
     * @param t 异常
     * @param module 模块名
     */
    public static void error(Object msg,Throwable t,String module)
    {
    	if(msg!=null){
			Category logger = getLogger(module);
			logger.error(msg.toString(),t);   
    	}
    } 
    
    /**
     * 打印Debug等级的信息
     * @param msg 打印内容
     */
    public static void Debug(String msg) {
        log(Priority.DEBUG, null, msg, getLogModuleName());
    }

    /**
     * 分模块打印Debug等级的信息
     * @param msg 打印内容
     * @param module 模块名
     */
    public static void Debug(String msg, String module) {
        log(Priority.DEBUG, null, msg, module);
    }

    public static void Debug(Throwable t) {
        log(Priority.DEBUG, t, null, getLogModuleName());
    }

    public static void Debug(Throwable t, String msg) {
        log(Priority.DEBUG, t, msg, getLogModuleName());
    }

    public static void Debug(Throwable t, String msg, String module) {
        log(Priority.DEBUG, t, msg, module);
    }
    
    /**
     * 打印Info等级的信息
     * @param msg 打印内容
     */
    public static void logInfo(String msg) {
		log(Priority.INFO, null, msg, getLogModuleName());

	}

    /**
     * 分模块打印Info等级的信息
     * @param msg 打印内容
     * @param module 模块名
     */
    public static void logInfo(String msg, String module) {
        log(Priority.INFO, null, msg, module);
    }

    public static void logInfo(Throwable t) {
        log(Priority.INFO, t, null, getLogModuleName());
    }

    public static void logInfo(Throwable t, String msg) {
        log(Priority.INFO, t, msg, getLogModuleName());
    }

    public static void logInfo(Throwable t, String msg, String module) {
        log(Priority.INFO, t, msg, module);
    }

    /**
     * 打印Warn等级的信息
     * @param msg 打印内容
     */
    public static void logWarning(String msg) {
    	log(Priority.WARN, null, msg, getLogModuleName());
	}

    /**
     * 分模块打印Warn等级的信息
     * @param msg 打印内容
     * @param module 模块名
     */
    public static void logWarning(String msg, String module) {
        log(Priority.WARN, null, msg, module);
    }

    public static void logWarning(Throwable t) {
        log(Priority.WARN, t, null, getLogModuleName());
    }

    public static void logWarning(Throwable t, String msg) {
        log(Priority.WARN, t, msg, getLogModuleName());
    }

    public static void logWarning(Throwable t, String msg, String module) {
        log(Priority.WARN, t, msg, module);
    }

    /**
     * 打印Error等级的信息
     * @param msg 打印内容
     */
    public static void logError(String msg) {
    	log(Priority.ERROR, null,msg, getLogModuleName());
	}

    /**
     * 分模块打印Error等级的信息
     * @param msg 打印内容
     * @param module 模块名
     */
    public static void logError(String msg, String module) {
        log(Priority.ERROR, null, msg, module);
    }

    public static void logError(Throwable t) {
        log(Priority.ERROR, t, null, getLogModuleName());
    }

    public static void logError(Throwable t, String msg) {
        log(Priority.ERROR, t, msg, getLogModuleName());
    }

    public static void logError(Throwable t, String msg, String module) {
        log(Priority.ERROR, t, msg, module);
    }

    /**
     * 打印Fatal等级的信息
     * @param msg 打印内容
     */
    public static void logFatal(String msg) {
        log(Priority.FATAL, null, msg, getLogModuleName());
    }

    /**
     * 分模块打印Fatal等级的信息
     * @param msg 打印内容
     * @param module 模块名
     */
    public static void logFatal(String msg, String module) {
        log(Priority.FATAL, null, msg, module);
    }

    public static void logFatal(Throwable t) {
        log(Priority.FATAL, t, null, getLogModuleName());
    }

    public static void logFatal(Throwable t, String msg) {
        log(Priority.FATAL, t, msg, getLogModuleName());
    }

    public static void logFatal(Throwable t, String msg, String module) {
        log(Priority.FATAL, t, msg, module);
    }
}