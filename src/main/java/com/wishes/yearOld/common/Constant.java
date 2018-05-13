package com.wishes.yearOld.common;

/**
 * Created by IntelliJ IDEA.
 * User:    lyra
 * Company: Yesky.com Inc
 * Date:    2016/9/29
 * Description:
 */
public class Constant {
    public static final boolean DEBUG_MODE = true;

    public static final int USER_GROUP_USER = 0;
    public static final int USER_GROUP_MODEL = 1;
    public static final int USER_GROUP_PHOTOGRAPHER= 2;

    public static final int USER_CERTIFY_PROCESSING = 0;
    public static final int USER_CERTIFY_PASS = 1;
    public static final int USER_CERTIFY_REFUSED = 2;

    public static final String UPLOAD_IMAGE_DIR = "/home/siteupload/image/";
    public static final String UPLOAD_VIDEO_DIR = "/home/siteupload/upload/";
    /**
     * 图片上传目录(缩略图)
     */
    public static final String IMAGE_SAVE_PATH = "upload";
    /**
     * 图片上传目录(水印大图)
     */
    public static final String BIG_IMAGE_SAVE_PATH="upload_big";
    /**
     * 图片上传目录(水印大图)
     */
    public static final String OLD_IMAGE_SAVE_PATH="upload_x";
    
    //图片上传目录(原图)
    public static final String ORIGINAL_IMAGE_SAVE_PATH = "upload_original";//用户自定义上传原图路径
    
    public static final String FILE_NAME_FIELD = "name";
    
    /**
     * 获取保存图片路径对应的KEY
     */
    public static final String IMAGE_NAME_PATH = "namePath";
    
    /**
     * 变量图集控制 
     */
    public static final int STREAM_CONTROL_LEVELIOS=0;//ios版本级别
    public static final int STREAM_CONTROL_LEVELANDR=0;//安卓版本级别
    public static final int STREAM_CONTROL_LEVELWX=1;//小程序版本
    
    /**
     * 压缩包中图片分辨率
     */
    public static final String IMAGE_NAME_SUFFIX_RAR = "_2400x3600";
    
    public static final int PHOTO_PAGE_FREE = 5;
    public static final int PHOTO_PAGE_1 = 15;
    public static final int PHOTO_PAGE_ALL = -1;

    public static final int MESSAGE_TYPE_SYSTEM = 1;
    public static final int MESSAGE_TYPE_REFER = 2;
    public static final int MESSAGE_TYPE_COMMENT = 3;

    /**
     * 登录类型： 1-QQ、2-微信、3-微博、4-手机、5-邮箱
     */
    public static final int LOGIN_TYPE_QQ = 1;
    public static final int LOGIN_TYPE_WECHAT = 2;
    public static final int LOGIN_TYPE_WEIBO = 3;
    public static final int LOGIN_TYPE_MOBILE = 4;
    public static final int LOGIN_TYPE_EMAIL = 5;

    /**
     * tgod_user_like
     */
    public static final int DEF_COMMENT_TOID = -1;

    /**
     * tgod_user_like
     */
    public static final int LIKE_TYPE_GOD = 1;
    public static final int LIKE_TYPE_ALBUM = 2;
    public static final int LIKE_TYPE_COMMENT = 3;

    /**
     * 审核中
     */
    public static final byte ALBUM_VERIFYING = 1;

    /**
     * 已审核，待上架
     */
    public static final byte ALBUM_VERIFIED = 2;

    /**
     * 销售中(审核通过，已上架)
     */
    public static final byte ALBUM_SALE = 3;

    /**
     * 审核未通过
     */
    public static final byte ALBUM_VERIFYFAILED = 4;

    /**
     * 审核通过，已下架
     */
    public static final byte ALBUM_UNSALE = 5;

    /**
     *  评论status默认正常状态
     */
    public static final byte DEFAULT_COMMENT_STATUS = 0;

    /**
     *  评论有"@"对象
     */
    public static final byte REFER_USER_YES = 1;

    /**
     *  评论无"@"对象
     */
    public static final byte REFER_USER_NO = 0;

    /**
     *  "@"来源:评论
     */
    public static final int REFER_FROM_COM = 1;

    /**
     *  "@"来源:个人动态
     */
    public static final int REFER_FROM_LINE = 2;

    /**
     *  commentType为图集
     */
    public static final byte ALBUM_COMMENT_TYPE = 1;

    /**
     *  commentType为个人动态
     */
    public static final byte TIMELINE_COMMENT_TYPE = 2;

    // TODO: 需要设置
     public static final String IMAGE_BASE="http://image.qingdouke.com/";
     // public static final String IMAGE_BASE="http://192.168.2.196:8080/tgod_app_api/home";

    // 一次发送短信验证码的有效时间，默认5分钟
    public static final long SECURE_CODE_ALIVE = 5 * 60 * 1000L;
    public static final int SECURE_CODE_REG = 1;
    public static final int SECURE_CODE_LOGIN = 2;
    public static final int SECURE_CODE_CHMOBILE = 3;
    public static final int SECURE_CODE_CHPWD = 4;
    public static final int SECURE_CODE_CASHOUT = 5;

    public static final int DEF_PAGE_SIZE = 10;
    public static final int DEF_PAGE_NUM = 1;

    public static final byte REG_BY_MOBILE = 0;
    public static final byte REG_BY_ACCOUNT = 4;

    public static final byte GOODS_TYPE_ALBUM = 1;

    public static final byte GOODS_TYPE_ACTIVITY = 2;

    /**
     * 订单状态（1：初始状态，等待买家付款(未支付) 2：支付中  3：交易支付完成 4：交易结束，不可退款(关闭) 5：未付款交易超时关闭(或支付失败) 6：退款中 7.退款成功 8.退款失败）
     */
    public static final byte ORDER_UNPAY = 1;
    public static final byte ORDER_PAYING = 2;
    public static final byte ORDER_FINISHED = 3;
    public static final byte ORDER_CLOSED = 4;
    public static final byte ORDER_FAILED = 5;
    public static final byte ORDER_REFUNDING = 6;
    public static final byte ORDER_REFUND_SUCCESS = 7;
    public static final byte ORDER_REFUND_FAIL = 8;

    /**
     * 支付类型
     */
    public static final byte TRADE_TYPE_ALIPAY = 1;
    public static final byte TRADE_TYPE_WECHAT = 2;

    /**
     * 下载权限状态
     */
    public static final byte DOWN_LIMIT_UNUSED = 1;
    public static final byte DOWN_LIMIT_USED = 2;

    /**
     * 青豆明细类型
     */
    public static final byte QD_INCOME_INVITE = 1;
    public static final byte QD_INCOME_SHARE = 2;
    public static final byte QD_PAY_ALBUM = 3;
    public static final byte QD_SYSTEM_PROCESS = 4;


    /**
     * 用户交易类型
     */
    public static final int USER_RECORD_ALBUM_IN = 1;
    public static final int USER_RECORD_QINGDOU = 2;
    public static final int USER_RECORD_ALBUM_OUT = 4;
    public static final int USER_RECORD_ACTIVITY_OUT = 5;
    public static final int USER_RECORD_ACTIVITY_IN = 3;

    /**
     * 用户转账申请状态
     */
    public static final byte USER_RECORD_CASHOUT_INIT = 1;
    public static final byte USER_RECORD_CASHOUT_SUCCESS = 2;
    public static final byte USER_RECORD_CASHOUT_FAIL = 3;
    public static final byte USER_RECORD_CASHOUT_INHAND = 4;

    /**
     * 用户转账是否成功状态
     */
   public static final byte USER_RECORD_CASHOUT_USER_INIT = 1;
   public static final byte USER_RECORD_CASHOUT_USER_SUCCESS = 2;
   public static final byte USER_RECORD_CASHOUT_USER_FAIL = 3;

    public static final int FOCUS_USER = 0;
    public static final int FOCUS_ALBUM = 4;


    /**
     * 用户提现状态
     * 支付状态(0:未支付,1:待支付,2:支付中,3:支付成功,4:支付失败)
     */
    public static final byte CASHOUT_UNPAY = 0;
    public static final byte CASHOUT_INIT = 1;
    public static final byte CASHOUT_INHAND= 2;
    public static final byte CASHOUT_SUCCESS = 3;
    public static final byte CASHOUT_FAIL = 4;



}
