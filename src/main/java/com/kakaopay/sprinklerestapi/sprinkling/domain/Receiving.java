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

    @Column(name = "RECEIVED_MONEY")
    private Money receivedMoney;

    @Column(name = "RECEIVED_TIME")
    private LocalDateTime receivedTime;

    // Lock 버전
    @Version
    private Integer version;

    @Builder
    private Receiving(Long id, Long receiverId, Money receivedMoney, LocalDateTime receivedTime){
        this.id = id;
        this.receiverId = receiverId;
        this.receivedMoney = receivedMoney;
        this.receivedTime = receivedTime;
    }

    public Receiving(Money receivedMoney){
        this.receivedMoney = receivedMoney;
    }

    public Money receive(Long receiverId){
        this.receiverId = receiverId;
        this.receivedTime = LocalDateTime.now();
        return receivedMoney;
    }

    public boolean isEqualToReceiverId(Long receiverId){
        return Objects.equals(this.receiverId, receiverId);
    }
}
