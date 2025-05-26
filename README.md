# Virtual Thread Test in Spring Batch (updated at 26/may/2025)
## 프로젝트의 목적
- Spring Batch의 성능 향상을 위해 다양한 처리 방식을 테스트하고 비교한다.
  - Single Threaded
  - Multi Threaded
    - Normal thread
    - Virtual thread

## 테스트 방법
- application.yml 파일 설정으로 테스트를 수행할 수 있다.
```application.yml
app:
  virtual-thread-test:
    total-test-size: 1000000 # 총 처리 대상 데이터 수
    chunk-size: 10000 # 청크당 처리 단위 개수 (Spring Batch chunk size)
    grid-size: 100 # 파티션 수 (= 병렬 처리 스레드 수)

    use-partitioner: true # true: 파티셔닝(멀티 쓰레드) 활성화, false: 단일 스레드 실행
    use-virtual-thread: true # true: Virtual Thread 사용, false: 일반 ThreadPoolExecutor 사용
    normal-thread-pool-min-size: 50 # 일반 ThreadPool 최소 사이즈
    normal-thread-pool-max-size: 100 # 일반 ThreadPool 최대 사이즈
    normal-thread-pool-queue-capacity: 1000 # 일반 ThreadPool 큐 용량
```

## TODO
- IN/OUT 설정을 통한 실제 성능 테스트
  - 대상: DB, Cache, Queue, File
- Exception handling 전략 추가
  - 전략 유형: Fail Fast, Retry, Skip, DLQ
