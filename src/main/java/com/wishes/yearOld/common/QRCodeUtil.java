package com.wishes.yearOld.common;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Hashtable;

/**
 * Created by tmg-yesky on 2017/1/14.
 * 二维码
 */
public class QRCodeUtil {

    private static String DEFAULT_CHARSET = "UTF-8";
    private static int DEFAULT_WIDTH = 200;
    private static int DEFAULT_HEIGHT = 200;

    public static boolean createQrCode(String text, String path){
        return createQrCode(text, DEFAULT_WIDTH, DEFAULT_HEIGHT, path);
    }

    public static void createQrCode(String text, HttpServletResponse resp){
        createQrCode(text, DEFAULT_WIDTH, DEFAULT_HEIGHT, resp);
    }

    public static boolean createQrCode(String text, int width, int height, String path){
        try {
            return createQrCode(text, width, height, QRCodeImgType.PNG, DEFAULT_CHARSET, path);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void createQrCode(String text, int width, int height, HttpServletResponse resp){
        try {
            createQrCode(text, width, height, QRCodeImgType.PNG, DEFAULT_CHARSET, resp);
        } catch (WriterException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static boolean createQrCode(String text, int width, int height, QRCodeImgType format, String charset, String path) throws WriterException, IOException{
        Hashtable hints= new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, charset);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        File outputFile = new File(path);
        File parent = outputFile.getParentFile();
        if(!parent.exists()) parent.mkdirs();
        MatrixToImageWriter.writeToPath(bitMatrix, format.name(), outputFile.toPath());
        return outputFile.exists();
    }

    private static void createQrCode(String text, int width, int height, QRCodeImgType format, String charset, HttpServletResponse resp) throws WriterException, IOException{
        Hashtable hints= new Hashtable();
        hints.put(EncodeHintType.CHARACTER_SET, charset);
        BitMatrix bitMatrix = new MultiFormatWriter().encode(text, BarcodeFormat.QR_CODE, width, height, hints);
        MatrixToImageWriter.writeToStream(bitMatrix, format.name(), resp.getOutputStream());
    }

    public static void main(String[] args) throws WriterException, IOException {
        String text = "weixin://wxpay/bizpayurl?pr=g4NIZJA";
        String path = "E:\\home\\tgod_img\\qrcode.png";
        boolean result = createQrCode(text, path);
        System.out.println("result:"+result);
    }
}

enum QRCodeImgType {
    JPG,
    PNG,
    BMP
}