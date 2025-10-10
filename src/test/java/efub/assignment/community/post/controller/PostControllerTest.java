package efub.assignment.community.post.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import efub.assignment.community.board.dto.Request.BoardRequestDTO;
import efub.assignment.community.post.domain.ContentNullException;
import efub.assignment.community.post.dto.request.PostRequestDTO;
import efub.assignment.community.post.service.PostService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = PostController.class)
class PostControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PostService postService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("게시물_생성_성공")
    public void 게시글_생성_성공() {
        //given

        // (+) post 생성하려면 post가 해당하는 board도 필요함
        BoardRequestDTO board = new BoardRequestDTO().builder()
                .boardName("신입게시판")
                .announcement("공지사항입니다")
                .description("게시판입니다")
                .masterId(1L)
                .build();

        PostRequestDTO request = new PostRequestDTO().builder()
                .memberId(1L)
                .anonymity(false)
                .content("안녕하세요")
                .build();

        given(postService.createPost(1L, any(PostRequestDTO.class)));

        //when & then
        mockMvc.perform(post("/api/boards/1/posts")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());

        then(postService).should().createPost(1L, request);
    }

    @Test
    @DisplayName("내용이_없을경우_게시물_생성_실패")
    public void 내용이_없을경우_게시물_생성_실패() {
        //given - given 뒤에는 어떤 결과를 반환할지 or 예외를 발생시킬지 명시하는 계열의 메서드 필요
        PostRequestDTO request = new PostRequestDTO().builder()
                .anonymity(false)
                .memberId(1L)
                .content("")
                .build();
        given(postService.createPost(anyLong(), request))      //아무 Long값
                .willThrow(new ContentNullException());
    }

}