package unmsm.quipu.erp.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.Collection;

@Service
@Scope("session")
public class UserService {

    @Autowired
    private AuthenticationManager authenticationManager;

    @PreAuthorize("hasRole('admin')")
    public Collection<? extends GrantedAuthority> getAuthorities(UserDetails userDetails) {
        return userDetails.getAuthorities();
    }

    public Authentication login(String user) {
        SecurityContextHolder.clearContext();
        Authentication result = null;

        Authentication request = new UsernamePasswordAuthenticationToken(
                user, "koala");
        result = authenticationManager.authenticate(request);
        SecurityContextHolder.getContext().setAuthentication(result);

        return result;
    }
}
