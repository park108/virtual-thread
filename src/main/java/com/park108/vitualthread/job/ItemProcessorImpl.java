package com.park108.vitualthread.job;

import com.park108.vitualthread.entity.TestRecord;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class ItemProcessorImpl implements ItemProcessor<Integer, TestRecord> {

    /**
     * 단일 아이템 처리 메서드.
     * 입력된 정수값(Integer)을 TestRecord로 변환한다.
     *
     * @param item 입력 데이터 (예: 순번)
     * @return 변환된 TestRecord 객체 (id + 스레드명 + 처리 시각 포함)
     */
    @Override
    public TestRecord process(Integer item) {

        // 테스트용 레코드 생성 및 반환
        return new TestRecord(
                item,                                 // ID 또는 인덱스 값
                Thread.currentThread().getName(),     // 처리에 사용된 스레드 이름 기록
                LocalDateTime.now()                   // 처리 시간 기록
        );
    }
}