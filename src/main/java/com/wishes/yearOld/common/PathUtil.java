package com.wishes.yearOld.common;

import java.io.File;
import java.util.Date;



/**
 * Created by IntelliJ IDEA.
 * User: garmbrood
 * Time: 2009-5-25 11:15:15
 * Company: 天极传媒集团
 */
public class PathUtil {


    /**
     *注释 web根路径改成从配置文件获取
     */
   private static String appPath = "/home/tgod/tgod-api/tgod_app_api/";
	
  
	/**
     * web根路径，由项目初始化时在Lisener中赋值
     */
    //private static String appPath = Configurations.getFileRepository();
    
	
    public static final String FILE_SAVE_PATH = "upload";
    /**
     * 图片上传目录
     */
    public static final String IMAGE_SAVE_PATH = Constant.UPLOAD_IMAGE_DIR;
    /**
     * 视频上传目录
     */
    public static final String VIDEO_SAVE_PATH = Constant.UPLOAD_VIDEO_DIR;

    /**
     * 返回 ROOT 路径
     */
    public static String getAppPath(){
        return appPath;
    }

    /**
     * 设置ROOT路径
     */
    public static void setAppPath(String appPath) {
        PathUtil.appPath = appPath;
    }

    /**
     * 获取上传文件的随机名称，自动建立父级目录，文件名字按12位随机生成
     * @param s_FileExt 文件后缀，如：.jpg .gif等
     * @return 文件句柄
     */
    public static File getRandFile(String s_FileExt) {
        String file = DateUtils.format(new Date(), "yyyy" + File.separator + "DDD" + File.separator+"ss"+File.separator) + getGuid(12) + s_FileExt;
        File fileName = new File(new File(appPath , FILE_SAVE_PATH), file);
        if (!fileName.getParentFile().exists()) {
            fileName.getParentFile().mkdirs();
        }
        return fileName;
    }

    public static File getRandImageFile(String s_FileExt) {
        String file = DateUtils.format(new Date(), "yyyy" + File.separator + "DDD" + File.separator+"ss"+File.separator) + getGuid(12) + s_FileExt;
        File fileName = new File(new File(IMAGE_SAVE_PATH), file);
        if (!fileName.getParentFile().exists()) {
            fileName.getParentFile().mkdirs();
        }
        return fileName;
    }
    public static File getRandImageFilePath(String s_FileExt,String sorcePath) {
        String file = DateUtils.format(new Date(), "yyyy" + File.separator + "DDD" + File.separator+"ss"+File.separator) + getGuid(12) + s_FileExt;
        File fileName = new File(new File(sorcePath), file);
        if (!fileName.getParentFile().exists()) {
            fileName.getParentFile().mkdirs();
        }
        return fileName;
    }
    /**
     * 缩略图，原图以及水印图片存放的路径
     * @param s_FileExt,文件路径filePaths
     * @return
     */
    public static File getAllImageFile(String s_FileExt,String filePaths) {
        String file = DateUtils.format(new Date(), "yyyy" + File.separator + "DDD" + File.separator+"ss"+File.separator) + getGuid(12) + s_FileExt;
        File fileName = new File(new File(filePaths), file);
        if (!fileName.getParentFile().exists()) {
            fileName.getParentFile().mkdirs();
        }
        return fileName;
    }
    public static String getImageFileUrlFromPath(String path) {
        if(!StringUtils.isEmpty(path)){
            path = path.replace("\\", "/");
            return StringUtils.substringAfter(path, "/home/");
        }
        return "";
    }

    public static File getRandVideoFile(String s_FileExt) {
        String file = DateUtils.format(new Date(), "yyyy" + File.separator + "DDD" + File.separator+"ss"+File.separator) + getGuid(12) + s_FileExt;
        File fileName = new File(new File(VIDEO_SAVE_PATH), file);
        if (!fileName.getParentFile().exists()) {
            fileName.getParentFile().mkdirs();
        }
        return fileName;
    }

    public static String getVideoFileUrlFromPath(String path) {
        if(!StringUtils.isEmpty(path)){
            path = path.replace("\\", "/");
            return StringUtils.substringAfter(path, "/home/");
        }
        return "";
    }

    /**
     * 获取随机文件名，文件名字按12位随机生成
     * @param s_FileExt 文件后缀，如：.jpg .gif等
     * @return
     */
    public static String getRandFileName(String s_FileExt) {
        String fileName = getGuid(12) + s_FileExt;
        return fileName;
    }

    /**
     * 获取上传文件的随机名称，自动建立父级目录，文件名字按12位随机生成
     * @param s_FileExt 文件后缀，如：.jpg .gif等
     * @return 文件句柄
     */
    public static File getRandFile(String s_FileExt,String name) {
        String file = DateUtils.format(new Date(), "yyyy"+File.separator+"DDD"+File.separator+"ss"+File.separator) + getGuid(12) +"_"+name + s_FileExt;
        File fileName = new File(new File(appPath , FILE_SAVE_PATH), file);
        if (!fileName.getParentFile().exists()) {
            fileName.getParentFile().mkdirs();
        }
        return fileName;
    }

    /**
     * 根据web下的文件句柄返回该文件的url
     * @param s_File 处在web目录下的文件句柄
     * @return 文件的url
     */
    public static String getRandFileUrl(File s_File){
        return new StringBuilder().append("/")
                .append(StringUtils.substringAfter(s_File.getPath(), appPath).replace(File.separator, "/")).toString();
    }

    /**
     * 获取当前系统下的PATH
     * @param path
     * @return
     */
    public static String getRightPath(String path){
        return new StringBuilder().append(File.separator)
                .append(path.replace("\\", File.separator).replace("/", File.separator)).toString();
    }
    
    /**
     * 新增
     * 根据源文件名生成 "源文件名+下划线+5位随机字符串+后缀" 的文件
     * @param fileName 源文件文件名
     * @return
     */
    public static File getRandFileS(String fileName) {
        return getRandFileS(fileName, Constant.IMAGE_SAVE_PATH);
    }
    /**
     * 新增
     * @return
     */
    public static File getRandFileS(String fileName, String path) {
        if(StringUtils.isEmpty(path)){
            path = Constant.IMAGE_SAVE_PATH;
        }
        String file = DateUtils.format(new Date(), "yyyy" + File.separator + "DDD" + File.separator+"ss"+File.separator) + StringUtils.substringBefore(fileName, ".") + "_" + getGuid(5) + "." + StringUtils.substringAfter(fileName, ".");
        File fileT = new File(new File(appPath , path), file);
        if (!fileT.getParentFile().exists()) {
            fileT.getParentFile().mkdirs();
        }
        return fileT;
    }
    /**
     * 根据url获取对应磁盘中的文件
     * @param url 资源的url
     * @return 资源对应的文件
     */
    public static File getFileFromUrl(String url){
        if(url == null)
            return null;
        if(url.startsWith("/")) {
            return new File(appPath,url);
        }else if(url.startsWith("http")){
            url = StringUtils.substringAfter(url,"http://");
            url = StringUtils.substringAfter(url,"/");
            return new File(appPath,url);
        }else
            return null;
    }

    /**
     * 产生一个指定长度的的guid<br>
     * 由大写字母+数字构成
     * --------------------------------------------------------------------------<br>
     * 创建者：杨思勇<br>
     * 创建日期：2002-12-5<br>
     * <br>
     * 修改者：<br>
     * 修改日期：<br>
     * 修改原因：<br>
     * --------------------------------------------------------------------------
     *
     * @param len
     * @return guid
     */
    public static String getGuid(int len) {
        StringBuffer sb = new StringBuffer(len);
        for (int i = 0; i < len; i++) {
            int k;
            switch ((int) (Math.random() * 2D)) {
                case 0: // '\0'
                    k = (int) (Math.random() * 10D) + 48;
                    break;

                case 1: // '\001'
                    k = (int) (Math.random() * 26D) + 65;
                    break;

                default:
                    k = 95;
                    break;
            }
            sb.append((char) k);
        }

        return sb.toString();
    }

}
