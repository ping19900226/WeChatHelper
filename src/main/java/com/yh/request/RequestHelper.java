package com.yh.request;

import org.apache.http.client.CookieStore;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.config.Registry;
import org.apache.http.config.RegistryBuilder;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.socket.ConnectionSocketFactory;
import org.apache.http.conn.socket.PlainConnectionSocketFactory;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.*;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;

import java.net.CookieHandler;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class RequestHelper {

   private static CookieStore store;
   private static CloseableHttpClient client;
   private static CookieManager manager;

   static {
      try {
         SSLContextBuilder builder = new SSLContextBuilder();
         // 全部信任 不做身份鉴定
         builder.loadTrustMaterial(null, new TrustStrategy() {

            public boolean isTrusted(X509Certificate[] arg0, String arg1) throws CertificateException {
               return true;
            }
         });

         SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(builder.build(),
                 new String[] { "SSLv2Hello", "SSLv3", "TLSv1", "TLSv1.2" }, null, NoopHostnameVerifier.INSTANCE);
         Registry<ConnectionSocketFactory> registry = RegistryBuilder.<ConnectionSocketFactory>create()
                 .register("https", new PlainConnectionSocketFactory()).register("https", sslsf).build();
         PoolingHttpClientConnectionManager cm = new PoolingHttpClientConnectionManager(registry);
         store = new BasicCookieStore();
         HttpClientContext localContext = HttpClientContext.create();
         localContext.setCookieStore(store);

         client = HttpClients.custom().setSSLSocketFactory(null).setConnectionManager(null)
                 .setConnectionManagerShared(true).setDefaultCookieStore(store).build();

         manager = new CookieManager(store);
         cm.setMaxTotal(200);// max connection
      } catch (Exception e) {
         e.printStackTrace();
      }


   }

   //现在没有多线程的问题
   public static org.apache.http.client.CookieStore getCookieStore() {
      return store;
   }

   public static CookieHandler getCookieHander() {
      return manager;
   }

   public static CloseableHttpClient getHttpClient() {
      return client;
   }
}
