# Virtual Thread Test in Spring Batch
(updated at 26/may/2025)

## 프로젝트의 목적
- Spring Batch의 성능 향상을 위해 다양한 처리 방식을 테스트하고 비교한다.
  - Single Threaded
  - Multi Threaded
    - Normal thread
    - Virtual thread

## 테스트 방법
- application.yml 파일 설정으로 테스트를 수행할 수 있다.

### 설정값
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
### 결과
- Single-threaded
```
🧵 일반 ThreadPoolExecutor 모드 사용
🧪 테스트 케이스 정보
   - 총 테스트 건수 (total-test-size): 100,000
   - 청크 사이즈 (chunk-size): 100
   - 예상 청크 개수: 1,000개
✅ 배치 작업 종료: singleChunkJob
   - 시작 시각: 2025년 05월 26일 18:01:50.366
   - 종료 시각: 2025년 05월 26일 18:01:53.136
   - 소요 시간: 0일 00시간 00분 02.770초
📦 처리된 총 건수: 100,000 건
```
- Multi-threaded
```
🧵 일반 ThreadPoolExecutor 모드 사용
🧪 테스트 케이스 정보
   - 총 테스트 건수 (total-test-size): 100,000
   - 청크 사이즈 (chunk-size): 100
   - 예상 청크 개수: 1,000개
✅ 배치 작업 종료: partitionedChunkJob
   - 시작 시각: 2025년 05월 26일 18:02:20.880
   - 종료 시각: 2025년 05월 26일 18:02:22.181
   - 소요 시간: 0일 00시간 00분 01.301초
📦 처리된 총 건수: 100,000 건
```
- Multi-threaded (virtual thread)
```
🚀 Virtual Thread 모드 사용
🧪 테스트 케이스 정보
   - 총 테스트 건수 (total-test-size): 100,000
   - 청크 사이즈 (chunk-size): 100
   - 예상 청크 개수: 1,000개
✅ 배치 작업 종료: partitionedChunkJob
   - 시작 시각: 2025년 05월 26일 18:02:42.259
   - 종료 시각: 2025년 05월 26일 18:02:43.461
   - 소요 시간: 0일 00시간 00분 01.202초
📦 처리된 총 건수: 100,000 건
```

## TODO
- 실 인프라 환경 테스트
  - 클러스터로 구성된 컴퓨팅 환경에서 분산된 Job 들이 개별 노드의 인프라 자원을 소모할 수 있어야 한다.
    - 테스트 결과를 보고 클러스터 구성, Job 별 할당 자원을 최적화 할 수 있어야 한다.
  - 각 솔루션들이 개별 할당된 인스턴스에서 동작해야 한다.
    - 대상: DB, Cache, Queue, File
    - Network 비용, I/O 병목을 확인하고 최적화할 수 있어야 한다.
- Exception handling 전략 추가
  - 전략 유형: Fail Fast, Retry, Skip, DLQ
