package com.kakaopay.sprinklerestapi.sprinkling.api;

import com.kakaopay.sprinklerestapi.sprinkling.domain.Sprinkling;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingService;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;

import static com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingDto.CreateResponse;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/sprinklings")
@RequiredArgsConstructor
public class SprinklingController {

    private final SprinklingService sprinklingService;

    @PostMapping
    public ResponseEntity<CreateResponse> createSprinkling(
            @RequestHeader("X-USER-ID") @Positive int creatorId,
            @RequestHeader("X-ROOM-ID") @NotBlank String roomId,
            @RequestBody @Valid SprinklingDto.CreateRequest createRequest){

        Sprinkling sprinkling = sprinklingService.createSprinkling(createRequest, creatorId, roomId);

        CreateResponse createResponse = CreateResponse.builder()
                .id(sprinkling.getId())
                .token(sprinkling.getToken())
                .build()

                ;

        WebMvcLinkBuilder selfLinkBuilder = linkTo(SprinklingController.class).slash(createResponse.getId());


        return ResponseEntity.created(selfLinkBuilder.toUri()).body(createResponse);
    }
}
