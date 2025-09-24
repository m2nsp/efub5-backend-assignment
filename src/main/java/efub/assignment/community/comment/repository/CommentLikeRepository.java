package efub.assignment.community.comment.repository;

import efub.assignment.community.comment.domain.Comment;
import efub.assignment.community.comment.domain.CommentLike;
import efub.assignment.community.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentLikeRepository extends JpaRepository<CommentLike, Long> {
    //해당사용자가 해당 코멘트에 좋아요 했는지
    Optional<CommentLike> findByCommentAndMember(Comment comment, Member member);
}
