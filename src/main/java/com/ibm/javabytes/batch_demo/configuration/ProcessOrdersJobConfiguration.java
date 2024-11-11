package com.ibm.javabytes.batch_demo.configuration;

import com.ibm.javabytes.batch_demo.entity.Order;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.builder.FlatFileItemReaderBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.transaction.PlatformTransactionManager;

import java.util.Map;

@Configuration
public class ProcessOrdersJobConfiguration {
    @Autowired
    private PlatformTransactionManager platformTransactionManager;

    @Autowired
    private JobRepository jobRepository;

    @Bean("processOrdersJobBean")
    public Job processOrdersJob(@Qualifier("processOrdersStep") Step processOrdersStep) {
        return new JobBuilder("processOrdersJob", jobRepository)
                .start(processOrdersStep)
                .build();
    }

    @Bean("processOrdersStep")
    public Step processOrdersStep(ItemReader<Order> itemReader, ItemProcessor<Order, Order> itemProcessor, ItemWriter<Order> itemWriter) {
        return new StepBuilder("process", jobRepository)
                .<Order, Order>chunk(3, platformTransactionManager)
                .reader(itemReader)
                .processor(itemProcessor)
                .writer(itemWriter)
                .build();
    }

    @Bean
    @JobScope
    public FlatFileItemReader<Order> itemReader(@Value("#{jobParameters}") Map<String, Object> jobParameters) {
        return new FlatFileItemReaderBuilder<Order>()
                .name("itemReader")
                .delimited()
                .delimiter(",") // Default is comma anyways
                .names("id", "productId", "name", "emailAddress", "phoneNumber", "address", "active")
                .targetType(Order.class)
                .resource(new ClassPathResource(jobParameters.get("file") != null ? jobParameters.get("file").toString() : "orders.csv"))
                .build();
    }

    @Bean
    @JobScope
    public ItemProcessor<Order, Order> itemProcessor() {
        return new ItemProcessor<>() {
            @Override
            public Order process(Order item) throws Exception {
                if (item.isActive()) {
                    // Do further validations?
                    return item;
                }

                return null;
            }
        };
    }

    @Bean
    @JobScope
    public ItemWriter<Order> itemWriter() {
        return new ItemWriter<>() {
            @Override
            public void write(Chunk<? extends Order> chunk) throws Exception {
                chunk.getItems().forEach(
                        System.out::println
                );
            }
        };
    }
}
