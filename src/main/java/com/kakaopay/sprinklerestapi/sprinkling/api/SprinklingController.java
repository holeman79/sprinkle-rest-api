package com.kakaopay.sprinklerestapi.sprinkling.api;

import com.kakaopay.sprinklerestapi.response.ApiResponseDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.SprinklingService;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.CreateSprinklingRequestDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.CreateSprinklingResponseDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.GetSprinklingResponseDto;
import com.kakaopay.sprinklerestapi.sprinkling.service.dto.UpdateSprinklingResponseDto;
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
            @RequestBody @Valid CreateSprinklingRequestDto createRequest){

        CreateSprinklingResponseDto createResponseDto = sprinklingService.create(createRequest, creatorId, roomId);

        var selfLinkBuilder = linkTo(SprinklingController.class).slash(createResponseDto.getId());
        URI createdUri = selfLinkBuilder.toUri();
        ApiResponseDto<CreateSprinklingResponseDto> apiResponseDto =
                ApiResponseDto.OK(createResponseDto)
                        .add(selfLinkBuilder.withSelfRel())
                        .add(selfLinkBuilder.withRel("receiving"))
                        .add(Link.of("/docs/index.html#sprinkling-create").withRel("profile"));
        return ResponseEntity.created(createdUri).body(apiResponseDto);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto> receive(@PathVariable Long id,
                                                  @RequestHeader("X-USER-ID") @Positive Long receiverId,
                                                  @RequestHeader("X-ROOM-ID") @NotBlank String roomId,
                                                  @RequestHeader("X-TOKEN") @NotBlank String token){

        UpdateSprinklingResponseDto updateResponseDto = sprinklingService.receive(id, receiverId, roomId, token);

        var selfLinkBuilder = linkTo(SprinklingController.class).slash(updateResponseDto.getId());
        ApiResponseDto<UpdateSprinklingResponseDto> apiResponseDto =
                ApiResponseDto.OK(updateResponseDto)
                        .add(selfLinkBuilder.withSelfRel())
                        .add(Link.of("/docs/index.html#sprinkling-receiving").withRel("profile"));

        return ResponseEntity.ok(apiResponseDto);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto> findById(@PathVariable Long id,
                                                   @RequestHeader("X-USER-ID") @Positive Long viewerId,
                                                   @RequestHeader("X-TOKEN") @NotBlank String token){

        GetSprinklingResponseDto getResponseDto = sprinklingService.findById(id, viewerId, token);

        var selfLinkBuilder = linkTo(SprinklingController.class).slash(getResponseDto.getId());
        ApiResponseDto<GetSprinklingResponseDto> apiResponseDto =
                ApiResponseDto.OK(getResponseDto)
                        .add(selfLinkBuilder.withSelfRel())
                        .add(selfLinkBuilder.withRel("receiving"))
                        .add(Link.of("/docs/index.html#sprinkling-get").withRel("profile"));
        return ResponseEntity.ok(apiResponseDto);
    }
}
