package com.zx.app.factory.net;

import android.text.format.DateFormat;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSS;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.zx.app.common.app.Application;
import com.zx.app.utils.HashUtil;

import java.io.File;
import java.util.Date;

/**
 * author Afton
 * date 2020/2/13
 * 上传工具类，用于上传任意文件到阿里OSS存储
 */
public class UploadHelper {

    private static final String TAG = UploadHelper.class.getName();
    // 与你们的存储区域有关系
    public static final String ENDPOINT = "http://oss-cn-beijing.aliyuncs.com";
    // 上传的仓库名
    private static final String BUCKET_NAME = "ztalker";

    /**
     * OSS Client
     *
     * @return
     */
    private static OSS getClient() {
        // 明文设置secret的方式建议只在测试时使用，更多鉴权模式请参考后面的`访问控制`章节
        // https://help.aliyun.com/document_detail/32046.html?spm=a2c4g.11186623.6.900.2ff87ca2WzupMX
        OSSCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider("LTAI4FqYjjBuf5vRgaZtGqbU", "7Af2jNCgn6VXBgcbbzsPQZ1wXFl7LF");
        return new OSSClient(Application.getInstance(), ENDPOINT, credentialProvider);
    }

    /**
     * 上传的最终方法，成功返回则一个路径
     *
     * @param objKey 上传上去后，在服务器上的独立的KEY
     * @param path   需要上传的文件的路径
     * @return 存储地址
     */
    private static String upload(String objKey, String path) {
        /*构造一个上传请求*/
        PutObjectRequest putObjectRequest = new PutObjectRequest(BUCKET_NAME, objKey, path);
        OSS client = getClient();
        /*同步上传*/
        try {
            PutObjectResult result = client.putObject(putObjectRequest);
            /*得到一个可访问的地址*/
            String url = client.presignPublicObjectURL(BUCKET_NAME, objKey);
            return url;
        } catch (Exception e) {
            e.printStackTrace();
            /*异常就返回空*/
            return null;
        }
    }

    /**
     * 上传普通图片
     *
     * @param path
     * @return
     */
    public static String uploadImage(String path) {
        String key = getObjKey(path, "image");
        return upload(key, path);
    }

    /**
     * 上传头像
     *
     * @param path
     * @return
     */
    public static String uploadPortrait(String path) {
        String key = getObjKey(path, "portrait");
        return upload(key, path);
    }

    public static String uploadAudio(String path) {
        String key = getObjKey(path, "audio");
        return upload(key, path);
    }

    /**
     * 分月存储，避免一个文件夹太多
     *
     * @return yyyyMM
     */
    private static String getDateString() {
        return DateFormat.format("yyyyMM", new Date()).toString();
    }

    /**
     * ObjKey
     *
     * @param path 文件路径
     * @param dir  文件夹
     * @return
     */
    private static String getObjKey(String path, String dir) {
        String fileMd5 = HashUtil.getMD5String(new File(path));
        String dateString = getDateString();
        return String.format(dir + "/%s/%s.jpg", dateString, fileMd5);
    }
}
