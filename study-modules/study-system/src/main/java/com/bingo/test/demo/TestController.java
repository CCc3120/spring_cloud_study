package com.bingo.test.demo;

import cn.hutool.extra.spring.SpringUtil;
import com.bingo.common.redis.service.RedisService;
import com.bingo.study.common.component.deprecatedInterface.annotation.DeprecatedInterfaceSee;
import com.bingo.study.common.component.limiter.LimitRealize;
import com.bingo.study.common.component.limiter.LimitType;
import com.bingo.study.common.component.limiter.annotation.RateLimiter;
import com.bingo.study.common.component.nosql.service.EsUpdateService;
import com.bingo.study.common.component.returnValue.annotation.ReturnField;
import com.bingo.study.common.core.page.AjaxResult;
import com.bingo.study.common.core.page.AjaxResultFactory;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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

    @Autowired
    private RedissonClient redissonClient;

    @RateLimiter(count = 5, time = 10, limitType = LimitType.DEFAULT, limitRealize = LimitRealize.TOKEN_BUCKET)
    @ReturnField(enable = false)
    @RequestMapping(path = "/testLimiter", method = RequestMethod.GET)
    public AjaxResult<String> testLimiter() {



        return AjaxResultFactory.success();
    }

    @ReturnField(enable = false)
    @RequestMapping(path = "/testLock", method = RequestMethod.GET)
    public AjaxResult<String> testLock() throws InterruptedException {
        String data = teacherService.testLock("123", new Teacher());
        return AjaxResultFactory.success(data);
    }

    @ReturnField(enable = false)
    @RequestMapping(path = "/testType", method = RequestMethod.GET)
    public String testType() throws InterruptedException {
        String data = teacherService.testLock("123", new Teacher());
        return data;
    }

    @DeprecatedInterfaceSee(value = "/test01")
    @ReturnField(specify = {"fdName", "fdSex"})
    @RequestMapping(path = "/advice", method = RequestMethod.GET)
    public AjaxResult<Teacher> test00() {
        Teacher teacher = new Teacher();
        teacher.setFdName("张三");
        teacher.setFdAge(20);
        teacher.setFdBirthday(new Date());
        teacher.setFdNo("ZX002");
        teacher.setFdIdCard("182934567879036251");
        teacher.setFdSex("男");
        teacher.setFdCreateTime(new Date());
        teacher.setFdUpdateTime(new Date());


        return AjaxResultFactory.success(teacher);
    }

    @RequestMapping(path = "/test01")
    public AjaxResult test01() {

        EsUpdateService bean = SpringUtil.getBean(EsUpdateService.class);

        redisService.setCacheObject("test_key", "asdsadfnskdjfs");

        return AjaxResultFactory.success(redisService.getCacheObject("test_key"));
    }

    @RequestMapping(path = "/test02")
    public AjaxResult test02() {

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
        Teacher teacher = new Teacher();
        teacher.setFdId(fdId);

        teacherService.select(teacher);

        return AjaxResultFactory.success();
    }

    @RequestMapping(path = "/test04")
    public AjaxResult test04(String fdId) {

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
    public AjaxResult test05(String fdId) {

        Teacher teacher = new Teacher();
        teacher.setFdId(fdId);

        teacherService.delete(teacher);

        return AjaxResultFactory.success();
    }

}
