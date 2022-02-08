package com.sha.gateway.controller;

import com.sha.gateway.model.entity.User;
import com.sha.gateway.model.service.AbstractAuthenticationService;
import com.sha.gateway.model.service.AbstractUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("api/authentication")
@RestController
public class AuthenticationController
{
    @Autowired
    private AbstractAuthenticationService authenticationService;

    @Autowired
    private AbstractUserService userService;

    // OTURUM AC
    @PostMapping("sign-in")
    public ResponseEntity<?> signIn(@RequestBody User user)
    {
        String signInJWT = authenticationService.signInAndReturnJWT(user);

        return new ResponseEntity<>(signInJWT, HttpStatus.OK);
    }


    // KAYDOL
    @PostMapping("sign-up")
    public ResponseEntity<?> signUp(@RequestBody User user)
    {
        if(userService.findByUserName(user.getUsername()).isPresent())
        {
            return new ResponseEntity<>(HttpStatus.CONFLICT);
        }

        return new ResponseEntity<>(userService.save(user), HttpStatus.CREATED);
    }
}
