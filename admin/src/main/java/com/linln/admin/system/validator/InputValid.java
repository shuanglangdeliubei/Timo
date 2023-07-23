package com.linln.admin.system.validator;


import javax.validation.constraints.NotEmpty;
import java.io.Serializable;

public class InputValid implements Serializable {
    @NotEmpty(message = "商品名称不能为空")
    private String prodname;
    @NotEmpty(message = "商品品牌不能为空")
    private String prodbrand;
    @NotEmpty(message = "商品库存不能为空")
    private String prodinventory;
}
