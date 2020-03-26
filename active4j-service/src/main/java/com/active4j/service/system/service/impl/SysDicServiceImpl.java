package com.active4j.service.system.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.active4j.dao.system.dao.SysDicDao;
import com.active4j.entity.system.entity.SysDicEntity;
import com.active4j.service.system.service.SysDicService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;


/**
 * 数据字典管理service类
 * @author teli_
 *
 */
@Service("sysDicService")
@Transactional
public class SysDicServiceImpl extends ServiceImpl<SysDicDao, SysDicEntity> implements SysDicService {

	

}
