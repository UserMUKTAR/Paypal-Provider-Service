package com.hulkhiretech.payments.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.util.MultiValueMap;

import lombok.Data;

@Data
public class HttpRequest {
	
		private HttpMethod httpMethod;
		private String url;
		private HttpHeaders HttpHeaders;
		private Object requestBody;
		
		public HttpMethod getHttpMethod() {
			return httpMethod;
		}
		public void setHttpMethod(HttpMethod httpMethod) {
			this.httpMethod = httpMethod;
		}
		public String getUrl() {
			return url;
		}
		public void setUrl(String url) {
			this.url = url;
		}
		public HttpHeaders getHttpHeaders() {
			return HttpHeaders;
		}
		public void setHttpHeaders(HttpHeaders httpHeaders) {
			HttpHeaders = httpHeaders;
		}
		public Object getRequestBody() {
			return requestBody;
		}
		public void setRequestBody(Object requestBody) {
			this.requestBody = requestBody;
		}
		
	


}
