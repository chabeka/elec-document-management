package fr.edom.chad.config;



import javax.sql.DataSource;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import fr.edom.chad.items.DocumentItemReader;
import fr.edom.chad.items.DocumentItemWriter;
import fr.edom.chad.massarchiving.archivinglistners.CheckStateAfterListener;
import fr.edom.chad.massarchiving.tasklets.CheckSommaireFileTasklet;



@Configuration
@EnableBatchProcessing
public class BatchConfig {

	
	@Autowired
	JobBuilderFactory jobBuilder;
	
	@Autowired
	StepBuilderFactory stepBuilder;
	
	@Autowired
	DataSource datasource;
	
	@Bean
	 public DocumentItemReader reader() {
	  return new DocumentItemReader();
	 }
	 
	 @Bean
	 public DocumentItemWriter writer() {
	  return new DocumentItemWriter();
	 }
	 
	 @Bean
	 public Step step1() {
	  return stepBuilder.get("step1")
	    .<Object, Object> chunk(10)
	    .reader(reader())
	    .writer(writer())
	    .build();
	 }
	 
	 @Bean
	 public Job massArchivingDocument() {
		 return jobBuilder.get("massArchivingDocument")
				 .incrementer(new RunIdIncrementer())
				 .flow(checkSommaireFileStep())
				 .end()
				 .build();
	 }
	 
	 @Bean
	 public Step checkSommaireFileStep() {
		 return stepBuilder.get("checkSommaireFileStep")
				 .tasklet(checkSommaireFileTasklet())
				 .listener(checkStateAfterListener())
				 .build();
	 }
	 
	 @Bean
	 public CheckStateAfterListener checkStateAfterListener() {
		 return new CheckStateAfterListener();
	 }
	 
	 @Bean
	 public CheckSommaireFileTasklet checkSommaireFileTasklet() {
		 return new CheckSommaireFileTasklet();
	 }
	 @Bean
	 public Job testJob() {
	  return jobBuilder.get("testAJob")
	    .incrementer(new RunIdIncrementer())
	    .flow(step1())
	    .end()
	    .build();
	 }
}
