package com.wishes.yearOld.common;

import com.alibaba.fastjson.JSON;
import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifDirectoryBase;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by tmg-yesky on 2016/12/28.
 * 获取图片相关信息工具类
 */
public class PicExifUtil {

    /**
     * 返回json格式的图片信息字符串
     * @param picPath 图片路径
     * @return
     */
    public static String getPicInfo(String picPath){
        if(StringUtils.isEmpty(picPath))
            return null;
        File pic = new File(picPath);
        if(!pic.exists())
            return null;
        String info = null;
        try {
            String fileType = ImageTypeCheck.getImageType(pic);
            if("".equals(fileType)){
                System.out.println("couldn't get image type");
                return null;
            }
            Map<String, Object> data = new HashMap<>();
            Metadata metadata = ImageMetadataReader.readMetadata(pic);
            Collection<ExifDirectoryBase> exif = metadata.getDirectoriesOfType(ExifDirectoryBase.class);
            for (Directory directory : exif){
                for (Tag tag : directory.getTags()) {
                    //System.out.println(tag);
                    data.put(tag.getTagName(), tag.getDescription());
                }
            }
            /*for (Directory directory : metadata.getDirectories()) {
                Map<String, String> tags = new HashMap<>();
                for (Tag tag : directory.getTags()) {
                    tags.put(tag.getTagName(), tag.getDescription());
                    //System.out.println(tag);
                }
                data.put(directory.getName(), tags);
                //System.out.println(directory.getTagName(ExifDirectoryBase.TAG_IMAGE_WIDTH)+":"+directory.getDescription(ExifDirectoryBase.TAG_IMAGE_WIDTH));
            }*/
            info = JSON.toJSONString(data);
        } catch (ImageProcessingException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
        return info;
    }

    public static void main(String[] args) {
    	//String files = new File("E:\\home\\tgod-image\\8.jpg");
    	
        String picName = "E:\\home\\tgod-image\\5.jpg";
        String info = getPicInfo(picName);
        
        System.out.println(info);
    }

}
