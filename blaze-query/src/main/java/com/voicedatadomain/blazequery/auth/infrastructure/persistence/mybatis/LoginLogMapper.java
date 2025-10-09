package com.voicedatadomain.blazequery.auth.infrastructure.persistence.mybatis;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.voicedatadomain.blazequery.auth.infrastructure.persistence.po.LoginLogPo;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
@Mapper
public interface LoginLogMapper extends BaseMapper<LoginLogPo> {
    // Interface methods
}
