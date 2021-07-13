package com.aiden.pk.test;


import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestFactory;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.RequestLine;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ay05
 */
public class AppsearchHttp {


    public static void main(String[] args) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("Content-Type", "application/json");
        requestHeaders.add("Authorization", "Bearer private-fqzz2kit27diz41et72g2gkw");
        Map<String,String> map = new HashMap();
        map.put("id", "10001ppp");
        map.put("title", "aiden");
        map.put("body", "aidenTest");
        map.put("url", "www.baidu.com");
        map.put("type", "json");
        HttpEntity<Map> requestEntity = new HttpEntity<>(map, requestHeaders);

        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity("http://10.16.78.141:9002/api/as/v1/engines/temp/documents", requestEntity, String.class);
        System.out.println(stringResponseEntity.getBody());
    }
}
