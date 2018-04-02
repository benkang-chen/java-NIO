package learn.nio;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

public class FileChannelTest {
    public static void fileChannel(){
        try {
            RandomAccessFile randomAccessFile = new RandomAccessFile("java-nio-learn\\data\\nio-data.txt","rw");
            FileChannel inChannel = randomAccessFile.getChannel();
            ByteBuffer byteBuffer = ByteBuffer.allocate(512);
            // 读数据到buffer
            int bytesRead = inChannel.read(byteBuffer);
            System.out.println("the bytesRead is " + bytesRead);
            while (bytesRead != -1) {
                // 缓冲区读写转换
                byteBuffer.flip();
                while (byteBuffer.hasRemaining()) {
                    System.out.println((char) byteBuffer.get());
                }
                byteBuffer.clear();
                bytesRead = inChannel.read(byteBuffer);
            }
            inChannel.close();
            randomAccessFile.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        fileChannel();
    }
}
