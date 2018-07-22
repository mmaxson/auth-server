package com.murun.authserver.model;

import org.springframework.security.core.CredentialsContainer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serializable;
import java.util.Collection;

public class ApplicationUser extends User implements UserDetails, CredentialsContainer {

    private String firstName;
    private String lastName;
    private String email;


    public ApplicationUser(String username, String password, boolean enabled, /*boolean accountNonExpired, boolean credentialsNonExpired,
                    boolean accountNonLocked,*/ Collection<? extends GrantedAuthority> authorities, String firstName, String lastName, String email ){

        super(username, password, enabled, true, true, true, authorities);
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {

        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


}
