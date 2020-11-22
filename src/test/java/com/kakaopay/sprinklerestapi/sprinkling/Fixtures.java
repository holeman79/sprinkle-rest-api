package com.kakaopay.sprinklerestapi.sprinkling;


import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Receiving;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;

import java.time.LocalDateTime;
import java.util.Arrays;

import static com.kakaopay.sprinklerestapi.sprinkling.domain.Receiving.ReceivingBuilder;
import static com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling.SprinklingBuilder;

public class Fixtures {
    public static SprinklingBuilder aSprinkling() {
        return Sprinkling.builder()
                .id(1L)
                .roomId("R1")
                .creatorId(1L)
                .peopleCount(3)
                .sprinkledMoney(Money.wons(1000))
                .token("a79")
                .receivings(Arrays.asList(
                        aReceiving()
                                .id(1L)
                                .receivedMoney(Money.wons(170))
                                .build(),
                        aReceiving()
                                .id(2L)
                                .receivedMoney(Money.wons(480))
                                .build(),
                        aReceiving()
                                .id(3L)
                                .receivedMoney(Money.wons(350))
                                .build()
                ))
                .sprinkledTime(LocalDateTime.now())
                ;
    }

    public static ReceivingBuilder aReceiving(){
        return Receiving.builder()
                .id(1L)
                .receivedMoney(Money.wons(300))
                .receivedTime(LocalDateTime.now());
    }
}