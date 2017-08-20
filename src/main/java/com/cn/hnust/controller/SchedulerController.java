package com.cn.hnust.controller;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.cn.hnust.pojo.PageTrigger;
import com.cn.hnust.service.JobService;

/**
 * 控制器
 * 
 * @author sea
 *
 */
@Controller
public class SchedulerController {

	private boolean init = false;

	@Autowired
	private JobService jobService;

	/**
	 * 跳转到show页面
	 * 
	 * @param request
	 * @return
	 */
	@RequestMapping("/show")
	public String show(HttpServletRequest request) {
		if (!init) {
			jobService.stopScheduler();
		}

		getTriggers(request);
		return "show";
	}

	@RequestMapping("/{name}/{group}/stop")
	public String stop(@PathVariable String name, @PathVariable String group) {
		jobService.stopJob(name, group);
		return "redirect:/show";
	}

	@RequestMapping("/{name}/{group}/del")
	public String del(@PathVariable String name, @PathVariable String group) {
		jobService.delJob(name, group);
		return "redirect:/show";
	}

	@RequestMapping("/{name}/{group}/{cron}/modify")
	public String modify(@PathVariable String name, @PathVariable String group,
			@PathVariable String cron) {
		jobService.modifyTrigger(name, group, cron);
		return "redirect:/show";
	}

	@RequestMapping("/{name}/{group}/startNow")
	public String stratNow(@PathVariable String name, @PathVariable String group) {
		jobService.startNowJob(name, group);
		return "redirect:/show";
	}

	@RequestMapping("/{name}/{group}/resume")
	public String resume(@PathVariable String name, @PathVariable String group) {
		jobService.restartJob(name, group);
		return "redirect:/show";
	}

	public void getTriggers(HttpServletRequest request) {
		List<PageTrigger> triggers = jobService.getTriggersInfo();
		request.setAttribute("triggers", triggers);
	}

	@RequestMapping("/add")
	public String add(String name, String group) {
		SimpleDateFormat sf =new SimpleDateFormat("yyyyMMddHHmmsss");
		name=sf.format(new Date());
		name="a";
		jobService.addJob(name, group);
		return "redirect:/show";
	}

}