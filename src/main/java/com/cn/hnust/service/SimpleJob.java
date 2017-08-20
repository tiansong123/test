package com.cn.hnust.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * �򵥵�job��
 * ʵ��job�ӿ�
 * @author sea
 *
 */
public class SimpleJob implements Job{

	public void execute(JobExecutionContext context) throws JobExecutionException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy��MM��dd�� HH:mm:ss");
		System.out.println(dateFormat.format(new Date()) +" : " + context.getJobDetail().getKey());
	}

}
