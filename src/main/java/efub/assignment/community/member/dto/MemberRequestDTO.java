package efub.assignment.community.member.dto;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
// 회원 가입 시 필요한 데이터 받는 DTO
public class MemberRequestDTO {
    private String email;
    private String studentNumber;
    private String nickname;
    private String school;
    private String password;

    //기본 생성자 (Spring이 JSON을 객체로 변환할 때 필요)
    public MemberRequestDTO() {}

    public MemberRequestDTO(String email, String studentNumber, String nickname, String school, String password){
        this.email = email;
        this.studentNumber = studentNumber;
        this.nickname = nickname;
        this.school = school;
        this.password = password;
    }

    //Getter
//    public String getEmail(){
//        return email;
//    }
//
//    public String getStudentNumber(){
//        return studentNumber;
//    }
//
//    public String getNickname(){
//        return nickname;
//    }
//
//    public String getSchool(){
//        return school;
//    }
//
//    public String getPassword(){
//        return password;
//    }
}
