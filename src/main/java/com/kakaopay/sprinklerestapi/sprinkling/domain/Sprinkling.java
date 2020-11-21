package com.kakaopay.sprinklerestapi.sprinkling.domain;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.exception.ReceivingIsEmptyException;
import com.kakaopay.sprinklerestapi.sprinkling.exception.SprinklingFinishedReceivingException;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "SPRINKLINGS")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Sprinkling {

    private final long EXPIRE_RECEIVING_MINUTES = 10;

    private final long EXPIRE_READ_DAYS = 7;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "SPRINKLING_ID")
    private Long id;

    @Column(name = "ROOM_ID")
    private String roomId;

    @Column(name = "CREATOR_ID")
    private Long creatorId;

    @Column(name = "PEOPLE_COUNT")
    private Integer peopleCount;

    @Column(name = "AMOUNT")
    private Money amount;

    @Column(name = "TOKEN")
    private String token;

    @CreatedDate
    @Column(name = "SPRINKLED_TIME")
    private LocalDateTime sprinkledTime;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "SPRINKLING_ID")
    private List<Receiving> receivings = new ArrayList<>();

    public Sprinkling(String roomId, Long creatorId, Integer peopleCount, Money amount, String token, List<Receiving> receivings){
        this.roomId = roomId;
        this.creatorId = creatorId;
        this.peopleCount = peopleCount;
        this.amount = amount;
        this.token = token;
        this.sprinkledTime = LocalDateTime.now();
        this.receivings = receivings;
    }

    @Builder
    private Sprinkling(Long id, String roomId, Long creatorId, Integer peopleCount, Money amount,
                      String token, LocalDateTime sprinkledTime, List<Receiving> receivings){
        this.id = id;
        this.roomId = roomId;
        this.creatorId = creatorId;
        this.peopleCount = peopleCount;
        this.amount = amount;
        this.token = token;
        this.sprinkledTime = sprinkledTime;
        this.receivings = receivings;
    }

    public Money getMaxRandomMoney(){
        if(receivings.size() == 0){
            throw new ReceivingIsEmptyException();
        }
        Long randomMaxMoney = receivings.stream()
                .map(receiving -> receiving.getAmount().longValue())
                .max(Long::compareTo)
                .get();
        return Money.wons(randomMaxMoney);
    }

    public Money receiving(Long receiverId){
        Receiving target = receivings.stream()
                .filter(receiving -> receiving.getReceiverId() == null)
                .findFirst()
                .orElseThrow(() -> new SprinklingFinishedReceivingException());
        return target.receivingMoney(receiverId);
    }

    public boolean isEqualToToken(String token){
        return Objects.equals(this.token, token);
    }

    public boolean isExpiredReceiving(){
        return this.sprinkledTime.plusMinutes(EXPIRE_RECEIVING_MINUTES).isBefore(LocalDateTime.now());
    }

    public boolean isEqualToRoomId(String roomId){
        return Objects.equals(this.roomId, roomId);
    }

    public boolean isCreatorId(Long Id){
        return Objects.equals(this.creatorId, Id);
    }

    public boolean isDuplicatedReceiverId(Long receiverId){
        return receivings.stream()
                .anyMatch(receiving -> receiving.isEqualToReceiverId(receiverId));
    }

    public boolean isExpiredRead(){
        return this.sprinkledTime.plusDays(EXPIRE_READ_DAYS).isBefore(LocalDateTime.now());
    }
}
