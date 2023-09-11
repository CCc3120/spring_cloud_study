package com.bingo.test.esAndMongo;

import com.bingo.study.common.core.controller.BaseController;
import com.bingo.study.common.core.web.response.RSX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-09-08 15:14
 * @Version 1.0
 */
@RestController
@RequestMapping(path = "/esMongo")
public class EsAndMongoModelController extends BaseController {

    @Autowired
    private EsAndMongoModelService esAndMongoModelService;

    @RequestMapping(path = "/add", method = RequestMethod.GET)
    public RSX<String> insert() {
        for (int i = 0; i < 500; i++) {
            EsAndMongoModel esAndMongoModel = new EsAndMongoModel();
            esAndMongoModel.setFdInt(i << 2);
            esAndMongoModel.setFdChar("测试数据" + i);
            esAndMongoModel.setFdLong((long) i << 4);
            esAndMongoModel.setFdDate(new Date());
            esAndMongoModelService.insert(esAndMongoModel);
        }
        return success();
    }

    @RequestMapping(path = "/addOne", method = RequestMethod.GET)
    public RSX<String> insertOne() {
        EsAndMongoModel esAndMongoModel = new EsAndMongoModel();
        esAndMongoModel.setFdInt(123);
        esAndMongoModel.setFdChar("测试数据" + 9999999);
        esAndMongoModel.setFdLong(12345L);
        esAndMongoModel.setFdDate(new Date());
        esAndMongoModelService.insert(esAndMongoModel);
        return success();
    }

    @RequestMapping(path = "/delete/{fdId}", method = RequestMethod.GET)
    public RSX<String> delete(@PathVariable("fdId") String fdId) {
        EsAndMongoModel esAndMongoModel = new EsAndMongoModel();
        esAndMongoModel.setFdId(fdId);
        esAndMongoModelService.delete(esAndMongoModel);
        return success();
    }

    @RequestMapping(path = "/listEs", method = RequestMethod.GET)
    public RSX<List<EsAndMongoModel>> listEs() throws Exception {
        return success(esAndMongoModelService.listEs());
    }

    @RequestMapping(path = "/listMongo", method = RequestMethod.GET)
    public RSX<List<EsAndMongoModel>> listMongo() throws Exception {
        return success(esAndMongoModelService.listMongo());
    }
}
