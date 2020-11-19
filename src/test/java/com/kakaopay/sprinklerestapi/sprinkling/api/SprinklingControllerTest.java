package com.kakaopay.sprinklerestapi.sprinkling.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingDto.CreateRequest;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(SprinklingController.class)
public class SprinklingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private SprinklingService sprinklingService;

    private final String X_USER_ID = "X-USER-ID";

    private final String X_ROOM_ID = "X-ROOM-ID";

    @Test
    @DisplayName("뿌리기 금액 또는 받기 인원 수가 0 이하일 경우 Bad Request")
    public void create_sprinkling_bad_request_amount_less_than_zero() throws Exception{
        CreateRequest createRequest = CreateRequest
                .builder()
                .amount(0l)
                .peopleCount(4)
                .build();

        this.mockMvc.perform(post("/api/sprinklings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isBadRequest())
        ;
    }

    @Test
    @DisplayName("뿌리기 생성 성공 테스트")
    public void create_sprinkling_success() throws Exception{
        //given
        CreateRequest createRequest = CreateRequest
                .builder()
                .amount(1000)
                .peopleCount(4)
                .build();
        int creatorId = 1;
        String roomId = "R1";
        Sprinkling sprinkling = Sprinkling.builder()
                                        .id(1L)
                                        .amount(Money.wons(1000))
                                        .peopleCount(4)
                                        .creatorId(creatorId)
                                        .roomId(roomId)
                                        .token("k8A")
                                        .build();

        given(sprinklingService.createSprinkling(createRequest, creatorId, roomId))
                .willReturn(sprinkling);

        //when & then

        this.mockMvc.perform(post("/api/sprinklings")
                            .header(X_USER_ID, creatorId)
                            .header(X_ROOM_ID, roomId)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(this.objectMapper.writeValueAsString(createRequest)))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("id").value(1L))
                .andExpect(jsonPath("token").exists())
                ;
    }

}
