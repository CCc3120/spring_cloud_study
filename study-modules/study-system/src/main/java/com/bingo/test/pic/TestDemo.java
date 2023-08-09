package com.bingo.test.pic;

import com.bingo.study.common.core.utils.file.FileType;
import com.bingo.study.common.core.utils.file.JudgeFileType;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.imaging.jpeg.JpegProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifIFD0Directory;
import org.apache.commons.imaging.ImageReadException;
import org.apache.commons.imaging.Imaging;
import org.apache.commons.imaging.common.ImageMetadata;
import org.apache.commons.imaging.formats.jpeg.JpegImageMetadata;
import org.apache.commons.imaging.formats.tiff.TiffImageMetadata;
import org.apache.commons.imaging.formats.tiff.constants.ExifTagConstants;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @Author h-bingo
 * @Date 2023-08-04 16:37
 * @Version 1.0
 */
public class TestDemo {
    public static void main(String[] args) throws ImageProcessingException, IOException {

        String[] paths = {
                "/Users/h-bingo/Desktop/system/image/picture/1.jpg",
                "/Users/h-bingo/Desktop/system/image/picture/115.png",
        };

        for (String s : paths) {
            readDemo(s);
            System.out.println("=================");
        }

        // String name = "av.jpeg";
        //
        // FileType type = FileType.JPEG;
        //
        // String s = type.name();
        //
        // int i = name.lastIndexOf(".");
        //
        //
        // if ((name.length() - i - 1) != s.length()) {
        //     System.out.println("文件被修改");
        // }
        //
        // String substring = name.substring(i + 1);
        // if (substring.toUpperCase().equals(s)) {
        //     System.out.println("文件正确");
        // } else {
        //     System.out.println("文件被修改2");
        // }
    }

    public static void readDemo(String path) throws ImageProcessingException, IOException {
        File file = new File(path);
        Metadata metadata = ImageMetadataReader.readMetadata(file);

        ExifIFD0Directory exifIFD0Directory = metadata.getDirectory(ExifIFD0Directory.class);

        for (Directory next : metadata.getDirectories()) {

            // next.getTagName()

            for (Tag tag : next.getTags()) {
                System.out.println(tag);
                // System.out.println(tag.getTagName() + ":" + tag.getDescription());
            }
        }
    }

    public static void fileType(String path) {
        FileType type = JudgeFileType.getType(path);
        System.out.println(type);
    }

    public static void readPicInfo2(String path) {
        File file = new File(path);

        try {
            ImageMetadata metadata = Imaging.getMetadata(file);

            // 强转为JpegImageMetadata
            JpegImageMetadata jpegMetadata = (JpegImageMetadata) metadata;

            // 获取TiffImageMetadata
            TiffImageMetadata exif = jpegMetadata.getExif();

            // exif.getGPS();

            String[] fieldValue = exif.getFieldValue(ExifTagConstants.EXIF_TAG_DATE_TIME_ORIGINAL);

            // String fieldValue1 = exif.getFieldValue(MicrosoftTagConstants.EXIF_TAG_XPAUTHOR);
            // System.out.println(fieldValue1);

            for (String s : fieldValue) {
                System.out.println(s);
            }

            List<? extends ImageMetadata.ImageMetadataItem> directories = exif.getDirectories();

            for (ImageMetadata.ImageMetadataItem directory : directories) {
                System.out.println(directory);
            }
            // 转换为流
            // TiffOutputSet out = exif.getOutputSet();
            // 获取TiffOutputDirectory
            // TiffOutputDirectory exifDirectory = out.getOrCreateExifDirectory();

            // List<TiffOutputField> fields = exifDirectory.getFields();


        } catch (ImageReadException | IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void readPicInfo(String path) {
        File file = new File(path);

        Metadata metadata;

        try {
            metadata = JpegMetadataReader.readMetadata(file);

            System.out.println("Directory Count: " + metadata.getDirectoryCount());


            for (Directory next : metadata.getDirectories()) {

                // next.getTagName()

                for (Tag tag : next.getTags()) {
                    System.out.println(tag);
                    // System.out.println(tag.getTagName() + ":" + tag.getDescription());
                }

            }
        } catch (JpegProcessingException | IOException e) {
            throw new RuntimeException(e);
        }
    }
}
