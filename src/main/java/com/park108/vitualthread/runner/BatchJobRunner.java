package com.park108.vitualthread.runner;

import com.park108.vitualthread.config.VirtualThreadTestProperties;
import org.springframework.batch.core.*;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class BatchJobRunner implements CommandLineRunner {

    private final JobLauncher jobLauncher;
    private final Job singleChunkJob;
    private final Job partitionedChunkJob;

    private final VirtualThreadTestProperties properties;

    public BatchJobRunner(
            JobLauncher jobLauncher,
            Job singleChunkJob,
            Job partitionedChunkJob,
            VirtualThreadTestProperties properties

    ) {
       this.jobLauncher =  jobLauncher;

       this.singleChunkJob = singleChunkJob;
       this.partitionedChunkJob = partitionedChunkJob;

       this.properties = properties;
    }

    @Override
    public void run(String... args) throws Exception {

        JobParameters params = new JobParametersBuilder()
                .addLong("timestamp", System.currentTimeMillis())
                .toJobParameters();

        System.out.println("▶▶▶ 배치 잡 실행 시작");

        JobExecution execution = properties.isUsePartitioner()
                ? jobLauncher.run(partitionedChunkJob, params)
                : jobLauncher.run(singleChunkJob, params);

        System.out.println("▶▶▶ 배치 잡 실행 완료: " + execution.getStatus());
    }
}