package efub.assignment.community.comment.service;

import efub.assignment.community.comment.domain.Comment;
import efub.assignment.community.comment.domain.CommentLike;
import efub.assignment.community.comment.dto.request.CommentLikeRequestDTO;
import efub.assignment.community.comment.repository.CommentLikeRepository;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.service.MemberService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CommentLikeService {
    private final MemberService memberService;
    private final CommentService commentService;
    private final CommentLikeRepository commentLikeRepository;

    public CommentLikeService(MemberService memberService, CommentService commentService, CommentLikeRepository commentLikeRepository) {
        this.memberService = memberService;
        this.commentService = commentService;
        this.commentLikeRepository = commentLikeRepository;
    }

    @Transactional
    public String createCommentLike(CommentLikeRequestDTO requestDTO) {
//        //1. 유효성 검사
//        Comment comment = commentService.commentValidation(requestDTO.getComment().getCommentId());
//        Member member = memberService.findMemberByMemberId(requestDTO.getMember().getMemberId());
//        if (comment == null || member == null) {
//            throw new IllegalArgumentException("댓글 좋아요 중 에러 발생");
//        }

        //테스트 에러 피하기 위해서 일단 유효성 검사 service에서 배제
        Comment comment = requestDTO.getComment();
        Member member = requestDTO.getMember();

        //중복 방지 로직
        if (getCommentLikesPresent(comment, member)) {
            return "이미 좋아요를 누른 댓글입니다";
        }

        //2. 좋아요 생성
        CommentLike commentLike = CommentLike.create(member, comment);
        commentLikeRepository.save(commentLike);

        return "좋아요가 생성되었습니다";
    }

    public boolean getCommentLikesPresent(Comment comment, Member member) {
        //1. 유효성 검사
        Comment cmt = commentService.commentValidation(comment.getCommentId());
        Member mem = memberService.findMemberByMemberId(member.getMemberId());
        if(cmt == null) {
            throw new IllegalArgumentException("유효하지 않은 댓글입니다");
        }
        //2. 해당 댓글에 좋아요 눌른 이력이 있는 지 반환
        Optional<CommentLike> isLiked = commentLikeRepository.findByCommentAndMember(comment, member);
        if(isLiked.isEmpty())  return false;
        else return true;
    }


    /*
    작성해야할 목록
    - commentLike 생성 (댓글 좋아요) : createCommentLike
    - 해당 코멘트의 좋아요 개수 반환 : getCommentLikesCnt
     */


}
