package com.hml.utils;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.EnvironmentAware;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
public class SpringUtil implements ApplicationContextAware, EnvironmentAware {
   
	private static ApplicationContext applicationContext;
    private static Environment environment;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
    }

    @Override
    public void setEnvironment(Environment environment) {
        SpringUtil.environment = environment;
    }

    public static <T> T getBean(Class<T> clz) {
        return applicationContext.getBean(clz);
    }

    public static String getProperty(String key) {
        return environment.getProperty(key);
    }
    
    public static Object getBean(String beanName) {
    	return applicationContext.getBean(beanName);
    }
}
