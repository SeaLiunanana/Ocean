package com.sea.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.sea.entity.AddressBook;
import com.sea.service.AddressBookService;
import com.sea.mapper.AddressBookMapper;
import org.springframework.stereotype.Service;

/**
* @author 刘海洋
* @description 针对表【address_book(地址管理)】的数据库操作Service实现
* @createDate 2023-02-19 08:33:26
*/
@Service
public class AddressBookServiceImpl extends ServiceImpl<AddressBookMapper, AddressBook>
    implements AddressBookService{

}




