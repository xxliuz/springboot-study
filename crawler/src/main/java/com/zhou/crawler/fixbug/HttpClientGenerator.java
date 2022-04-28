package com.zhou.crawler.fixbug;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpRequestInterceptor;
import org.apache.http.client.CookieStore;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.config.SocketConfig;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.DefaultHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.HttpContext;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.downloader.CustomRedirectStrategy;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.IOException;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Map;

/**
 * @Author: zhou.liu
 * @Date: 2022/4/28 13:37
 * @Description: Http 客户端生成器
 */
@Slf4j
public class HttpClientGenerator {

    private PoolingHttpClientConnectionManager poolingHttpClientConnectionManager;

    public HttpClientGenerator(){
        Registry<ConnectionSocketFactory> reg = RegistryBuilder.<ConnectionSocketFactory>create()
                .register("http", PlainConnectionSocketFactory.INSTANCE)
                .register("https",buildSSLConectionSocketFactroy())
                .build();
        poolingHttpClientConnectionManager = new PoolingHttpClientConnectionManager(reg);
        poolingHttpClientConnectionManager.setDefaultMaxPerRoute(100);
    }

    private SSLConnectionSocketFactory buildSSLConectionSocketFactroy(){
        try {
            return new SSLConnectionSocketFactory(createIgnoreVerifySSL(),
                    new String[]{"SSLv3","TLSv1","TLSv1.1","TLSv1.2"},
                    null,
                    new DefaultHostnameVerifier());//优先绕过安全证书
        } catch (NoSuchAlgorithmException e) {
            log.error(e.getMessage(),e);
        } catch (KeyManagementException e) {
            log.error(e.getMessage(),e);
        }
        return SSLConnectionSocketFactory.getSocketFactory();
    }

    private SSLContext createIgnoreVerifySSL() throws NoSuchAlgorithmException, KeyManagementException {
        // 实现一个X509TrustManager接口，用于绕过验证，不用修改里面的方法
        X509TrustManager trustManager = new X509TrustManager() {
            @Override
            public void checkClientTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public void checkServerTrusted(X509Certificate[] x509Certificates, String s) throws CertificateException {

            }

            @Override
            public X509Certificate[] getAcceptedIssuers() {
                return null;
            }
        };
        SSLContext sslContext = SSLContext.getInstance("SSLv3");
        sslContext.init(null,new TrustManager[]{trustManager},null);
        return sslContext;
    }

    public HttpClientGenerator setPoolSize(int poolSize){
        poolingHttpClientConnectionManager.setMaxTotal(poolSize);
        return this;
    }

    public CloseableHttpClient getClient(Site site){
        return generateClient(site);
    }

    private CloseableHttpClient generateClient(Site site) {
        HttpClientBuilder httpClientBuilder = HttpClients.custom();
        httpClientBuilder.setConnectionManager(poolingHttpClientConnectionManager);

        if(site.getUserAgent()!=null){
            httpClientBuilder.setUserAgent(site.getUserAgent());
        }else{
            httpClientBuilder.setUserAgent("");
        }

        if(site.isUseGzip()){
            httpClientBuilder.addInterceptorFirst(new HttpRequestInterceptor() {

                @Override
                public void process(final HttpRequest httpRequest,
                                    final HttpContext httpContext) throws HttpException, IOException {
                    if(!httpRequest.containsHeader("Accept-Encoding")){
                        httpRequest.addHeader("Accept-Encoding", "gzip");
                    }
                }
            });
        }

        //解决post/redirect/post 302跳转问题
        httpClientBuilder.setRedirectStrategy(new CustomRedirectStrategy());

        SocketConfig.Builder socketConfigBuilder = SocketConfig.custom();
        socketConfigBuilder.setSoKeepAlive(true).setTcpNoDelay(true);
        socketConfigBuilder.setSoTimeout(site.getTimeOut());
        SocketConfig socketConfig = socketConfigBuilder.build();
        httpClientBuilder.setDefaultSocketConfig(socketConfig);
        poolingHttpClientConnectionManager.setDefaultSocketConfig(socketConfig);
        httpClientBuilder.setRetryHandler(new DefaultHttpRequestRetryHandler(site.getRetryTimes(), true));
        generateCookie(httpClientBuilder, site);
        return httpClientBuilder.build();
    }

    private void generateCookie(HttpClientBuilder httpClientBuilder, Site site) {
        if (site.isDisableCookieManagement()) {
            httpClientBuilder.disableCookieManagement();
            return;
        }
        CookieStore cookieStore = new BasicCookieStore();
        for (Map.Entry<String, String> cookieEntry : site.getCookies().entrySet()) {
            BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
            cookie.setDomain(site.getDomain());
            cookieStore.addCookie(cookie);
        }
        for (Map.Entry<String, Map<String, String>> domainEntry : site.getAllCookies().entrySet()) {
            for (Map.Entry<String, String> cookieEntry : domainEntry.getValue().entrySet()) {
                BasicClientCookie cookie = new BasicClientCookie(cookieEntry.getKey(), cookieEntry.getValue());
                cookie.setDomain(domainEntry.getKey());
                cookieStore.addCookie(cookie);
            }
        }
        httpClientBuilder.setDefaultCookieStore(cookieStore);
    }
}
