package com.linln.modules.system.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.linln.common.enums.StatusEnum;
import com.linln.common.utils.StatusUtil;
import com.linln.component.excel.annotation.Excel;
import com.linln.component.excel.enums.ExcelType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Data
@Entity
@Table(name = "sys_input")
@ToString(exclude = {"dept", "roles"})
@EqualsAndHashCode(exclude = {"dept", "roles"})
@EntityListeners(AuditingEntityListener.class)
@SQLDelete(sql = "update sys_input" + StatusUtil.SLICE_DELETE)
@Where(clause = StatusUtil.NOT_DELETE)
@Excel("用户数据")
public class Input implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Excel(value = "用户ID", type = ExcelType.EXPORT)
    private Long id;
    @Excel("商品图片")
    private String prodpicture;
    @Excel("商品名称")
    private String prodname;
    @Excel("商品品牌")
    private String prodbrand;
    @Excel("现有库存")
    private String prodinventory;
    @CreatedDate
    @Excel("创建时间")
    private Date createDate;
    @LastModifiedDate
    @Excel("更新时间")
    private Date updateDate;

    @JsonIgnore
    private Set<Role> roles = new HashSet<>(0);
}
