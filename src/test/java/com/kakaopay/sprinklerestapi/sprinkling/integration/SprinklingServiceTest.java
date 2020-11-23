package com.kakaopay.sprinklerestapi.sprinkling.integration;

import com.kakaopay.sprinklerestapi.sprinkling.domain.Receiving;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import com.kakaopay.sprinklerestapi.sprinkling.domain.SprinklingRepository;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingService;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.CreateSprinklingRequestDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.CreateSprinklingResponseDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.GetSprinklingResponseDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.UpdateSprinklingResponseDto;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static com.kakaopay.sprinklerestapi.sprinkling.Fixtures.aSprinkling;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class SprinklingServiceTest {
    @Autowired
    private SprinklingService sprinklingService;

    @Autowired
    private SprinklingRepository sprinklingRepository;

    @Test
    @DisplayName("뿌리기 생성 통합 테스트")
    public void create_sprinkling(){
        //given
        long sprinkledMoney = 1000L;
        int peopleCount = 4;
        var createRequestDto = CreateSprinklingRequestDto.builder()
                .sprinkledMoney(sprinkledMoney)
                .peopleCount(peopleCount)
                .build();
        Long creatorId = 1L;
        String roomId = "R1";

        //when
        CreateSprinklingResponseDto responseDto = sprinklingService.create(createRequestDto, creatorId, roomId);

        //then
        assertThat(responseDto.getCreatorId()).isEqualTo(creatorId);
        assertThat(responseDto.getPeopleCount()).isEqualTo(peopleCount);
        assertThat(responseDto.getRoomId()).isEqualTo(roomId);
        assertThat(responseDto.getSprinkledMoney()).isEqualTo(sprinkledMoney);
    }

    @Test
    @DisplayName("받기 통합 테스트")
    public void receive_sprinkling(){
        //given
        Sprinkling sprinkling = aSprinkling().build();
        sprinklingRepository.save(sprinkling);

        Long sprinklingId = sprinkling.getId();
        String token = sprinkling.getToken();
        Long receiverId = 100L;
        String roomId = sprinkling.getRoomId();

        //when
        UpdateSprinklingResponseDto updateResponseDto = sprinklingService.receive(sprinklingId, receiverId, roomId, token);

        Receiving receiving = sprinkling.getReceivings().get(0);
        //then
        assertThat(updateResponseDto.getCreatorId()).isNotEqualTo(receiverId);
        assertThat(updateResponseDto.getReceiverId()).isEqualTo(receiverId);
        assertThat(updateResponseDto.getReceivedMoney()).isEqualTo(receiving.getReceivedMoney().longValue());

    }

    @Test
    @DisplayName("조회 통합 테스트")
    public void get_sprinkling(){
        //given
        Sprinkling sprinkling = aSprinkling().build();
        sprinklingRepository.save(sprinkling);

        Long sprinklingId = sprinkling.getId();
        String token = sprinkling.getToken();
        Long viewerId = sprinkling.getCreatorId();

        //when
        GetSprinklingResponseDto getResponseDto = sprinklingService.findById(sprinklingId, viewerId, token);

        //then
        assertThat(getResponseDto.getId()).isEqualTo(sprinklingId);
        assertThat(getResponseDto.getCreatorId()).isEqualTo(viewerId);
        assertThat(getResponseDto.getMaxRandomMoney()).isEqualTo(sprinkling.getMaxRandomMoney().longValue());
        assertThat(getResponseDto.getReceivedMoney()).isEqualTo(sprinkling.getTotalReceivedMoney().longValue());

    }

    @Test
    @DisplayName("받기가 완료된 금액의 합은 처음 뿌린 금액의 합과 같은지 테스트")
    public void get_sprinkling_money_test(){
        //given
        long sprinkledMoney = 1000L;
        int peopleCount = 3;
        var createRequestDto = CreateSprinklingRequestDto.builder()
                .sprinkledMoney(sprinkledMoney)
                .peopleCount(peopleCount)
                .build();
        Long creatorId = 1L;
        String roomId = "R1";
        Long receiverId = 101L;
        Long receiverId2 = 102L;
        Long receiverId3 = 103L;

        //when
        CreateSprinklingResponseDto createResponseDto = sprinklingService.create(createRequestDto, creatorId, roomId);
        sprinklingService.receive(createResponseDto.getId(), receiverId, roomId, createResponseDto.getToken());
        sprinklingService.receive(createResponseDto.getId(), receiverId2, roomId, createResponseDto.getToken());
        sprinklingService.receive(createResponseDto.getId(), receiverId3, roomId, createResponseDto.getToken());

        GetSprinklingResponseDto getResponseDto = sprinklingService.findById(createResponseDto.getId(), creatorId, createResponseDto.getToken());

        //then
        assertThat(getResponseDto.getReceivedMoney()).isEqualTo(sprinkledMoney);

    }


}
