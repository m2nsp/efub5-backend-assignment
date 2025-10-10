package efub.assignment.community.post.domain;

import efub.assignment.community.comment.domain.Comment;
import efub.assignment.community.global.domain.BaseEntity;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.board.domain.Board;
import jakarta.persistence.*;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Builder
@Entity
public class Post extends BaseEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "board_id", nullable = false)
    private Board board;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="member_id", nullable = false)
    private Member writer;

    @Column
    private boolean anonymity;

    @Column
    private String content;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> commentList = new ArrayList<>();

    //postLike와 양방향 매핑 생성 (post:postLike = 1:N)
    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostLike> postLike = new ArrayList<>();

    protected Post() {}
    private Post(Board board, Member writer, boolean anonymity, String content){
        this.board = board;
        this.writer = writer;
        this.anonymity = anonymity;
        this.content = content;
    }

    public static Post create(Board board, Member writer, boolean anonymity, String content){
        return new Post(board, writer, anonymity, content);
    }

    // Getter
    public Long getPostId() {
        return postId;
    }

    public Board getBoard() {
        return board;
    }

    public Member getWriter() {
        return writer;
    }

    public boolean isAnonymity() {
        return anonymity;
    }

    public String getContent() {
        return content;
    }

    //Setter
    public void setAnonymity(boolean anonymity) {
        this.anonymity = anonymity;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
