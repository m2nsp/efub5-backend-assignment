package efub.assignment.comment;

import efub.assignment.community.board.domain.Board;
import efub.assignment.community.comment.domain.Comment;
import efub.assignment.community.comment.domain.CommentLike;
import efub.assignment.community.comment.dto.request.CommentLikeRequestDTO;
import efub.assignment.community.comment.repository.CommentLikeRepository;
import efub.assignment.community.comment.service.CommentLikeService;
import efub.assignment.community.comment.service.CommentService;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.domain.MemberStatus;
import efub.assignment.community.member.service.MemberService;
import efub.assignment.community.post.domain.Post;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class CommentLikeTest {

    @InjectMocks
    CommentLikeService commentLikeService;

    @Mock
    CommentLikeRepository commentLikeRepository;

    @Mock
    MemberService memberService;

    @Mock
    CommentService commentService;

    private Member member;
    private Board board;
    private Post post;
    private Comment comment;

    private List<CommentLike> commentLikes; //테스트용 in-memory store


    @Test
    public void 사용자는_댓글에_좋아요를_누를수가_있다() {
        //given
//        Member member = Member.create("2222222", "tester", "ewha", "test@test.com", "000000", MemberStatus.ACTIVE);
//        Board board = Board.create("컴공게시판", "공지사항", "desc", member);
//        Post post = Post.create(board, member, true, "컴공댕들 안녕");
//        Comment comment  = Comment.create(post, true, "테스트댓글", member);

        //when
        CommentLike commentLike = CommentLike.create(member, comment);
        //then
        assertEquals(member, commentLike.getMember());
        assertEquals(comment, commentLike.getComment());
    }


    @Test
    public void 같은_사용자가_중복_좋아요_불가() {
        //Given

        //in-memory store 초기화
        commentLikes = new ArrayList<>();

        ReflectionTestUtils.setField(comment, "commentId", 1L);
        CommentLikeRequestDTO requestDTO = CommentLikeRequestDTO.builder()
                .member(member)
                .comment(comment)
                .build();

        //commentValidation이 comment 반환하도록 설정
        given(commentService.commentValidation(comment.getCommentId())).willReturn(comment);
        given(memberService.findMemberByMemberId(member.getMemberId())).willReturn(member);

//        given(commentLikeRepository.findAllByComment(comment))
//                .willReturn(List.of(CommentLike.create(member, comment)));

        //findAllByComment는 현재 commentLikes의 복사본(현재 상태) 반환
//        given(commentLikeRepository.findAllByComment(comment))
//                .willAnswer(invocation -> new ArrayList<>(commentLikes));

        //findByCommentAndMember -> 상태에 따라서 Optional 반환
        given(commentLikeRepository.findByCommentAndMember(comment, member))
                .willAnswer(invocation -> commentLikes.stream()
                        .filter(cL -> cL.getComment().equals(comment) && cL.getMember().equals(member))
                        .findFirst());

        //save가 호출되면 commentLikes에 추가하고 저장된 객체 리턴
        when(commentLikeRepository.save(any(CommentLike.class)))
                .thenAnswer(invocation -> {
                    CommentLike saved = invocation.getArgument(0);
                    commentLikes.add(saved);
                    return saved;
                });

        //When
//        String msg1 = commentLikeService.createCommentLike(requestDTO);
//        String msg2 = commentLikeService.createCommentLike(requestDTO);     //중복 좋아요 시도

        String msg1 = commentLikeService.createCommentLike(comment.getCommentId(), member.getMemberId());
        String msg2 = commentLikeService.createCommentLike(comment.getCommentId(), member.getMemberId());


        //Then
        // assertEquals("좋아요가 생성되었습니다", msg1);
        // assertEquals("이미 좋아요를 누른 댓글입니다", msg2);

        //코드리뷰 반영 "문자열 그대로 비교하는 것은 권장 X -> .contains("좋아요")와 같이 부분 검증으로 변환"
        assertTrue(msg1.contains("생성되었습니다"));
        assertTrue(msg2.contains("이미 좋아요"));
        assertEquals(1, commentLikes.size());

//        // save는 실제로 한 번만 호출되었어야 함 (중복이면 save하지 않도록 서비스가 구현 될 필요 有)
//        verify(commentLikeRepository, atMost(1)).save(any(CommentLike.class));
    }


    // 필요한 객체들 생성
    @BeforeEach
    public void setUp(){
        member = Member.create("2222222", "tester", "ewha", "test@test.com", "000000", MemberStatus.ACTIVE);
        board = Board.create("컴공게시판", "공지사항", "desc", member);
        post = Post.create(board, member, true, "컴공댕들 안녕");
        comment  = Comment.create(post, true, "테스트댓글", member);
    }
}
