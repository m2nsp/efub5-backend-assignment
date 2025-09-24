package efub.assignment.community.board.dto.Request;

import efub.assignment.community.member.domain.Member;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
// 게시판 생성 시 필요한 정보 받는 DTO
public class BoardRequestDTO {
    private String boardName;
    private String announcement;
    private String description;
    private Long masterId;

    public BoardRequestDTO() {};

    public BoardRequestDTO(String boardName, String announcement, String description, Long masterId){
        this.boardName = boardName;
        this.announcement = announcement;
        this.description = description;
        this.masterId = masterId;
    }

    //Getter
//    public String getBoardName() {
//        return boardName;
//    }
//
//    public String getAnnouncement() {
//        return announcement;
//    }
//
//    public String getDescription() {
//        return description;
//    }
//
//    public Long getMasterId() {
//        return masterId;
//    }
}
