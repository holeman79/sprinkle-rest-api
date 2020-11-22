package com.kakaopay.sprinklerestapi.sprinkling.infra;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.RandomMoneySplitter;
import org.springframework.stereotype.Component;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Component
public class SimpleRandomMoneySplitter implements RandomMoneySplitter {
    private SecureRandom secureRandom = new SecureRandom();

    public List<Money> split(Money amount, int peopleCount) {
        List<Money> splattedMoneys = new ArrayList<>();
        for(int remainPeople = peopleCount-1; remainPeople > 0; remainPeople--){
            Money splattedMoney = splitMoney(amount, remainPeople);
            splattedMoneys.add(splattedMoney);
            amount = amount.minus(splattedMoney);
        }
        splattedMoneys.add(amount);
        return splattedMoneys;
    }

    private Money splitMoney(Money amount, int remainPeople) {
        long min = 1;
        long max = amount.longValue() - remainPeople + 1; // 한명당 최소 1원은 받을 수 있도록 금액을 남겨야 한다
        long splattedMoney = min == max ? 1 : secureRandom.longs(1, min, max).sum();
        return Money.wons(splattedMoney);
    }
}
