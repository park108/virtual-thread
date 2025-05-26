package com.park108.vitualthread.job;

import com.park108.vitualthread.config.VirtualThreadTestProperties;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobExecutionListener;
import org.springframework.batch.core.StepExecution;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Component
public class JobExecutionListenerImpl implements JobExecutionListener {

    private final VirtualThreadTestProperties properties;

    public JobExecutionListenerImpl(VirtualThreadTestProperties properties) {
        this.properties = properties;
    }

    private long startTime;

    @Override
    public void beforeJob(JobExecution jobExecution) {
        startTime = System.currentTimeMillis();
        System.out.println("🟢 배치 작업 시작: " + jobExecution.getJobInstance().getJobName());
    }

    @Override
    public void afterJob(JobExecution jobExecution) {

        long endTime = System.currentTimeMillis();
        long elapsed = endTime - startTime;

        // 🧵 실행 모드
        if (properties.isUseVirtualThread()) {
            System.out.println("🚀 Virtual Thread 모드 사용");
        }
        else {
            System.out.println("🧵 일반 ThreadPoolExecutor 모드 사용");
        }

        // 파티셔너 별 쓰기 건수 집계
        long writeCount;
        if(properties.isUsePartitioner()) {
            writeCount = jobExecution.getStepExecutions()
                    .stream()
                    .filter(se -> se.getStepName().startsWith("slaveStep"))
                    .mapToLong(StepExecution::getWriteCount)
                    .sum();
        }
        else {
            writeCount = jobExecution.getStepExecutions()
                    .stream()
                    .filter(se -> se.getStepName().startsWith("chunkStep"))
                    .mapToLong(StepExecution::getWriteCount)
                    .sum();
        }

        System.out.println("🧪 테스트 케이스 정보");
        System.out.println("   - 총 테스트 건수 (total-test-size): " + String.format("%,d", properties.getTotalTestSize()));
        System.out.println("   - 청크 사이즈 (chunk-size): " + String.format("%,d", properties.getChunkSize()));
        System.out.println("   - 예상 청크 개수: " + (properties.getTotalTestSize() / properties.getChunkSize()) + "개");

        System.out.println("✅ 배치 작업 종료: " + jobExecution.getJobInstance().getJobName());
        System.out.println("⏱ 시작 시각: " + formatKoreanTimestamp(startTime));
        System.out.println("⏱ 종료 시각: " + formatKoreanTimestamp(endTime));
        System.out.println("⏱ 총 소요 시간: " + formatElapsedTime(elapsed));
        System.out.println("📦 처리된 총 건수: " + String.format("%,d", writeCount) + " 건");
    }

    private static String formatElapsedTime(long elapsedMillis) {
        long millis = elapsedMillis % 1000;
        long totalSeconds = elapsedMillis / 1000;

        long seconds = totalSeconds % 60;
        long totalMinutes = totalSeconds / 60;

        long minutes = totalMinutes % 60;
        long totalHours = totalMinutes / 60;

        long hours = totalHours % 24;
        long days = totalHours / 24;

        return String.format("%d일 %02d시간 %02d분 %02d.%03d초", days, hours, minutes, seconds, millis);
    }

    public static String formatKoreanTimestamp(long epochMillis) {
        LocalDateTime dateTime = LocalDateTime.ofInstant(
                Instant.ofEpochMilli(epochMillis),
                ZoneId.systemDefault()
        );
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일 HH:mm:ss.SSS");
        return dateTime.format(formatter);
    }
}