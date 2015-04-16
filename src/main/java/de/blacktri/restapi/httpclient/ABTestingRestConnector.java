package de.blacktri.restapi.httpclient;

import org.apache.commons.codec.binary.Base64;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.StatusLine;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.StringEntity;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.type.TypeReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.HttpMethod;
import org.springframework.web.util.UriComponents;
import org.springframework.web.util.UriComponentsBuilder;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Map;

import static org.apache.http.client.utils.HttpClientUtils.closeQuietly;

public class ABTestingRestConnector {

  private HttpClient httpClient;
  private int connectionRequestTimeout = -1;
  private int connectionTimeout = -1;
  private int socketTimeout = -1;
  private int connectionPoolSize = 200;
  private static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

  /**
   * The user's api key
   * <p/>
   * It's a string defined by the API automatically when a new user is created, it is stored in the DB and
   * identifies uniquely an API user
   */
  private String apiKey = "";

  /**
   * The user's api secret
   * <p/>
   * A pair apikey/apisecret is required to perform any request to the API in the server, if this pair
   * does not match, an exception is thrown before any action takes place
   */
  private String apiSecret = "";

  private String serviceEndpoint;

  private static final String HEADER_CONTENT_TYPE = "Content-Type";

  public static final String MIME_TYPE_JSON = "application/json";


  private static final Logger LOG = LoggerFactory.getLogger(ABTestingRestConnector.class);

  public <T> T callService(HttpMethod serviceMethod, String uriTemplate, TypeReference returnType,
                           Map<String, Object> queryParameters,
                           Map<String, String> additionalHeaders,
                           Object bodyData) {
    T result = null;

    URI uri;
    try {
      uri = buildRequestUri(uriTemplate, queryParameters);
    } catch (IllegalArgumentException e) {
      LOG.warn("unable to derive REST URI components for method {} with vars {} and query params {}", serviceMethod, queryParameters);
      return null;
    }

    HttpUriRequest httpClientRequest = getRequest(uri, serviceMethod, bodyData, additionalHeaders);

    try {
      HttpClient client = getHttpClient();

      long start = 0L;
      if (LOG.isTraceEnabled()) {
        start = System.currentTimeMillis();
      }

      HttpResponse response = client.execute(httpClientRequest);
      StatusLine statusLine = response.getStatusLine();
      int statusCode = statusLine.getStatusCode();

      if (LOG.isTraceEnabled()) {
        long time = System.currentTimeMillis() - start;
        LOG.trace(serviceMethod + " " + uri + ": " + statusCode + " took " + time + " ms");
      }

      try {
        HttpEntity entity;

        //Handle success here
        if (statusCode >= 200 && statusCode != 204 && statusCode < 300) {
          entity = response.getEntity();
          if (entity != null) {
            InputStream inputStream = response.getEntity().getContent();
            ObjectMapper mapper = new ObjectMapper();
            mapper.setDateFormat(DATE_FORMAT);
            result = mapper.readValue(inputStream, returnType);
          } else {
            LOG.trace("response entity is null");
          }
        } else if (statusCode == 204) {
          LOG.trace("result from " + httpClientRequest.getURI() + " will be interpreted as \"no result found\": " +
                  statusCode + " (" + statusLine.getReasonPhrase() + ")");

        } else {
          throw new RuntimeException(
                  String.format("Remote Error occurred: %s ( Error Code: %s)", statusLine,
                          statusCode));
        }
      } finally {
        closeQuietly(response);
      }

    } catch (Exception e) {
      LOG.warn("Error while calling REST: {} ({})", httpClientRequest.getURI(), e.getMessage());
      LOG.trace("The corresponding stacktrace is...", e);
    }
    return result;
  }

  URI buildRequestUri(String relativeUrl, Map<String, Object> queryParameters) {
    String uri = relativeUrl;

    String endpoint = getServiceEndpoint();

    if (!endpoint.endsWith("/")) {
      endpoint += "/";
    }
    uri = endpoint + uri;
    UriComponentsBuilder uriBuilder = UriComponentsBuilder.fromUriString(uri);
    for (Map.Entry<String, Object> entry : queryParameters.entrySet()) {
      uriBuilder.queryParam(entry.getKey(), entry.getValue());
    }

    UriComponents uriComponents = uriBuilder.buildAndExpand();
    return uriComponents.encode().toUri();
  }

  private HttpUriRequest getRequest(URI uri, HttpMethod serviceMethod, Object bodyData, Map<String, String> additionalHeaders) {

    HttpUriRequest request = null;

    if (serviceMethod == HttpMethod.POST) {
      request = new HttpPost(uri);
    } else if (serviceMethod == HttpMethod.GET) {
      request = new HttpGet(uri);
    } else if (serviceMethod == HttpMethod.DELETE) {
      request = new HttpDelete(uri);
    } else if (serviceMethod == HttpMethod.PUT) {
      request = new HttpPut(uri);
    }

    if (request != null) {

      request.addHeader(HEADER_CONTENT_TYPE, MIME_TYPE_JSON);
      String encoded = Base64.encodeBase64String((apiKey + ":" + apiSecret).getBytes());
      request.addHeader("Authorization", "Basic " + encoded);


      for (Map.Entry<String, String> item : additionalHeaders.entrySet()) {
        request.addHeader(item.getKey(), item.getValue());
      }

      try {
        //apply parameter to body
        if (bodyData != null) {
          String json = toJson(bodyData);
          if (LOG.isTraceEnabled()) {
            LOG.trace("{}\n{}", request, json);
          }
          StringEntity entity = new StringEntity(json);
          ((HttpEntityEnclosingRequest) request).setEntity(entity);
        }
      } catch (IOException e) {
        LOG.warn("Error while encoding body data: {}", e.getMessage(), e);
      }
    }

    return request;
  }

  private String toJson(Object model) throws IOException {
    ObjectMapper mapper = new ObjectMapper();
    mapper.setDateFormat(DATE_FORMAT);
    mapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_NULL);
    mapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
    return mapper.writeValueAsString(model);
  }


  protected HttpClient getHttpClient() {
    if (httpClient == null) {
      httpClient = HttpClientFactory.createHttpClient(false,
              connectionPoolSize, socketTimeout, connectionTimeout, connectionRequestTimeout);
    }
    return httpClient;
  }

  @Required
  public void setServiceEndpoint(String serviceEndpoint) {
    this.serviceEndpoint = serviceEndpoint;
  }

  public String getServiceEndpoint() {
    return serviceEndpoint;
  }

  public void setConnectionRequestTimeout(int connectionRequestTimeout) {
    this.connectionRequestTimeout = connectionRequestTimeout;
  }

  public void setConnectionTimeout(int connectionTimeout) {
    this.connectionTimeout = connectionTimeout;
  }

  public void setSocketTimeout(int socketTimeout) {
    this.socketTimeout = socketTimeout;
  }

  public void setConnectionPoolSize(int connectionPoolSize) {
    this.connectionPoolSize = connectionPoolSize;
  }

  @Required
  public void setApiKey(String apiKey) {
    this.apiKey = apiKey;
  }

  @Required
  public void setApiSecret(String apiSecret) {
    this.apiSecret = apiSecret;
  }

  public String getApiKey() {
    return apiKey;
  }

  public String getApiSecret() {
    return apiSecret;
  }
}
