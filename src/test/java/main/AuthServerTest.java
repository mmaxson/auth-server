package main;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.murun.authserver.main.ApplicationConfiguration;
import com.murun.authserver.main.AuthServer;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.json.JacksonJsonParser;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.web.FilterChainProxy;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;

import org.springframework.test.web.servlet.MockMvc;


import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.Base64Utils;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import com.github.tomakehurst.wiremock.junit.WireMockRule;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;





@ActiveProfiles("test")
@RunWith(SpringRunner.class)

@ContextConfiguration(classes= {ApplicationConfiguration.class})


//@AutoConfigureMockMvc
@WebAppConfiguration
@SpringBootTest(classes = AuthServer.class)

public class AuthServerTest {

    @Autowired
    private WebApplicationContext wac;

   // @Autowired
   // private FilterChainProxy springSecurityFilterChain;

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9001);

    @Before
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
             //   .addFilter(springSecurityFilterChain).build();
    }

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

    //@Test
    public void passwordGrantTypeShouldReturnToken() throws Exception {

        String authorization = "Basic "
                + new String(Base64Utils.encode("trusted-client:trusted-client-secret".getBytes()));

        String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";

        // @formatter:off
        mockMvc.perform(
                        post("/oauth/token")
                                .header("Authorization", authorization)
                                .header("origin","http://localhost:4200")
                                .contentType(MediaType.APPLICATION_JSON)
                                .accept(MediaType.APPLICATION_JSON)
                                .param("username", "notimportant")
                                .param("password", "notimportant")
                                .param("grant_type", "password"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.access_token", is(notNullValue())))
                .andExpect(jsonPath("$.token_type", is(equalTo("bearer"))))
               // .andExpect(jsonPath("$.refresh_token", is(notNullValue())))
                .andExpect(jsonPath("$.expires_in", is(greaterThan(30))))
                .andExpect(jsonPath("$.scope", is(equalTo("read  write  trust"))));

        // @formatter:on


    }


    @Test
    public void shouldReturnTokenForPasswordGrantType() throws Exception {

        String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";

        long unixTimestamp = Instant.now().plusSeconds(10).getEpochSecond();
        stubFor(com.github.tomakehurst.wiremock.client.WireMock.post(urlPathEqualTo("/oauth/token"))
                .withBasicAuth("trusted-client", "trusted-client-secret")
                .withQueryParam("username", WireMock.equalTo("tester"))
                .withQueryParam("password", WireMock.equalTo("password"))
                .withQueryParam("grant_type", WireMock.equalTo("password"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{\"access_token\":\"token\",\"token_type\":\"bearer\",\"expires_in\":59,\"scope\":\"read  write  trust\"}")));

        // @formatter:off

        mockMvc.perform( get("/oauth/check_token")
                                .param("token", "token"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(contentType))
                .andExpect(jsonPath("$.user_name", is(equalTo("tester"))))
                .andExpect(jsonPath("$.authorities", is(equalTo("ROLE_ADMIN\",\"ROLE_USER"))))
                .andExpect(jsonPath("$.client_id", is(equalTo("trusted-client"))))
                .andExpect(jsonPath("$.scope", is(equalTo("read  write  trust"))));

        // @formatter:on


    }
}
