package com.wishes.yearOld.apppay;

/**
 * Created by tmg-yesky on 2016/10/18.
 * 三方支付配置
 */
public class AppPayConfig {

    /**
     * 微信APPID
     */
    public static final String WECHAT_APPID = "wxff990bae3f71f666";
    /**
     * 小程序appid
     */
    public static final String WECHAT_XCXID = "wxce72954aece9f7d9";

    /**
     * 商户号微信
     */
    public static final String WECHAT_MCHID = "1402451302";
    
    /**
     * 商户号微信小程序
     */
    public static final String WECHAT_MCHIDXCX = "1496805582";

    /**
     * 微信KEY
     */
    public static final String WECHAT_KEY = "EBE142F3F6DB0C29717C770CE6725E5C";

    public static final String WECHAT_INPUT_CHARSET = "UTF-8";

    public static final String WECHAT_SIGN_TYPE = "MD5";

    public static final String WECHAT_RETURN_CODE = "return_code";

    public static final String WECHAT_RESULT_CODE = "result_code";

    public static final String WECHAT_SUCCESS = "SUCCESS";

    public static final String WECHAT_FAIL = "FAIL";

    public static final String WECHAT_CODE_URL = "code_url";

    public static final String WECHAT_PREPAY_ID = "prepay_id";
    /**
     * 微信小程序商户秘钥
     * 
     */
    public static final String WECHATXCX_PREPAY_ID = "64b7d69489aceaa87e2232e995e0af03"; 
    /**
     * 统一下单接口(wechat)
     */
    public static final String WECHAT_ORDER = "https://api.mch.weixin.qq.com/pay/unifiedorder";

    /**
     * 支付结果通知url
     * 通知url必须为直接可访问的url，不能携带参数(wechat)
     * TODO 修改域名
     */
    public static final String WECHAT_NOTIFY_URL = "http://appapi.qingdouke.com/payment/wechat_order_notify.do";
    
    /**
     * 微信wap端支付结果通知url
     * 通知url必须为直接可访问的url，不能携带参数(wechat)
     * TODO 修改域名
     */
    public static final String WECHATWAP_NOTIFY_URL = "http://appapi.qingdouke.com/wap/pay/wechat_order_notify.do";
    
    /**
     * 微信wap端扫码支付结果通知url
     * 通知url必须为直接可访问的url，不能携带参数(wechat)
     * TODO 修改域名
     */
    public static final String WECHATSCANCODE_NOTIFY_URL = "http://appapi.qingdouke.com/wap/scanCode/wechat_order_notify.do";
    /**
     * 微信小程序支付结果通知url
     * 通知url必须为直接可访问的url，不能携带参数(wechat)
     * TODO 修改域名
     */
    public static final String WECHATXCX_NOTIFY_URL = "http://47.95.207.69/payment/wechatApplet_order_notify.do";

    /**
     * 查询订单接口(wechat)
     */
    public static final String WECHAT_ORDER_QUERY = "https://api.mch.weixin.qq.com/pay/orderquery";

    /**
     * 申请退款(wechat)
     */
    public static final String WECHAT_REFUND = "https://api.mch.weixin.qq.com/secapi/pay/refund";

    /**
     * 查询退款(wechat)
     */
    public static final String WECHAT_REFUND_QUERY = "https://api.mch.weixin.qq.com/pay/refundquery";

    public static final String ALIPAY_APP_ID = "2016093002014477";

    public static final String ALIPAY_APP_PRIVATE_KEY =
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAO4pSb6+DAH+DvTS" +
            "fxQ0UpdCpvHFoRYZuv1LzHwJPkEkGcnchUWWsaMa/VAqXDxdHzQkCK6RlaETzS2M" +
            "tq/wO1rjNHV6xiJPyNvw59FV5Dk1jOgKFrRaXe9PoaPiUH6q0g8xBk07zZprTeC2" +
            "cCz84VCsfoy4BQrxHeaSOBq+nuz7AgMBAAECgYEAy6GLx0YxeCak3Rl5xGm0UFaR" +
            "7VjAEehwJBECRu+URhg64IjnfoeJXN8/xaJieM5aVreUoMYjvLT0ksSkkywBYl9v" +
            "a6r2dA7DNBONUQHPXBn0Fi0MDjn5XcnsgmEmfv31aP/RdshWCm9cMWzx/JXmUmCH" +
            "PQjkOsSsLZ+SVZlWBtkCQQD4OcQVpEjKJvNly5gnMitP0EJ6iS39ZJncE5t4ZEyH" +
            "I5UEvO/NO1M1O6g6Xf5z1BzqxiovdFl+fOmNoiQ4yRPnAkEA9Z7T3iTfvlBEyeBG" +
            "zP2HC5+LWH3HwfIjrhYFBoLUiCSEAhIjJsucOK1NRWIhH18rlY/rIO40SsqImkjQ" +
            "DWZ7zQJAE24iJTeI4xoNb2hybo8EyOiaa5EVYoJfcNSwFMp1Gxbz8T0A4GXGa6JR" +
            "1s/IvTRbVx1P7AsFfls0xPtuLmu+/QJBAJzDdi2+Ex+AU6cVAZsr5NlCRsl/3dKn" +
            "HKarXyG8trD9vVMlCFJmeZqi4UJ7UvCW6VZXuJYX8kAZ6/2soz0qP7ECQCG9zZdo" +
            "o0Hvw3oqkgLeaqSFRzCjkgEwLFV2W2zkbCqYqo/bR2DmVfGnH9e74pqNWSgyPJMa" +
            "gUpfo7iSkOr8ueU=";

    public static final String ALIPAY_APP_FORMAT = "JSON";

    public static final String ALIPAY_APP_CHASET = "utf-8";

    public static final String ALIPAY_APP_SIGN_TYPE = "RSA";

    public static final String ALIPAY_APP_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss";

    public static final String ALIPAY_APP_VERSION = "1.0";

    public static final String ALIPAY_APP_NOTIFY_URL = "http://appapi.qingdouke.com/payment/post_payment_notify";

    public static final String ALIPAY_APP_PUBLIC_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDuKUm+vgwB/g700n8UNFKXQqbx" +
            "xaEWGbr9S8x8CT5BJBnJ3IVFlrGjGv1QKlw8XR80JAiukZWhE80tjLav8Dta4zR1" +
            "esYiT8jb8OfRVeQ5NYzoCha0Wl3vT6Gj4lB+qtIPMQZNO82aa03gtnAs/OFQrH6M" +
            "uAUK8R3mkjgavp7s+wIDAQAB";
    public static final String ALIPAY_APP_ALI_PUBLIC_KEY =
            "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIfCOaTXy" +
            "iUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJB" +
            "VHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5jVmRGi60j8Ue1efIlzPXV9j" +
            "e9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";

    public static final String ALIPAY_APP_PRODUCT_CODE = "QUICK_MSECURITY_PAY";

    public static final String ALIPAY_APP_PAY_METHOD = "alipay.trade.app.pay";

    public static final String ALIPAY_APP_REFUND_METHOD = "https://openapi.alipay.com/gateway.do";

    public static final String ALIPAY_APP_PID = "2088421715258605";

    public static final String ALIPAY_APP_BATCH_TRANS_SERVICE = "batch_trans_notify";

    public static final String ALIPAY_APP_BATCH_QUERY_SERVICE = "btn_status_query";
    public static final String ALIPAY_APP_BATCH_NOTIFY_VERIFY = "notify_verify";

    public static final String ALIPAY_APP_BATCH_NOTIFY_URL = "http://appapi.qingdouke.com/payment/post_payment_notify_trans";

    public static final String ALIPAY_APP_ACCOUNT = "qingdouke@qq.com";

    public static final String ALIPAY_APP_ACCOUNT_NAME = "初开（重庆）科技有限公司";

    public static final String ALIPAY_APP_ALIPAY_GATEWAY_NEW = "https://mapi.alipay.com/gateway.do";

    public static final String ALIPAY_APP_BATCH_TRANS_PRIVATE_KEY =
            "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAMSzTxg2EMn2jFL1" +
            "y3t+4IkNEP1KWAGqrJZ/r4ntQmxVUACBGzKCi6ww8+NoPy4acltY8f7xnUE/BuiD" +
            "sDKGmsPgq+il8hFt4vurR3G/RToRp+nyhuI3ouRJ7+g6u4W2Fe8bS1mUEuD4unL3" +
            "Z/vEP9uVTA9kFe1n7Loc7AF1hY9NAgMBAAECgYEAmAQf141JMly5nA/b36hxrVmM" +
            "zMQc+cTGt1Uc+9KCaCkSuCEPPN0JRt1aT2Csm1vTD2fRNn/SUa99Dktf69x9VBLB" +
            "/DT8E7s6L0KIVnOCGjq7r1YH/uHurRA/g4ndn24qMTDpHsC3eWdMH4cxBE7qmJSy" +
            "vmtITxnxOxwmVIUnsd0CQQDyBBfD4oZi+Ls6FZleUfOVdPw9OMUsQCP9+neIKX2O" +
            "45OiPHsUWHgY8aNdTHd40tDY2lNl7DVPmu6qGDQ3ueNLAkEA0BDoNttaV44knoEb" +
            "urLIV+3VqnjNpTahodCYQuxRJlWVqy0GfXkGgbbk3T4D+qRdZOkfo9uD/jeJcPhc" +
            "oIWgxwJAJKiYDPTYFE/G1GKHK7FQKzhgKMnbm/tPOF5jEr3uJeG08CMnJYrNDkIz" +
            "06MMicGtHtvbPBoObTNzqVInPrWqhwJAFz+8YtzR1HS6nMd6bLsrbb/SnzDOccaP" +
            "+v/O87NqQelQRwdJ8V1Dn9zq59nVIf2ZvfvaoDmYHyDfh7MIzpr/4wJBAMEiVjZA" +
            "iWiqOcyEHBrtBislyfwowkAaNJ8AinbbwcWvkG6XAdoReXNqUJJMkhjWXJP5f1CL" +
            "UNm2rVQmCBKMPME=";
    
    
    //wap支付宝应用支付service
    public static final String ALIPAY_WAP_PAY_SERVICE = "alipay.trade.wap.pay";

    //wap支付宝应用id
    public static final String ALIPAY_WAP_APPID = "2016111902994384";

    //wap支付宝私钥
    public static final String ALIPAY_WAP_PRIVATE_KEY =
            "MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAObfjbeJmjggBGm0" +
            "Wt9rN5XEN+OpdCEcYdTUXJjKNcVxKVviNaQQ6PptA792ZLRtuVVx+kgFxhL6CgwZ" +
            "XtzdY3jYhZld0Uo9ilpqEgAL5CBoterWu/oSq+Qfv69xD4TntrZ4I3GKQnyRIPod" +
            "Oxk3zYpLbbaSOrb6FDe20u1+7I9xAgMBAAECgYANBvxGKPuMwzG8jTK7fJ9MUiAd" +
            "NoiY/bL/gWQgMvjJxVLiHmeMdG/RAam+ef3AuJhGnAHVYo4EsBs1B5F0wQFFyu/L" +
            "rAnaNLkfjhpW5JmMzmhI0bE1tY4kLfZ4/7aPdcVijXqagtJG5NJy0+088fxdawey" +
            "lcUcKW6YNZbGz1RbCQJBAPmmkikNwNDE6aZX8fitdExejwTwxZiIZacq8lu7X5JJ" +
            "B+tck4wj0p4dFKQqbPa9PN1A/JwXes9GBDGmPVWbEhcCQQDsvrn18GD06s24YYOx" +
            "/R3sFYIsM6PhBQ18aZzzBdarWXH8bbQ4NzCWkvFamdHUJhS02Nm0QUi6myMIFUMN" +
            "jAe3AkBRqP+0OBBy+YYn6Cp5GlupQ2Dqz8sCitMdbDnccZzgX80cWqrUsz8IgyCm" +
            "Vk5QIzROdxxp/nAobpGen+7+Gnw/AkBpd55InknTUjdLiMWuqWCHKB3Rqz+6oxGa" +
            "90HgUh2MQFCzb/v1f0C6E8RJ5HTB2iUnW+PAlcTcvPgiRbaVdmiJAkEA7Ka76igg" +
            "9bikII34EKPQgwYAU5laXcfOUtDMXb4Y5HUtdkCxPbLQJdDffL2vxzkZigqDbBlm" +
            "8CwOhdyHzJgxUQ==";

    //wap支付宝公钥
    public static final String ALIPAY_WAP_ALI_PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDDI6d306Q8fIf" +
            "COaTXyiUeJHkrIvYISRcc73s3vF1ZT7XN8RNPwJxo8pWaJMmvyTn9N4HQ632qJBVHf8sxHi/fEsraprwCtzvzQETrNRwVxLO5j" +
            "VmRGi60j8Ue1efIlzPXV9je9mkjzOmdssymZkh2QhUrCmZYI/FCEa3/cNMW0QIDAQAB";


    //wap支付宝支付异步通知地址
    public static final String ALIPAY_WAP_NOTIFY_URL = "http://appapi.qingdouke.com/wap/pay/alipay_payment_notify";

    public static final String ALIPAY_APP_ALIPAY_GATEWAY = "https://openapi.alipay.com/gateway.do";
    
    public static final String ALIPAY_WAP_PRODUCT_CODE = "QUICK_WAP_PAY";
    /**
     * 支付宝wap返回页面类型
     * pageType("1":qdk-xiangqing.html,"2":qdk-pic.html)
     */
    public static final String PAGE_XQ = "1";
    public static final String PAGE_PIC = "2";
    /**
     * 微信APPID(公众号)
     */
    public static final String WECHAT_PUBLIC_APPID = "wxcb0f747f6e77fe7f";

    /**
     * 商户号(公众号)
     */
    public static final String WECHAT_PUBLIC_MCHID = "1389589202";
    
    /**
     * 支付秘钥(公众号)
     */
    public static final String WECHAT_PUBLIC_KEY = "d06a8672e932700d7556ad17ad65b3da";
    
}
