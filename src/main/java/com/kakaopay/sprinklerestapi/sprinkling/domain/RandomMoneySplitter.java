package com.kakaopay.sprinklerestapi.sprinkling.domain;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public interface RandomMoneySplitter {
    List<Money> split(Money amount, int peopleCount);
}
