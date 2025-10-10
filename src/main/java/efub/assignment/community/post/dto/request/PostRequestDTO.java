package efub.assignment.community.post.dto.request;

import lombok.Builder;

@Builder
public class PostRequestDTO {
    private Long memberId;
    private boolean anonymity;
    private String content;

    public PostRequestDTO() {}
    public PostRequestDTO(Long memberId, boolean anonymity, String content){
        this.memberId = memberId;
        this.anonymity = anonymity;
        this.content = content;
    }

    // Getter
    public Long getMemberId() {
        return memberId;
    }

    public boolean isAnonymity() {
        return anonymity;
    }

    public String getContent() {
        return content;
    }

    public void setAnonymity(boolean anonymity) {
        this.anonymity = anonymity;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
