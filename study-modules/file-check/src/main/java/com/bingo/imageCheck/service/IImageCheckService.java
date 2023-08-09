package com.bingo.imageCheck.service;

import com.bingo.imageCheck.response.CheckResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-08-07 14:39
 * @Version 1.0
 */
public interface IImageCheckService {

    CheckResult checkImage(MultipartFile multipartFile);

    List<CheckResult> checkImage(List<MultipartFile> fileList);
}
