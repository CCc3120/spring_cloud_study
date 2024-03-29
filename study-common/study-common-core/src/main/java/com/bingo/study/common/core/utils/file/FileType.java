package com.bingo.study.common.core.utils.file;

import com.bingo.study.common.core.enums.DescEnum;
import lombok.Getter;

/**
 * @Author h-bingo
 * @Date 2023-08-07 11:33
 * @Version 1.0
 */
@Getter
public enum FileType implements DescEnum {

    /**
     * JEPG.
     */
    JPEG("FFD8FF"),


    /**
     * PNG.
     */
    PNG("89504E47"),


    /**
     * GIF.
     */
    GIF("47494638"),


    /**
     * TIFF.
     */
    TIFF("49492A00"),


    /**
     * Windows Bitmap.
     */
    BMP("424D"),


    /**
     * CAD.
     */
    DWG("41433130"),


    /**
     * Adobe Photoshop.
     */
    PSD("38425053"),


    /**
     * Rich Text Format.
     */
    RTF("7B5C727466"),


    /**
     * XML.
     */
    XML("3C3F786D6C"),


    /**
     * HTML.
     */
    HTML("68746D6C3E"),


    /**
     * Email [thorough only].
     */
    EML("44656C69766572792D646174653A"),


    /**
     * Outlook Express.
     */
    DBX("CFAD12FEC5FD746F"),


    /**
     * Outlook (pst).
     */
    PST("2142444E"),


    /**
     * MS Word/Excel.
     */
    XLS_DOC("D0CF11E0"),


    /**
     * MS Access.
     */
    MDB("5374616E64617264204A"),


    /**
     * WordPerfect.
     */
    WPD("FF575043"),


    /**
     * Postscript.
     */
    EPS("252150532D41646F6265"),


    /**
     * Adobe Acrobat.
     */
    PDF("255044462D312E"),


    /**
     * Quicken.
     */
    QDF("AC9EBD8F"),


    /**
     * Windows Password.
     */
    PWL("E3828596"),


    /**
     * ZIP Archive.
     */
    ZIP("504B0304"),


    /**
     * RAR Archive.
     */
    RAR("52617221"),


    /**
     * Wave.
     */
    WAV("57415645"),


    /**
     * AVI.
     */
    AVI("41564920"),


    /**
     * Real Audio.
     */
    RAM("2E7261FD"),


    /**
     * Real Media.
     */
    RM("2E524D46"),


    /**
     * MPEG (mpg).
     */
    MPG("000001BA"),


    /**
     * Quicktime.
     */
    MOV("6D6F6F76"),


    /**
     * Windows Media.
     */
    ASF("3026B2758E66CF11"),


    /**
     * MIDI.
     */
    MID("4D546864"),

    ;


    private final String desc;


    /**
     * Constructor.
     *
     * @param desc
     */
    FileType(String desc) {
        this.desc = desc;
    }
}
