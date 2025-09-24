package efub.assignment.community.comment.controller;

import efub.assignment.community.comment.domain.Comment;
import efub.assignment.community.comment.dto.request.CommentLikeRequestDTO;
import efub.assignment.community.comment.dto.request.CommentRequestDTO;
import efub.assignment.community.comment.dto.request.CommentUpdateRequestDTO;
import efub.assignment.community.comment.dto.response.CommentListResponseDTO;
import efub.assignment.community.comment.dto.response.CommentResponseDTO;
import efub.assignment.community.comment.service.CommentLikeService;
import efub.assignment.community.comment.service.CommentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class CommentController {
    private final CommentService commentService;
    private final CommentLikeService commentLikeService;

    public CommentController(CommentService commentService, CommentLikeService commentLikeService) {
        this.commentService = commentService;
        this.commentLikeService = commentLikeService;
    }

    //댓글 생성
    @PostMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentResponseDTO> createComment(@PathVariable Long postId, @RequestBody CommentRequestDTO requestDTO) {
        CommentResponseDTO created = commentService.create(postId, requestDTO);
        return (created != null)?
                ResponseEntity.status(HttpStatus.CREATED).body(created) :
                ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        //null로 성공/실패를 구분하는 방식은 안전성이 떨어진다고 한다. -> 서비스단에서 명시적인 예외를 던지고, Controller에서는 예외처리하지 않거나 @ControllerAdvice에서 전역처리하는 것이 좋음
    }

    //게시글에 해당하는 댓글 목록 조회
    @GetMapping("/posts/{postId}/comments")
    public ResponseEntity<CommentListResponseDTO> getCommentList(@PathVariable Long postId) {
        CommentListResponseDTO commentList = commentService.getCommentList(postId);
        //해당 게시글에 댓글이 없는 경우도 null임..(not ERROR)
        //피드백 : REST에서는 빈 리스트([]) 반환이 표준임. 따라서 서비스단에서 List<>가 비어있을 경우에도 Collections.emptyList() 반환하기
        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    //작성자별 댓글 목록 조회
    @GetMapping("/members/{memberId}/comments")
    public ResponseEntity<CommentListResponseDTO> getMemberCommentList(@PathVariable Long memberId) {
        CommentListResponseDTO commentList = commentService.getMemberCommentList(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(commentList);
    }

    //댓글 수정
    @PatchMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<CommentResponseDTO> updateComment(@PathVariable Long postId,
                                                            @PathVariable Long commentId,
                                                            @RequestBody CommentUpdateRequestDTO requestDTO,
                                                            @RequestHeader Long memberId) {
        CommentResponseDTO updatedComment = commentService.updateComment(postId, requestDTO, memberId);
        return (updatedComment != null)?
                ResponseEntity.status(HttpStatus.OK).body(updatedComment) :
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
    }

    //댓글 삭제
    @DeleteMapping("/posts/{postId}/comments/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long postId, @PathVariable Long commentId,
                                                @RequestHeader Long memberId) {
        commentService.deleteComment(commentId, memberId);
        return ResponseEntity.status(HttpStatus.OK).body("댓글이 삭제되었습니다.");
    }

    // 댓글 좋아요 기능 추가 : TDD
    @PostMapping("/comments/{commentId}/like")
    public ResponseEntity<String> createCommentLike(CommentLikeRequestDTO requestDTO) {
       String response = commentLikeService.createCommentLike(requestDTO);
       return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

}
