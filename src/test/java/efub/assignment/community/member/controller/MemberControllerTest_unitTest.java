package efub.assignment.community.member.controller;

import efub.assignment.community.member.domain.DuplicateMemberEmailException;
import efub.assignment.community.member.dto.MemberRequestDTO;
import efub.assignment.community.member.dto.MemberResponseDTO;
import efub.assignment.community.member.service.MemberService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class MemberControllerTest_unitTest {
    @Mock
    private MemberService memberService;

    @InjectMocks
    private MemberController memberController;

    @Test
    public void 회원가입_단위테스트() {
        //given
        MemberRequestDTO request = new MemberRequestDTO("test@ewhain.net", "2222222", "동길", "이대", "0000000");
        MemberResponseDTO response = new MemberResponseDTO(1L, "2222222", "동길", "이대", "test@ewhain.net");

        when(memberService.registerMember(any(MemberRequestDTO.class))).thenReturn(response);

        //when
        ResponseEntity<MemberResponseDTO> result = memberController.registerMember(request);

        //then
        assertThat(result.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertNotNull(result.getBody());     //NPE 방지용
        assertThat(result.getBody().getNickname()).isEqualTo("동길");
        verify(memberService).registerMember(any(MemberRequestDTO.class));
    }

    @Test
    public void 이미가입된_메일일경우_회원가입_실패() {
        //given
        MemberRequestDTO request = new MemberRequestDTO("test@ewhain.net", "2222222", "동길", "이대", "0000000");

        when(memberService.registerMember(any(MemberRequestDTO.class))).thenThrow(IllegalArgumentException.class);

        Assertions.assertNotNull(request);
        assertThrows(DuplicateMemberEmailException.class, () -> memberController.registerMember(request));

        verify(memberService).registerMember(any(MemberRequestDTO.class));
    }
}
