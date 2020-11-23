package net.gosecure.email.init;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.StandardPasswordEncoder;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;

@Configuration
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .passwordEncoder(new BCryptPasswordEncoder())
                .withUser("guest").password("$2y$12$qT7t9YmWc5k3jMA1s/Uk0eU1K4wxBBGe3F2I8jVBvzrmyXgE6RsAq").roles("USER").and() //123456
                .withUser("admin").password("$2y$12$kdUCx0H0VVv3bsLoMdITLeBJDV4piMJfShPZQrsgf1.Y6W.F3vyke").roles("ADMIN"); //hackfest
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        String[] permitted = new String[]{
                "/", "/css/**","/icons/**","/img/**","/js/**","/layer/**"
        };

        http.authorizeRequests()
                .antMatchers(permitted).permitAll()
                .antMatchers("/admin/**").access("hasRole('ROLE_ADMIN')")
                .and().formLogin();
    }

    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/" };

}