package com.example.retinopatia;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class OpenFile {
    public static MappedByteBuffer loadModelFile(Context context,String fileName) {
        try{
            AssetFileDescriptor fileDescriptor = context.getAssets().openFd(fileName);
            FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
            FileChannel fileChannel = inputStream.getChannel();
            long startOffset = fileDescriptor.getStartOffset();
            long declaredLength = fileDescriptor.getDeclaredLength();
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
            fileChannel.close();
            inputStream.close();
            fileDescriptor.close();
            return mappedByteBuffer;
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }
}
