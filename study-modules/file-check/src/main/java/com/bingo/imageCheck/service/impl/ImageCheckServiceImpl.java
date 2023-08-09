package com.bingo.imageCheck.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import com.bingo.imageCheck.response.CheckResult;
import com.bingo.imageCheck.service.IImageCheckService;
import com.bingo.study.common.core.utils.file.FileType;
import com.bingo.study.common.core.utils.file.JudgeFileType;
import com.drew.imaging.ImageMetadataReader;
import com.drew.metadata.Metadata;
import com.drew.metadata.adobe.AdobeJpegDirectory;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import com.drew.metadata.exif.GpsDirectory;
import com.drew.metadata.photoshop.PhotoshopDirectory;
import com.drew.metadata.photoshop.PsdHeaderDirectory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-08-07 14:45
 * @Version 1.0
 */
@Slf4j
@Service
public class ImageCheckServiceImpl implements IImageCheckService {
    @Override
    public CheckResult checkImage(MultipartFile multipartFile) {
        String originalFilename = multipartFile.getOriginalFilename();

        CheckResult result = CheckResult.success(originalFilename, null);

        String name = originalFilename.replaceAll("\\.", "");

        File file = null;
        try {
            file = File.createTempFile(name, ".tmp");
            // 写入临时文件
            multipartFile.transferTo(file);

            if (!checkImageHeader(file, originalFilename)) {
                result.setResult(false);
            }

            if (!checkImageInfo(file, result)) {
                result.setResult(false);
            }

        } catch (IOException e) {
            log.info("文件解析异常", e);
        } finally {
            if (file != null) {
                file.deleteOnExit();
            }
        }
        return result;
    }

    @Override
    public List<CheckResult> checkImage(List<MultipartFile> fileList) {
        List<CheckResult> list = new ArrayList<>();
        if (!CollectionUtil.isEmpty(fileList)) {
            for (MultipartFile multipartFile : fileList) {
                list.add(checkImage(multipartFile));
            }
        }
        return list;
    }

    private boolean checkImageInfo(File file, CheckResult result) {
        Metadata metadata;
        boolean isTrue = true;
        try {
            metadata = ImageMetadataReader.readMetadata(file);

            PhotoshopDirectory photoshopDirectory = metadata.getDirectory(PhotoshopDirectory.class);

            AdobeJpegDirectory adobeJpegDirectory = metadata.getDirectory(AdobeJpegDirectory.class);

            PsdHeaderDirectory psdHeaderDirectory = metadata.getDirectory(PsdHeaderDirectory.class);

            if (photoshopDirectory != null || psdHeaderDirectory != null || adobeJpegDirectory != null) {
                isTrue = false;
            }

            ExifIFD0Directory exifIFD0Directory = metadata.getDirectory(ExifIFD0Directory.class);

            if (exifIFD0Directory != null) {
                if (exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_DATETIME)) {
                    String dataTime = exifIFD0Directory.getDescription(ExifIFD0Directory.TAG_DATETIME);
                    result.setDateTime(dataTime);
                }

                if (exifIFD0Directory.containsTag(ExifIFD0Directory.TAG_SOFTWARE)) {
                    String software = exifIFD0Directory.getDescription(ExifIFD0Directory.TAG_SOFTWARE);
                    result.setSoftware(software);
                }
            }

            ExifSubIFDDirectory exifSubIFDDirectory = metadata.getDirectory(ExifSubIFDDirectory.class);

            if (exifSubIFDDirectory != null) {
                String original = exifSubIFDDirectory.getDescription(ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);
                String digitized = exifSubIFDDirectory.getDescription(ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED);
            }

            GpsDirectory gpsDirectory = metadata.getDirectory(GpsDirectory.class);

            if (gpsDirectory != null) {
                if (gpsDirectory.containsTag(GpsDirectory.TAG_LATITUDE)) {
                    String latitude = gpsDirectory.getDescription(GpsDirectory.TAG_LATITUDE);
                    result.setLatitude(latitude);
                }

                if (gpsDirectory.containsTag(GpsDirectory.TAG_LONGITUDE)) {
                    String longitude = gpsDirectory.getDescription(GpsDirectory.TAG_LONGITUDE);
                    result.setLongitude(longitude);
                }
            }
        } catch (Exception e) {
            log.info("文件exif解析异常", e);
        }

        return isTrue;
    }

    private boolean checkImageHeader(File file, String fileName) {
        FileType fileType = JudgeFileType.getType(file);

        if (fileType != null) {
            String name = fileName;

            String s = fileType.name();

            int i = name.lastIndexOf(".");

            if ((name.length() - i - 1) != s.length()) {
                return false;
            }

            String substring = name.substring(i + 1);
            if (!substring.toUpperCase().equals(s)) {
                return false;
            }
        }

        return true;
    }
}
