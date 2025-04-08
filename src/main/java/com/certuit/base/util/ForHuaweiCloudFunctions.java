package com.certuit.base.util;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

public class ForHuaweiCloudFunctions {
    public static String CARPETA="GMTERPV8PQ/";
    public static String PREFIJO_KEY = "AWS4";
    public static String SECRET_ACCESS_KEY = "oaLMImmaTrq4FqTevwD5A3k5C90IM6v6Wo8Qw1WT";
    public static String ACCESS_KEY_ID = "6ROWJGWMNFPXA19YOETK";


    //es la region donde esta contratdo el servicio en este caso mexico
    public static String AWS_REGION = "na-mexico-1";
    public static String AWS_SERVICE = "s3";
    public static String AWS_REQUEST = "aws4_request";
    public static String CONTENT_TYPE = "text/plain";
    public static String BUCKET_AND_ENDPOINT = "obs-gmterp-produccion.obs.na-mexico-1.myhuaweicloud.com";
    public static String CONTENT_SHA256 = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";
    public static String SIGNED_HEADERS = "content-type;host;x-amz-content-sha256;x-amz-date";

    public static String readFullyAsString(InputStream inputStream, String encoding) throws IOException {
        return readFully(inputStream).toString(encoding);
    }
    public static ByteArrayOutputStream readFully(InputStream inputStream) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buffer = new byte[1024];
        int length = 0;
        while ((length = inputStream.read(buffer)) != -1) {
            baos.write(buffer, 0, length);
        }
        baos.flush();
        baos.close();
        return baos;
    }
    public static String bytestoHex(byte[] bytes) {
        char[] hexArray = "0123456789ABCDEF".toCharArray();
        char[] hexChars=new char[bytes.length*2];
        for(int j=0;j<bytes.length;j++)
        {
            int v=bytes[j] & 0xFF;
            hexChars[j*2]=hexArray[v>>>4];
            hexChars[j*2+1]=hexArray[v & 0x0F];
        }
        return new String(hexChars).toLowerCase();
    }
    public static byte[] hacerHMAC256(byte[] key,String data) throws Exception{
        String algorithm="HmacSHA256";
        Mac mac=Mac.getInstance(algorithm);
        mac.init(new SecretKeySpec(key,algorithm));
        String p= org.apache.commons.codec.binary.Hex.encodeHexString(mac.doFinal(data.getBytes(StandardCharsets.UTF_8)));
        return mac.doFinal(data.getBytes(StandardCharsets.UTF_8));
    }
}
