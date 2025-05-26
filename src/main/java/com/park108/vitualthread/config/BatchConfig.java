package com.park108.vitualthread.config;

import com.park108.vitualthread.entity.TestRecord;
import com.park108.vitualthread.job.*;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@EnableBatchProcessing
public class BatchConfig {

    private final VirtualThreadTestProperties properties;

    public BatchConfig(VirtualThreadTestProperties properties) {
        this.properties = properties;
    }

    /**
     * Single job
     */
    @Bean
    public Job singleChunkJob(JobRepository jobRepository,
                                     Step chunkStep,
                                     JobExecutionListenerImpl listener) {
        return new JobBuilder("singleChunkJob", jobRepository)
                .start(chunkStep)
                .listener(listener)
                .build();
    }

    @Bean
    public Step chunkStep(JobRepository jobRepository,
                          PlatformTransactionManager transactionManager,
                          SingleItemReaderImpl reader,
                          ItemProcessorImpl processor,
                          ItemWriterImpl writer,
                          TaskExecutor taskExecutor) {

        return new StepBuilder("chunkStep", jobRepository)
                .<Integer, TestRecord>chunk(properties.getChunkSize(), transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .taskExecutor(taskExecutor)
                .build();
    }

    /**
     * Partitioned job
     * A master step splits slave steps by partitioner
     */
    @Bean
    public Job partitionedChunkJob(JobRepository jobRepository,
                                     Step masterStep,  // chunkStep or masterStep
                                     JobExecutionListenerImpl listener) {
        return new JobBuilder("partitionedChunkJob", jobRepository)
                .start(masterStep) // chunkStep or masterStep
                .listener(listener)
                .build();
    }

    /**
     * Master Step
     */
    @Bean
    public Step masterStep(JobRepository jobRepository,
                           TaskExecutor taskExecutor,
                           Step slaveStep) {
        return new StepBuilder("masterStep", jobRepository)
                .partitioner("slaveStep", new BatchPartitioner(properties))
                .step(slaveStep)
                .gridSize(properties.getGridSize())  // 병렬 파티션 수
                .taskExecutor(taskExecutor)
                .build();
    }

    /**
     * Slave Step
     */
    @Bean
    public Step slaveStep(JobRepository jobRepository,
                          PlatformTransactionManager transactionManager,
                          PartitionedItemReaderImpl reader,
                          ItemProcessorImpl processor,
                          ItemWriterImpl writer) {
        return new StepBuilder("slaveStep", jobRepository)
                .<Integer, TestRecord>chunk(properties.getChunkSize(), transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(writer)
                .build();
    }
}