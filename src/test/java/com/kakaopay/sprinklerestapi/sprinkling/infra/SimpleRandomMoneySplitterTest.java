package com.kakaopay.sprinklerestapi.sprinkling.infra;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.RandomMoneySplitter;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class SimpleRandomMoneySplitterTest {
    private RandomMoneySplitter moneySplitter = new SimpleRandomMoneySplitter();

    @Test
    @DisplayName("split된 금액의 수가 인원 수에 맞는지 테스트")
    public void check_splatted_money_count(){
        Money sprinkledMoney = Money.wons(10000);
        int peopleCount = 4;
        List<Money> splattedMoneys = moneySplitter.split(sprinkledMoney, peopleCount);

        assertThat(splattedMoneys.size()).isEqualTo(peopleCount);

    }

    @Test
    @DisplayName("split된 금액의 합계가 뿌린 금액과 같은지 테스트")
    public void splatted_money_sum_equal_to_sprinkled_money(){
        Money sprinkledMoney = Money.wons(10000);
        int peopleCount = 4;
        List<Money> splattedMoneys = moneySplitter.split(sprinkledMoney, peopleCount);

        Money sumSplattedMoney = splattedMoneys.stream().reduce(Money.ZERO, Money::plus);

        assertThat(sprinkledMoney.equals(sumSplattedMoney)).isTrue();

    }
}
