package com.kakaopay.sprinklerestapi.sprinkling.domain;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "SPRINKLINGS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sprinkling {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SPRINKLING_ID")
    private Long id;

    @Column(name = "ROOM_ID")
    private String roomId;

    @Column(name = "CREATOR_ID")
    private int creatorId;

    @Column(name = "PEOPLE_COUNT")
    private Integer peopleCount;

    @Column(name = "AMOUNT")
    private Money amount;

    @Column(name = "TOKEN")
    private String token;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "SPRINKLING_ID")
    private List<Receiving> receivings = new ArrayList<>();

    @Builder
    public Sprinkling(Long id, String roomId, int creatorId, Integer peopleCount, Money amount, String token){
        this.id = id;
        this.roomId = roomId;
        this.creatorId = creatorId;
        this.peopleCount = peopleCount;
        this.amount = amount;
        this.token = token;
    }
}
