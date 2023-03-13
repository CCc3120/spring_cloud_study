package com.bingo.test.demo;

import com.bingo.common.redis.service.RedisService;
import com.bingo.study.common.component.nosql.service.EsUpdateService;
import com.bingo.study.common.core.page.AjaxResult;
import com.bingo.study.common.core.page.AjaxResultFactory;
import com.bingo.study.common.core.utils.SpringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

/**
 * @Author h-bingo
 * @Date 2023-01-12 10:04
 * @Version 1.0
 */
@RestController
@RequestMapping(path = "/test")
public class TestController {

    @Autowired
    private RedisService redisService;

    @Autowired
    private TeacherService teacherService;

    @RequestMapping(path = "/test01")
    public AjaxResult test01(){

        EsUpdateService bean = SpringUtil.getBean(EsUpdateService.class);

        redisService.setCacheObject("test_key", "asdsadfnskdjfs");

        return AjaxResultFactory.success(redisService.getCacheObject("test_key"));
    }

    @RequestMapping(path = "/test02")
    public AjaxResult test02(){

        Teacher teacher = new Teacher();
        teacher.setFdName("张三");
        teacher.setFdAge(20);
        teacher.setFdBirthday(new Date());
        teacher.setFdNo("ZX002");
        teacher.setFdIdCard("182934567879036251");
        teacher.setFdSex("男");

        teacherService.insert(teacher);

        return AjaxResultFactory.success();
    }

    @RequestMapping(path = "/test03")
    public AjaxResult test03(String fdId) throws Exception {
        Teacher teacher = new Teacher() ;
        teacher.setFdId(fdId);

        teacherService.select(teacher);

        return AjaxResultFactory.success();
    }

    @RequestMapping(path = "/test04")
    public AjaxResult test04(String fdId){

        Teacher teacher = new Teacher();
        teacher.setFdName("张三");
        teacher.setFdAge(32);
        teacher.setFdBirthday(new Date());
        teacher.setFdNo("ZX004");
        teacher.setFdIdCard("182934567879036251");
        teacher.setFdSex("女");
        teacher.setFdId(fdId);

        teacherService.update(teacher);

        return AjaxResultFactory.success();
    }

    @RequestMapping(path = "/test05")
    public AjaxResult test05(String fdId){

        Teacher teacher = new Teacher() ;
        teacher.setFdId(fdId);

        teacherService.delete(teacher);

        return AjaxResultFactory.success();
    }

}
