package api;

import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import de.lobo.spring.mvc.Application;
import de.lobo.spring.mvc.api.DemoController;
import de.lobo.spring.mvc.service.DemoService;
import io.restassured.module.mockmvc.RestAssuredMockMvc;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;


@RunWith(SpringRunner.class)
@WebMvcTest(DemoController.class)
@ContextConfiguration(classes = Application.class)
public class DemoControllerTest {

  @MockBean
  private DemoService demoService;

  @Autowired
  private MockMvc mvc;

  @Before
  public void setup() {
    RestAssuredMockMvc.mockMvc(mvc);
  }

  @Test
  public void testApiListUsers() throws Exception {
    mvc.perform(
        MockMvcRequestBuilders
            .get("/users")
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(MockMvcResultMatchers.content().json("[]"));
  }

  @Test
  public void testApiDeleteUsers() throws Exception {
    mvc.perform(
        MockMvcRequestBuilders
            .delete("/users/1")
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().isOk());
  }

  @Test
  public void testApiDeleteUsers_shouldFail_400() throws Exception {
    doThrow(new EmptyResultDataAccessException(0)).when(demoService).deleteUser(anyLong());

    mvc.perform(
        MockMvcRequestBuilders
            .delete("/users/1")
            .accept(MediaType.APPLICATION_JSON_VALUE))
        .andDo(print())
        .andExpect(status().is4xxClientError());
  }
}
