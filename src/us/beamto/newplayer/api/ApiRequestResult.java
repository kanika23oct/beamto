package us.beamto.newplayer.api;

public class ApiRequestResult {

	public static final int NO_ERROR = 0;
    public static final int ERROR_PARSING_SERVER_ANSWER = 1;
    public static final int ERROR_SENDING_REQUEST_TO_SERVER = 2;
    public static final int ERROR_INVALID_URL = 3;
    public static final int ERROR_SERVER_CONNECTION = 4;
    public static final int ERROR_WRONG_RESPONSE = 5;

    /**
     * Final result of the call. Shows if API call was successful.
     */
    public boolean success;

    /**
     * String message received from server.
     */
    public String message;

    /**
     * HTTP status code.
     */
    public int statusCode;

    /**
     * Result code. If it is != {@link #NO_ERROR} proper user notification is required.
     */
    public int resultCode;

    /**
     * Container for extra result object created on parsing request result.
     */
    public Object resultObject;
    

    public ApiRequestResult() {
        success = false;
        statusCode = 0;
        resultCode = NO_ERROR;
    }

    public ApiRequestResult(boolean pSuccess, String pMessage, int pStatusCode) {
        success = pSuccess;
        message = pMessage;
        statusCode = pStatusCode;
    }

    /**
     * Check HTTP status code.
     * @return true if HTTP status is successful (is of 200 family)
     */
    public boolean isStatusSuccessful() {
    	return statusCode >= 200 && statusCode <= 299;
    }

    
}
