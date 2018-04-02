JAVA NIO的核心组成部分：
* Channels
* Buffers
* Selectors
## Channel 和 Buffer
基本上，所有的 IO 在NIO 中都从一个Channel 开始。Channel 有点象流。 数据可以从Channel读到Buffer中，也可以从Buffer 写到Channel中。
![ChannelAndBuffer](https://raw.githubusercontent.com/benkang-chen/java-NIO/master/picture/ChannelAndBuffer.png)
Channel和Buffer有好几种类型。下面是JAVA NIO中的一些主要Channel的实现：
* FileChannel
* DatagramChannel
* SocketChannel
* ServerSocketChannel

Java NIO里关键的Buffer实现：
* ByteBuffer
* CharBuffer
* DoubleBuffer
* FloatBuffer
* IntBuffer
* LongBuffer
* ShortBuffer

这些Buffer覆盖了你能通过IO发送的基本数据类型：byte, short, int, long, float, double 和 char。
## Selector
elector允许单线程处理多个 Channel。如果你的应用打开了多个连接（通道），但每个连接的流量都很低，使用Selector就会很方便。例如，在一个聊天服务器中。

这是在一个单线程中使用一个Selector处理3个Channel的图示：
![Select](https://raw.githubusercontent.com/benkang-chen/java-NIO/master/picture/Select.png)