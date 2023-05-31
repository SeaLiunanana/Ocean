package com.sea.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.sea.Utils.SMSUtils;
import com.sea.Utils.ValidateCodeUtils;
import com.sea.common.R;
import com.sea.entity.User;
import com.sea.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Map;

@RestController
@RequestMapping("/user")
@Slf4j
public class UserController {
    @Autowired
    private UserService userService;

    @PostMapping("/sendMsg")
    public R<String> sendMsg(@RequestBody User user, HttpSession httpSession) {
        String phone = user.getPhone();
        if (phone != null) {
            String code = ValidateCodeUtils.generateValidateCode(4).toString();
            SMSUtils.sendMessage("阿里云短信测试", "SMS_154950909", phone, code);
            httpSession.setAttribute(phone, code);
            return R.success("验证码已发送");
        }

        return R.error("短信发送失败");
    }

    @PostMapping("/login")
    public R<User> login(@RequestBody Map map, HttpSession httpSession) {
        String phone = map.get("phone").toString();
        String code = map.get("code").toString();
        Object codeInSession = httpSession.getAttribute(phone);
        if (codeInSession != null && codeInSession.equals(code)) {
            LambdaQueryWrapper<User> lambdaQueryWrapper = new LambdaQueryWrapper();
            lambdaQueryWrapper.eq(User::getPhone, phone);
            User user = userService.getOne(lambdaQueryWrapper);
            if (user == null) {
                user = new User();
                user.setPhone(phone);
                user.setName("新用户" + phone);
                userService.save(user);
            }
            httpSession.setAttribute("user",user.getId());
            return R.success(user);
        }

        return R.error("登录失败");


    }
}
