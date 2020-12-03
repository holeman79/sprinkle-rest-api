package com.kakaopay.sprinklerestapi.sprinkling.api.document;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.api.ApiDocumentationTest;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.CreateSprinklingRequestDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.CreateSprinklingResponseDto;
import lombok.Getter;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.kakaopay.sprinklerestapi.sprinkling.api.document.utils.ApiDocumentUtils.getDocumentRequest;
import static com.kakaopay.sprinklerestapi.sprinkling.api.document.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SprinklingCreateDocumentationTest extends ApiDocumentationTest {

    @Test
    public void create_sprinkling() throws Exception{
        // given
        Long sprinklingId = 1L;
        Long creatorId = 100L;
        String roomId = "R1";
        String token = "k8A";
        int peopleCount = 4;
        Long sprinkledMoney = 1000L;
        Long maxRandomMoney = 250L;

        CreateSprinklingResponseDto responseDto = CreateSprinklingResponseDto.builder()
                .id(sprinklingId)
                .roomId(roomId)
                .creatorId(creatorId)
                .peopleCount(peopleCount)
                .sprinkledMoney(Money.wons(sprinkledMoney))
                .token(token)
                .maxRandomMoney(Money.wons(maxRandomMoney))
                .build();

        given(sprinklingService.create(any(CreateSprinklingRequestDto.class), eq(creatorId), eq(roomId)))
                .willReturn(responseDto);

        Request request = new Request();
        request.sprinkledMoney = sprinkledMoney;
        request.peopleCount = peopleCount;

        // when
        String requestUrl = "/api/sprinklings";
        ResultActions result = this.mockMvc.perform(post(requestUrl)
                .header("X-USER-ID", creatorId)
                .header("X-ROOM-ID", roomId)
                .content(this.objectMapper.writeValueAsString(request))
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(header().exists(HttpHeaders.LOCATION))
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("data.id").value(sprinklingId))
                .andExpect(jsonPath("data.creatorId").value(creatorId))
                .andExpect(jsonPath("data.roomId").value(roomId))
                .andExpect(jsonPath("data.peopleCount").value(peopleCount))
                .andExpect(jsonPath("data.sprinkledMoney").value(sprinkledMoney))
                .andExpect(jsonPath("data.token").exists())
                .andExpect(jsonPath("data.maxRandomMoney").value(maxRandomMoney))
                .andExpect(jsonPath("code").value("OK"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.receiving").exists())
                .andExpect(jsonPath("_links.read").exists())
                .andExpect(jsonPath("_links.profile").exists())

                .andDo(document("sprinkling-create",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("receiving").description("받기 링크"),
                                linkWithRel("read").description("조회 링크"),
                                linkWithRel("profile").description("뿌리기 api 문서 이동 Link")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type 헤더"),
                                headerWithName("X-USER-ID").description("뿌리기 생성자 ID"),
                                headerWithName("X-ROOM-ID").description("대화방 ID")
                        ),
                        requestFields(
                                fieldWithPath("sprinkledMoney").type(JsonFieldType.NUMBER).description("뿌린 금액"),
                                fieldWithPath("peopleCount").type(JsonFieldType.NUMBER).description("뿌린 인원")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.LOCATION).description("Location 헤더(self uri)"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type 헤더(hal+json)")
                        ),
                        responseFields(beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("뿌리기 ID"),
                                fieldWithPath("roomId").type(JsonFieldType.STRING).description("뿌리기가 생성된 대화방 ID"),
                                fieldWithPath("creatorId").type(JsonFieldType.NUMBER).description("뿌리기 생성자 ID"),
                                fieldWithPath("peopleCount").type(JsonFieldType.NUMBER).description("뿌린 인원"),
                                fieldWithPath("sprinkledMoney").type(JsonFieldType.NUMBER).description("뿌린 금액"),
                                fieldWithPath("token").type(JsonFieldType.STRING).description("예측 불가능한 3자리 문자열 토큰"),
                                fieldWithPath("maxRandomMoney").type(JsonFieldType.NUMBER).description("받을 수 있는 최고 금액")
                        )
                ));
    }

    @Getter
    public static class Request {
        long sprinkledMoney;
        int peopleCount;

    }
}
