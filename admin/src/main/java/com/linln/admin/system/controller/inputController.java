package com.linln.admin.system.controller;

import com.linln.admin.system.validator.DeptValid;
import com.linln.admin.system.validator.InputValid;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.SaveAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.modules.system.domain.Input;
import com.linln.modules.system.service.InputService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/input")
public class inputController {

    @Autowired
    private InputService inputService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("system:input:index")
    public String index(Model model, Input input) {
        // 创建匹配器，进行动态查询匹配
        ExampleMatcher matcher = ExampleMatcher.matching().
                withMatcher("title", match -> match.contains());

        // 获取字典列表
        Example<Input> example = Example.of(input, matcher);
        // 获取用户列表
        Page<Input> list = inputService.getPageList(example);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        return "/system/input/index";
    }

    /**
     * 跳转到添加页面
     */
    @GetMapping("/add")
    @RequiresPermissions("system:input:add")
    public String toAdd() {
        return "/system/input/add";
    }

//    /**
//     * 跳转到编辑页面
//     */
//    @GetMapping("/edit/{id}")
//    @RequiresPermissions("system:input:edit")
//    public String toEdit(@PathVariable("id") Input input, Model model) {
//        model.addAttribute("input", input);
//        return "/system/input/add";
//    }

    /**
     * 保存添加/修改的数据
     * @param input 实体对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"system:input:add"})
    @ResponseBody
    @ActionLog(name = "入库管理", message = "字典：${title}", action = SaveAction.class)
    public ResultVo save(@EntityParam Input input) {
        // 保存数据
        inputService.save(input);
        return ResultVoUtil.SAVE_SUCCESS;
    }


}
