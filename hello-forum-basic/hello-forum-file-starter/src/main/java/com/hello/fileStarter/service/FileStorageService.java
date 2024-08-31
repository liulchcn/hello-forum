package com.hello.fileStarter.service;

import io.minio.errors.*;

import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

/**
 * @author itheima
 */
public interface FileStorageService {


    /**
     *  上传图片文件
     * @param prefix  文件前缀
     * @param filename  文件名
     * @param inputStream 文件流
     * @return  文件全路径
     */
    public String uploadImgFile(String prefix, String filename,InputStream inputStream);

    /**
     *  上传html文件
     * @param prefix  文件前缀
     * @param filename   文件名
     * @param inputStream  文件流
     * @return  文件全路径
     */
    public String uploadHtmlFile(String prefix, String filename,InputStream inputStream);


    /**
     *  上传视频文件
     * @param prefix  文件前缀
     * @param filename  文件名
     * @param inputStream 文件流
     * @return  文件全路径
     */
    public  String uploadVideoFile(String prefix, String filename, InputStream inputStream);

    /**
     * 上传视频文件，用于分片上传。
     * @param fileName
     * @param stream
     * @param contentType
     * @return
     */
    Boolean uploadVideoPartFile(String fileName, InputStream stream, String contentType);

    /**
     * 删除文件
     * @param pathUrl  文件全路径
     */
    public void delete(String pathUrl);

    /**
     * 下载文件
     * @param pathUrl  文件全路径
     * @return
     *
     */
    public byte[]  downLoadFile(String pathUrl);

    /**
     * 合并分片文件为完整的视频文件
     *
     * @param resumableIdentifier 分片上传的唯一标识符，用于识别属于同一文件的所有分片
     * @param videoName           合并后的完整视频文件名
     * @return
     */
    String composePart(String resumableIdentifier, String videoName) throws ServerException, InsufficientDataException, ErrorResponseException, IOException, NoSuchAlgorithmException, InvalidKeyException, InvalidResponseException, XmlParserException, InternalException;

}
