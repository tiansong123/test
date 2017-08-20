package com.cn.hnust.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.quartz.CronScheduleBuilder;
import org.quartz.CronTrigger;
import org.quartz.JobBuilder;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.SimpleTrigger;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.quartz.impl.matchers.GroupMatcher;
import org.quartz.spi.JobFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.cn.hnust.pojo.PageTrigger;

@Repository("jobService")
public class JobService {

	@Autowired
	private Scheduler scheduler;

	/**
	 * ��ȡ���е�job
	 * 
	 * @return
	 */
	public List<JobDetail> getJobs() {
		try {
			GroupMatcher<JobKey> matcher = GroupMatcher.anyJobGroup();
			Set<JobKey> jobKeys = scheduler.getJobKeys(matcher);

			List<JobDetail> jobDetails = new ArrayList<JobDetail>();

			for (JobKey key : jobKeys) {
				jobDetails.add(scheduler.getJobDetail(key));
			}
			return jobDetails;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

		return null;
	}

	// ��ȡ���еĴ�����
	public List<PageTrigger> getTriggersInfo() {
		try {
			GroupMatcher<TriggerKey> matcher = GroupMatcher.anyTriggerGroup();
			Set<TriggerKey> Keys = scheduler.getTriggerKeys(matcher);
			List<PageTrigger> triggers = new ArrayList<PageTrigger>();

			// state��ֵ��������񴥷�����״̬��
			// STATE_BLOCKED 4 ����
			// STATE_COMPLETE 2 ���
			// STATE_ERROR 3 ����
			// STATE_NONE -1 ������
			// STATE_NORMAL 0 ����
			// STATE_PAUSED 1 ��ͣ
			for (TriggerKey key : Keys) {
				Trigger trigger = scheduler.getTrigger(key);
				PageTrigger pageTrigger = new PageTrigger();
				pageTrigger.setName(trigger.getJobKey().getName());
				pageTrigger.setGroup(trigger.getJobKey().getGroup());
				pageTrigger.setStatus(scheduler.getTriggerState(key) + "");
				if (trigger instanceof SimpleTrigger) {
					SimpleTrigger simple = (SimpleTrigger) trigger;
					pageTrigger.setExpression("�ظ�����:"
							+ (simple.getRepeatCount() == -1 ? "����" : simple
									.getRepeatCount()) + ",�ظ����:"
							+ (simple.getRepeatInterval() / 1000L));
					pageTrigger.setDesc(simple.getDescription());
				}
				if (trigger instanceof CronTrigger) {
					CronTrigger cron = (CronTrigger) trigger;
					pageTrigger.setExpression(cron.getCronExpression());
					pageTrigger.setDesc(cron.getDescription());
				}
				triggers.add(pageTrigger);
			}
			return triggers;
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
		return null;
	}

	// ��ͣ����
	public void stopJob(String name, String group) {
		JobKey key = new JobKey(name, group);
		try {
			scheduler.pauseJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	// �ָ�����
	public void restartJob(String name, String group) {
		JobKey key = new JobKey(name, group);
		try {
			scheduler.resumeJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	// ����ִ��һ������
	public void startNowJob(String name, String group) {
		try {
			JobKey key = new JobKey(name, group);
			JobDetail job = JobBuilder
					.newJob(scheduler.getJobDetail(key).getJobClass())
					.storeDurably().build();
			scheduler.addJob(job, false);
			scheduler.triggerJob(job.getKey());
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	// ɾ������
	public void delJob(String name, String group) {
		JobKey key = new JobKey(name, group);
		try {
			scheduler.deleteJob(key);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	// �޸Ĵ�����ʱ��
	public void modifyTrigger(String name, String group, String cron) {
		try {
			TriggerKey key = TriggerKey.triggerKey(name, group);
			Trigger trigger = scheduler.getTrigger(key);

			CronTrigger newTrigger = (CronTrigger) TriggerBuilder.newTrigger()
					.withIdentity(key)
					.withSchedule(CronScheduleBuilder.cronSchedule(cron))
					.build();
			scheduler.rescheduleJob(key, newTrigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

	// ��ͣ������
	public void stopScheduler() {
		try {
			if (!scheduler.isInStandbyMode()) {
				scheduler.standby();
			}
		} catch (SchedulerException e) {
			e.printStackTrace();
		}
	}

	public void addJob(String name, String group) {
		JobKey key = new JobKey(name, group);
		JobDetail jobDetail = JobBuilder.newJob(com.cn.hnust.service.JobFactory.class).withIdentity(key)
				.build();
		SimpleScheduleBuilder builder = SimpleScheduleBuilder.simpleSchedule();
		Trigger trigger = TriggerBuilder.newTrigger().withIdentity(name, group)
				.startNow().withSchedule(builder).startAt(new Date()).build();
		try {
			scheduler.scheduleJob(jobDetail, trigger);
		} catch (SchedulerException e) {
			e.printStackTrace();
		}

	}

}
