package com.bingo.study.common.component.dict.service.impl;

import com.bingo.study.common.component.dict.service.IDictTranslateService;
import com.bingo.study.common.core.dict.IDictDataModel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;

import java.util.Map;
import java.util.Optional;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

/**
 * @Author h-bingo
 * @Date 2023-08-14 13:55
 * @Version 1.0
 */
@Slf4j
public abstract class AbstractDictTranslateService<D extends IDictDataModel>
        implements IDictTranslateService<D>, InitializingBean {

    /**
     * 临时的本地缓存
     * 在大量数据进行翻译的时候，减少redis 网络io次数
     * 定时清理，生效时间 1 分钟
     */
    private final Map<String, Map<String, D>> LOCAL_CACHE = new ConcurrentHashMap<>(8);

    @Override
    public Optional<D> getDictOpt(String code, String type) {
        return Optional.ofNullable(this.getDict(code, type));
    }

    @Override
    public void dictTran(String code, String type, Consumer<D> consumer) {
        Map<String, D> dictMap = this.LOCAL_CACHE.putIfAbsent(type, new ConcurrentHashMap<>());
        D d = dictMap.get(code);
        if (d != null) {
            consumer.accept(d);
        } else {
            this.getDictOpt(code, type).ifPresent(data -> {
                dictMap.put(code, data);
                consumer.accept(data);
            });
        }
    }

    private void scheduleClean() {
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                LOCAL_CACHE.clear();
                log.info("字典本地缓存已清理");
            }
        }, 60 * 1000, 60 * 1000);

        log.info("已开启定时清理字典本地缓存");
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        scheduleClean();
    }
}
