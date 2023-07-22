package com.linln.modules.system.service;

import com.linln.common.enums.StatusEnum;
import com.linln.modules.system.domain.Input;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface InputService {


    /**
     * 获取分页列表数据
     * @param example 查询实例
     * @return 返回分页数据
     */
    Page<Input> getPageList(Example<Input> example);
    /**
     * 保存用户
     * @param input 用户实体类
     * @return 用户信息
     */
    Input save(Input input);

    /**
     * 保存用户列表
     * @param userList 用户实体类
     * @return 用户列表
     */
    List<Input> save(List<Input> userList);



    /**
     * 根据用户ID查询用户数据
     * @param id 用户ID
     * @return 用户信息
     */
    Input getById(Long id);

    /**
     * 状态(启用，冻结，删除)/批量状态处理
     * @param statusEnum 数据状态
     * @param idList 数据ID列表
     * @return 操作结果
     */
    @Transactional
    Boolean updateStatus(StatusEnum statusEnum, List<Long> idList);
}
