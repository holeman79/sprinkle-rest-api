package com.kakaopay.sprinklerestapi.sprinkling.domain;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

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

    @Column(name = "RECEIVED_TIME")
    private LocalDateTime receivedTime;

    @Builder
    private Receiving(Long id, Long receiverId, Money amount, LocalDateTime receivedTime){
        this.id = id;
        this.receiverId = receiverId;
        this.amount = amount;
        this.receivedTime = receivedTime;
    }

    public Receiving(Money amount){
        this.amount = amount;
    }

    public Money receivingMoney(Long receiverId){
        this.receiverId = receiverId;
        this.receivedTime = LocalDateTime.now();
        return amount;
    }

    public boolean isEqualToReceiverId(Long receiverId){
        return Objects.equals(this.receiverId, receiverId);
    }
}
