package sco3.acc.common;

public interface AccConstants {
	public static final String API_V1 = "/api/v1/";
	public static final String API_DOCS = "/v3/api-docs/**";
	public static final String SWAGGER_UI = "/swagger-ui/**";
	public static final String SWAGGER_UI_DOT = "/swagger-ui.*";
	public static final String ACTUATOR = "/actuator/**";
	public static final String[] NON_SECURE_MATCHERS = { //
			API_DOCS, //
			SWAGGER_UI, //
			SWAGGER_UI_DOT, //
			ACTUATOR //
	};

	public static final String INTEGRATION = "integration";
	public static final String API_USERS_ENDPOINT = API_V1 + "users";
	public static final String API_HEALTH_PATH = API_V1 + "health";
	public static final String HEALTH_ME = "/me";
	public static final String API_HEALTH_ME_ENDPOINT = API_HEALTH_PATH + HEALTH_ME;

}
