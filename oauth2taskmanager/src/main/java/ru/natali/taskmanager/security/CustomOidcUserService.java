package ru.natali.taskmanager.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
public class CustomOidcUserService extends OidcUserService {
    @Override
    public OidcUser loadUser(OidcUserRequest userRequest){
        OidcUser user=super.loadUser(userRequest);
        Set<GrantedAuthority> authorities = new HashSet<>(user.getAuthorities());
        if ("boeingx37@gmail.com".equalsIgnoreCase(user.getEmail())){
            authorities.add(new SimpleGrantedAuthority("ROLE_ADMIN"));
        } if ("boeingx667@gmail.com".equalsIgnoreCase(user.getEmail())){
            authorities.add(new SimpleGrantedAuthority("ROLE_USER"));
        }
        return new DefaultOidcUser(
                authorities,
                userRequest.getIdToken(),
                user.getUserInfo(),
                "email"
        );
    }
}
