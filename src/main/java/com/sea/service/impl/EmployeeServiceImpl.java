package com.sea.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sea.entity.Employee;
import com.sea.service.EmployeeService;
import com.sea.mapper.EmployeeMapper;
import org.springframework.stereotype.Service;

/**
* @author 刘海洋
* @description 针对表【employee(员工信息)】的数据库操作Service实现
* @createDate 2023-02-12 11:06:33
*/
@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee>
    implements EmployeeService{

}




