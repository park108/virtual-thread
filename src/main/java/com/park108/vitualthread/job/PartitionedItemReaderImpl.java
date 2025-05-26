package com.park108.vitualthread.job;

import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@StepScope
@Component
public class PartitionedItemReaderImpl implements ItemStreamReader<Integer> {

    private int current;
    private final int end;

    /**
     * 파티셔너 또는 JobParameter로 전달받은 범위 설정
     *
     * @param start 시작 인덱스 (예: partition별 시작 지점)
     * @param end   종료 인덱스 (exclusive, 즉 end 이전까지 처리)
     */
    public PartitionedItemReaderImpl(
            @Value("#{stepExecutionContext['start']}") int start,
            @Value("#{stepExecutionContext['end']}") int end
    ) {

        this.current = start;
        this.end = end;

        // TODO: Cache에서 데이터 읽기 (예: Hazelcast, Redis, Altibase)

        // TODO: RDB에서 데이터 읽기 (예: Oracle, PostgreSQL/EDB)

        // TODO: 이벤트 소스에서 읽기 (예: Kafka, IBM MQ)

        // TODO: 파일에서 데이터 읽기

        // TODO: 예외 처리 전략 추가 (e.g. Skip, Retry, FaultTolerance)

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
        // 재시작을 대비해 현재 포인터(current) 저장
        executionContext.putInt("current", current);
    }

    @Override
    public void close() throws ItemStreamException {
        // Step 종료 시 호출됨
        // 자원 해제 및 정리 작업 수행 위치
    }
}