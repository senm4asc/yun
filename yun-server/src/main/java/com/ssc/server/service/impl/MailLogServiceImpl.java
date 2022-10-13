package com.ssc.server.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ssc.server.mapper.MailLogMapper;
import com.ssc.server.pojo.MailLog;
import com.ssc.server.service.IMailLogService;
import org.springframework.stereotype.Service;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author ssc
 * @since 2022-10-12
 */
@Service
public class MailLogServiceImpl extends ServiceImpl<MailLogMapper, MailLog> implements IMailLogService {

}
