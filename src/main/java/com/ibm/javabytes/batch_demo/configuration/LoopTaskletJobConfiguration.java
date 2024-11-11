package com.ibm.javabytes.batch_demo.configuration;

import com.ibm.javabytes.batch_demo.tasklet.LoopTasklet;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class LoopTaskletJobConfiguration {
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean("loopJobBean")
    public Job loopJob(@Qualifier("loopJobStep") Step loopJobStep) {
        return new JobBuilder("loopJob", jobRepository)
                .start(loopJobStep)
                .build();
    }

    @Bean("loopJobStep")
    public Step loopJobStep() {
        return new StepBuilder("process", jobRepository)
                .tasklet(loopTasklet(), platformTransactionManager)
                .build();
    }

    @Bean
    public Tasklet loopTasklet() {
        return new LoopTasklet();
    }
}
