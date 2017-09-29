package com.murun.authserver.service;


import com.murun.authserver.model.ApplicationUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.jdbc.JdbcDaoImpl;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Service("userDetailsService")
public class CustomUserDetailsService extends JdbcDaoImpl{

    @Autowired
    private DataSource dataSource;

    @PostConstruct
    private void initialize() {
        setDataSource(dataSource);
    }

    @Override
    @Value("select username, password, enabled, email, firstname, lastname from users where username=?")
    public void setUsersByUsernameQuery(String usersByUsernameQueryString) {
        super.setUsersByUsernameQuery(usersByUsernameQueryString);
    }

    @Override
    @Value("select b.username, a.role from user_roles a, users b where b.username=? and a.user_id = b.user_id")
    public void setAuthoritiesByUsernameQuery(String queryString) {
        super.setAuthoritiesByUsernameQuery(queryString);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException{
        return

                (ApplicationUser) getJdbcTemplate().queryForObject(getUsersByUsernameQuery(), new Object[]{username},
                new RowMapper<ApplicationUser>() {

                    public ApplicationUser mapRow(ResultSet rs, int rowNum) throws SQLException {
                        String username = rs.getString("username");
                        String password = rs.getString("password");
                        boolean enabled = rs.getBoolean("enabled");
                    //    boolean accountNonExpired = rs.getBoolean("accountNonExpired");
                    //    boolean credentialsNonExpired = rs.getBoolean("credentialsNonExpired");
                    //    boolean accountNonLocked = rs.getBoolean("accountNonLocked");

                        List<GrantedAuthority> authorities = loadUserAuthorities(username);

                        String firstName = rs.getString("firstName");
                        String lastName = rs.getString("lastName");
                        String email = rs.getString("email");
                        return new ApplicationUser(username, password, enabled, /*accountNonExpired, credentialsNonExpired,
                                accountNonLocked,  */ authorities,  firstName, lastName, email );
                    }

                });
    }

}