package com.xmsmartcity.maintain.common;

import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.Modifier;
import javassist.NotFoundException;
import javassist.bytecode.CodeAttribute;
import javassist.bytecode.LocalVariableAttribute;
import javassist.bytecode.MethodInfo;
import net.sf.json.JSONObject;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.xmsmartcity.maintain.controller.util.SendEmailUtils;
import com.xmsmartcity.util.Constant;

public class InterceptedExceptionAspect{
	
	private ExecutorService fixedThreadPool = Executors.newFixedThreadPool(2);
	
	private String[] mails = null;//{"wugw@xmsmartcity.com","chenby@xmsmartcity.com"};
	
	public String[] getMails() {
		return mails;
	}
	
	public void setMails(String[] mails) {
		this.mails = mails;
	}

	/**
	 * 报错日志发送到指定邮箱
	 * @author:zhugw
	 * @time:2017年6月19日 下午3:35:48
	 * @param point
	 * @return
	 */
	public Object exeAround(ProceedingJoinPoint point){
		InetAddress addr = getAddress();
		String requestContent = "HOST: "+(addr != null ? addr.getHostName(): "") + " <br/>";
		try {
			//日志输出
			Object target = point.getTarget();
			Logger logger = Logger.getLogger(target.getClass());
			RequestAttributes ra = RequestContextHolder.getRequestAttributes();
	        ServletRequestAttributes sra = (ServletRequestAttributes) ra;
	        HttpServletRequest request = sra.getRequest();
	        String userAgent = request.getHeader("user-agent");
			logger.info("visit client: "+userAgent);
			String host = CommonUtils.getRemoteHost(request);
			requestContent = requestContent+" visit client:"+host+" "+userAgent+" <br/>";
			logger.info("method: "+point.getSignature().getDeclaringTypeName()+"." + point.getSignature().getName());
			requestContent = requestContent+ "method: "+point.getSignature().getDeclaringTypeName()+"." + point.getSignature().getName()+"<br/>";
			String classType = point.getTarget().getClass().getName();  
	        Class<?> clazz = Class.forName(classType);
	        String clazzName = clazz.getName();
	        String methodName = point.getSignature().getName();
	        String[] paramNames = getFieldsName(this.getClass(), clazzName, methodName);
			Object[] objs = point.getArgs();
			for(int i =0;i<objs.length;i++){
				if(objs[i] != null){
					if(objs[i].getClass().getAnnotation(Entity.class) != null){
						logger.info("param: "+paramNames[i]+"="+JSONObject.fromObject(objs[i]).toString());
						requestContent = requestContent+"param: "+paramNames[i]+"="+JSONObject.fromObject(objs[i]).toString()+"<br/>";
					}else{
						logger.info("param: "+paramNames[i]+"="+objs[i]);
						requestContent = requestContent+"param: "+paramNames[i]+"="+objs[i]+"<br/>";
					}
				}
			}
			Object result = point.proceed();
			logger.info("result: "+result);
			return result;
		} catch (Throwable e) {
			e.printStackTrace();
			return toFailJson(e.getMessage(), requestContent, e);
		}
	}
	
	/**
	 * 转换成失败json
	 * @param msg
	 * @return
	 */
	private String toFailJson(String msg,String requestContent,Throwable e){
		e.getClass();
		Logger logger = Logger.getLogger(e.getClass());
		if(!StringUtils.isNotEmpty(msg)){
			msg = Constant.SERVICE_ERROR;
		}
		JSONObject obj = new JSONObject();
		if(e != null){
			String[] tempArr = null != msg ? msg.split("_") : null;
			if(null != tempArr && tempArr.length > 1 && "paramsError".equals(tempArr[0])){
				toSendEmail(e,requestContent);
			}
			if(!(e instanceof IllegalStateException) && !(e instanceof IllegalArgumentException)){
        		msg = "serviceError_服务器异常，请稍后重试";
        		toSendEmail(e,requestContent);
        	}
        }
		logger.error(msg);
		// msg = "isOtherDeviceLogin_您的帐号已在其他设备上登录。"
		// msg = "isOtherDeviceLogin_您的帐号已被禁用，请联系客服人员。"
		String[] tempArr = null != msg ? msg.split("_") : null;
		String newMsg = null;
		if(null != tempArr && tempArr.length > 1){
			// 这里的tempArr[0] 是 isOtherDeviceLogin 在 ApiHelper -> validateToken 方法中
			if("isOtherDeviceLogin".equals(tempArr[0])){
				//您的帐号已在其他设备上登录
				obj.put("info", tempArr[1]);
				obj.put("code", -1);
			}else if("paramsError".equals(tempArr[0])){
				//参数错误，请联系开发人员
				obj.put("info", tempArr[1]);
				obj.put("code", 101);
			}else if("serviceError".equals(tempArr[0])){
				//服务器异常，请稍后重试
				obj.put("info", tempArr[1]);
				obj.put("code", 102);
			}else if("accountDeprecated".equals(tempArr[0])){
				//您的账号已被禁用，请联系客服人员。
				obj.put("info", tempArr[1]);
				obj.put("code", 103);
			}
		}else{
			newMsg = msg;
			obj.put("code", 1);
			obj.put("info", newMsg);
		}
		obj.put("data", new Object());
		return obj.toString();
	}
	
	/**
	 * 发送邮件
	 * @author:zhugw
	 * @time:2017年11月29日 上午11:14:38
	 * @param e
	 * @param requestContent
	 */
	public void toSendEmail(Throwable e,String requestContent){
		StackTraceElement[] errors = e.getStackTrace();
		String msgs = "";
		for(StackTraceElement error : errors){
			msgs = msgs+"className: "+error.getClassName()+"."+error.getMethodName()+":"+error.getLineNumber()+"<br/>";
		}
		final String content = requestContent+"<br/> error message: "+e.getMessage()+"<br/>"+msgs;
		for(final String mail : mails){
    		fixedThreadPool.execute(new Runnable() {  
    			public void run() {    				       
					SendEmailUtils sendMail = new SendEmailUtils(mail);
					sendMail.sendHtmlMail("咚咚维保云平台报错信息",content);        			
    			}
    		}); 
		}
	}
	
	/** 
     * 得到方法参数的名称 
     * @param cls 
     * @param clazzName 
     * @param methodName 
     * @return 
     * @throws NotFoundException 
     */
	private String[] getFieldsName(Class<?> cls, String clazzName, String methodName) throws NotFoundException{  
        ClassPool pool = ClassPool.getDefault();
        ClassClassPath classPath = new ClassClassPath(cls);  
        pool.insertClassPath(classPath);  
          
        CtClass cc = pool.get(clazzName);  
        CtMethod cm = cc.getDeclaredMethod(methodName);  
        MethodInfo methodInfo = cm.getMethodInfo();  
        CodeAttribute codeAttribute = methodInfo.getCodeAttribute();  
        LocalVariableAttribute attr = (LocalVariableAttribute) codeAttribute.getAttribute(LocalVariableAttribute.tag);  
        if (attr == null) {  
            // exception  
        }  
        String[] paramNames = new String[cm.getParameterTypes().length];  
        int pos = Modifier.isStatic(cm.getModifiers()) ? 0 : 1;  
        for (int i = 0; i < paramNames.length; i++){  
            paramNames[i] = attr.variableName(i + pos); //paramNames即参数名  
        }  
        return paramNames;  
    }
	
	/**
	 * 获取远程地址
	 * @author:zhugw
	 * @time:2017年11月29日 上午10:59:39
	 * @return
	 */
	private static InetAddress getAddress() {
        try {
            for (Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces(); interfaces.hasMoreElements();) {
                NetworkInterface networkInterface = interfaces.nextElement();
                if (networkInterface.isLoopback() || networkInterface.isVirtual() || !networkInterface.isUp()) {
                    continue;
                }
                Enumeration<InetAddress> addresses = networkInterface.getInetAddresses();
                if (addresses.hasMoreElements()) {
                    return addresses.nextElement();
                }
            }
        } catch (SocketException e) {
        	//logger.debug("Error when getting host ip address: <{}>.", e.getMessage());
        }
        return null;
    }
	
}
