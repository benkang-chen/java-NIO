package learn.nio.nio;// $Id$

import java.io.*;
import java.nio.*;
import java.nio.channels.*;

public class FastCopyFile
{
  static public void main( String args[] ) throws Exception {
    String infile = "java-nio-learn\\data\\nio-data.txt";
    String outfile = "java-nio-learn\\data\\nio-data1.txt";

    FileInputStream fin = new FileInputStream( infile );
    FileOutputStream fout = new FileOutputStream( outfile );

    FileChannel fcin = fin.getChannel();
    FileChannel fcout = fout.getChannel();

    ByteBuffer buffer = ByteBuffer.allocateDirect( 1024 );

    while (true) {
      buffer.clear();

      int r = fcin.read( buffer );

      if (r==-1) break;

      buffer.flip();

      fcout.write( buffer );
    }
  }
}
