package efub.assignment.community.member.service;

import efub.assignment.community.member.domain.MemberStatus;
import efub.assignment.community.member.repository.MemberRepository;
import efub.assignment.community.member.dto.MemberRequestDTO;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.dto.MemberResponseDTO;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MemberService {
    private final MemberRepository memberRepository;    //필드 선언

    //생성자 주입
    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    //회원 가입
    @Transactional
    public MemberResponseDTO registerMember(MemberRequestDTO requestDTO){
        //중복 검사
        validateDuplicateMember(requestDTO.getStudentNumber(), requestDTO.getEmail());

        //회원 생성 & DB 저장
        Member newMember = Member.create(
                requestDTO.getStudentNumber(),
                requestDTO.getNickname(),
                requestDTO.getSchool(),
                requestDTO.getEmail(),
                requestDTO.getPassword(),
                MemberStatus.ACTIVE);
        Member savedMember = memberRepository.save(newMember);

        return MemberResponseDTO.from(savedMember);
    }

    //회원 조회
    @Transactional(readOnly = true)
    public MemberResponseDTO getMemberByMemberId(Long memberId){
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
        return MemberResponseDTO.from(member);
    }

    // 닉네임 변경
    @Transactional
    public void updateNickname(Long memberId, String newNickname){
        Member member = findMemberByMemberId(memberId);
        member.updateNickname(newNickname);
    }

    // 이메일 변경
    @Transactional
    public MemberResponseDTO changeEmail(Long memberId, String newEmail){
        Member member = findMemberByMemberId(memberId);

        //중복 검사
        if(memberRepository.existsByEmail(newEmail)){
            throw new IllegalArgumentException("이미 사용 중인 이메일 입니다");
        }
        member.changeEmail(newEmail);

        //변경된 회원 정보 DTO로 변환 후 반환
        return MemberResponseDTO.from(member);
    }

    // 비밀번호 변경
    @Transactional
    public void changePassword(Long memberId, String newPassword){
        Member member = findMemberByMemberId(memberId);
        member.changePassword(newPassword);
    }

    // 회원 탈퇴 (논리적)
    @Transactional
    public void logicalDeleteAccount(Long memberId){
        Member member = findMemberByMemberId(memberId);
        member.deactivate();    //상태 변경
    }

    // 중복 검사 로직
    private void validateDuplicateMember(String studentNumber, String email){
        if(memberRepository.existsByStudentNumber(studentNumber)){
            throw new IllegalArgumentException("이미 존재하는 학번 입니다.");
        }
        if(memberRepository.existsByEmail(email)){
            throw new IllegalArgumentException("이미 존재하는 이메일입니다.");
        }
    }

    // Member 엔티티를 찾는 함수(서비스 내부에서 사용)
    public Member findMemberByMemberId(Long memberId){
        return memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당 회원이 존재하지 않습니다."));
    }

}
