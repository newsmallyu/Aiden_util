package com.aiden.pk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Iterator;
import java.util.SortedSet;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SpringBootTest
class AidenUtilApplicationTests {


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
}
