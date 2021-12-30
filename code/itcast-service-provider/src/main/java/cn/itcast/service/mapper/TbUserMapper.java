package cn.itcast.service.mapper;

import cn.itcast.service.pojo.TbUser;
import org.apache.ibatis.annotations.Mapper;

public interface TbUserMapper {
    int deleteByPrimaryKey(Long id);

    int insert(TbUser record);

    int insertSelective(TbUser record);

    TbUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(TbUser record);

    int updateByPrimaryKey(TbUser record);

    TbUser selectUserById(Long id);
}