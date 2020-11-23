package com.kakaopay.sprinklerestapi.sprinkling.repository;

import com.kakaopay.sprinklerestapi.sprinkling.domain.Receiving;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import com.kakaopay.sprinklerestapi.sprinkling.domain.SprinklingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static com.kakaopay.sprinklerestapi.sprinkling.Fixtures.aSprinkling;
import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SprinklingRepositoryTest {
    @Autowired
    private SprinklingRepository sprinklingRepository;

    @Autowired
    private TestEntityManager testEntityManager;

    @BeforeEach
    public void before(){
        Sprinkling sprinkling = aSprinkling().peopleCount(5).build();
        sprinklingRepository.save(sprinkling);
    }

    @Test
    @DisplayName("데이터 수정시 낙관적 락 version 값이 증가되는지 확인 테스트")
    public void version_value_test(){
        //given
        Sprinkling sprinkling = sprinklingRepository.findById(1l).orElseThrow(IllegalArgumentException::new);

        //when
        sprinkling.receive(101L);
        sprinkling.receive(102L);
        testEntityManager.flush();

        Receiving receiving1 = sprinkling.getReceivings().get(0);
        Receiving receiving2 = sprinkling.getReceivings().get(1);
        Receiving receiving3 = sprinkling.getReceivings().get(2);

        //then
        assertThat(receiving1.getVersion()).isEqualTo(1);
        assertThat(receiving2.getVersion()).isEqualTo(1);
        assertThat(receiving3.getVersion()).isEqualTo(0);

    }

    @Test
    @DisplayName("동시 받기가 될 경우 1개 receiver ID만 저장")
    @Disabled
    public void 미완전_테스트_어떤_트랜잭션이_먼저_커밋했는지_알수없음() throws Exception{
        Sprinkling sprinkling = sprinklingRepository.findById(1l).orElseThrow(IllegalArgumentException::new);
        int poolSize = 5;
        Long receiverId = 101L;
        final ExecutorService executor = Executors.newFixedThreadPool(poolSize);

        executor.execute(() -> sprinkling.receive(receiverId));
        executor.execute(() -> sprinkling.receive(receiverId+1L));

        executor.shutdown();
        executor.awaitTermination(1, TimeUnit.MINUTES);

        Receiving receiving1 = sprinkling.getReceivings().get(0);
        Receiving receiving2 = sprinkling.getReceivings().get(1);
        assertThat(receiving1.getReceiverId()).isNotNull();
        assertThat(receiving2.getReceiverId()).isNull();
    }
}
