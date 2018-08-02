package learn.nio.nio;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class SliceBuffer
{
  static public void main( String args[] ) throws Exception {
    ByteBuffer buffer = ByteBuffer.allocate( 10 );

    for (int i=0; i<buffer.capacity(); ++i) {
      buffer.put( (byte)i );
    }
    // 窗口的起始和结束位置通过设置 position 和 limit 值来指定
    buffer.position( 3 );
    buffer.limit( 7 );
    // slice() 方法根据现有的缓冲区创建一种 子缓冲区 。也就是说，它创建一个新的缓冲区，新缓冲区与原来的缓冲区的一部分共享数据。
    ByteBuffer slice = buffer.slice();

    for (int i=0; i<slice.capacity(); ++i) {
      byte b = slice.get( i );
      b *= 11;
      slice.put( i, b );
    }

    buffer.position( 0 );
    buffer.limit( buffer.capacity() );

    while (buffer.remaining()>0) {
      System.out.println( buffer.get() );
    }
  }
}
