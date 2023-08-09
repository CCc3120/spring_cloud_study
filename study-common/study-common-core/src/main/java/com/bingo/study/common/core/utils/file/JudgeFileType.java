package com.bingo.study.common.core.utils.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

/**
 * @Author h-bingo
 * @Date 2023-08-07 11:34
 * @Version 1.0
 */
@Slf4j
public class JudgeFileType {
    /**
     * 将文件头转换成16进制字符串
     *
     * @param src 待转换的字节流
     * @return 转成十六进制的字符形式
     */
    private static String bytesToHexString(byte[] src) {
        StringBuilder sb = new StringBuilder();

        if (src == null) {
            return null;
        }

        for (byte b : src) {
            int j = b & 0xFF;
            String hexString = Integer.toHexString(j);
            if (hexString.length() < 2) {
                sb.append(0);
            }
            sb.append(hexString);
        }

        return sb.toString();
    }

    /**
     * @param filePath 文件路径
     * @return 文件头
     */
    private static String getFileContent(String filePath) {
        byte[] b = new byte[28];
        InputStream inputStream = null;

        try {
            inputStream = Files.newInputStream(Paths.get(filePath));
            inputStream.read(b, 0, b.length);
        } catch (IOException e) {
            log.error("读取文件头失败", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("io流关闭异常", e);
                }
            }
        }
        return bytesToHexString(b);
    }

    public static FileType getType(File file) {
        byte[] b = new byte[28];

        InputStream inputStream = null;

        try {
            inputStream = Files.newInputStream(file.toPath());
            inputStream.read(b, 0, b.length);
        } catch (IOException e) {
            log.error("读取文件头失败", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("io流关闭异常", e);
                }
            }
        }

        String s = bytesToHexString(b);

        if (StringUtils.isBlank(s)) {
            return null;
        }

        s = s.toUpperCase();

        FileType[] fileTypes = FileType.values();

        for (FileType type : fileTypes) {
            if (s.startsWith(type.getDesc()) || type.getDesc().startsWith(s)) {
                return type;
            }
        }

        return null;
    }

    public static FileType getType(InputStream inputStream) {
        byte[] b = new byte[28];

        try {
            inputStream.read(b, 0, b.length);
        } catch (IOException e) {
            log.error("读取文件头失败", e);
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    log.error("io流关闭异常", e);
                }
            }
        }

        String s = bytesToHexString(b);

        if (StringUtils.isBlank(s)) {
            return null;
        }

        s = s.toUpperCase();

        FileType[] fileTypes = FileType.values();

        for (FileType type : fileTypes) {
            if (s.startsWith(type.getDesc()) || type.getDesc().startsWith(s)) {
                return type;
            }
        }

        return null;
    }

    /**
     * 判断文件类型
     *
     * @param filePath 文件路径
     * @return 文件类型
     */
    public static FileType getType(String filePath) {
        String fileHead = getFileContent(filePath);

        if (fileHead == null || fileHead.isEmpty()) {
            return null;
        }

        fileHead = fileHead.toUpperCase();

        FileType[] fileTypes = FileType.values();

        for (FileType type : fileTypes) {
            if (fileHead.startsWith(type.getDesc()) || type.getDesc().startsWith(fileHead)) {
                return type;
            }
        }

        return null;
    }
}
