package fr.edom.chad.batchLauncher;



import java.util.Map;

import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.configuration.JobLocator;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.NoSuchJobException;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.quartz.QuartzJobBean;

public final class QuartzJobLauncher extends QuartzJobBean{

	private String jobName;
	private JobLauncher jobLauncher;
	private JobLocator jobLocator;
	
	@Override
	protected void executeInternal(JobExecutionContext context) throws JobExecutionException {
		JobParameters parameters = new JobParametersBuilder().addLong("time", System.currentTimeMillis()).toJobParameters();
		try {
			Map<String, Object> jobDataMap = context.getMergedJobDataMap();
            String jobName = (String) jobDataMap.get("jobName");
            
		   Job job = jobLocator.getJob(jobName);
		   JobExecution jobExecution = jobLauncher.run(job, parameters); 
		   System.out.println("########### Status: " + jobExecution.getStatus());
			   
		  } catch(JobExecutionAlreadyRunningException | JobRestartException | JobInstanceAlreadyCompleteException 
		    | JobParametersInvalidException | NoSuchJobException  e) {
		   e.printStackTrace();
		  } 
	}

	// 
		
	public String getJobName() {
		return jobName;
	}

	public void setJobName(String jobName) {
		this.jobName = jobName;
	}

	public JobLauncher getJobLauncher() {
		return jobLauncher;
	}

	public void setJobLauncher(JobLauncher jobLauncher) {
		this.jobLauncher = jobLauncher;
	}

	public JobLocator getJobLocator() {
		return jobLocator;
	}

	public void setJobLocator(JobLocator jobLocator) {
		this.jobLocator = jobLocator;
	}
	
}
