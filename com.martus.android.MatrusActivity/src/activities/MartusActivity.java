package activities;

import java.util.Vector;
import org.martus.clientside.ClientSideNetworkGateway;
import org.martus.clientside.ClientSideNetworkHandlerUsingXmlRpcForNonSSL;
import org.martus.common.crypto.MockMartusSecurity;
import org.martus.common.network.NetworkResponse;
import org.martus.common.network.NonSSLNetworkAPI;
import com.martus.android.R;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class MartusActivity extends Activity {
        
    String serverIP = "66.201.46.82";
    String serverIPNew = "50.112.118.184";
    TextView serverCode;
    TextView serverVersion;
    
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        setTitle("Martus Android");
         
        final Button button = (Button) findViewById(R.id.gotoPing);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    Intent intent = new Intent(MartusActivity.this, activities.pingme.class);
                    startActivity(intent);
                    } catch (Exception e) {
                    Log.e("error", "Failed starting pingme activity");
                    e.printStackTrace();
                }
            }
        });
        serverCode = new TextView(this); 
        serverCode=(TextView)findViewById(R.id.serverResponseCode); 
        serverVersion = new TextView(this); 
        serverVersion=(TextView)findViewById(R.id.serverResponseVersion); 
        
        final Button buttonServerInfo = (Button) findViewById(R.id.serverInfo);
        buttonServerInfo.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                try {
                    MockMartusSecurity security = new MockMartusSecurity();
                    security.createKeyPair();
                    NonSSLNetworkAPI server = new ClientSideNetworkHandlerUsingXmlRpcForNonSSL(serverIPNew);
                    String serverPublicKey = server.getServerPublicKey(security);
                    ClientSideNetworkGateway gateway = ClientSideNetworkGateway.buildGateway(serverIPNew, serverPublicKey);
                    NetworkResponse response = gateway.getServerInfo();
                    Log.d("martus" , "NetworkResponse getResultCode: "+ response.getResultCode());
                    serverCode.setText("ServerInfo resultCode ("+response.getResultCode() +")");
                    
                    Vector result = response.getResultVector();
                      for(int i = 0; i < result.size(); ++i)
                        {
                          Object[] summary = (Object[]) result.get(i);
                          Log.d("martus" , "NetworkResponse data sixe" + summary.length);
                          for(int l = 0; l < summary.length; ++l){
                              Log.d("martus" , "NetworkResponse getResultVector data: "+ summary[l]);
                              serverVersion.setText("ServerInfo version ("+summary[l].toString()+")");
                          }
                        }
                    } catch (Exception e) {
                    Log.e("error", "Failed starting MockMartusSecurity");
                    e.printStackTrace();
                }
            }
        });
    }
    
}