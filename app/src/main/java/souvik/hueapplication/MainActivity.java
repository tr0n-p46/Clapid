package souvik.hueapplication;

import android.content.DialogInterface;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;

import souvik.hueapplication.HTTP.PHHttpConnection;
import souvik.hueapplication.Log.PHLog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private Button getButton;
    private Button putButton;
    private Button postButton;
    private Button deleteButton;
    private Button usernameButton;

    private EditText responseText;
    private EditText bodyText;
    private EditText resourceText;

    private String IP = "";
    private String username = "newdeveloper";

    private PHHttpConnection connection = new PHHttpConnection();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);

        responseText = (EditText) findViewById(R.id.response_edit_text);
        bodyText = (EditText) findViewById(R.id.body_edit_text);
        resourceText = (EditText) findViewById(R.id.resource_edit_text);

        getButton = (Button) findViewById(R.id.get_button);
        putButton = (Button) findViewById(R.id.put_button);
        postButton = (Button) findViewById(R.id.post_button);
        deleteButton = (Button) findViewById(R.id.delete_button);

        usernameButton = (Button) findViewById(R.id.username_button);
        usernameButton.setOnClickListener(this);

        getButton.setOnClickListener(this);
        putButton.setOnClickListener(this);
        postButton.setOnClickListener(this);
        deleteButton.setOnClickListener(this);

        PHLog.setSdkLogLevel(PHLog.LogLevel.DEBUG);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onClick(View view) {
        resourceText.setEnabled(false);
        resourceText.setEnabled(true);
        PHHttpConnection.RequestMethod method;

        switch (view.getId()) {
            case R.id.get_button:
                method = PHHttpConnection.RequestMethod.GET;
                request(method);
                break;
            case R.id.put_button:
                method = PHHttpConnection.RequestMethod.PUT;
                request(method);
                break;
            case R.id.post_button:
                method = PHHttpConnection.RequestMethod.POST;
                request(method);
                break;
            case R.id.delete_button:
                method = PHHttpConnection.RequestMethod.DELETE;
                request(method);
                break;
            case R.id.username_button:
                username = getUsername();
                break;
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View view = inflater.inflate(R.layout.prompt_ip, null);
        final EditText ipText = (EditText) view.findViewById(R.id.ip_edit_text);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setView(view)
                .setCancelable(false)
                .setPositiveButton("Set", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        IP = ipText.getText().toString().trim();
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        return super.onOptionsItemSelected(item);
    }

    public String getURL() {
        String URL = "http://";
        String resource = resourceText.getText().toString().trim();
        URL = URL + IP + "/api/" + username + "/" + resource;
        return URL;
    }

    public String getBODY() {
        String BODY = bodyText.getText().toString();
        return BODY;
    }

    public void request(final PHHttpConnection.RequestMethod method) {
        final String URL = getURL();
        final String BODY = getBODY();
        new Thread(new Runnable() {
            @Override
            public void run() {
                switch (method) {
                    case GET:
                        setResponseText(connection.getData(URL));
                        break;
                    case PUT:
                        setResponseText(connection.putData(URL, BODY));
                        break;
                    case POST:
                        setResponseText(connection.postData(URL, BODY));
                        break;
                    case DELETE:
                        setResponseText(connection.deleteData(URL));
                        break;
                }
            }
        }).start();
    }

    public void setResponseText(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                responseText.setText(text);
            }
        });
    }

    public String getUsername() {
        String deviceType = "{\"devicetype\":\"Clapid#" + Build.MODEL + "\"}";
        String response = connection.postData("http://" + IP + "/api/", deviceType);
        PHLog.d("TAG", response);
        return "newdeveloper";
    }
}
