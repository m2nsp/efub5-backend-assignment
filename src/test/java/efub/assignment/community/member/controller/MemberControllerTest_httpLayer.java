package efub.assignment.community.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import efub.assignment.community.member.domain.DuplicateMemberEmailException;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.dto.MemberRequestDTO;
import efub.assignment.community.member.dto.MemberResponseDTO;
import efub.assignment.community.member.service.MemberService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

// HTTP 계층 테스트
@WebMvcTest(MemberController.class)
class MemberControllerTest_httpLayer {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean   //Controller에서 의존하는 Service Mocking && MockBean은 deprecated되었고 MockitoBean을 그 대신 사용해야함
    private MemberService memberService;

    @Autowired
    private ObjectMapper objectMapper;  //JSON 직렬화/역직렬화용

    @Test
    public void 회원가입_성공() throws Exception {
        //given
        MemberRequestDTO request = new MemberRequestDTO("test@ewhain.net", "홍길동", "동길", "이대", "0000000");
        MemberResponseDTO response = new MemberResponseDTO(1L, "2222222", "동길", "이대", "test@ewhain.net");

        given(memberService.registerMember(any(MemberRequestDTO.class)));

        //when & then
        mockMvc.perform(post("/api/members/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())                                                    // HTTP 상태 코드 검증
// 이 부분 작성할 때, value 하드 코딩 보다는 dto에서 getter로 값을 가져오는 것이 더 적절하다고 한다
//                .andExpect(jsonPath("$.id").value(1L))                         // 응답 JSON 데이터 검증
//                .andExpect(jsonPath("$.studentNumber").value("2222222"))
//                .andExpect(jsonPath("$.nickname").value("동길"))
//                .andExpect(jsonPath("$.email").value("ewhain.net"));
                .andExpect(jsonPath("$.id").value(response.getMemberId()))
                .andExpect(jsonPath("$.studentNumber").value(response.getStudentNumber()))
                .andExpect(jsonPath("$.nickname").value(response.getNickname()))
                .andExpect(jsonPath("$.email").value(response.getEmail()));

        then(memberService).should().registerMember(any(MemberRequestDTO.class));                   // 서비스 메서드 호출 여부 검증
    }

    @Test
    @DisplayName("이미 가입 된 이메일로 회원가입 시도 시 400 Bad Request 응답")
    public void 회원가입_실패_중복_이메일() throws Exception {
        //given
        // 1. 요청할 데이터 준비
        MemberRequestDTO request = new MemberRequestDTO("test@ewhain.net", "홍길동", "동길", "이대", "0000000");

        // 2. mocking - service 메서드 호출 시 예외(DuplicateMemberException)가 발생하도록 설정
        given(memberService.registerMember(any(MemberRequestDTO.class)))
                .willThrow(new DuplicateMemberEmailException("이미 존재하는 회원 이메일 입니다."));

        //when&then
        mockMvc.perform(post("/api/members/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                //검증 : 상태코드 400
                .andExpect(status().isBadRequest());

        // 서비스에서 메서드가 호출되었는 지 확인
        then(memberService).should().registerMember(any(MemberRequestDTO.class));
    }



}