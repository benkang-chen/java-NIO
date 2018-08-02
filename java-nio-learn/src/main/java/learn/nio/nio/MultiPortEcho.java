package learn.nio.nio;// $Id$

import java.io.*;
import java.net.*;
import java.nio.*;
import java.nio.channels.*;
import java.util.*;

public class MultiPortEcho
{
  private int ports[];
  private ByteBuffer echoBuffer = ByteBuffer.allocate( 1024 );

  public MultiPortEcho( int ports[] ) throws IOException {
    this.ports = ports;

    go();
  }

  private void go() throws IOException {
    // Create a new selector
    Selector selector = Selector.open();

    // Open a listener on each port, and register each one
    // with the selector
    for (int i=0; i<ports.length; ++i) {
      ServerSocketChannel ssc = ServerSocketChannel.open();
      ssc.configureBlocking( false );
      ServerSocket ss = ssc.socket();
      InetSocketAddress address = new InetSocketAddress( ports[i] );
      ss.bind( address );
      // 将新打开的 ServerSocketChannels 注册到 Selector上,register() 的第一个参数总是这个 Selector。
      // 第二个参数是 OP_ACCEPT，这里它指定我们想要监听 accept 事件，也就是在新的连接建立时所发生的事件
      SelectionKey key = ssc.register( selector, SelectionKey.OP_ACCEPT );

      System.out.println( "Going to listen on "+ports[i] );
    }

    while (true) {
      //  Selector 的 select() 方法。这个方法会阻塞，直到至少有一个已注册的事件发生。
      // 当一个或者更多的事件发生时， select() 方法将返回所发生的事件的数量
      int num = selector.select();
      // 接下来，我们调用 Selector 的 selectedKeys() 方法，它返回发生了事件的 SelectionKey 对象的一个 集合 。
      Set selectedKeys = selector.selectedKeys();
      //我们通过迭代 SelectionKeys 并依次处理每个 SelectionKey 来处理事件。
      // 对于每一个 SelectionKey，您必须确定发生的是什么 I/O 事件，以及这个事件影响哪些 I/O 对象。
      Iterator it = selectedKeys.iterator();

      while (it.hasNext()) {
        SelectionKey key = (SelectionKey)it.next();

        if ((key.readyOps() & SelectionKey.OP_ACCEPT)
          == SelectionKey.OP_ACCEPT) {
          // Accept the new connection
          // 因为我们知道这个服务器套接字上有一个传入连接在等待，所以可以安全地接受它；也就是说，不用担心 accept() 操作会阻塞：
          ServerSocketChannel ssc = (ServerSocketChannel)key.channel();
          SocketChannel sc = ssc.accept();
          // 下一步是将新连接的 SocketChannel 配置为非阻塞的。而且由于接受这个连接的目的是为了读取来自套接字的数据，
          // 所以我们还必须将 SocketChannel 注册到 Selector上，如下所示：
          sc.configureBlocking( false );
          // Add the new connection to the selector
          // 注意我们使用 register() 的 OP_READ 参数，将 SocketChannel 注册用于 读取 而不是 接受 新连接。
          SelectionKey newKey = sc.register( selector, SelectionKey.OP_READ );
          // 在处理 SelectionKey 之后，我们几乎可以返回主循环了。但是我们必须首先将处理过的 SelectionKey 从选定的键集合中删除。
          // 如果我们没有删除处理过的键，那么它仍然会在主集合中以一个激活的键出现，这会导致我们尝试再次处理它。
          // 我们调用迭代器的 remove() 方法来删除处理过的 SelectionKey：
          it.remove();

          System.out.println( "Got connection from "+sc );
        } else if ((key.readyOps() & SelectionKey.OP_READ)
          == SelectionKey.OP_READ) {
          // Read the data
          SocketChannel sc = (SocketChannel)key.channel();

          // Echo data
          int bytesEchoed = 0;
          int r = 0;
          while (true) {
            echoBuffer.clear();
            // 防止客户端主动关闭是异常退出
            try {
                r = sc.read( echoBuffer );
            } catch (IOException e) {
                key.cancel();
                sc.socket().close();
                sc.close();
            }

            if (r<=0) {
              break;
            }

            echoBuffer.flip();

            sc.write( echoBuffer );
            bytesEchoed += r;
          }

          System.out.println( "Echoed "+bytesEchoed+" from "+sc );

          it.remove();
        }

      }

//System.out.println( "going to clear" );
//      selectedKeys.clear();
//System.out.println( "cleared" );
    }
  }

  static public void main( String args[] ) throws Exception {

    int ports[] = {5566, 5567};

    new MultiPortEcho( ports );
  }
}
