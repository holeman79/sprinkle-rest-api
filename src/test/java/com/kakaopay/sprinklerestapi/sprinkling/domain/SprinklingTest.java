package com.kakaopay.sprinklerestapi.sprinkling.domain;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.exception.FinishedReceivingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.kakaopay.sprinklerestapi.sprinkling.Fixtures.aReceiving;
import static com.kakaopay.sprinklerestapi.sprinkling.Fixtures.aSprinkling;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SprinklingTest {

    @Test
    @DisplayName("receive 테스트")
    public void receive() {
        Long creatorId = 1L;
        String roomId = "R1";
        String token = "a8z";
        int peopleCount = 2;

        Sprinkling sprinkling = aSprinkling().creatorId(creatorId).peopleCount(peopleCount).roomId(roomId).token(token).receivings(Arrays.asList(
                aReceiving()
                        .id(1L)
                        .receivedMoney(Money.wons(170))
                        .build(),
                aReceiving()
                        .id(2L)
                        .receivedMoney(Money.wons(480))
                        .build()
        )).build();

        Long receiverId = 101L;
        Money firstReceivedMoney = sprinkling.receive(receiverId);
        Long targetReceiverId = sprinkling.getReceivings().get(0).getReceiverId();
        LocalDateTime targetReceivedTime = sprinkling.getReceivings().get(0).getReceivedTime();

        assertThat(firstReceivedMoney.longValue()).isEqualTo(170L);
        assertThat(targetReceiverId).isEqualTo(receiverId);
        assertThat(targetReceivedTime).isNotNull();

    }

    @Test
    @DisplayName("receiving 마감 후 receiving을 수행했을 때 finishedReceiving 에러발생")
    public void finished_receiving_Exception_발생() {
        Sprinkling sprinkling = aSprinkling().build();

        Money firstReceivedMoney = sprinkling.receive(101L);
        Money secondReceivedMoney = sprinkling.receive(102L);
        Money thirdReceivedMoney = sprinkling.receive(103L);

        assertThrows(FinishedReceivingException.class, ()-> sprinkling.receive(104L));
    }

    @Test
    @DisplayName("Max Random Money 값 가져오기")
    public void get_max_random_money(){
        Sprinkling sprinkling = aSprinkling().receivings(Arrays.asList(
                aReceiving()
                        .receivedMoney(Money.wons(170))
                        .build(),
                aReceiving()
                        .receivedMoney(Money.wons(480))
                        .build(),
                aReceiving()
                        .receivedMoney(Money.wons(350))
                        .build()
        )).build();
        Money maxRandomMoney = sprinkling.getMaxRandomMoney();
        assertThat(maxRandomMoney.equals(Money.wons(480))).isTrue();
    }



    @Test
    @DisplayName("받기 완료된 List 가져오기")
    public void get_completed_receiving_list(){
        Sprinkling sprinkling = aSprinkling().receivings(Arrays.asList(
                aReceiving()
                        .receivedMoney(Money.wons(170))
                        .build(),
                aReceiving()
                        .receivedMoney(Money.wons(480))
                        .build(),
                aReceiving()
                        .receivedMoney(Money.wons(350))
                        .build()
        )).build();

        Long receiverId1 = 101L;
        Long receiverId2 = 102L;

        sprinkling.receive(receiverId1);
        sprinkling.receive(receiverId2);

        List<Receiving> receivingCompletedList = sprinkling.getReceivingCompletedList();
        assertThat(receivingCompletedList.size()).isEqualTo(2);
        assertThat(receivingCompletedList.get(0).getReceiverId()).isEqualTo(receiverId1);
        assertThat(receivingCompletedList.get(1).getReceiverId()).isEqualTo(receiverId2);
    }

    @Test
    @DisplayName("받기 완료된 List 가져오기(받은 사람이 아무도 없을 경우)")
    public void get_completed_receiving_list_with_no_receiver(){
        Sprinkling sprinkling = aSprinkling().receivings(Arrays.asList(
                aReceiving()
                        .receivedMoney(Money.wons(170))
                        .build(),
                aReceiving()
                        .receivedMoney(Money.wons(480))
                        .build(),
                aReceiving()
                        .receivedMoney(Money.wons(350))
                        .build()
        )).build();

        List<Receiving> receivingCompletedList = sprinkling.getReceivingCompletedList();
        assertThat(receivingCompletedList.size()).isEqualTo(0);
    }

    @Test
    @DisplayName("받기 완료된 총 금액 테스트")
    public void get_total_received_money() {
        Money firstReceivedMoney = Money.wons(170);
        Money secondReceivedMoney = Money.wons(480);
        Money thirdReceivedMoney = Money.wons(350);
        Long firstReceiverId = 101L;
        Long secondReceiverId = 102L;
        Long thirdReceiverId = 103L;

        Sprinkling sprinkling = aSprinkling().receivings(Arrays.asList(
                aReceiving()
                        .receivedMoney(firstReceivedMoney)
                        .build(),
                aReceiving()
                        .receivedMoney(secondReceivedMoney)
                        .build(),
                aReceiving()
                        .receivedMoney(thirdReceivedMoney)
                        .build()
        )).build();

        sprinkling.receive(firstReceiverId);
        sprinkling.receive(secondReceiverId);
        sprinkling.receive(thirdReceiverId);

        Money totalReceivedMoney = sprinkling.getTotalReceivedMoney();
        assertThat(totalReceivedMoney.equals(
                firstReceivedMoney
                        .plus(secondReceivedMoney)
                        .plus(thirdReceivedMoney)))
                .isTrue();
    }






}
