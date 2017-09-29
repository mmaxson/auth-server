package com.murun.authserver.config;

import com.murun.authserver.model.ApplicationUser;
import org.springframework.security.oauth2.common.DefaultOAuth2AccessToken;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.security.core.userdetails.User;
import java.util.Map;
import java.util.HashMap;

public class ApplicationTokenEnhancer implements TokenEnhancer {

    @Override
    public OAuth2AccessToken enhance(OAuth2AccessToken accessToken, OAuth2Authentication authentication) {
        ApplicationUser user = (ApplicationUser) authentication.getPrincipal();
        final Map<String, Object> additionalInfo = new HashMap<>();

        additionalInfo.put("firstName", user.getFirstName());
        additionalInfo.put("lastName", user.getLastName());

        additionalInfo.put("email", user.getEmail());
        additionalInfo.put("authorities", user.getAuthorities());


        ((DefaultOAuth2AccessToken) accessToken).setAdditionalInformation(additionalInfo);

        return accessToken;
    }

}