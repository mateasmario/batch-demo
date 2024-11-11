package com.ibm.javabytes.batch_demo.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("batch")
public class BatchJobController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    @Qualifier("processOrdersJobBean")
    private Job processOrdersJob;

    @Autowired
    @Qualifier("loopJobBean")
    private Job loopJob;

    @GetMapping("/processOrders")
    public ResponseEntity processOrders() throws Exception{
        jobLauncher.run(processOrdersJob, new JobParameters());
        return ResponseEntity.ok().build();
    }

    @GetMapping("/loop")
    public ResponseEntity loop() throws Exception{
        jobLauncher.run(loopJob, new JobParameters());
        return ResponseEntity.ok().build();
    }
}