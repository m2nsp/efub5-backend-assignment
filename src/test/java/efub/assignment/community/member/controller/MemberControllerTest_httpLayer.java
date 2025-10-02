package efub.assignment.community.member.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import efub.assignment.community.member.domain.Member;
import efub.assignment.community.member.dto.MemberRequestDTO;
import efub.assignment.community.member.dto.MemberResponseDTO;
import efub.assignment.community.member.service.MemberService;
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
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.studentNumber").value("2222222"))
                .andExpect(jsonPath("$.nickname").value("동길"))
                .andExpect(jsonPath("$.email").value("ewhain.net"));

        then(memberService).should().registerMember(any(MemberRequestDTO.class));

    }



}