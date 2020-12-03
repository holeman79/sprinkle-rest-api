package com.kakaopay.sprinklerestapi.sprinkling.lock;

import com.kakaopay.sprinklerestapi.sprinkling.domain.Receiving;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import com.kakaopay.sprinklerestapi.sprinkling.domain.SprinklingRepository;
import lombok.Getter;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.orm.ObjectOptimisticLockingFailureException;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionCallbackWithoutResult;
import org.springframework.transaction.support.TransactionTemplate;

import javax.persistence.EntityManager;
import java.util.Arrays;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.kakaopay.sprinklerestapi.sprinkling.Fixtures.aReceiving;
import static com.kakaopay.sprinklerestapi.sprinkling.Fixtures.aSprinkling;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
//@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class SprinklingLockTest {
    @Autowired
    private SprinklingRepository sprinklingRepository;

    @Autowired
    private TransactionTemplate transactionTemplate;

    @Autowired
    private EntityManager entityManager;


    @BeforeEach
    public void beforeAll(){
        Sprinkling sprinkling = aSprinkling().peopleCount(3).build();
        Sprinkling sprinkling2 = aSprinkling().id(2L).peopleCount(3)
                .receivings(Arrays.asList(
                        aReceiving().id(4L).build(),
                        aReceiving().id(5L).build(),
                        aReceiving().id(6L).build()
                ))
                .build();
        sprinklingRepository.save(sprinkling);
        sprinklingRepository.save(sprinkling2);
    }

    @Test
    @DisplayName("데이터 수정시 낙관적 락 version 값이 증가되는지 확인 테스트")
    @Transactional
    @Disabled
    public void version_value_test(){
        //given
        Sprinkling sprinkling = sprinklingRepository.findById(2L).orElseThrow(IllegalArgumentException::new);

        //when
        sprinkling.receive(1001L);
        sprinkling.receive(1002L);

        Sprinkling savedSprinkling = sprinklingRepository.save(sprinkling);
        entityManager.flush();

        Receiving receiving1 = savedSprinkling.getReceivings().get(0);
        Receiving receiving2 = savedSprinkling.getReceivings().get(1);
        Receiving receiving3 = savedSprinkling.getReceivings().get(2);

        //then
        assertThat(receiving1.getVersion()).isEqualTo(1);
        assertThat(receiving2.getVersion()).isEqualTo(1);
        assertThat(receiving3.getVersion()).isEqualTo(0);

    }


    @Test
    @DisplayName("유저 아이디 101번과 102번이 동시에 1번 id 받기 요청이 들어오게 된 경우 먼저 받기 요청한 유저 id 101번이 update")
    public void optimistic_lock_test() throws ExecutionException, InterruptedException {
        int poolSize = 3;
        Long sprinklingId = 1L;
        Long receiverId1 = 101L;
        Long receiverId2 = 102L;
        int sleepTime1 = 1000;
        int sleepTime2 = 3000;

        ExecutorService executorService = Executors.newFixedThreadPool(poolSize);
        Future<Result> firstResult = executorService.submit(() -> receive(sprinklingId, receiverId1, sleepTime1));
        Future<Result> secondResult = executorService.submit(() -> receive(sprinklingId, receiverId2, sleepTime2));

        assertThat(firstResult.get().isSuccess()).isTrue();
        assertThat(secondResult.get().isSuccess()).isFalse();
        assertThat(secondResult.get().getException()).isExactlyInstanceOf(ObjectOptimisticLockingFailureException.class);

        Receiving receiving = entityManager.find(Receiving.class, 1L);
        assertThat(receiving.getReceiverId()).isEqualTo(101L);

        executorService.shutdown();

    }

    private Result receive(Long sprinklingId, Long receiverId, int sleepTime){
        try{
            transactionTemplate.execute(new TransactionCallbackWithoutResult() {
                @Override
                protected void doInTransactionWithoutResult(TransactionStatus status) {
                    Sprinkling sprinkling = sprinklingRepository.findById(sprinklingId).orElseThrow(IllegalArgumentException::new);
                    sprinkling.receive(receiverId);
                    sleep(sleepTime);
                }
            });
            return Result.SUCCESS;
        } catch (Exception e){
            return Result.fail(e);
        }
    }

    private void sleep(int sleepTime) {
        try {
            Thread.sleep(sleepTime);
        } catch (InterruptedException e) {
        }
    }

    @Getter
    public static class Result {
        private boolean success;

        private Exception exception;

        public static Result SUCCESS = new Result(true, null);
        public static Result fail(Exception ex) {
            return new Result(false, ex);
        }

        private Result(boolean success, Exception exception) {
            this.success = success;
            this.exception = exception;
        }

    }

}
