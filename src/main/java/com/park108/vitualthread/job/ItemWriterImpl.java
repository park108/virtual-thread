package com.park108.vitualthread.job;

import com.park108.vitualthread.entity.TestRecord;
import com.park108.vitualthread.repository.TestRecordRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
public class ItemWriterImpl implements ItemWriter<TestRecord> {

    private final TestRecordRepository repository;

    /**
     * TestRecordRepository 주입
     *
     * @param repository JPA 기반 저장소
     */

    public ItemWriterImpl(
            TestRecordRepository repository
    ) {
        this.repository = repository;
    }


    /**
     * Chunk 단위로 데이터 저장 처리
     *
     * @param items 현재 Chunk에 포함된 TestRecord 목록
     */
    @Override
    public void write(Chunk<? extends TestRecord> items) {

        // TODO: 예외 발생 시 재시도, 스킵 등 Fault Tolerance 처리 추가

        // JPA saveAll()을 이용해 일괄 저장
        repository.saveAll(items);

        // 처리 로그 출력 (스레드 확인용)
        System.out.println("✍\uFE0F Write " + String.format("%,d", items.size()) + " records at 🧵"
                + Thread.currentThread().getName());
    }
}