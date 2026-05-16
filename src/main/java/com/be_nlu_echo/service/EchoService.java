package com.be_nlu_echo.service;

import com.be_nlu_echo.dto.request.CommentRequest;
import com.be_nlu_echo.dto.request.MediaRequest;
import com.be_nlu_echo.dto.request.RequestCreateEcho;
import com.be_nlu_echo.dto.respone.*;
import com.be_nlu_echo.entity.Comment;
import com.be_nlu_echo.entity.Echo;
import com.be_nlu_echo.entity.EchoMedia;
import com.be_nlu_echo.entity.User;
import com.be_nlu_echo.enums.*;
import com.be_nlu_echo.exception.AppException;
import com.be_nlu_echo.repository.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.data.domain.PageRequest;
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
    LikeRepository likeRepository;
    CommentRepository commentRepository;
    EchoUnLockRepository echoUnLockRepository;

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


    public List<EchoPreviewResponse> getEchoPreviews(Double latitude, Double longitude, int page, int limit) {

        List<EchoPreviewProjection>  projections = echoRepository.getEchoPreviews(
                latitude,
                longitude,
                EchoStatus.ACTIVE.name(),
                Visibility.PUBLIC.name(),
                PageRequest.of(page, limit)
        ).getContent();


        return projections.stream()
                .map(p -> EchoPreviewResponse.builder()
                        .id(p.getId())
                        .title(p.getTitle())
                        .echoType(EchoType.valueOf(p.getEchoType()))
                        .distance(p.getDistance())
                        .likeCount(p.getLikeCount())
                        .commentCount(p.getCommentCount())
                        .anonymous(p.getAnonymous())
                        .capsule(p.getCapsule())
                        .unlockTime(p.getUnlockTime())
                        .mode(resolveEchoMode(p.getAnonymous()
                                , p.getCapsule()))
                        .unlockRadiusM(p.getUnlockRadiusM())
                        .longitude(p.getLongitude())
                        .latitude(p.getLatitude())
                        .build())
                .toList();
    }

    private EchoMode resolveEchoMode(Boolean anonymous, Boolean capsule) {
        if (Boolean.TRUE.equals(anonymous)) {
            return EchoMode.GHOST;
        }
        if (Boolean.TRUE.equals(capsule)) {
            return EchoMode.CAPSULE;
        }
        return EchoMode.NORMAL;
    }

    public EchoDetailResponse getEchoDetail(Long id,Long userId) {
        Echo echo = echoRepository.findById(id)
                .orElseThrow(() ->
                        new AppException("Echo not found with id: " + id,StatusCode.NOT_FOUND));

        UserResponse user = UserResponse.builder()
                .id(echo.getUser().getId())
                .name(echo.getUser().getFullName())
                .avatarUrl(echo.getUser().getAvatarUrl())
                .build();


        List<EchoMediaResponse> media = echo.getMediaList() != null
                ? echo.getMediaList().stream()
                .map(m -> EchoMediaResponse.builder()
                        .id(m.getId())
                        .mediaType(m.getMediaType())
                        .mediaUrl(m.getMediaUrl())
                        .width(m.getWidth())
                        .height(m.getHeight())
                        .durationSeconds(m.getDurationSeconds())
                        .build())
                .toList()
                : List.of();

        boolean isLike = likeRepository.existsByEchoIdAndUserId(id, userId);

            return EchoDetailResponse.builder()
                    .id(echo.getId())
                    .title(echo.getTitle())
                    .description(echo.getContent())
                    .locationName(echo.getLocationName())
                    .anonymous(echo.isAnonymous())
                    .capsule(echo.isCapsule())
                    .unlockTime(echo.getUnlockAt())
                    .likeCount(echo.getLikeCount())
                    .unlockCount(echo.getUnlockCount())
                    .commentsCount(echo.getCommentCount())
                    .media(media)
                    .user(user)
                    .isLike(isLike)
                    .createdAt(echo.getCreatedAt())
                    .build();

    }

    public Echo getEchoById(Long id) {
        return echoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Echo not found with id: " + id));
    }

    public CommentResponse commentForEcho(Long id, CommentRequest request) {

        Echo echo = echoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Echo not found with id: " + id));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .echo(echo)
                .user(user)
                .status(CommentStatus.ACTIVE)

                .build();

        commentRepository.save(comment);

        return toResponse(comment);

    }

    private CommentResponse toResponse(Comment comment) {
        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .user(UserResponse.builder()
                        .id(comment.getUser().getId())
                        .name(comment.getUser().getFullName())
                        .avatarUrl(comment.getUser().getAvatarUrl())
                        .build())
                .createdAt(comment.getCreatedAt())
                .build();
    }

    public EchoHistorySummaryResponse getEchoHistorySummary(Long userId) {
        long totalExploredEchoes = echoUnLockRepository.countDistinctUnlockedEchoes(userId);
        long totalLikedEchoes = likeRepository.countLikedEchoesByUserId(userId);
        long totalCreateEchos = echoRepository.countCreatedEchoByUserId(userId);

        EchoHistorySummaryResponse response = new EchoHistorySummaryResponse();
        response.setTotalExploredEchoes(totalExploredEchoes);
        response.setTotalLikedEchoes(totalLikedEchoes);
        response.setTotalCreateEchos(totalCreateEchos);

        return response;



    }


}
