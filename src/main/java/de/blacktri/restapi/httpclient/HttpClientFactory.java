package de.blacktri.restapi.httpclient;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;

/**
 * Creates an httpClient which accepts all https certificates.
 */
public class HttpClientFactory {

  protected static HttpClient createHttpClient(boolean acceptCookies, int connectionPoolSize, int socketTimeout, int connectionTimeout, int connectionRequestTimeout) {
    HttpClientBuilder clientBuilder = HttpClientBuilder.create().disableRedirectHandling().useSystemProperties();
    clientBuilder.setConnectionManager(createDefaultConnectionMgr(connectionPoolSize));
    RequestConfig.Builder builder = RequestConfig.custom()
            .setSocketTimeout(socketTimeout)
            .setConnectTimeout(connectionTimeout)
            .setConnectionRequestTimeout(connectionRequestTimeout)
            .setStaleConnectionCheckEnabled(true);
    if (!acceptCookies) {
      builder.setCookieSpec(CookieSpecs.IGNORE_COOKIES);
    }
    clientBuilder.setDefaultRequestConfig(builder.build());

    return clientBuilder.build();
  }

  private static PoolingHttpClientConnectionManager createDefaultConnectionMgr(int connectionPoolSize) {
    PoolingHttpClientConnectionManager defaultConnectionPoolMgr = new PoolingHttpClientConnectionManager();
    defaultConnectionPoolMgr.setMaxTotal(connectionPoolSize);
    defaultConnectionPoolMgr.setDefaultMaxPerRoute(connectionPoolSize);
    return defaultConnectionPoolMgr;
  }

}