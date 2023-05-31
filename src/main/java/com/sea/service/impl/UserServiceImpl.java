package com.sea.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sea.entity.User;
import com.sea.service.UserService;
import com.sea.mapper.UserMapper;
import org.springframework.stereotype.Service;

/**
* @author 刘海洋
* @description 针对表【user(用户信息)】的数据库操作Service实现
* @createDate 2023-02-18 16:33:48
*/
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
    implements UserService{

}




