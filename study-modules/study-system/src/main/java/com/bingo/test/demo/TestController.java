package com.bingo.test.demo;

import cn.hutool.extra.spring.SpringUtil;
import com.bingo.common.redis.service.RedisService;
import com.bingo.study.common.component.deprecatedInterface.annotation.DeprecatedInterfaceSee;
import com.bingo.study.common.component.dict.service.IDictService;
import com.bingo.study.common.component.dict.translate.util.TranslateUtil;
import com.bingo.study.common.component.limiter.LimitRealize;
import com.bingo.study.common.component.limiter.LimitType;
import com.bingo.study.common.component.limiter.annotation.RateLimiter;
import com.bingo.study.common.component.nosql.service.EsUpdateService;
import com.bingo.study.common.component.responseFieldHandler.annotation.ResponseField;
import com.bingo.study.common.core.web.page.AjaxResult;
import com.bingo.study.common.core.web.page.AjaxResultFactory;
import com.bingo.test.translate.dict.DictData;
import com.bingo.test.translate.dict.DictDataTwo;
import com.bingo.test.translate.dict.DictType;
import com.bingo.test.translate.dict.DictTypeTwo;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.*;

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

    // @Autowired
    // private IDictCacheService dictCacheService;

    @Autowired
    private RedissonClient redissonClient;

    @Autowired
    private IDictService<DictType, DictData> dictService;

    @Autowired
    private IDictService<DictTypeTwo, DictDataTwo> dictService2;

    @RequestMapping(path = "/testParam/{fdId}", method = RequestMethod.GET)
    public AjaxResult<List<String>> testParam(@PathVariable("fdId") String fdId, @RequestParam(value = "id", required = false) List<String> id) {
        // try {
        //     teacherService.exceSql();
        // } catch (Exception e) {
        //     System.out.println(e.getMessage());
        // }

        // teacherService.exceMaster();

        // teacherService.exceSlave();

        // teacherService.exceSqlTransactional();
        id.add(fdId);
        return AjaxResultFactory.success(id);
    }

    // @RateLimiter(count = 5, time = 10, limitType = LimitType.DEFAULT, limitRealize = LimitRealize.TOKEN_BUCKET)
    // @ResponseBodyFieldFilter(enable = false)
    @RequestMapping(path = "/testDataSource", method = RequestMethod.GET)
    public AjaxResult<String> testDataSource() {
        // try {
        //     teacherService.exceSql();
        // } catch (Exception e) {
        //     System.out.println(e.getMessage());
        // }

        // teacherService.exceMaster();

        // teacherService.exceSlave();

        teacherService.exceSqlTransactional();

        return AjaxResultFactory.success();
    }

    @RateLimiter(count = 5, time = 10, limitType = LimitType.DEFAULT, limitRealize = LimitRealize.TOKEN_BUCKET)
    // @ReturnField(enable = false)
    @RequestMapping(path = "/testLimiter", method = RequestMethod.GET)
    public AjaxResult<String> testLimiter() {
        System.out.println("执行。。。。");
        return AjaxResultFactory.success();
    }

    @RequestMapping(path = "/testTranslate", method = RequestMethod.GET)
    public AjaxResult<String> testTranslate() {
        TranslateUtil bean = SpringUtil.getBean(TranslateUtil.class);
        return AjaxResultFactory.success();
    }

    // @ResponseBodyFieldFilter(enable = false)
    @RequestMapping(path = "/testLock", method = RequestMethod.GET)
    public AjaxResult<String> testLock() throws InterruptedException {
        String data = teacherService.testLock(new Teacher(), "123");
        return AjaxResultFactory.success(data);
    }

    @ResponseField(filedFilter = false)
    @RequestMapping(path = "/testType", method = RequestMethod.GET)
    public String testType() throws InterruptedException {
        // String data = teacherService.testLock("123", new Teacher());
        return "data";
    }

    @DeprecatedInterfaceSee(clazz = TestController.class, method = "testType")
    @ResponseField()
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

    @ResponseField(filedFilter = false, wrapper = false)
    @RequestMapping(path = "/test000", method = RequestMethod.GET)
    public Teacher test000() {
        Teacher teacher = new Teacher();
        teacher.setFdName("张三");
        teacher.setFdAge(20);
        teacher.setFdBirthday(new Date());
        teacher.setFdNo("ZX002");
        teacher.setFdIdCard("182934567879036251");
        teacher.setFdSex("男");
        teacher.setFdCreateTime(new Date());
        teacher.setFdUpdateTime(new Date());
        return teacher;
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

    @RequestMapping(path = "/test", method = RequestMethod.GET)
    public AjaxResult test() {
        return AjaxResultFactory.success();
    }

    @RequestMapping(path = "/testDict", method = RequestMethod.GET)
    public AjaxResult testDict() {
        Map<DictType, List<DictData>> map = new LinkedHashMap<>();
        for (int i = 0; i < 5; i++) {
            DictType dictType = new DictType();
            dictType.setFdName("类型名" + i);
            dictType.setFdType("type-" + i);

            List<DictData> list = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                DictData data = new DictData();
                data.setFdCode(i + "||" + j);
                data.setFdName("字典数据" + j);
                data.setFdType("type-" + i);
                list.add(data);
            }

            map.put(dictType, list);
        }

        Map<DictTypeTwo, List<DictDataTwo>> map2 = new LinkedHashMap<>();
        for (int i = 0; i < 5; i++) {
            DictTypeTwo dictType = new DictTypeTwo();
            dictType.setFdName("类型名" + i);
            dictType.setFdType("type-" + i);

            List<DictDataTwo> list = new ArrayList<>();
            for (int j = 0; j < 5; j++) {
                DictDataTwo data = new DictDataTwo();
                data.setFdCode(i + "||" + j);
                data.setFdName("字典数据" + j);
                data.setFdType("type-" + i);
                list.add(data);
            }

            map2.put(dictType, list);
        }

        // dictCacheService.setDictCache(map);

        // List<DictData> dictCache = dictCacheService.getDictCache("type-0");
        //
        // DictData data = dictCacheService.getDictCache("0||2", "type-0");
        //
        // Map<DictType, List<DictData>> dictCache1 = dictCacheService.getDictCache();

        // dictCacheService.refreshDictCache();
        // dictCacheService.refreshDictCache(map);

        // dictCacheService.removeDictCache();

        dictService.refreshDict(map);

        dictService2.refreshDict(map2);

        return AjaxResultFactory.success();
    }
}
