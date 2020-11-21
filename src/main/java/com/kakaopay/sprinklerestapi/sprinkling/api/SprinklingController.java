package com.kakaopay.sprinklerestapi.sprinkling.api;

import com.kakaopay.sprinklerestapi.sprinkling.response.ApiResponseDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingCreateRequestDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingCreateResponseDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingService;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingUpdateResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.Link;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.net.URI;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@RestController
@RequestMapping("/api/sprinklings")
@RequiredArgsConstructor
public class SprinklingController {

    private final SprinklingService sprinklingService;

    @PostMapping
    public ResponseEntity<ApiResponseDto> create(
            @RequestHeader("X-USER-ID") @Positive Long creatorId,
            @RequestHeader("X-ROOM-ID") @NotBlank String roomId,
            @RequestBody @Valid SprinklingCreateRequestDto createRequest){

        SprinklingCreateResponseDto createResponseDto = sprinklingService.create(createRequest, creatorId, roomId);
        var selfLinkBuilder = linkTo(SprinklingController.class).slash(createResponseDto.getId());
        URI createdUri = selfLinkBuilder.toUri();
        ApiResponseDto<SprinklingCreateResponseDto> apiResponseDto =
                ApiResponseDto.OK(createResponseDto)
                .add(selfLinkBuilder.withSelfRel())
                .add(selfLinkBuilder.withRel("receiving"))
                .add(Link.of("/docs/index.html#sprinkling-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(apiResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto> receiving(@PathVariable Long id,
                                                    @RequestHeader("X-USER-ID") @Positive Long receiverId,
                                                    @RequestHeader("X-ROOM-ID") @NotBlank String roomId,
                                                    @RequestHeader("X-TOKEN") @NotBlank String token){
        SprinklingUpdateResponseDto updateResponseDto = sprinklingService.receive(id, receiverId, roomId, token);
        var selfLinkBuilder = linkTo(SprinklingController.class).slash(updateResponseDto.getId());
        ApiResponseDto<SprinklingUpdateResponseDto> apiResponseDto =
                ApiResponseDto.OK(updateResponseDto)
                .add(selfLinkBuilder.withSelfRel())
                .add(Link.of("/docs/index.html#sprinkling-receiving").withRel("profile"));

        return ResponseEntity.ok(apiResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> findById(@PathVariable Long id){

        return null;
    }
}
