package com.park108.vitualthread.repository;

import com.park108.vitualthread.entity.TestRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface TestRecordRepository extends JpaRepository<TestRecord, UUID> {
    // 추가적인 쿼리 메서드가 필요하면 여기에 정의할 수 있습니다.
}
