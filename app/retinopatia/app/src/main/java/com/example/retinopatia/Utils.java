package com.example.retinopatia;

import android.content.Context;
import android.content.res.AssetFileDescriptor;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;


public class Utils {
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
        public static String switchResultado(int resultado){
            switch (resultado){
                case 0:
                    return "NPDR";
                case 1:
                    return "NPDR leve";
                case 2:
                    return "NPDR moderada";
                case 3:
                    return "NPDR severa";
                case 4:
                    return "PDR";
                default:
                    return "Error";
            }
    }
}
