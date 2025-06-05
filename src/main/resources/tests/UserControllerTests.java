package tests;

import org.springframework.beans.factory.annotation.Autowired;

import com.bkr.shopen.model.User;
import com.bkr.shopen.repository.UserRepository;
import com.bkr.shopen.services.UserService;
import org.junit.jupiter.api.Test;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.hamcrest.Matchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerTests {
    
    @Autowired
    private MockMvc mockMvc;


    @Test
    public void testGetUser_shouldReturnOk() throws Exception {
        
        mockMvc.perform(get("/api/v1/users"))
        .accept(MediaType.APPLICATION_JSON)
        .andExpect(status().isOk()
        .andExpect(jsonPath("$", hasSize(1))));
    }

    @Test
    public void testAddUser_shouldCreateUserAndReturn201() throws Exception {
                String userJson = """
            {
                "name": "Test User"
                "email": "test@example.com",
                "password": "password",
                "address": "addresssss",
                "phone": "958456",
            }
        """;

        mockMvc.perform(post("/api/v1/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content(userJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.email", is("test@example.com")))
                .andExpect(jsonPath("$.username", is("asd")));

    }
    
}
