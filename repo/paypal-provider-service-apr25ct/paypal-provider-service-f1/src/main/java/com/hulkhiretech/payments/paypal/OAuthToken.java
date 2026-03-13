package com.hulkhiretech.payments.paypal;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Data;

@Data
public class OAuthToken {
	 private String scope;

	    @JsonProperty("access_token")
	    private String accessToken;

	    @JsonProperty("token_type")
	    private String tokenType;

	    @JsonProperty("app_id")
	    private String appId;

	    @JsonProperty("expires_in")
	    private int expiresIn;

	    private String nonce;

	    // Getters and Setters

	    public String getScope() {
	        return scope;
	    }

	    public void setScope(String scope) {
	        this.scope = scope;
	    }

	    public String getAccessToken() {
	        return accessToken;
	    }

	    public void setAccessToken(String accessToken) {
	        this.accessToken = accessToken;
	    }

	    public String getTokenType() {
	        return tokenType;
	    }

	    public void setTokenType(String tokenType) {
	        this.tokenType = tokenType;
	    }

	    public String getAppId() {
	        return appId;
	    }

	    public void setAppId(String appId) {
	        this.appId = appId;
	    }

	    public int getExpiresIn() {
	        return expiresIn;
	    }

	    public void setExpiresIn(int expiresIn) {
	        this.expiresIn = expiresIn;
	    }

	    public String getNonce() {
	        return nonce;
	    }

	    public void setNonce(String nonce) {
	        this.nonce = nonce;
	    }

}
