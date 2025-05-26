package com.park108.vitualthread.config;

import jakarta.annotation.PreDestroy;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.TaskExecutorAdapter;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class TaskExecutorConfig {

    private final VirtualThreadTestProperties properties;
    private ExecutorService virtualExecutor;

    /**
     * 실행 구성값 주입 (virtual thread 여부 판단)
     */
    public TaskExecutorConfig(VirtualThreadTestProperties properties) {
        this.properties = properties;
    }

    /**
     * TaskExecutor Bean 정의
     * - useVirtualThread = true  → Java Virtual Thread 기반 ExecutorService 사용
     * - useVirtualThread = false → Spring ThreadPoolTaskExecutor 사용
     *
     * @return TaskExecutor (Spring Batch에서 병렬 처리에 사용)
     */
    @Bean
    @Primary
    public TaskExecutor taskExecutor() {
        if (properties.isUsePartitioner()) {
            // 🧩 Partitioning 모드

            if (properties.isUseVirtualThread()) {

                // 🚀 Virtual Thread 기반 ExecutorService 설정 (Java 21+)
                if (virtualExecutor == null) {
                    virtualExecutor = Executors.newThreadPerTaskExecutor(
                            Thread.ofVirtual().name("batch-vthread-", 0).factory()
                    );
                }

                System.out.println("🧩 Partitioning 활성화 + 🚀 Virtual Thread 모드 사용");

                return new TaskExecutorAdapter(virtualExecutor);
            }
            else {
                // 🧵 일반 ThreadPoolExecutor 기반 병렬 처리
                ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

                executor.setCorePoolSize(properties.getNormalThreadPoolMinSize());
                executor.setMaxPoolSize(properties.getNormalThreadPoolMaxSize());
                executor.setQueueCapacity(properties.getNormalThreadPoolQueueCapacity());
                executor.setThreadNamePrefix("batch-thread-");
                executor.initialize();

                System.out.println("🧩 Partitioning 활성화 + 🧵 일반 ThreadPoolExecutor 모드 사용");

                return executor;
            }
        }
        else {
            // 🧮 단일 스레드 처리 (No Partitioning)

            ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

            executor.setCorePoolSize(1);
            executor.setMaxPoolSize(1);
            executor.setQueueCapacity(properties.getNormalThreadPoolQueueCapacity());
            executor.setThreadNamePrefix("batch-thread-");
            executor.initialize();

            System.out.println("🧮 Partitioning 미사용 → 단일 스레드 모드");

            return executor;
        }
    }

    /**
     * 어플리케이션 종료 시 Virtual Executor 자원 해제
     */
    @PreDestroy
    public void shutdown() {
        if (virtualExecutor != null && !virtualExecutor.isShutdown()) {
            virtualExecutor.shutdown();
        }
    }
}
