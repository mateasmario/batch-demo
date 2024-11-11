package com.ibm.javabytes.batch_demo.tasklet;

import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;

public class LoopTasklet implements Tasklet {
    @Override
    public RepeatStatus execute(StepContribution contribution, ChunkContext chunkContext) throws Exception {
        for(int i = 0; i<1000; i++) {
            System.out.println("Count: " + i);
        }

        return RepeatStatus.FINISHED;
    }
}
