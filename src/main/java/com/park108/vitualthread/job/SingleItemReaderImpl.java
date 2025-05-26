package com.park108.vitualthread.job;

import com.park108.vitualthread.config.VirtualThreadTestProperties;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicInteger;

@StepScope
@Component
public class SingleItemReaderImpl implements ItemStreamReader<Integer> {

    private int current;
    private final int end;

    /**
     * Chunk Size 를 단일 스레드에서 처리
     */
    public SingleItemReaderImpl(VirtualThreadTestProperties properties) {

        AtomicInteger index = new AtomicInteger();
        int start = properties.getChunkSize() * index.get();
        this.current = start;
        this.end = Math.max(start + properties.getChunkSize(), properties.getTotalTestSize());

        // TODO: Cache에서 데이터 읽기 (예: Hazelcast, Redis, Altibase)

        // TODO: RDB에서 데이터 읽기 (예: Oracle, PostgreSQL/EDB)

        // TODO: 이벤트 소스에서 읽기 (예: Kafka, IBM MQ)

        // TODO: 파일에서 데이터 읽기

        // TODO: 예외 처리 전략 추가 (e.g. Skip, Retry, FaultTolerance)

        index.getAndIncrement();
    }

    @Override
    public Integer read() {
        if (current < end) {
            return current++;
        }
        else {
            return null;
        }
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        // Step 실행 시 최초 1회 호출됨
        // 필요 시 이전 상태 복원 가능 (e.g. current 값 불러오기)
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
    }

    @Override
    public void close() throws ItemStreamException {
        // Step 종료 시 호출됨
        // 자원 해제 및 정리 작업 수행 위치
    }
}