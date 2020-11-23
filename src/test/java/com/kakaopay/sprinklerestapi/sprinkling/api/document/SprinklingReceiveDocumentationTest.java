package com.kakaopay.sprinklerestapi.sprinkling.api.document;

import com.kakaopay.sprinklerestapi.sprinkling.api.ApiDocumentationTest;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.UpdateSprinklingResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import static com.kakaopay.sprinklerestapi.sprinkling.api.document.utils.ApiDocumentUtils.getDocumentRequest;
import static com.kakaopay.sprinklerestapi.sprinkling.api.document.utils.ApiDocumentUtils.getDocumentResponse;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.put;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SprinklingReceiveDocumentationTest extends ApiDocumentationTest {

    @Test
    public void receiving_success() throws Exception{
        // given
        Long sprinklingId = 1L;
        String roomId = "R1";
        Long creatorId = 100L;
        Long receiverId = 1000L;
        Long receivedMoney = 574L;
        boolean isMaxRandomMoney = false;

        UpdateSprinklingResponseDto responseDto = UpdateSprinklingResponseDto.builder()
                .id(sprinklingId)
                .creatorId(creatorId)
                .receiverId(receiverId)
                .roomId(roomId)
                .receivedMoney(receivedMoney)
                .isMaxRandomMoney(isMaxRandomMoney)
                .build();

        String token = "aHZ";
        given(sprinklingService.receive(sprinklingId, receiverId, roomId, token))
                .willReturn(responseDto);

        // when
        String requestUrl = "/api/sprinklings/{id}";
        ResultActions result = this.mockMvc.perform(put(requestUrl, sprinklingId)
                .header("X-USER-ID", receiverId)
                .header("X-ROOM-ID", roomId)
                .header("X-TOKEN", token)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaTypes.HAL_JSON)
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("data.id").value(sprinklingId))
                .andExpect(jsonPath("data.receiverId").value(receiverId))
                .andExpect(jsonPath("data.receivedMoney").value(receivedMoney))
                .andExpect(jsonPath("code").value("OK"))
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.profile").exists())

                .andDo(document("sprinkling-receive",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("뿌리기 아이디")
                        ),
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("profile").description("받기 api 문서 이동 Link")
                        ),
                        requestHeaders(
                                headerWithName(HttpHeaders.ACCEPT).description("Accept 헤더"),
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type 헤더"),
                                headerWithName("X-USER-ID").description("뿌리기 생성자 ID"),
                                headerWithName("X-ROOM-ID").description("대화방 ID"),
                                headerWithName("X-TOKEN").description("뿌리기 생성 시 발급된 토큰")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type 헤더(hal+json)")
                        ),
                        responseFields(beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("뿌리기 ID"),
                                fieldWithPath("roomId").type(JsonFieldType.STRING).description("뿌리기가 생성된 대화방 ID"),
                                fieldWithPath("creatorId").type(JsonFieldType.NUMBER).description("뿌리기 생성자 ID"),
                                fieldWithPath("receiverId").type(JsonFieldType.NUMBER).description("받기 완료한 유저 ID"),
                                fieldWithPath("receivedMoney").type(JsonFieldType.NUMBER).description("받은 금액"),
                                fieldWithPath("isMaxRandomMoney").type(JsonFieldType.BOOLEAN).description("최고 받은 금액 여부")
                        )
                ));
    }
}
