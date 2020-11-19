package com.kakaopay.sprinklerestapi.sprinkling.domain;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Entity
@Table(name = "RECEIVINGS")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Receiving {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "RECEIVING_ID")
    private Long id;

    @Column(name = "RECEIVER_ID")
    private Long receiverId;

    @Column(name = "AMOUNT")
    private Money amount;
}
