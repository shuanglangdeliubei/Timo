package com.linln.modules.system.service.impl;

import com.linln.common.data.PageSort;
import com.linln.common.enums.StatusEnum;
import com.linln.modules.system.domain.Dept;
import com.linln.modules.system.domain.Input;
import com.linln.modules.system.domain.Role;
import com.linln.modules.system.repository.InputRepository;
import com.linln.modules.system.service.InputService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Service
public class InputServiceImpl implements InputService {

    @Autowired
    private InputRepository inputRepository;



    /**
     * 根据用户ID获取用户信息
     * @param id 用户ID
     */
    @Override
    public Input getById(Long id) {
        return inputRepository.findById(id).orElse(null);
    }

    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    @Override
    public Page<Input> getPageList(Example<Input> example) {
        // 创建分页对象
        PageRequest page = PageSort.pageRequest(Sort.Direction.ASC);
        return inputRepository.findAll(example, page);
    }

    /**
     * 保存用户
     * @param input 用户实体类
     */
    @Override
    public Input save(Input input){
        return inputRepository.save(input);
    }

    /**
     * 保存用户列表
     * @param userList 用户实体类
     */
    @Override
    @Transactional
    public List<Input> save(List<Input> userList){
        return inputRepository.saveAll(userList);
    }


    /**
     * 状态(启用，冻结，删除)/批量状态处理
     */
    @Override
    @Transactional
    public Boolean updateStatus(StatusEnum statusEnum, List<Long> ids){
        // 联级删除与角色之间的关联
        if(statusEnum == StatusEnum.DELETE){
            return inputRepository.deleteByIdIn(ids) > 0;
        }
        return inputRepository.updateStatus(statusEnum.getCode(), ids) > 0;
    }
}