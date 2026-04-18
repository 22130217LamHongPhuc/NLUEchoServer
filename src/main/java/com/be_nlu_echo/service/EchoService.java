package com.be_nlu_echo.service;

import com.be_nlu_echo.dto.request.MediaRequest;
import com.be_nlu_echo.dto.request.RequestCreateEcho;
import com.be_nlu_echo.dto.respone.CreateEchoResponse;
import com.be_nlu_echo.dto.respone.EchoListItemResponse;
import com.be_nlu_echo.dto.respone.MediaResponse;
import com.be_nlu_echo.entity.Echo;
import com.be_nlu_echo.entity.EchoMedia;
import com.be_nlu_echo.entity.User;
import com.be_nlu_echo.enums.EchoStatus;
import com.be_nlu_echo.enums.MediaType;
import com.be_nlu_echo.enums.Visibility;
import com.be_nlu_echo.repository.EchoMediaRepository;
import com.be_nlu_echo.repository.EchoRepository;
import com.be_nlu_echo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
@Data
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public class EchoService {
    private static final int CONTENT_PREVIEW_LIMIT = 160;

    EchoRepository echoRepository;
    EchoMediaRepository echoMediaRepository;
    UserRepository userRepository;

    public List<EchoListItemResponse> getAllEchos() {
        return echoRepository.findFeedEchos(EchoStatus.ACTIVE, Visibility.PUBLIC)
                .stream()
                .map(this::mapToListItemResponse)
                .toList();
    }

    public CreateEchoResponse createEcho(RequestCreateEcho request, User user) {

        User userEntity = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + user.getId()));

        // 1. Create Echo
        Echo echo = Echo.builder()
                .user(userEntity)
                .title(request.getTitle())
                .content(request.getContent())
                .echoType(request.getEchoType())

                .latitude(BigDecimal.valueOf(request.getLatitude()))
                .longitude(BigDecimal.valueOf(request.getLongitude()))
                .gpsAccuracy(BigDecimal.valueOf(request.getGpsAccuracy()))
                .locationName(request.getLocationName())

                .unlockRadiusM(30) // default radius (bạn có thể cho từ request)

                .anonymous(request.isAnonymous())
                .capsule(request.isCapsule())

                .unlockAt(
                        request.getUnlockAt() != null && request.isCapsule()
                                ? request.getUnlockAt()
                                .atZone(ZoneId.systemDefault())
                                .toLocalDateTime()
                                : null
                )

                .visibility(request.getVisibility())
                .status(EchoStatus.ACTIVE)
                .likeCount(0)
                .commentCount(0)
                .unlockCount(0)
                .build();

        Echo savedEcho = echoRepository.save(echo);

        List<EchoMedia> mediaList = new ArrayList<>();

        if (request.getImages() != null) {
            int sortOrder = 0;
            for (MediaRequest media : request.getImages()) {
                mediaList.add(
                        EchoMedia.builder()
                                .echo(savedEcho)
                                .mediaType(MediaType.IMAGE)
                                .mediaUrl(media.getUrl())
                                .publicId(media.getPublicId())
                                .mimeType(media.getMimeType())
                                .sortOrder(sortOrder++)
                                .build()
                );
            }
        }

        if (request.getAudio() != null) {
            mediaList.add(
                    EchoMedia.builder()
                            .echo(savedEcho)
                            .mediaType(MediaType.AUDIO)
                            .mediaUrl(request.getAudio().getUrl())
                            .publicId(request.getAudio().getPublicId())
                            .mimeType(request.getAudio().getMimeType())
                            .durationSeconds(request.getAudio().getDuration())
                            .sortOrder(0)
                            .build()
            );
        }

        if (!mediaList.isEmpty()) {
            echoMediaRepository.saveAll(mediaList);
        }

        savedEcho.setMediaList(mediaList);

        // 4. Map → Response DTO
        return mapToResponse(savedEcho);
    }

    private CreateEchoResponse mapToResponse(Echo echo) {
        return CreateEchoResponse.builder()
                .id(echo.getId())
                .title(echo.getTitle())
                .content(echo.getContent())
                .echoType(echo.getEchoType())

                .latitude(echo.getLatitude().doubleValue())
                .longitude(echo.getLongitude().doubleValue())
                .gpsAccuracy(
                        echo.getGpsAccuracy() != null
                                ? echo.getGpsAccuracy().doubleValue()
                                : null
                )
                .locationName(echo.getLocationName())

                .unlockRadiusM(echo.getUnlockRadiusM())

                .anonymous(echo.isAnonymous())
                .capsule(echo.isCapsule())
                .unlockAt(echo.getUnlockAt())

                .visibility(echo.getVisibility())
                .status(echo.getStatus().name())

                .likeCount(echo.getLikeCount())
                .commentCount(echo.getCommentCount())
                .unlockCount(echo.getUnlockCount())

                .createdAt(echo.getCreatedAt())
                .updatedAt(echo.getUpdatedAt())

                .mediaList(
                        echo.getMediaList() != null
                                ? echo.getMediaList().stream()
                                .map(m -> MediaResponse.builder()
                                        .url(m.getMediaUrl())
                                        .build())
                                .toList()
                                : List.of()
                )
                .build();
    }

    private EchoListItemResponse mapToListItemResponse(Echo echo) {
        boolean isAnonymous = echo.isAnonymous();

        return EchoListItemResponse.builder()
                .id(echo.getId())
                .title(echo.getTitle())
                .contentPreview(toContentPreview(echo.getContent()))
                .echoType(echo.getEchoType())
                .anonymous(isAnonymous)
                .authorName(isAnonymous ? null : echo.getUser().getFullName())
                .authorAvatarUrl(isAnonymous ? null : echo.getUser().getAvatarUrl())
                .likeCount(echo.getLikeCount())
                .commentCount(echo.getCommentCount())
                .createdAt(echo.getCreatedAt())
                .build();
    }

    private String toContentPreview(String content) {
        if (content == null || content.isBlank()) {
            return content;
        }

        if (content.length() <= CONTENT_PREVIEW_LIMIT) {
            return content;
        }

        return content.substring(0, CONTENT_PREVIEW_LIMIT).trim() + "...";
    }
}
