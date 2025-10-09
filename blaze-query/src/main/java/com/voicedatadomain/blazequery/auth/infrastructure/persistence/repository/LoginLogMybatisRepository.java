package com.voicedatadomain.blazequery.auth.infrastructure.persistence.repository;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.voicedatadomain.blazequery.auth.domain.repository.LoginLogRepository;
import com.voicedatadomain.blazequery.auth.infrastructure.persistence.mybatis.LoginLogMapper;
import com.voicedatadomain.blazequery.auth.infrastructure.persistence.po.LoginLogPo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

/**
 * @author 王玉涛
 * @version 1.0
 * @since 2025/10/9
 */
@Repository
@RequiredArgsConstructor
public class LoginLogMybatisRepository
        extends ServiceImpl<LoginLogMapper, LoginLogPo> implements LoginLogRepository {
}
