package com.example.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.entity.dto.auth.Account;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface AccountMapper extends BaseMapper<Account> {

    @Select("select * from users where username = #{text} or email = #{text}")
    Account findAccountByNameOrEmail(String text);

    @Insert("insert into users(email, username, password, role) VALUES(#{email}, #{username}, #{password}, #{role})")
    int createAccount(String username, String password, String email, String role);

    @Update("update users set password = #{password} where email = #{email}")
    int resetPasswordByEmail(String password, String email);
}
