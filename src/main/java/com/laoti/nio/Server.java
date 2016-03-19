package com.laoti.nio;

import java.io.EOFException;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Date;
import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class Server extends Thread {
	Log log = LogFactory.getLog(this.getClass());
	private ExecutorService executorService;
	Selector selector;
	ServerSocketChannel serverSocketChannel = null;

	public Server(int portnumber, ExecutorService executor) throws IOException {
		this.executorService = executor;
		selector = Selector.open();
		serverSocketChannel = ServerSocketChannel.open();
		serverSocketChannel.socket().bind(new InetSocketAddress(portnumber));
		serverSocketChannel.configureBlocking(false);
		SelectionKey key = serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
		key.attach(new Acceptor());
		log.info("server starting.....");
	}

	public void run() {
		try {
			while (!Thread.interrupted() && selector.isOpen()) {
				int nKeys = selector.select();
				if (nKeys > 0) {
					Iterator<SelectionKey> it = selector.selectedKeys().iterator();
					while (it.hasNext()) {
						SelectionKey key = it.next();
						it.remove();
						dispatch(key);
					}
				}
			}
		} catch (IOException e) {
		}
	}

	private void dispatch(SelectionKey key) throws IOException {

		if (!key.isValid() || !key.channel().isOpen())
			return;
		if (key.isAcceptable()) {
			SocketChannel sc = serverSocketChannel.accept(); // 接受请求 if (sc !=
																// null) {
			sc.configureBlocking(false);
			sc.register(selector, SelectionKey.OP_READ, new Reader(executorService));

		} else if (key.isReadable() || key.isWritable()) {

			Reactor reactor = (Reactor) key.attachment();
			reactor.execute(key);
		}
		// Runnable run = (Runnable) key.attachment();
		// if (run != null) {
		// executorService.execute(run);
		// }
	}

	public static void main(String[] args) throws IOException {
		new Server(12345, Executors.newFixedThreadPool(5)).start();
	}

	class Acceptor implements Runnable {
		SocketChannel sc = null;

		@Override
		public void run() {
			try {
				sc = serverSocketChannel.accept();
				if (sc != null) {
					new Handler(sc, selector);
				}
			} catch (IOException e) {
				log.error(e.getCause().getMessage());
			}

		}

	}
}

class Handler implements Runnable {
	Log log = LogFactory.getLog(this.getClass());
	SocketChannel sc = null;
	Selector selector = null;
	SelectionKey seletionKey = null;
	public static final int SEND = 1, READ = 0;
	int status = READ;

	public Handler(SocketChannel sc, Selector selector) throws IOException {
		this.sc = sc;
		this.selector = selector;
		sc.configureBlocking(false);
		// sc.register(selector, SelectionKey.OP_READ, this);
		// selector.wakeup();
		this.seletionKey = sc.register(selector, SelectionKey.OP_READ);
		seletionKey.attach(this);
		seletionKey.interestOps(SelectionKey.OP_READ);
		selector.wakeup();
	}

	public void run() {
		if (status == READ) {
			read();

		} else if (status == SEND) {
			send();
		}
	}

	private void send() {
		try {
			log.info("send starting ..... ");
			ByteBuffer buffer = ByteBuffer.allocate(100);
			buffer.put("hello,world".getBytes());
			sc.write(buffer);

			if (!buffer.hasRemaining()) {
				buffer.clear();
				seletionKey.cancel();
			}

		} catch (

		ClosedChannelException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private void read() {
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			int len = -1;
			while (sc.isConnected() && (len = sc.read(buffer)) > 0) {
				buffer.flip();
				byte[] content = new byte[buffer.limit()];
				buffer.get(content);
				log.info("read ....." + new String(content, "UTF-8"));
				buffer.clear();
			}

			status = SEND;
			log.info("change status into " + (status == SEND ? "send" : "read"));
			this.seletionKey = sc.register(selector, SelectionKey.OP_WRITE);
			this.seletionKey.attach(this);
			this.seletionKey.interestOps(SelectionKey.OP_WRITE);
			selector.wakeup();
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}

interface Reactor {
	void execute(SelectionKey key);
}

class Reader implements Reactor {
	private static final Log log = LogFactory.getLog(Reader.class);

	private ExecutorService executor;

	public Reader(ExecutorService executor) {
		this.executor = executor;
	}

	@Override
	public void execute(SelectionKey key) {
		log.info("entry");
		SocketChannel sc = (SocketChannel) key.channel();
		try {
			ByteBuffer buffer = ByteBuffer.allocate(1024);
			int len = -1;
			while (sc.isConnected() && (len = sc.read(buffer)) > 0) {
				buffer.flip();
				byte[] content = new byte[buffer.limit()];
				buffer.get(content);
				log.info("read ....." + new String(content, "UTF-8"));
				buffer.clear();
			}
//			if (len == 0) {
//				key.interestOps(SelectionKey.OP_READ);
//				key.selector().wakeup();
//			} else if (len == -1) {
				Callable<byte[]> call = new Callable<byte[]>() {
					@Override
					public byte[] call() throws Exception {
						String header = "HTTP/1.1 200 OK\r\nServer: test1.0\r\nX-Powered-By: test\r\nCache-Control:no-cache\r\nDate: "
								+ new Date().getTime() + "\r\nAccept-Ranges: bytes\r\nContent-Type: text/html;charset="
								+ "UTF-8" + "\r\n\r\n";

						return header.getBytes("UTF-8");
					}
				};
				Future<byte[]> task = executor.submit(call);
				ByteBuffer output = ByteBuffer.wrap(task.get());
				sc.register(key.selector(), SelectionKey.OP_WRITE, new Writer(output));
//			}
		} catch (Exception e) {
			log.info(e);
		}
	}
}

class Writer implements Reactor {
	private static final Log log = LogFactory.getLog(Writer.class);

	private ByteBuffer output;

	public Writer(ByteBuffer output) {
		this.output = output;
	}

	public void execute(SelectionKey key) {
		log.info("entry");

		SocketChannel sc = (SocketChannel) key.channel();

		try {
			while (sc.isConnected() && output.hasRemaining()) {
				int len = sc.write(output);
				if (len < 0) {
					throw new EOFException();
				}
				if (len == 0) {
					break;
				}
			}

			key.interestOps(SelectionKey.OP_WRITE);
			key.selector().wakeup();
			if (!output.hasRemaining()) {
				output.clear();
				key.cancel();
			//	sc.close();
			}
		} catch (IOException e) {
			log.info(e);
		}
	}
}
