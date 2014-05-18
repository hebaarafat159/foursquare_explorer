package com.android.foursquareexplorer;

public class Constants {

	// prefrences keys
	public static final String USER_CURRENT_LOCATION_PREF_KEY = "user_current_location";
	public static final String USER_SIGNATURE_PREF_KEY = "user_signature";
	public static final String TOKEN_PREF_KEY = "oauth_token";

	// requests codes
	public static final int GET_NEAREST_VENUES_REQUEST_CODE = 100;
	public static final int CHECKIN_REQUEST_CODE = 101;
	public static final int GET_USER_SIGNATURE_REQUEST_CODE = 102;
	public static final int GET_TOKEN_REQUEST_CODE = 103;
	public static final int GET_TOKEN_CODE_REQUEST_CODE = 104;
	public static final int GET_VENUES_FROM_DABASE_REQUEST_CODE = 105;
	public static final int GET_CURRENT_LOCATION_REQUEST_CODE = 106;

	public static final int RESPONSE_SUCCESS_CODE = 200;
	public static final int RESPONSE_ERROR_CODE = 600;

	// foursqaure URLs
	public static final String CLIENT_ID = "JUNUQYRMZQYQ3X51I0OTIK3CLCISS3DZN0T5BCRMQ2N1U40X";
	public static final String CLIENT_SECRET = "RMALHMPXEJVZRCD4JRAXZFLRJBD30RRQEOTEARUGFAXYTCNW";
	public static final String FOURSQUARE_URL = "https://www.foursquare.com";
	public static final String ACCESS_TOKEN_URL = FOURSQUARE_URL
			+ "/oauth2/authenticate";
	public static final String AUTHORIZE_URL = FOURSQUARE_URL
			+ "/oauth2/authorize";
	public static final String REDIRECT_ACCESS_TOKEN_URL = "http://www.mapfirstapp.com/rediret_url";
	public static final String REDIRECT_CODE_URL = FOURSQUARE_URL
			+ "/rediret_url?";

	public static final String FOURSQUARE_API = "https://api.foursquare.com/v2";
	public static final String VENUES_SEARCH_API = FOURSQUARE_API
			+ "/venues/search";
	public static final String CHECK_IN_API = FOURSQUARE_API + "/checkins/";
	public static final String CHECK_IN_ADD_API = CHECK_IN_API + "add";

	// request parameter keys
	public static final String REQUEST_CLIENT_ID_PARAM = "client_id";
	public static final String REQUEST_CLIENT_SECRET_PARAM = "client_secret";
	public static final String REQUEST_VERSION_PARAM = "v";
	public static final String REQUEST_SIGNATURE_PARAM = "signature";
	public static final String REQUEST_OAUTH_TOKEN_PARAM = "oauth_token";
	public static final String REQUEST_VENUES_SEARCH_LOCATION_PARAM = "ll";
	public static final String REQUEST_VENUES_RADIUS_PARAM = "radius";
	public static final String REQUEST_ACCESS_TOKEN_PARAM = "#access_token";
	public static final String REQUEST_AUTH_CODE_PARAM = "code";
	public static final String REQUEST_TOKEN_TYPE_PARAM = "response_type";
	public static final String REQUEST_TOKEN_TYPE_VALUE = "token";
	public static final String REQUEST_REDIRECT_URI_PARAM = "redirect_uri";

	// response json keys
	public static final String RESPONSE_META_DATA_JSON_KEY = "meta";
	public static final String RESPONSE_CODE_JSON_KEY = "code";
	public static final String RESPONSE_STRING_JSON_KEY = "response";
	public static final String RESPONSE_VENUES_ARRAY_JSON_KEY = "venues";

	// VENUE columns Names
	public static final String VENUE_ID_KEY = "id";
	public static final String VENUE_NAME_KEY = "name";
	public static final String VENUE_LOCATION_KEY = "location";
	public static final String VENUE_LOCATION_ID_KEY = "location_id";
	public static final String VENUE_LOCATION_LATITUDE_KEY = "lat";
	public static final String VENUE_LOCATION_LONGITUDE_KEY = "lng";
	public static final String VENUE_LOCATION_DISTANCE_KEY = "distance";
	public static final String VENUE_LOCATION_CC_KEY = "cc";
	public static final String VENUE_LOCATION_COUNTRY_KEY = "country";

	// database data
	public static final String DATABASE_NAME_KEY = "venues_db";
	public static final int DATABASE_VERSION = 1;
	public static final String VENUES_TABLE_NAME_KEY = "VenueEntity";
	public static final String LOCATION_TABLE_NAME_KEY = "LocationEntity";

}
