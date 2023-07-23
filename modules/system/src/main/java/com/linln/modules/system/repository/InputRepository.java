package com.linln.modules.system.repository;

import com.linln.modules.system.domain.Input;
import com.linln.modules.system.domain.User;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
public interface InputRepository extends BaseRepository<Input, Long>, JpaSpecificationExecutor<Input> {



//    /**
//     * 根据用户名查询用户数据,且排查指定ID的用户
//     * @param prodname 用户名
//     * @param id 排除的用户ID
//     * @return 用户数据
//     */
//    public Input findByUsernameAndIdNot(String prodname, Long id);

    Input findFirstByProdnameOrderByCreateDateDesc(String prodname);
    /**
     * 删除多条数据
     * @param ids ID列表
     * @return 影响行数
     */
    public Integer deleteByIdIn(List<Long> ids);
}
