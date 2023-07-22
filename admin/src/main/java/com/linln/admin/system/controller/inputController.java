package com.linln.admin.system.controller;

import com.linln.admin.system.validator.UserValid;
import com.linln.common.constant.AdminConst;
import com.linln.common.enums.ResultEnum;
import com.linln.common.enums.StatusEnum;
import com.linln.common.exception.ResultException;
import com.linln.common.utils.EntityBeanUtil;
import com.linln.common.utils.ResultVoUtil;
import com.linln.common.utils.SpringContextUtil;
import com.linln.common.utils.StatusUtil;
import com.linln.common.vo.ResultVo;
import com.linln.component.actionLog.action.StatusAction;
import com.linln.component.actionLog.action.UserAction;
import com.linln.component.actionLog.annotation.ActionLog;
import com.linln.component.actionLog.annotation.EntityParam;
import com.linln.component.excel.ExcelUtil;
import com.linln.component.fileUpload.config.properties.UploadProjectProperties;
import com.linln.component.shiro.ShiroUtil;
import com.linln.modules.system.domain.Role;
import com.linln.modules.system.domain.User;
import com.linln.modules.system.repository.UserRepository;
import com.linln.modules.system.service.RoleService;
import com.linln.modules.system.service.UserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * @author 小懒虫
 * @date 2018/8/14
 */
@Controller
@RequestMapping("/system/input")
public class inputController {

    @Autowired
    private UserService userService;

    @Autowired
    private RoleService roleService;

    /**
     * 列表页面
     */
    @GetMapping("/index")
    @RequiresPermissions("system:input:index")
    public String index(Model model, User user) {

        // 获取用户列表
        Page<User> list = userService.getPageList(user);

        // 封装数据
        model.addAttribute("list", list.getContent());
        model.addAttribute("page", list);
        model.addAttribute("dept", user.getDept());
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

    /**
     * 跳转到编辑页面
     */
    @GetMapping("/edit/{id}")
    @RequiresPermissions("system:input:edit")
    public String toEdit(@PathVariable("id") User user, Model model) {
        model.addAttribute("input", user);
        return "/system/input/add";
    }

    /**
     * 保存添加/修改的数据
     * @param valid 验证对象
     * @param user 实体对象
     */
    @PostMapping("/save")
    @RequiresPermissions({"system:input:add", "system:input:edit"})
    @ResponseBody
    @ActionLog(key = UserAction.USER_SAVE, action = UserAction.class)
    public ResultVo save(@Validated UserValid valid, @EntityParam User user) {

        // 验证数据是否合格
        if (user.getId() == null) {

            // 判断密码是否为空
            if (user.getPassword().isEmpty() || "".equals(user.getPassword().trim())) {
                throw new ResultException(ResultEnum.USER_PWD_NULL);
            }

            // 判断两次密码是否一致
            if (!user.getPassword().equals(valid.getConfirm())) {
                throw new ResultException(ResultEnum.USER_INEQUALITY);
            }

            // 对密码进行加密
            String salt = ShiroUtil.getRandomSalt();
            String encrypt = ShiroUtil.encrypt(user.getPassword(), salt);
            user.setPassword(encrypt);
            user.setSalt(salt);
        }

        // 判断用户名是否重复
        if (userService.repeatByUsername(user)) {
            throw new ResultException(ResultEnum.USER_EXIST);
        }

        // 复制保留无需修改的数据
        if (user.getId() != null) {
            // 不允许操作超级管理员数据
            if (user.getId().equals(AdminConst.ADMIN_ID) &&
                    !ShiroUtil.getSubject().getId().equals(AdminConst.ADMIN_ID)) {
                throw new ResultException(ResultEnum.NO_ADMIN_AUTH);
            }

            User beUser = userService.getById(user.getId());
            String[] fields = {"password", "salt", "picture", "roles"};
            EntityBeanUtil.copyProperties(beUser, user, fields);
        }

        // 保存数据
        userService.save(user);
        return ResultVoUtil.SAVE_SUCCESS;
    }

    /**
     * 跳转到详细页面
     */
    @GetMapping("/detail/{id}")
    @RequiresPermissions("system:input:detail")
    public String toDetail(@PathVariable("id") User user, Model model) {
        model.addAttribute("input", user);
        return "/system/input/detail";
    }


    /**
     * 获取用户头像
     */
    @GetMapping("/picture")
    public void picture(String p, HttpServletResponse response) throws IOException {
        String defaultPath = "/images/user-picture.jpg";
        if (!(StringUtils.isEmpty(p) || p.equals(defaultPath))) {
            UploadProjectProperties properties = SpringContextUtil.getBean(UploadProjectProperties.class);
            String fuPath = properties.getFilePath();
            String spPath = properties.getStaticPath().replace("*", "");
            File file = new File(fuPath + p.replace(spPath, ""));
            if (file.exists()) {
                FileCopyUtils.copy(new FileInputStream(file), response.getOutputStream());
                return;
            }
        }
        Resource resource = new ClassPathResource("static" + defaultPath);
        FileCopyUtils.copy(resource.getInputStream(), response.getOutputStream());
    }




}
