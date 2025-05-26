package com.park108.vitualthread.config;

import org.springframework.batch.core.partition.support.Partitioner;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class BatchPartitioner implements Partitioner {

    private final VirtualThreadTestProperties properties;

    /**
     * 생성자에서 설정값 주입
     *
     * @param properties 배치 실행 시 필요한 테스트 범위, grid 크기 등 포함
     */
    public BatchPartitioner(VirtualThreadTestProperties properties) {
        this.properties = properties;
    }

    /**
     * gridSize(파티션 개수)에 따라 데이터 범위를 분할하고
     * 각 파티션에 start ~ end 범위를 ExecutionContext로 설정
     *
     * @param gridSize 파티션 개수 (병렬 실행 수)
     * @return 파티션 키 → ExecutionContext(start, end) 매핑
     */
    @Override
    public Map<String, ExecutionContext> partition(int gridSize) {

        Map<String, ExecutionContext> partitions = new HashMap<>();

        // 📦 파티션 수 로그 출력
        System.out.println("🧮 gridSize = " + gridSize);

        int totalTestSize = properties.getTotalTestSize();
        int chunkSize = totalTestSize / gridSize;

        for (int i = 0; i < gridSize; i++) {

            ExecutionContext context = new ExecutionContext();

            // 각 파티션의 처리 범위 계산
            int start = i * chunkSize;
            int end = (i == gridSize - 1) ? totalTestSize : start + chunkSize;
            String partitionKey = "partition-" + i;

            // 🎯 파티션 범위 출력
            System.out.println("🧩 Partition [" + partitionKey + "] → Range: "
                    + String.format("%,d", start) + " ~ " + String.format("%,d", end));

            // 파티션별 처리 범위를 ExecutionContext에 설정
            context.putInt("start", start);
            context.putInt("end", end);

            partitions.put(partitionKey, context);
        }

        return partitions;
    }
}