package com.be_nlu_echo.service;

import com.be_nlu_echo.dto.request.CommentRequest;
import com.be_nlu_echo.dto.respone.CommentResponse;
import com.be_nlu_echo.dto.respone.UserResponse;
import com.be_nlu_echo.entity.Comment;
import com.be_nlu_echo.entity.Echo;
import com.be_nlu_echo.entity.User;
import com.be_nlu_echo.enums.CommentStatus;
import com.be_nlu_echo.enums.MissionEventType;
import com.be_nlu_echo.repository.CommentRepository;
import com.be_nlu_echo.repository.EchoRepository;
import com.be_nlu_echo.repository.UserRepository;
import lombok.AllArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
@AllArgsConstructor
public class CommentService {
    UserRepository userRepository;
    EchoRepository echoRepository;
    CommentRepository commentRepository;
    MissionService missionService;

    public List<CommentResponse> getCommentsByEchoId(Long echoId) {
        Echo echo = echoRepository.findById(echoId)
                .orElseThrow(() -> new RuntimeException("Echo not found with id: " + echoId));
        List<Comment> comments = echo.getComments();

        return comments.stream().map(this::toResponse).toList();
    }

    public CommentResponse createComment(Long echoId, CommentRequest request) {
        Echo echo = echoRepository.findById(echoId)
                .orElseThrow(() -> new RuntimeException("Echo not found with id: " + echoId));

        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        Comment comment = Comment.builder()
                .content(request.getContent())
                .echo(echo)
                .user(user)
                .status(CommentStatus.ACTIVE)

                .build();

        commentRepository.save(comment);

        missionService.handleEvent(MissionEventType.ECHO_COMMENTED,user);


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
}
