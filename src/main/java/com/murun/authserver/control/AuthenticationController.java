package com.murun.authserver.control;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;


@RestController
@RequestMapping(value= "", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthenticationController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());


    @RequestMapping(method = RequestMethod.GET, value ="/user")
    public Principal user(Principal user) {
        return user;
    }


  }