package com.cn.hnust.service;

import org.quartz.spi.TriggerFiredBundle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.AutowireCapableBeanFactory;
import org.springframework.scheduling.quartz.AdaptableJobFactory;

public class JobFactoryAuto extends AdaptableJobFactory {
	
	@Autowired
	private AutowireCapableBeanFactory capableBeanFactory;
	@Override
	protected Object createJobInstance(TriggerFiredBundle bundle) throws Exception {
		//���ø���ķ���
	    Object jobInstance = super.createJobInstance(bundle);
	    //����ע��
	    capableBeanFactory.autowireBean(jobInstance);
	    return jobInstance;
	}
	
}
