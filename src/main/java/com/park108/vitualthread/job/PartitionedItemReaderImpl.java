package com.park108.vitualthread.job;

import com.park108.vitualthread.config.VirtualThreadTestProperties;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@StepScope
@Component
public class PartitionedItemReaderImpl implements ItemStreamReader<Integer> {

    private final VirtualThreadTestProperties properties;
    List<Integer> list;

    private final int start;
    private final int end;

    private int chunkNo;
    private int index;

    /**
     * 파티셔너 또는 JobParameter로 전달받은 범위 설정
     *
     * @param start 시작 인덱스 (예: partition별 시작 지점)
     * @param end   종료 인덱스 (exclusive, 즉 end 이전까지 처리)
     */
    public PartitionedItemReaderImpl(
            VirtualThreadTestProperties properties,
            @Value("#{stepExecutionContext['start']}") int start,
            @Value("#{stepExecutionContext['end']}") int end
    ) {

        this.properties = properties;

        // 파티션 범위 설정
        this.start = start;
        this.end = end;

        this.chunkNo = 0;
        this.index = 0;
    }

    @Override
    public Integer read() {

        // 조회한 목록이 없거나 모두 읽어서 처리했을 경우 다음 청크 조회
        if(null == list || index >= list.size()) {
            list = getChunk(start, end, chunkNo, properties.getChunkSize());
            ++chunkNo; // 청크 번호 증가
        }

        // 마지막 건을 처리했을 경우 빈 리스트라서 읽기 종료
        if(list.isEmpty()) return null;

        return list.get(index++);
    }

    /**
     * 청크 크기대로 데이터 조회
     * @return
     */
    private List<Integer> getChunk(int start, int end, int chunkNo, int chunkSize) {

        // 데이터와 인덱스 초기화
        list = new ArrayList<>();
        index = 0;


        // TODO: 데이터 소스 별 구현 로직 필요
        // 데이터 설정
        int startOffset = start + chunkNo * chunkSize;
        int nextOffset = startOffset + chunkSize;
        int endOffset = Math.min(nextOffset - 1, end);

        for(int i = startOffset; i <= endOffset; i++) {
            list.add(i);
        }

        // 조회 할 건 없으면 빈 리스트 리턴
        if(list.isEmpty()) return list;

        // TODO: 청크 읽기 지연 속도 설정 - 실제 인프라 연결 된 후 삭제할 것
        try {

            // min ~ max 랜덤 정수
            Random random = new Random();
            int min = 1000;
            int max = 1000;

            int randomNumber = random.nextInt((max - min) + 1) + min;

            Thread.sleep(randomNumber);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("📝 Read chunk " + String.format("%,d", startOffset)
                + " ~ " + String.format("%,d", endOffset)
                + " at 🧵" + Thread.currentThread().getName());

        return list;
    }
}