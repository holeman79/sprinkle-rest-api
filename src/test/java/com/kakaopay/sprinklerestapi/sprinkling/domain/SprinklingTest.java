package com.kakaopay.sprinklerestapi.sprinkling.domain;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.exception.SprinklingCreatorCanNotReceiveException;
import com.kakaopay.sprinklerestapi.sprinkling.exception.SprinklingFinishedReceivingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.kakaopay.sprinklerestapi.sprinkling.Fixtures.aSprinkling;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SprinklingTest {

    @Test
    @DisplayName("receiving 테스트")
    public void receiving() {
        Sprinkling sprinkling = aSprinkling().build();

        Money firstReceivedMoney = sprinkling.receiving(101L);
        Money secondReceivedMoney = sprinkling.receiving(102L);
        Money thirdReceivedMoney = sprinkling.receiving(103L);

        assertThat(firstReceivedMoney.longValue()).isEqualTo(170L);
        assertThat(secondReceivedMoney.longValue()).isEqualTo(480L);
        assertThat(thirdReceivedMoney.longValue()).isEqualTo(350L);
    }

    @Test
    @DisplayName("receiving 마감 후 receiving을 수행했을 때 finishedReceiving 에러발생")
    public void sprinklingFinishedReceivingException_발생() {
        Sprinkling sprinkling = aSprinkling().build();

        Money firstReceivedMoney = sprinkling.receiving(101L);
        Money secondReceivedMoney = sprinkling.receiving(102L);
        Money thirdReceivedMoney = sprinkling.receiving(103L);

        assertThrows(SprinklingFinishedReceivingException.class, ()-> sprinkling.receiving(104L));
    }


}
