package com.sccl.attech.common.web;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.scheduling.quartz.QuartzJobBean;
import org.springframework.stereotype.Controller;

/**
 * 定时任务
 * @author luoyang
 *
 */
@Controller
public class TimerAttendanceControllers extends QuartzJobBean{
	private static Log log = LogFactory.getLog(TimerAttendanceControllers.class);
	

	@Override
	protected void executeInternal(JobExecutionContext context)
			throws JobExecutionException {
		
		
	}
	public void start() throws Exception {    
    
 }


}
