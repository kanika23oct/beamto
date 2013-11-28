package us.beamto.newplayer.api;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseApiRequest {

	 protected HttpClient mClient;
	    protected URI mUri;
	    protected List<NameValuePair> mParams;
	    protected byte[] mBinaryData;
	    protected String mBinaryParamName;


	    protected BaseApiRequest() {
	       // super();
	        System.out.println("****** Service Call Constructor  *****" );
	        HttpParams httpParameters = new BasicHttpParams();
	        HttpConnectionParams.setConnectionTimeout(httpParameters, 30000); 
	        HttpConnectionParams.setSoTimeout(httpParameters, 60000); 
	        mClient = new DefaultHttpClient(httpParameters);
	        
	    }
	    
	    protected BaseApiRequest(HttpClient httpClient) {
	        super();
	        mClient = httpClient;
	    }

	    /**
	     * Call this before filling any params, so older list won't affect the request in case your object is reused.
	     */
	    protected void resetParams() {
	        if (mParams != null)
	            mParams.clear();
	    }

	    protected void putLongJsonArrayParam(List<Long> list, String paramName) {
	        if (list != null && list.size() > 0) {
	            JSONArray a = new JSONArray();
	            for (Long l: list) {
	                a.put(l);
	            }
	            addParam(paramName, a.toString());
	        }
	    }

	    /**
	     * Actual call is action specific and implemented either in {@link PostApiRequest} or {@link GetApiRequest}
	     * @return Apache library response object
	     * @throws IOException
	     */
	    protected abstract HttpResponse startRequest() throws IOException;

	    /**
	     * Implement this in each API reqest class to define target URL
	     * @return Apache library response object
	     */    
	    protected abstract String getUrl();

	    /**
	     * Add HTTP parameter to call.
	     * @param paramName parameter name
	     * @param paramValue parameter value. If null, empty string is used as value
	     */
	    public void addParam(String paramName, Object paramValue) {
	    	if (mParams == null)
	    		mParams = new ArrayList<NameValuePair>();
	        if (paramValue == null) {
	            mParams.add(new BasicNameValuePair(paramName, ""));
	        } else {
	            mParams.add(new BasicNameValuePair(paramName, paramValue.toString()));
	        }
	    }

	    private void setBinaryData(byte[] data) {
	        mBinaryData = data;
	    }

	    public void setBinaryData(byte[] data, String binaryParamValue) {
	        setBinaryData(data);
	        mBinaryParamName = binaryParamValue;
	    }

	    /**
	     * Find value for previously stored HTTP parameter
	     * @param paramName name of parameter
	     * @return parameter value if parameter exists or null
	     *
	     * @see #addParam(String, Object)
	     */
	    protected String findParam(String paramName) {
	        if (mParams == null)
	            return null;
	        for (NameValuePair pair: mParams) {
	            if (pair.getName().equals(paramName))
	                return pair.getValue();
	        }
	        return null;
	    }

	    /**
	     * Performs request initialization adding server specific HTTP headers
	     * @param req request to initialize
	     */
	    protected void addHeaders(HttpRequestBase req) {
	        //example:	
	        //req.addHeader("version", "100");
	    }

	    /**
	     * Processes request provided as JSON object
	     * @param requestResult common result object for API operations
	     * @param data JSON from server
	     * @throws JSONException
	     */
	    protected void parseJsonResponse(ApiRequestResult requestResult, JSONObject data) throws JSONException {
	        requestResult.success = requestResult.isStatusSuccessful();
	    }

	    /**
	     * Processes response from server. Tries to read response as JSON object and calls {@link #parseJsonResponse(ApiRequestResult, org.json.JSONObject)}.
	     * When overriding this in descendants, do not call super() or HttpEntity will be consumed and unavailable for alternative processing.
	     * @param requestResult common result object for API operations
	     * @param entity HttpEntity of the response
	     * @throws IOException
	     * @throws JSONException
	     */
	    protected void parseResponse(ApiRequestResult requestResult, HttpEntity entity) throws IOException, JSONException {
	        String reply = EntityUtils.toString(entity);
	        JSONObject data = new JSONObject(reply);
	        parseJsonResponse(requestResult, data);
	    }

	    /**
	     * Called right after request is done to process HTTP response codes. Default behaviour is to set result.success = true
	     * if response code is in 200..299
	     * @param statusCode HTTP status code
	     * @param result common result object for API operations
	     */
	    protected void processStatusCodes(int statusCode, ApiRequestResult result) {
	        result.statusCode = statusCode;
	        result.success = result.isStatusSuccessful();
	    }

	    /**
	     * Creates and initializes common result object. Override this in descendants if result needs to hold some
	     * response data regardless of request flow.
	     * @return new result object
	     */
	    protected ApiRequestResult initRequestResult() {
	        return new ApiRequestResult();
	    }

	    /**
	     * Performs actual request to server. All parameters should be set before this method call.
	     * @param url full remote URL
	     * @return common result object, created by {@link #initRequestResult()} and filled in
	     * {@link #parseResponse(ApiRequestResult, org.apache.http.HttpEntity)}
	     */
	    public ApiRequestResult doRequest() {
	        ApiRequestResult result = initRequestResult();
	        try {
	            mUri = new URI(getUrl());
	            HttpResponse response = startRequest();
	            HttpEntity entity = response.getEntity();
	            processStatusCodes(response.getStatusLine().getStatusCode(), result);
	            if (!result.isStatusSuccessful()) {
	                result.message = response.getStatusLine().getReasonPhrase();
	            }
	            try {
	                parseResponse(result, entity);
	            } catch (JSONException e) {
	                result.success = false;
	                if (result.isStatusSuccessful()) {
	                    result.resultCode = ApiRequestResult.ERROR_PARSING_SERVER_ANSWER;
	                }
	            }
	        } catch (IOException e) {
	            result.success = false;
	            result.resultCode = ApiRequestResult.ERROR_SENDING_REQUEST_TO_SERVER;
	        } catch (URISyntaxException e) {
	            result.success = false;
	            result.resultCode = ApiRequestResult.ERROR_INVALID_URL;
	         } catch (Exception e) {
	            result.success = false;
	            result.resultCode = ApiRequestResult.ERROR_SERVER_CONNECTION;
	        }
	        return result;
	    }

	    
}
