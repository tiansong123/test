package com.cn.hnust.service;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.annotation.Resource;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class JobFactory implements Job {

	@Resource
	private IUserService userService;
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		System.out.println("------------------------");
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd hh:mm:ss");
		System.out.println(sdf.format(new Date()));
		System.out.println(userService.getUserById(1));
	}

}
