package com.sha.gateway.security.utility;

import com.sha.gateway.security.jwt.JWTAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration // icerde bean tanimlanacak
@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter
{
    // ***2
    @Autowired
    private UserDetailsService userDetailsService;

    @Bean
    public JWTAuthorizationFilter createJWTAuthorizationFilter()
    {
        return new JWTAuthorizationFilter();
    }

    /*
        kaynaklar arasi paylasim icim
        CORS (cross origin resource sharing) politikasi

        CORS konfigurasyonu ile
            izin verilen kaynaklar,
            izin verilen metotlar,
            izin verilen yollari belirleyecegiz.

        ornegin: Burasi sayesinde, uygulamay yalnizca GET istegi izni verilebilir.
                Belirli bir alan ad varsa, hostlar yazlnizca ona gore kisitlanabilir.

        CORS tarafindan bir istege izin verilmiyorsa, istek engellenir.

        Bu bir Java bean (bkz. @Bean annotion) oldugu icin,
        bundan yeni nesneler olusturulup tum aplikasyon bazinda kullanilabilir.
     */
    @Bean
    public WebMvcConfigurer corsConfigurer()
    {
        return new WebMvcConfigurer()
        {
            @Override
            public void addCorsMappings(CorsRegistry registry)
            {
                registry.addMapping("/**")
                        .allowedOrigins("*") // or: localhost
                        .allowedMethods("*"); // or: POST, GET etc.
            }
        };

    }

    /*
     ***5
     */
    @Override
    @Bean(BeanIds.AUTHENTICATION_MANAGER)
    public AuthenticationManager authenticationManagerBean() throws Exception
    {
        return super.authenticationManagerBean();
    }

    /*
        ***4 -> yetkilendirme(authorization) ile ilgili
     */
    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception
    {
        httpSecurity.csrf().disable();

        /*
          SessionCreationPolicy.ALWAYS -> session yoksa mutlaka olusturur.

          SessionCreationPolicy.NEVER -> Framework, yeni bir session hic bir zaman olusturmaz.
          Ne var ki, eger halihazirda varsa, onu kullanir.

          SessionCreationPolicy.IFREQUIRED -> Gerekliyse otuturm olusturur. (varsayilan hal bu)

          SessionCreationPolicy.STATELESS -> Framework, yeni bir session hic bir zaman olusturmaz ve kullanmaz.
         */
        httpSecurity.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);


        /*
           istemciler, web sunucusu tarafindan desteklenen istekleri ogrenmek icin
           OPTION istegi yollar.
           Burada, web sunucusu tarafindan desteklenen isteklere izin veriyoruz.
         */
        httpSecurity.authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS).permitAll()
                .antMatchers("/api/authentication/**").permitAll()
                .anyRequest().authenticated();

        httpSecurity.addFilterBefore(createJWTAuthorizationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    // ***3 -> kimlik dogrulama(authentication) ile ilgili
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception
    {
        auth.userDetailsService(userDetailsService).passwordEncoder(createPasswordEncoder());
    }

    // ***1
    @Bean
    public PasswordEncoder createPasswordEncoder()
    {
        return new BCryptPasswordEncoder();
    }
}
