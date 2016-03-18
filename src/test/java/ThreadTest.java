import java.util.Queue;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.Semaphore;

public class ThreadTest {

	final static LinkedBlockingDeque<String> queue = new LinkedBlockingDeque<String>();

	public static void main(String[] args) {
		final Object monitor = new Object();
		final Semaphore sss = new Semaphore(1);
		for (int i = 0; i < 10; i++) {
			new Thread(new Runnable() {

				@Override
				public void run() {
					try {
						// synchronized (monitor) {
						sss.acquire();
						String value = queue.take();
						Test.print(value);
						sss.release();
						// }
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}

			}).start();
		}

		for (int j = 0; j < 16; j++) {
			try {
				queue.put(j + "");
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}

class Test {

	public static void print(String value) {
		System.out.println(Thread.currentThread().getName() + ":" + value + "    " + System.currentTimeMillis() / 1000);
		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
