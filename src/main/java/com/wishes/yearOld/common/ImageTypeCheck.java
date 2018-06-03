package com.wishes.yearOld.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by IntelliJ IDEA.
 * User: garmbrood
 * Time: 2016/7/20
 * Company: 天极传媒集团
 * Descripion:
 * 根据图片文件头部字节，判断出图片的类型
 */
public class ImageTypeCheck {

    public static final Map<int[], String> TYPE_MAGIC_CODE_MAPS = new HashMap<int[], String>();

    static {
        TYPE_MAGIC_CODE_MAPS.put(new int[]{0x42, 0x4D}, "bmp");
        TYPE_MAGIC_CODE_MAPS.put(new int[]{0x89, 0x50, 0x4E, 0x47, 0x0D, 0x0A, 0x1A, 0x0A}, "png");
        TYPE_MAGIC_CODE_MAPS.put(new int[]{0x47, 0x49, 0x46, 0x38, 0x37, 0x61}, "gif");
        TYPE_MAGIC_CODE_MAPS.put(new int[]{0x47, 0x49, 0x46, 0x38, 0x39, 0x61}, "gif");
        TYPE_MAGIC_CODE_MAPS.put(new int[]{0xFF, 0xD8, 0xFF, 0xDB}, "jpg/jpeg");
        TYPE_MAGIC_CODE_MAPS.put(new int[]{0xFF, 0xD8, 0xFF, 0xE0, 0xAA, 0xAA, 0x4A, 0x46, 0x49, 0x46, 0x00, 0x01}, "jpg/jpeg");//0xAA表示匹配任意数字
        TYPE_MAGIC_CODE_MAPS.put(new int[]{0xFF, 0xD8, 0xFF, 0xE1, 0xAA, 0xAA, 0x45, 0x78, 0x69, 0x66, 0x00, 0x00}, "jpg/jpeg");
    }

    /**
     * 根据图片文件头部字节，判断出图片的类型
     *
     * @param imagefile 图片文件
     * @return 图片类型，空字符串则表示未匹配
     * @throws java.io.IOException
     */
    public static String getImageType(File imagefile) throws IOException {
        if (imagefile.exists() && imagefile.isFile()) {
            FileInputStream fis = new FileInputStream(imagefile);
            return getImageType(fis);
        }
        return "";
    }

    /**
     * 根据图片文件头部字节，判断出图片的类型
     *
     * @param fis t图片文件流
     * @return 图片类型，空字符串则表示未匹配
     * @throws java.io.IOException
     */
    private static String getImageType(FileInputStream fis) throws IOException {
        byte[] topbytes = new byte[16];
        fis.read(topbytes, 0, topbytes.length);
        for (int[] key : TYPE_MAGIC_CODE_MAPS.keySet()) {
            for (int i = 0; i < key.length; i++) {
                if (key[i] == 0xAA) {//0xAA表示匹配任意数字
                    continue;
                }
                if (key[i] != toUnsignedInt(topbytes[i])) {
                    break;
                }
                if (i == key.length - 1) {
                    return TYPE_MAGIC_CODE_MAPS.get(key);
                }
            }
        }
        return "";
    }

    private static int toUnsignedInt(byte x){
        return  ((int) x) & 0xff;
    }

}
