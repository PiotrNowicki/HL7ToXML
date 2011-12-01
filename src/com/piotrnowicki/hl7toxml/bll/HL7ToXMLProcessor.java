package com.piotrnowicki.hl7toxml.bll;

import java.text.ParseException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.quartz.CronTrigger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.impl.StdSchedulerFactory;

/**
 * Main processor of HL7 Messages which creates reflected Java structure from
 * input file
 * 
 * @author Piotr Nowicki
 * 
 */
public class HL7ToXMLProcessor {

	Log log = LogFactory.getLog(HL7ToXMLProcessor.class);

	public static void main(String[] args) throws SchedulerException,
			ParseException {

		// ProcessorJob p = new ProcessorJob();
		// p.execute(null);

		SchedulerFactory sf = new StdSchedulerFactory();
		Scheduler sched = sf.getScheduler();

		JobDetail job = new JobDetail("parserJob", "parserGroup",
				ProcessorJob.class);
		CronTrigger trigger = new CronTrigger("parserTrigger", "parserGroup",
				"parserJob", "parserGroup", "0/10 * * * * ?");

		sched.addJob(job, true);
		sched.scheduleJob(trigger);

		sched.start();
	}

}
