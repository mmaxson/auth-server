package main;

import com.murun.authserver.main.ApplicationConfiguration;
import com.murun.authserver.main.AuthServer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.Base64;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;



@ActiveProfiles("test")
@RunWith(SpringRunner.class)

@SpringBootTest(
        webEnvironment=SpringBootTest.WebEnvironment.MOCK,
        classes = AuthServer.class)
@ContextConfiguration(classes= {ApplicationConfiguration.class})

@AutoConfigureWebMvc
@AutoConfigureMockMvc
public class AuthServerTest {



    @Autowired
    private MockMvc mockMvc;

    private String authorization = "Basic " + Base64.getEncoder().encodeToString("trusted-client:trusted-client-secret".getBytes());

  /*  @Rule
    public WireMockRule wireMockRule = new WireMockRule(9001);
*/


//    @Test
//    public String obtainAccessToken() throws Exception {
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "password");
//        params.add("trusted-client", "trusted-client-secret");
//        params.add("username", "notimportant");
//        params.add("password", "notimportant");
//
//        ResultActions result
//                = mockMvc.perform(post("/oauth/token")
//                .with( httpBasic("trusted-client","trusted-client-secret"))
//                .params(params)
//
//                .accept("application/json;charset=UTF-8"))
//                .andExpect(status().isOk())
//                .andExpect(content().contentType("application/json;charset=UTF-8"));
//
//        String resultString = result.andReturn().getResponse().getContentAsString();
//
//        JacksonJsonParser jsonParser = new JacksonJsonParser();
//        return jsonParser.parseMap(resultString).get("access_token").toString();
//    }

    public String obtainToken( String tokenType ) throws Exception{
        // @formatter:off
        ResultActions result = mockMvc.perform(post("/oauth/token")
                .header("authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON)
                .param("username", "tester")
                .param("password", "bogus")
                .param("grant_type", "password"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
                .andExpect(jsonPath("$.refresh_token", is(notNullValue())))
                .andExpect(jsonPath("$.expires_in", is(greaterThan(598))))
                .andExpect(jsonPath("$.scope", is(equalTo("read  write  trust"))));

        // @formatter:on

        String resultString = result.andReturn().getResponse().getContentAsString();
        JacksonJsonParser jsonParser = new JacksonJsonParser();
        return jsonParser.parseMap(resultString).get(tokenType).toString();
    }

    @Test
    public void passwordGrantTypeShouldReturnToken() throws Exception {

        // @formatter:off
        mockMvc.perform(post("/oauth/token")
                .header("authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON)
                .param("username", "tester")
                .param("password", "bogus")
                .param("grant_type", "password"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
                .andExpect(jsonPath("$.refresh_token", is(notNullValue())))
                .andExpect(jsonPath("$.expires_in", is(greaterThan(598))))
                .andExpect(jsonPath("$.scope", is(equalTo("read  write  trust"))));

        // @formatter:on

    }


    @Test
    public void shouldValidateToken() throws Exception {

        String token = obtainToken("access_token");

        mockMvc.perform( get("/oauth/check_token")
                .header("authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON)
                .param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.user_name", is(equalTo("tester"))))
             //   .andExpect(jsonPath("$.authorities", is(equalTo("<[{\"authority\":\"ROLE_ADMIN\"},{\"authority\":\"ROLE_USER\"}]>"))))
                .andExpect(jsonPath("$.client_id", is(equalTo("trusted-client"))));
            //    .andExpect(jsonPath("$.scope", is(equalTo("<[\"read\",\" write\",\" trust\"]>"))));

        // @formatter:on


    }


    @Test
    public void refreshTokenGrantTypeShouldReturnToken() throws Exception {

        String refresh_token = obtainToken("refresh_token");

        // @formatter:off
        mockMvc.perform(post("/oauth/token")
                .header("authorization", authorization)
                .contentType(MediaType.APPLICATION_JSON_UTF8)
                .accept(MediaType.APPLICATION_JSON)
                .param("grant_type", "refresh_token")
                .param("refresh_token", refresh_token))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
                .andExpect(jsonPath("$.refresh_token", is(notNullValue())))
                .andExpect(jsonPath("$.expires_in", is(greaterThan(598))))
                .andExpect(jsonPath("$.scope", is(equalTo("read  write  trust"))));

        // @formatter:on

    }

}
