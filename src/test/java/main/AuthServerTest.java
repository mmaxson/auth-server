package main;

import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.matching.StringValuePattern;
import com.murun.authserver.main.ApplicationConfiguration;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureWebMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.util.Base64Utils;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import com.github.tomakehurst.wiremock.junit.WireMockRule;

import java.time.Instant;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ActiveProfiles("test")
@RunWith(SpringRunner.class)
@SpringBootTest
@ContextConfiguration(classes= {ApplicationConfiguration.class})

@AutoConfigureWebMvc
@AutoConfigureMockMvc
public class AuthServerTest {

    @Autowired
    private MockMvc mockMvc;

    @Rule
    public WireMockRule wireMockRule = new WireMockRule(9001);


    @Test
    public void passwordGrantTypeShouldReturnToken() throws Exception {

        String authorization = "Basic "
                + new String(Base64Utils.encode("trusted-client:trusted-client-secret".getBytes()));
        String contentType = MediaType.APPLICATION_JSON + ";charset=UTF-8";

        // @formatter:off
        mockMvc.perform(
                        post("/oauth/token")
                                .header("Authorization", authorization)
                                .header("origin","http://localhost:4200")
                                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                                .param("username", "tester")
                                .param("password", "password")
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


    //@Test
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
