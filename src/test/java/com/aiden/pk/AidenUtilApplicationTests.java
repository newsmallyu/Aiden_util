package com.aiden.pk;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.SortedSet;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class AidenUtilApplicationTests {

	private static Logger logger = LoggerFactory.getLogger(AidenUtilApplicationTests.class);
	@Test
	void contextLoads() {
	}
	static final String regex = "[^a-z]+$";
	static final Pattern pattern = Pattern.compile(regex);
	@Test
	void matcher(){
		Matcher matcher = pattern.matcher("logx2_k8s_ns_spinnaker-2021.05.09");
		matcher.matches();
		String trim = matcher.replaceAll("").trim();
		System.out.println(trim);
	}


	@Test
	void copyWriteList(){
		CopyOnWriteArrayList list = new CopyOnWriteArrayList();

		list.add(1);
		list.add(2);
		list.add(3);
		list.add(4);
		list.add(5);


		Iterator iterator = list.iterator();
		while (iterator.hasNext()) {
			Object next = iterator.next();
			System.out.println(next);
			iterator.remove();
		}
		System.gc();
	}


	@Test
	void testBitOperation(){
		System.out.println(1<<30);
	}


	@Test
	void testMod(){
		System.out.println( 16895%16384);
	}

	@Test
	void testCharLength(){
		String a = "哈";
		System.out.println(a.getBytes(StandardCharsets.UTF_8).length);

		String b = "abc";
		System.out.println(b.getBytes(StandardCharsets.UTF_8).length);

		System.out.println(Integer.valueOf('a'));

		System.out.println((char)97);
	}

	@Test
	void threadPoolDemo(){
		ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("threadPoolDemo" + "-%d").build();

		ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 0L,
				TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), nameThreadFactory);

		AtomicInteger atomicInteger = new AtomicInteger();

		for (int i = 0; i < 6; i++) {
			threadPool.execute(()-> logger.info(String.valueOf(atomicInteger.getAndIncrement())) );
		}
	}
}
