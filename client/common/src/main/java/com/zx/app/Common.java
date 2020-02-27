package com.zx.app;

/**
 * author Afton
 * date 2020/2/25
 * 一些不可变的参数，通常时一些配置
 */
public class Common {
    public interface Constance {
        /*手机号码正则*/
        String REGEX_MOBILE = "[1][3,4,5,7,8][0-9]{9}$";

        /*网络请求地址*/
        String API_URL = "http://172.18.223.77:8081/api/";

        /*最大图片上传大小860k*/
        long MAX_UPLOAD_IMAGE_LENGTH = 860 * 1024;
    }
}
