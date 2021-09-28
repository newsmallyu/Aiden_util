package com.aiden.pk.util;

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
import java.io.IOException;
import java.security.cert.X509Certificate;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

public class ElasticsearchUtil {
    private static final String HOST_SEPARATOR = ",";

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
}
