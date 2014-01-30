package us.beamto.newplayer.api;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.ByteArrayBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.params.CoreProtocolPNames;

import java.io.IOException;

/**
 * Api request holding POST-method specific calls.
 */
public abstract class PostApiRequest extends BaseApiRequest {
    protected HttpPost mRequest;

    public PostApiRequest() {
    	super();
    	  System.out.println("****** Service Call Constructor2  *****" );
        mRequest = new HttpPost();
        addHeaders(mRequest);
        mRequest.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);        
    }
    
    public PostApiRequest(HttpClient httpClient) {
    	super(httpClient);
        mRequest = new HttpPost();
        addHeaders(mRequest);
        mRequest.getParams().setParameter(CoreProtocolPNames.USE_EXPECT_CONTINUE, Boolean.FALSE);        
    }

    @Override
    public HttpResponse startRequest() throws IOException {
    	mRequest.setURI(mUri);
    	/*if (mParams != null) {
            if (mBinaryData != null) {
                ByteArrayBody bab = new ByteArrayBody(mBinaryData, mBinaryParamName);
                MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);
                reqEntity.addPart(mBinaryParamName, bab);
                for (NameValuePair nv: mParams) {
                    reqEntity.addPart(nv.getName(), new StringBody(nv.getValue()));
                }
                mRequest.setEntity(reqEntity);
            } else {
                mRequest.setEntity(new UrlEncodedFormEntity(mParams, "UTF-8"));
            }
        }*/
    	
    	System.out.println("******* Params :"+mParams.size());
    	mRequest.setEntity(new UrlEncodedFormEntity(mParams, "UTF-8"));
        return mClient.execute(mRequest);
    }
}
