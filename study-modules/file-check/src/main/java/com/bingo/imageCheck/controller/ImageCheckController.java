package com.bingo.imageCheck.controller;

import com.bingo.imageCheck.response.CheckResult;
import com.bingo.imageCheck.service.IImageCheckService;
import com.bingo.study.common.core.controller.BaseController;
import com.bingo.study.common.core.response.RSX;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-08-07 14:35
 * @Version 1.0
 */
@RestController
@RequestMapping(path = "/image")
public class ImageCheckController extends BaseController {

    @Autowired
    private IImageCheckService imageCheckService;

    @RequestMapping(path = "/check", method = RequestMethod.POST)
    public RSX<CheckResult> check(@RequestParam("file") MultipartFile multipartFile) {
        return success(imageCheckService.checkImage(multipartFile));
    }

    @RequestMapping(path = "/checks", method = RequestMethod.POST)
    public RSX<List<CheckResult>> checks(@RequestParam("files") List<MultipartFile> fileList) {
        return success(imageCheckService.checkImage(fileList));
    }
}
