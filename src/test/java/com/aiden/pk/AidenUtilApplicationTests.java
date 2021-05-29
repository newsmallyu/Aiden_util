package com.aiden.pk;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

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
}
