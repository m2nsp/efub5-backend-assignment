package efub.assignment.community.member.domain;

import jakarta.persistence.*;

@Entity
@Table(name="members")
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long memberId;

    @Column(nullable = false, unique = true)
    private String studentNumber;

    private String nickname;

    @Column(nullable = false)
    private String school;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MemberStatus memberStatus;

    // 기본 생성자 (JPA가 필요로 함)
    protected Member() {}

    // private 생성자 (정적 팩토리 메서드에서만 사용 가능)
    private Member(String studentNumber, String nickname, String school, String email, String password, MemberStatus memberStatus) {
        this.studentNumber = studentNumber;
        this.nickname = nickname;
        this.school = school;
        this.email = email;
        this.password = password;
        this.memberStatus = memberStatus;
    }

    // 정적 팩토리 메서드 (가독성 향상)
    public static Member create(String studentNumber, String nickname, String school, String email, String password, MemberStatus active){
        return new Member(studentNumber, nickname, school, email, password, MemberStatus.ACTIVE);
    }

    //Getter
    public Long getMemberId() {
        return memberId;
    }

    public String getStudentNumber() {
        return studentNumber;
    }

    public String getNickname() {
        return nickname;
    }

    public String getSchool() {
        return school;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    //Setter - 필요한 필드만
    public void updateNickname(String nickname){
        this.nickname = nickname;
    }

    public void changeEmail(String newEmail){
        this.email = newEmail;
    }

    public void changePassword(String newPassword){
        this.password = newPassword;
    }

    public void deactivate(){
        this.memberStatus = MemberStatus.INACTIVE;
    }

}
