package com.ssc.server.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ssc.server.mapper.AdminMapper;
import com.ssc.server.pojo.Admin;
import com.ssc.server.service.IAdminService;
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
public class AdminServiceImpl extends ServiceImpl<AdminMapper, Admin> implements IAdminService {

}
