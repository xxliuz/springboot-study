package com.zhou.easyexcel.util;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: zhou.liu
 * @Date: 2022/4/28 15:14
 * @Description: 数据转换工具
 */
public class DataConvertUtil {

    /**
     * 将inputStream转byte[]
     * @param inputStream
     * @return
     * @throws IOException
     */
    public static byte[] inputStreamTobyte2(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] buff = new byte[100];
        int rc = 0;
        while ((rc = inputStream.read(buff, 0, 100)) > 0) {
            byteArrayOutputStream.write(buff, 0, rc);
        }
        return byteArrayOutputStream.toByteArray();
    }

    /**
     * 将byte[]转inputStream
     * @param bytes
     * @return
     */
    public static InputStream byte2ToInputStream(byte[] bytes) {
        return new ByteArrayInputStream(bytes);
    }

}