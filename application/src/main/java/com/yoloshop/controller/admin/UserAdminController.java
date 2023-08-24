package com.yoloshop.controller.admin;

import com.yoloshop.annotation.Authenticate;
import com.yoloshop.common.Constant;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(Constant.PrefixPath.ADMIN + "/user")
public class UserAdminController {
    @GetMapping
    @Authenticate
    public ResponseEntity login() {
        return ResponseEntity.ok().body("Hello world");
    }
}
