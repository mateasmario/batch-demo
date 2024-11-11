package com.ibm.javabytes.batch_demo.runner;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BatchCommandLineRunner implements CommandLineRunner {
    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("processOrdersJobBean")
    private Job processOrdersJob;

    @Autowired
    @Qualifier("loopJobBean")
    private Job loopJob;

    @Override
    public void run(String... args) throws Exception {
        JobParameters jobParameters = new JobParameters();
        JobParametersBuilder jobParametersBuilder = new JobParametersBuilder();

        if (args.length > 0) {
            if (args[0].equals("processOrders")) {
                if (args.length >= 2) {
                    jobParametersBuilder.addString("file", args[1]);
                }
                this.jobLauncher.run(processOrdersJob, jobParametersBuilder.toJobParameters());
            } else if (args[0].equals("loop")) {
                this.jobLauncher.run(loopJob, jobParametersBuilder.toJobParameters());
            }
        }
    }
}
