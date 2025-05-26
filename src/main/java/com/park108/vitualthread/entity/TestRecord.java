package com.park108.vitualthread.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(
        name = "record",
        indexes = @Index(
                name = "idx_sequence",
                columnList = "jobExecutionId, sequence"
        )
)
public class TestRecord {

    @Id
    @GeneratedValue
    @Column(columnDefinition = "UUID", updatable = false, nullable = false)
    private UUID id;

    private Long jobExecutionId;

    private Integer sequence;

    private String threadName;

    private LocalDateTime processedAt;

    // 생성자, getter, setter 생략 (롬복 써도 무방)
    public TestRecord() {}

    public TestRecord(Long jobExecutionId,
                      Integer sequence,
                      String threadName,
                      LocalDateTime processedAt
    ) {
        this.jobExecutionId = jobExecutionId;
        this.sequence = sequence;
        this.threadName = threadName;
        this.processedAt = processedAt;
    }

    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public Integer getSequence() {
        return sequence;
    }
    public void setSequence(Integer sequence) {
        this.sequence = sequence;
    }
    public String getThreadName() {
        return threadName;
    }
    public void setThreadName(String threadName) {
        this.threadName = threadName;
    }
    public LocalDateTime getProcessedAt() {
        return processedAt;
    }
    public void setProcessedAt(LocalDateTime processedAt) {
        this.processedAt = processedAt;
    }
}
