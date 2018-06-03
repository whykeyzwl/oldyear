package com.wishes.yearOld.common;

import magick.ImageInfo;
import magick.MagickException;
import magick.MagickImage;

import org.apache.log4j.Logger;
import org.springframework.util.ResourceUtils;


import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriteParam;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 * 图片加水印，自动缩放等类，该类需要ImageMagick和JImageMaigick配合使用<br>
 * 该类CMS专用 <br>
 * 使用该类注意事项 <br>
 * 特别注意： <br>
 * 1、服务器上安装ImageMagick <br>
 * 下载地址：Linux: ftp://ftp.imagemagick.org/pub/ImageMagick/ImageMagick.tar.gz <br>
 * Windows：ftp://ftp.imagemagick.org/pub/ImageMagick/binaries/ImageMagick-6.2.6-8-Q16-windows-dll.exe
 * <br>
 * 2、服务器上安装：JMagick (PATH=/home5/jdk1.5.0_07/bin:$PATH) <br>
 * 下载地址：Linux：http://www.yeo.id.au/jmagick/quickload/JMagick-6.2.6-0.tar.gz <br>
 * Windows：http://www.yeo.id.au/jmagick/quickload/win-6.2.6/jmagick-6.2.6-win.zip
 * <br>
 * winodws 下使用6.3.9版本。 3、配置环境变量 <br>
 * 修改myconfig.sh <br>
 * 把 JAVA_OPTS="-server";export JAVA_OPTS <br>
 * 换成 JAVA_OPTS="-Djava.awt.headless=true -Djava.library.path=/usr/local/lib
 * -Djmagick.systemclassloader=no -server";export JAVA_OPTS <br>
 *
 * @author Yangsy <br>
 * @author garmbrood Date: 2006-4-25 10:14:37 <br>
 */
public class ImageUtils {
	private static final Logger logger = Logger.getLogger(ImageUtils.class);
    public static final int POS_TYPE_LEFT_TOP = 1;
    public static final int POS_TYPE_RIGHT_TOP = 2;
    public static final int POS_TYPE_LEFT_BOTTOM = 3;
    public static final int POS_TYPE_RIGHT_BOTTOM = 4;
    public static final int POS_TYPE_CENTER = 5;

    public static final int POS_TYPE_BOTTOM_CENTER = 6;//最下方居中

    public static final String WATER_MARK = "classpath:qdk.png";
    //横图水印  3600*2400
    public static final String WATER_MARK_ACROSS = "classpath:across.png";
    //竖图水印  2400*3600
    public static final String WATER_MARK_VERTICAL = "classpath:vertical.png";

    public static final Map<String, MagickImage> logoCache = new HashMap<String, MagickImage>();

    static {
        System.setProperty("jmagick.systemclassloader", "no");
    }

    /**
     * 获得宽高
     *
     * @param picFrom 图片地址
     * @return 返回宽
     */
    public static String getWidthHeight(String picFrom) throws MagickException {
        try {
            String fileType = ImageTypeCheck.getImageType(new File(picFrom));
            if("".equals(fileType)){
                logger.info("couldn't get image type");
                return "";
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        MagickImage image = new MagickImage(new ImageInfo(picFrom));
        logger.info(System.getProperty("java.library.path"));
        Dimension dimension = image.getDimension();
        int iWi = (int) dimension.getWidth();
        int iHi = (int) dimension.getHeight();
        logger.info("getWidthHeight====="+iWi+"getWidthHeight====="+iHi);
        return iWi + "," + iHi;
    }

    /**
     * 缩放图片
     *
     * @param picFrom 待添加水印的图片地址
     * @param picTo   缩放后的图片地址
     * @param width   缩放后的宽度
     * @param height  缩放后的高度
     * @param scale   是等比缩放，如果是等比缩放，则以最 width和 height表示最长的宽度和高度
     * @param flag  横竖图判断 参数是0的时候是横图参数是1的时候是竖图
     * @return 是否缩放成功
     */
    public static boolean resize(String picFrom, String picTo, int width,
                                 int height, boolean scale,int flag) {

        try {
            try {
                String fileType = ImageTypeCheck.getImageType(new File(picFrom));
                if("".equals(fileType)){
                    logger.info("couldn't get image type");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            long starttime = System.currentTimeMillis();
            width = width == 0 ? 500 : width;
            // Resize
            ImageInfo info = new ImageInfo(picFrom);
            MagickImage image = new MagickImage(new ImageInfo(picFrom));
            MagickImage scaled = null;// 小图片文件的大小.

            Dimension dimension = image.getDimension();
            int iWi = (int) dimension.getWidth();
            int iHi = (int) dimension.getHeight();
            logger.info("resizewidth====="+width+"resizeheight====="+height);
            logger.info("resizeiWi====="+iWi+"resizeiHi====="+iHi);
            if ((iWi <= width && width > 0) && (iHi <= height && height > 0)) {
                File srcFile = new File(picFrom);
                File destFile = new File(picTo);
                try {
                    FileUtils.copyFile(srcFile, destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            if (!scale) {
                scaled = image.scaleImage(width, height);
                logger.info("resize1====="+width+"resize1====="+height);
            } else {
                int toWidth = 0;
                int toHeight = 0;
                if (width < 1) {
                    toWidth = (int) (((float) height / (float) iHi) * iWi);
                    toHeight = height;
                    logger.info("resize2====="+toWidth+"resize2====="+toHeight);
                } else if (height < 1) {
                    toWidth = width;
                    toHeight = (int) (((float) width / (float) iWi) * iHi);
                    logger.info("resize3====="+toWidth+"resize3====="+toHeight);
                } 
                
                //竖图
                if(flag==1) 
                {
                	toWidth = height;
                    toHeight = width;
                }else{
                	toHeight = height;
                	toWidth = width;//(int) (((float) height / (float) iHi) * iWi);

                }
                //image.profileImage("*", null);// 移除图片的其他信息
                scaled = image.scaleImage(toWidth, toHeight);
                
                
            }

            scaled.setFileName(picTo);
            info.setQuality(80);
            scaled.writeImage(info);
            logger.info(picFrom+" resize cost "+(System.currentTimeMillis()-starttime)+" ms");
        } catch (MagickException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 缩放图片
     *
     * @param picFrom 待添加水印的图片地址
     * @param picTo   缩放后的图片地址
     * @param width   缩放后的宽度
     * @param height  缩放后的高度
     * @param scale   是等比缩放，如果是等比缩放，则以最 width和 height表示最长的宽度和高度
     * @return 是否缩放成功
     */
    public static boolean resizeAll(String picFrom, String picTo, int width,
                                    int height, boolean scale) {

        try {
            try {
                String fileType = ImageTypeCheck.getImageType(new File(picFrom));
                if("".equals(fileType)){
                    logger.info("couldn't get image type");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            // Resize
            ImageInfo info = new ImageInfo(picFrom);
            MagickImage image = new MagickImage(new ImageInfo(picFrom));
            MagickImage scaled = null;// 小图片文件的大小.
            Dimension dimension = image.getDimension();
            int iWi = (int) dimension.getWidth();
            int iHi = (int) dimension.getHeight();
            logger.info("resizeAll1====="+iWi+"resizeAll1====="+iHi);
            if ((iWi <= width && width > 0) && (iHi <= height && height > 0)) {
                File srcFile = new File(picFrom);
                File destFile = new File(picTo);
                try {
                    FileUtils.copyFile(srcFile, destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            if (!scale) {
                scaled = image.scaleImage(width, height);
                logger.info("resizeAll2====="+width+"resizeAll2====="+width);
            } else {
                int toWidth = 0;
                int toHeight = 0;
                if(width<1){
                    if(iHi<height){
                        toWidth = iWi;
                        toHeight = iHi;
                        logger.info("resizeAll2====="+toWidth+"resizeAll2====="+toHeight);
                    }else {
                        toWidth =  (int) (((float) iWi / (float) iHi) * height);
                        toHeight = height;
                        logger.info("resizeAll3====="+toWidth+"resizeAll3====="+toHeight);
                    }
                } else if(height<1){
                    if(iWi<width){
                        toWidth = iWi;
                        toHeight = iHi;
                        logger.info("resizeAll4====="+toWidth+"resizeAll4====="+toHeight);
                    }else {
                        toWidth = width;
                        toHeight = (int) (((float) width / (float) iWi) * iHi);
                        logger.info("resizeAll5====="+toWidth+"resizeAll5====="+toHeight);
                    }
                }  else if(iWi >= iHi){
                    if(iWi>=width){
                        toWidth = width;
                        toHeight = (int) (((float) width / (float) iWi) * iHi);
                        logger.info("resizeAll6====="+toWidth+"resizeAll6====="+toHeight);
                    }else {
                        toWidth = (int) (((float) iWi / (float) iHi) * height);
                        toHeight = height;
                        logger.info("resizeAll7====="+toWidth+"resizeAll7====="+toHeight);
                    }
                }  else if(iWi < iHi){
                    if(iHi>=height){
                        toWidth = (int) (((float) iWi / (float) iHi) * height);
                        toHeight = height;
                        logger.info("resizeAll8====="+toWidth+"resizeAll8====="+toHeight);
                    } else {
                        toWidth = width;
                        toHeight = (int) (((float) width / (float) iWi) * iHi);
                        logger.info("resizeAll9====="+toWidth+"resizeAll9====="+toHeight);
                    }
                }
                
                image.profileImage("*", null);// 移除图片的其他信息
                scaled = image.scaleImage(toWidth, toHeight);
            }

            scaled.setFileName(picTo);
            info.setQuality(90);
            scaled.writeImage(info);
        } catch (MagickException e) {
            e.printStackTrace();
        }
        return true;
    }

    /**
     * 加水印。建议水印文件使用透明png格式
     *
     * @param sourcePath 待加水印的图片
     * @param watermark  水印文件
     * @param targetPath 添加水印后的文件，即新生成的文件
     * @param x          边距
     * @param y          边距
     * @param align      位置,定义参加 ImageUtils.POS_TYPE*
     * @return 是否添加成功
     */

    public  static boolean generateMark(String sourcePath,String watermark,
                                        String targetPath,int x, int y, int align,String extName){
        ImageWriter writer = null;
        Iterator<ImageWriter> iter = null;
        ImageOutputStream outputStream = null;
        try {
            //目标文件
            File _file = new File(sourcePath);
            
            BufferedImage src = ImageIO.read(_file);
            int width = src.getWidth();
            int height = src.getHeight();
            Graphics g = src.createGraphics();
            
            //水印文件
            File waterM = null;
            if(watermark.startsWith("classpath")){
                waterM = ResourceUtils.getFile(watermark);
            }else {
                waterM = new File(watermark);
            }
            BufferedImage warterImg = ImageIO.read(waterM);
            int warterWidth = warterImg.getWidth();
            int warterHeight = warterImg.getHeight();
            //大图宽度如果小于5倍缩略图宽度，则缩小缩略图大小
            if (warterWidth * 4 > width) {
                int _warterWidth = warterWidth * width / (warterWidth * 4);
                int _warterHeight = warterHeight * _warterWidth / warterWidth;
                warterWidth = _warterWidth;
                warterHeight = _warterHeight;
            }
            int[] position = getPosition(width, height, warterWidth, warterHeight, x, y, align);
            g.drawImage(warterImg, position[0], position[1], warterWidth, warterHeight, null);
            g.dispose();

            if(extName.toLowerCase().indexOf("jpg") != -1 || extName.toLowerCase().indexOf("jpeg") != -1){
                iter = ImageIO.getImageWritersByFormatName("JPEG");
            }
            else if(extName.toLowerCase().indexOf("png") != -1 ){
                iter = ImageIO.getImageWritersByFormatName("PNG");
            }
            if(iter == null)
            {
                throw new NullPointerException("图像扩展名无法解析");
            }
            if (iter.hasNext()) {
                writer = iter.next();
            }
            IIOImage iioImage = new IIOImage(src, null, null);
            ImageWriteParam param = writer.getDefaultWriteParam();
            if(param.canWriteCompressed()){
                param.setCompressionMode(ImageWriteParam.MODE_EXPLICIT);
                param.setCompressionQuality(1.0f);
            }
            File targetFile = new File(targetPath);
            if(!targetFile.getParentFile().exists()){
                targetFile.getParentFile().mkdirs();
            }
            outputStream = ImageIO.createImageOutputStream(targetFile);
            writer.setOutput(outputStream);
            writer.write(iioImage.getMetadata(), iioImage, param);
            logger.info("watermark=" + watermark + " sourcfile=" + sourcePath);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }finally {
            try {
                if(outputStream != null)
                    outputStream.close();
                if(writer != null)
                    writer.dispose();
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }

    /**
     * 返回水印打印的坐标
     *
     * @param width   图片宽度
     * @param height  图片高度
     * @param wwidth  水印宽度
     * @param wheight 水印高度
     * @param marginX 横向边距
     * @param marginY 纵向边距
     * @param align   浮动位置
     * @return [x坐标，y坐标]
     */
    private static int[] getPosition(int width, int height, int wwidth, int wheight, int marginX, int marginY, int align) {
        int[] position = new int[2];

        switch (align) {
            case POS_TYPE_LEFT_TOP://左上
                position[0] = marginX;
                position[1] = marginY;
                break;
            case POS_TYPE_LEFT_BOTTOM://左下
                position[0] = marginX;
                position[1] = height - (marginY + wheight);
                break;
            case POS_TYPE_RIGHT_TOP://右上
                position[0] = width - (marginX + wwidth);
                position[1] = marginY;
                break;

            case POS_TYPE_CENTER://居中
                position[0] = (width + wwidth) / 2;
                position[1] = (height + wheight) / 2;
                break;

            case POS_TYPE_BOTTOM_CENTER://下部居中
                position[0] = (width - wwidth) / 2;
                position[1] = height - (marginY + wheight);
                break;

            default://POS_TYPE_RIGHT_BOTTOM 右下
                position[0] = width - (marginX + wwidth);
                position[1] = height - (marginY + wheight);
                break;
        }
        return position;
    }

    public static boolean resizeForSoft(String picFrom, String picTo, int width,
                                        int height, boolean scale) {

        try {
            try {
                String fileType = ImageTypeCheck.getImageType(new File(picFrom));
                if("".equals(fileType)){
                    logger.info("couldn't get image type");
                    return false;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            width = width == 0 ? 500 : width;
            // Resize
            ImageInfo info = new ImageInfo(picFrom);
            MagickImage image = new MagickImage(new ImageInfo(picFrom));
            MagickImage scaled = null;// 小图片文件的大小.

            Dimension dimension = image.getDimension();
            int iWi = (int) dimension.getWidth();
            int iHi = (int) dimension.getHeight();
            logger.info("resizeForSoft1====="+iWi+"resizeForSoft1====="+iHi);
            if ((iWi <= width && width > 0) && (iHi <= height && height > 0)) {
                File srcFile = new File(picFrom);
                File destFile = new File(picTo);
                try {
                    FileUtils.copyFile(srcFile, destFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return true;
            }

            if (!scale) {
                scaled = image.scaleImage(width, height);
                logger.info("resizeForSoft1====="+width+"resizeForSoft1====="+height);
            } else {
                int toWidth;
                int toHeight;
                if (width < 1) {
                    toWidth = (int) (((float) height / (float) iHi) * iWi);
                    toHeight = height;
                    logger.info("resizeForSoft2====="+toWidth+"resizeForSoft2====="+toHeight);
                } else if (height < 1) {
                    toWidth = width;
                    toHeight = (int) (((float) width / (float) iWi) * iHi);
                    logger.info("resizeForSoft3====="+toWidth+"resizeForSoft3====="+toHeight);
                } else if (((float) width / (float) height) > ((float) iWi / (float) iHi)) {
                    toWidth = (int) (((float) height / (float) iHi) * iWi);
                    toHeight = height;
                    logger.info("resizeForSoft4====="+toWidth+"resizeForSoft4====="+toHeight);
                } else {
                    toWidth = width;
                    toHeight = (int) (((float) width / (float) iWi) * iHi);
                    logger.info("resizeForSoft5====="+toWidth+"resizeForSoft5====="+toHeight);
                }
                image.profileImage("*", null);// 移除图片的其他信息
                scaled = image.scaleImage(toWidth, toHeight);
            }

            scaled.setFileName(picTo);
            info.setQuality(90);
            scaled.writeImage(info);
        } catch (MagickException e) {
            e.printStackTrace();
        }
        return true;
    }

    public static void main(String[] args) throws MagickException, IOException {

        System.setProperty("jmagick.systemclassloader", "no");
        String filepath = "E:\\home\\tgod\\upload\\20160919\\DF341F7FCDA6C1DBCFA.jpg";
        String targetpath = "E:\\home\\tgod\\upload\\20160919\\DF341F7FCDA6C1DBCFA_MARK.jpg";
        String lastName = filepath.substring(filepath.lastIndexOf("."));
        String watermark = "E:\\home\\tgod\\upload\\20160919\\19582148.jpg";
        //ImageUtils.resize(filepath, "E:\\home\\tgod\\upload\\20160919\\DF341F7FCDA6C1DBCFA_100x150.jpg", 100, 150, true);
        ImageUtils.generateMark(filepath,watermark,targetpath,5,5,POS_TYPE_RIGHT_BOTTOM,lastName);

    }
}
