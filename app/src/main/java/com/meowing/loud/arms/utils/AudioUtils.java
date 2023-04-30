package com.meowing.loud.arms.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioUtils {

    private static String getFilePathFromUri(Context context, Uri uri) {
        String filePath = null;

        if (uri != null) {
            if (DocumentsContract.isDocumentUri(context, uri)) {
                // 处理 Document 类型的 URI
                String documentId = DocumentsContract.getDocumentId(uri);
                String[] split = documentId.split(":");
                String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    // 如果是 primary 类型，表示在内部存储上
                    filePath = Environment.getExternalStorageDirectory() + "/" + split[1];
                } else {
                    // 否则，需要通过 DocumentProvider 查询实际路径
                    // 这里可以根据不同的类型选择不同的处理方式
                    // ...
                }
            } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                // 处理 content 类型的 URI
                String[] projection = { MediaStore.Images.Media.DATA };
                Cursor cursor = null;

                try {
                    cursor = context.getContentResolver().query(uri, projection, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        filePath = cursor.getString(columnIndex);
                    }
                } finally {
                    if (cursor != null) {
                        cursor.close();
                    }
                }
            } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                // 处理 file 类型的 URI
                filePath = uri.getPath();
            }
        }

        return filePath;
    }


    private static byte[] readAudioFileToByteArray(String filePath) {
        FileInputStream fileInputStream = null;
        ByteArrayOutputStream byteArrayOutputStream = null;

        try {
            // 创建文件输入流
            fileInputStream = new FileInputStream(filePath);
            byteArrayOutputStream = new ByteArrayOutputStream();

            // 读取文件数据到字节数组输出流
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fileInputStream.read(buffer)) != -1) {
                byteArrayOutputStream.write(buffer, 0, bytesRead);
            }

            // 返回读取到的字节数组
            return byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            // 关闭文件输入流和字节数组输出流
            if (fileInputStream != null) {
                try {
                    fileInputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (byteArrayOutputStream != null) {
                try {
                    byteArrayOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 将音频二进制数组转为base64字符串
     * @param filePath
     * @return
     */
    private static String toAudioStringFromByte(String filePath) {
        byte[] bytes = readAudioFileToByteArray(filePath);
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }

    public static String toAudioStringFromFile(Context context, Uri uri){
        String filePath = getFilePathFromUri(context, uri);
        if (filePath != null) {
            byte[] bytes = readAudioFileToByteArray(filePath);
            return Base64.encodeToString(bytes, Base64.DEFAULT);
        }
        return null;
    }

    public static Uri toUriFromAudioString(String audioString) {
        byte[] bytes = Base64.decode(audioString, Base64.DEFAULT);
        return byteArrayToUri(bytes);
    }

    private static Uri byteArrayToUri(byte[] byteArray) {
        Uri uri = null;
        FileOutputStream outputStream = null;
        try {
            // 将 byte 数组写入临时文件
            File tempFile = File.createTempFile("temp_audio", ".mp3");
            outputStream = new FileOutputStream(tempFile);
            outputStream.write(byteArray);
            outputStream.flush();
            outputStream.close();

            // 将临时文件的路径转换成 Uri
            uri = Uri.fromFile(tempFile);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (outputStream != null) {
                try {
                    outputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return uri;
    }

}
