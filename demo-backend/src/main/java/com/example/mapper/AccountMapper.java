package com.example.mapper;

import com.example.common.config.MyBaseMapper;
import com.example.entity.dto.auth.Account;
import com.example.entity.vo.response.AccountVO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.List;

@Mapper
public interface AccountMapper extends MyBaseMapper<Account> {

    @Select("select * from user where username = #{text} or address = #{text}")
    Account findAccountByNameOrEmail(String text);

    @Select("select property.user_id, real_name from user right join property on user.user_id = property.user_id where rid = 3")
    List<AccountVO> findAccountsProperty();

    @Update("update user set password = #{password} where address = #{email}")
    int resetPasswordByEmail(String password, String email);


}
