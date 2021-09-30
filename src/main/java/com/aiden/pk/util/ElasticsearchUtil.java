package com.aiden.pk.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.ssl.TrustStrategy;
import org.apache.http.util.EntityUtils;
import org.elasticsearch.client.*;
import org.elasticsearch.client.sniff.ElasticsearchNodesSniffer;
import org.elasticsearch.client.sniff.NodesSniffer;
import org.elasticsearch.client.sniff.Sniffer;
import org.springframework.util.StringUtils;

import javax.net.ssl.SSLContext;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.StringReader;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ElasticsearchUtil implements Closeable {
    private static final String HOST_SEPARATOR = ",";

    @Override
    public void close() throws IOException {
        restHighLevelClient.close();
    }

    public static class Method {
        private Method() {}
        public static final String GET = "GET";
        public static final String POST = "POST";
        public static final String PUT = "PUT";
        public static final String DELETE = "DELETE";
    }

    RestHighLevelClient restHighLevelClient;
    Sniffer sniffer;

    private final long masterTimeout = 1L;

    private final long requestTimeout = 1L;

    public ElasticsearchUtil(String hosts, boolean sniff) {
        this(hosts, sniff, null, null, false);
    }
    public ElasticsearchUtil(String hosts, boolean sniff,String userName, String password,boolean useHttps) {
        String schemeName;
        if (useHttps) {
            schemeName = "https";
        } else {
            schemeName = "http";
        }
        Set<HttpHost> set = new HashSet<>();
        String[] hostsString = StringUtils.trimAllWhitespace(hosts).split(HOST_SEPARATOR);
        for (String host : hostsString) {
            if (StringUtils.hasLength(host)) {
                if (host.contains(":")) {
                    set.add(new HttpHost(host.substring(0, host.indexOf(":")), Integer.parseInt(host.substring(host.indexOf(":") + 1)),schemeName));
                } else {
                    set.add(new HttpHost(host,-1,schemeName));
                }
            }
        }
        HttpHost[] hostsSet = set.toArray(new HttpHost[]{ });
        RestClientBuilder restClientBuilder = RestClient.builder(hostsSet);
        if (userName != null && password != null) {
            final CredentialsProvider credentialsProvider =
                    new BasicCredentialsProvider();
            credentialsProvider.setCredentials(AuthScope.ANY,
                    new UsernamePasswordCredentials(userName, password));
            SSLContext sslcontext = null;
            try {
                sslcontext = SSLContexts.custom()
                        .loadTrustMaterial(new TrustStrategy() {
                            @Override
                            public boolean isTrusted(X509Certificate[] chain, String authType) {
                                return true;
                            }
                        }).build();
            } catch (Exception e) {
                e.printStackTrace();
            }
            final SSLContext finalSslcontext = sslcontext;
            restClientBuilder.setHttpClientConfigCallback(httpClientBuilder -> {
                return httpClientBuilder
                        .setDefaultCredentialsProvider(credentialsProvider)
                        .setSSLContext(finalSslcontext)
                        .setSSLHostnameVerifier((hostname, session) -> true);
            });
        }
        restHighLevelClient = new RestHighLevelClient(restClientBuilder);
        if (sniff) {
            NodesSniffer nodesSniffer = new ElasticsearchNodesSniffer(restHighLevelClient.getLowLevelClient(), TimeUnit.SECONDS.toMillis(5), ElasticsearchNodesSniffer.Scheme.valueOf(schemeName.toUpperCase()));
            sniffer = Sniffer.builder(restHighLevelClient.getLowLevelClient()).setNodesSniffer(nodesSniffer).setSniffIntervalMillis(60000).build();
        }
    }
    public RestClient getLowLevelClient() {
        return restHighLevelClient.getLowLevelClient();
    }
    public String performRequest(String endpoint, String method, Map<String, String> params, String body) throws IOException {
        Response response = getLowLevelClient().performRequest(buildJsonRequest(endpoint, method, params, body));
        return EntityUtils.toString(response.getEntity());
    }
    public Request buildJsonRequest(String endpoint, String method, Map<String, String> params, String body) {
        Request request = new Request(method, endpoint);
        if (null != body) {
            request.setJsonEntity(body);
        }
        if (null != params && params.size() > 0) {
            request.addParameters(params);
        }
        return request;
    }
    public String performRequest(String endpoint) throws IOException {
        return performRequest(endpoint, Method.GET, null, null);
    }


    public String performRequest(String endpoint, String method) throws IOException {
        return performRequest(endpoint, method, null, null);
    }

    public String performRequest(String endpoint, String method, String body) throws IOException {
        return performRequest(endpoint, method, null, body);
    }

    public String performRequest(String endpoint, String method, Map<String, String> params) throws IOException {
        return performRequest(endpoint, method, params, null);
    }


    public static void main(String[] args) throws IOException, InterruptedException {

        ThreadFactory nameThreadFactory = new ThreadFactoryBuilder().setNameFormat("threadPoolDemo" + "-%d").build();

        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(5, 10, 0L,
                TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(1024), nameThreadFactory);

        ElasticsearchUtil elasticsearchUtil = new ElasticsearchUtil("172.16.129.105:8200", true);
        String s = elasticsearchUtil.performRequest("_cat/shards");

        BufferedReader bufReader = new BufferedReader(new StringReader(s));
        String line=null;
        // filter host
        String filterHost = "e11flm26.mercury.corp";
        String toHost = "e11flm23.mercury.corp";
        int count = 30;
        try {
            while ((line = bufReader.readLine()) != null) {
                String[] split = line.split("\\s+");
                if (split.length>=8 && filterHost.equals(split[7]) && count > 0) {
                    if (split[5].contains("kb")) {
                        String command = getCommand(split[0], Integer.parseInt(split[1]), split[7], toHost);
                        System.out.println(command);
                        threadPool.submit(()->{
                            try {
                                elasticsearchUtil.performRequest("_cluster/reroute", Method.POST, command);
                            } catch (IOException  exception) {
                                exception.printStackTrace();
                            }
                        });
                        Thread.sleep(1000);
                        count--;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            while (true) {
                Thread.sleep(10000);
                long taskCount = threadPool.getCompletedTaskCount();
                System.out.println(taskCount);
                if (taskCount == count) {
                    elasticsearchUtil.close();
                    break;
                }
            }
        }
    }

    public static String getCommand(String index,int shard,String from_node,String to_node){
        String str = "{\n" +
                "  \"commands\": [\n" +
                "    {\n" +
                "      \"move\": {\n" +
                "        \"index\": \""+index+"\",\n" +
                "        \"shard\": "+shard+",\n" +
                "        \"from_node\": \""+from_node+"\",\n" +
                "        \"to_node\": \""+to_node+"\"\n" +
                "      }\n" +
                "    }\n" +
                "  ]\n" +
                "}";
        return str;
    }

}
