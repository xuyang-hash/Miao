package com.meowing.loud.arms.utils;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class AudioUtils {

    private static String getFilePathFromUri(Context context, Uri uri) {
        String filePath = null;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            try (ParcelFileDescriptor pfd = context.getContentResolver().openFileDescriptor(uri, "r")) {
                if (pfd != null) {
                    FileDescriptor fd = pfd.getFileDescriptor();
                    FileInputStream inputStream = new FileInputStream(fd);
                    byte[] bytes = readAllBytes(inputStream);
                    filePath = saveByteArrayToFile(context, bytes, "audio");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String[] projection = {MediaStore.Audio.Media.DATA};
            Cursor cursor = context.getContentResolver().query(uri, projection, null, null, null);
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA);
                filePath = cursor.getString(columnIndex);
                cursor.close();
            }
        }

        return filePath;
    }

    private static byte[] readAllBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        byte[] buffer = new byte[4096];
        int n;
        while ((n = inputStream.read(buffer)) != -1) {
            output.write(buffer, 0, n);
        }
        return output.toByteArray();
    }

    private static String saveByteArrayToFile(Context context, byte[] data, String fileName) {
        String filePath = null;
        try {
            File dir = context.getExternalFilesDir(null);
            if (dir == null) {
                throw new IOException("External storage is not available.");
            }
            File file = new File(dir, fileName);
            FileOutputStream outputStream = new FileOutputStream(file);
            outputStream.write(data);
            outputStream.close();
            filePath = file.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
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
     * @return
     */
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
