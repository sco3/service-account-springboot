package sco3.acc.common;


public interface Constants {
	public static final String API_V1 = "/api/v1/";
	public static final String API_DOCS = "/v3/api-docs/**";
	public static final String SWAGGER_UI = "/swagger-ui/**";
	public static final String ACTUATOR = "/actuator/**";
	public static final String[] NON_SECURE_MATCHERS = { API_DOCS, SWAGGER_UI, ACTUATOR };

}
