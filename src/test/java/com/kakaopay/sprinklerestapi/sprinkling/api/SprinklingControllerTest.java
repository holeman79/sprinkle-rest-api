package com.kakaopay.sprinklerestapi.sprinkling.api;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kakaopay.sprinklerestapi.response.ApiResponseCode;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(SprinklingController.class)
public class SprinklingControllerTest {

    @Autowired
    protected MockMvc mockMvc;

    @Autowired
    protected ObjectMapper objectMapper;

    @MockBean
    protected SprinklingService sprinklingService;

    private final String X_USER_ID = "X-USER-ID";

    private final String X_ROOM_ID = "X-ROOM-ID";

    @Test
    @DisplayName("요청 Header 값 누락 시 BadRequest 발생")
    public void no_request_header() throws Exception{
        //given
        Request request = new Request();
        request.sprinkledMoney = 1000;
        request.peopleCount = 3;

        //when & then
        this.mockMvc.perform(post("/api/sprinklings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("code").value(ApiResponseCode.MISSING_REQUEST_HEADER.name()))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("subErrors").isEmpty());
    }

    @Test
    @DisplayName("요청 Header 값 검증 테스트 : X-USER-ID가 0보다 작거나 같으면 BadRequest")
    void validate_header_value_x_user_id() throws Exception {

        // Given
        Request request = new Request();
        request.sprinkledMoney = 1000;
        request.peopleCount = 3;

        // When
        final ResultActions actions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/sprinklings")
                                .header("X-USER-ID", 0)
                                .header("X-ROOM-ID", "")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON)
                                .content(objectMapper.writeValueAsString(request)));

        // Then
        actions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("code").value(ApiResponseCode.BAD_REQUEST_HEADER.name()))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("subErrors").isEmpty());
    }

    @Test
    @DisplayName("요청 Header 값 검증 테스트 : X-ROOM-ID가 blank면 BadRequest")
    void validate_header_value_x_room_id() throws Exception {

        // Given
        Request request = new Request();
        request.sprinkledMoney = 1000;
        request.peopleCount = 3;

        // When
        final ResultActions actions =
                mockMvc.perform(
                        RestDocumentationRequestBuilders.post("/api/sprinklings")
                                .header("X-USER-ID", 1L)
                                .header("X-ROOM-ID", "")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaTypes.HAL_JSON)
                                .content(objectMapper.writeValueAsString(request)));

        // Then
        actions
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("code").value(ApiResponseCode.BAD_REQUEST_HEADER.name()))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("subErrors").isEmpty());
    }



    @Test
    @DisplayName("뿌리기 금액 또는 받기 인원 수가 0 이하일 경우 Bad Request")
    public void create_sprinkling_request_less_than_zero() throws Exception{
        //given
        Long creatorId = 1L;
        String roomId = "R1";

        Request request = new Request();
        request.sprinkledMoney = 0;
        request.peopleCount = -3;

        //when & then
        this.mockMvc.perform(post("/api/sprinklings")
                .header(X_USER_ID, creatorId)
                .header(X_ROOM_ID, roomId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(this.objectMapper.writeValueAsString(request)))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("timestamp").exists())
                .andExpect(jsonPath("code").value(ApiResponseCode.BAD_PARAMETER.name()))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("subErrors").exists());
    }





    public static class Request {
        long sprinkledMoney;
        int peopleCount;

        public int getPeopleCount() {
            return peopleCount;
        }

        public long getSprinkledMoney() {
            return sprinkledMoney;
        }
    }
}
