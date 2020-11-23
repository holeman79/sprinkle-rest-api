package com.kakaopay.sprinklerestapi.sprinkling.api.document;

import com.kakaopay.sprinklerestapi.generic.money.domain.Money;
import com.kakaopay.sprinklerestapi.sprinkling.api.ApiDocumentationTest;
import com.kakaopay.sprinklerestapi.sprinkling.domain.Receiving;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.GetSprinklingResponseDto;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpHeaders;
import org.springframework.restdocs.payload.JsonFieldType;
import org.springframework.test.web.servlet.ResultActions;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static com.kakaopay.sprinklerestapi.sprinkling.Fixtures.aReceiving;
import static com.kakaopay.sprinklerestapi.sprinkling.api.document.utils.ApiDocumentUtils.getDocumentRequest;
import static com.kakaopay.sprinklerestapi.sprinkling.api.document.utils.ApiDocumentUtils.getDocumentResponse;
import static com.kakaopay.sprinklerestapi.sprinkling.api.document.utils.DocumentFormatGenerator.getDateFormat;
import static com.kakaopay.sprinklerestapi.sprinkling.service.dto.GetSprinklingResponseDto.ReceivingDto;
import static com.kakaopay.sprinklerestapi.sprinkling.service.dto.GetSprinklingResponseDto.builder;
import static java.util.stream.Collectors.toList;
import static org.mockito.BDDMockito.given;
import static org.springframework.restdocs.headers.HeaderDocumentation.*;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.linkWithRel;
import static org.springframework.restdocs.hypermedia.HypermediaDocumentation.links;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.mockmvc.RestDocumentationRequestBuilders.get;
import static org.springframework.restdocs.payload.PayloadDocumentation.*;
import static org.springframework.restdocs.request.RequestDocumentation.parameterWithName;
import static org.springframework.restdocs.request.RequestDocumentation.pathParameters;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class SprinklingReadDocumentationTest extends ApiDocumentationTest {

    @Test
    public void find_by_id_success() throws Exception{
        // given
        Long sprinklingId = 1L;
        String roomId = "R1";
        Long viewerId = 100L;
        LocalDateTime sprinkledTime = LocalDateTime.now();
        Long sprinkledMoney = 1000L;
        Long firstReceivedMoney = 283L;
        Long secondReceivedMoney = 574L;
        Long firstReceiverId = 1001L;
        Long secondReceiverId = 1002L;
        Long receivedMoney = firstReceivedMoney + secondReceivedMoney;
        Long maxRandomMoney = 574L;
        List<Receiving> receivings = Arrays.asList(
                aReceiving()
                        .id(1L)
                        .receiverId(firstReceiverId)
                        .receivedMoney(Money.wons(firstReceivedMoney))
                        .build(),
                aReceiving()
                        .id(2L)
                        .receiverId(secondReceiverId)
                        .receivedMoney(Money.wons(secondReceivedMoney))
                        .build()
        );
        String token = "aHZ";

        GetSprinklingResponseDto responseDto = builder()
                .id(sprinklingId)
                .roomId(roomId)
                .creatorId(viewerId)
                .sprinkledTime(sprinkledTime)
                .sprinkledMoney(sprinkledMoney)
                .receivedMoney(receivedMoney)
                .maxRandomMoney(maxRandomMoney)
                .receivingDtos(receivings.stream().map(ReceivingDto::new).collect(toList()))
                .build();

        given(sprinklingService.findById(sprinklingId, viewerId, token))
                .willReturn(responseDto);

        // when
        String requestUrl = "/api/sprinklings/{id}";
        ResultActions result = this.mockMvc.perform(get(requestUrl, sprinklingId)
                .header("X-USER-ID", viewerId)
                .header("X-TOKEN", token)
        );

        // then
        result
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_TYPE, MediaTypes.HAL_JSON_VALUE))
                .andExpect(jsonPath("data.id").value(sprinklingId))
                .andExpect(jsonPath("data.roomId").value(roomId))
                .andExpect(jsonPath("data.creatorId").value(viewerId))
                .andExpect(jsonPath("data.sprinkledTime").exists())
                .andExpect(jsonPath("data.sprinkledMoney").value(sprinkledMoney))
                .andExpect(jsonPath("data.receivedMoney").value(receivedMoney))
                .andExpect(jsonPath("data.maxRandomMoney").value(maxRandomMoney))
                .andExpect(jsonPath("data.receivingDtos[0].receivedMoney").value(firstReceivedMoney))
                .andExpect(jsonPath("data.receivingDtos[0].receiverId").value(firstReceiverId))
                .andExpect(jsonPath("code").value("OK"))
                .andExpect(jsonPath("message").exists())
                .andExpect(jsonPath("_links.self").exists())
                .andExpect(jsonPath("_links.receiving").exists())
                .andExpect(jsonPath("_links.profile").exists())

                .andDo(document("sprinkling-read",
                        getDocumentRequest(),
                        getDocumentResponse(),
                        pathParameters(
                                parameterWithName("id").description("뿌리기 아이디")
                        ),
                        links(
                                linkWithRel("self").description("link to self"),
                                linkWithRel("receiving").description("받기 링크"),
                                linkWithRel("profile").description("조회 api 문서 이동 Link")
                        ),
                        requestHeaders(
                                headerWithName("X-USER-ID").description("뿌리기 생성자 ID"),
                                headerWithName("X-TOKEN").description("뿌리기 생성 시 발급된 토큰")
                        ),
                        responseHeaders(
                                headerWithName(HttpHeaders.CONTENT_TYPE).description("Content-type 헤더(hal+json)")
                        ),
                        responseFields(beneathPath("data").withSubsectionId("data"),
                                fieldWithPath("id").type(JsonFieldType.NUMBER).description("뿌리기 ID"),
                                fieldWithPath("roomId").type(JsonFieldType.STRING).description("뿌리기가 생성된 대화방 ID"),
                                fieldWithPath("creatorId").type(JsonFieldType.NUMBER).description("뿌리기 생성자 ID"),
                                fieldWithPath("sprinkledTime").type(JsonFieldType.STRING).attributes(getDateFormat()).description("뿌리기 생성시간"),
                                fieldWithPath("sprinkledMoney").type(JsonFieldType.NUMBER).description("뿌린 금액"),
                                fieldWithPath("receivedMoney").type(JsonFieldType.NUMBER).description("현재까지 받기 완료된 금액"),
                                fieldWithPath("maxRandomMoney").type(JsonFieldType.NUMBER).description("최고 받을 수 있는 금액"),
                                fieldWithPath("receivingDtos[].receivedMoney").type(JsonFieldType.NUMBER)
                                        .description("받기 완료된 정보 [받은 금액] 리스트"),
                                fieldWithPath("receivingDtos[].receiverId").type(JsonFieldType.NUMBER)
                                        .description("받기 완료된 정보 [받은 사용자 아이디] 리스트")
                        )
                ));
    }
}
