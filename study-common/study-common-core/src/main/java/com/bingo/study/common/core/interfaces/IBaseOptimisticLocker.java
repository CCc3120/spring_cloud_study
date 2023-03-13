package com.bingo.study.common.core.interfaces;

import java.util.Date;

/**
 * 乐观锁接口
 *
 * @author bingo
 * @date 2022-03-25 12:51
 */
public interface IBaseOptimisticLocker extends IBaseModel {
    // private Date fdVersion;                     // 乐观锁

    Date getFdVersion();

    void setFdVersion(Date fdVersion);
}
