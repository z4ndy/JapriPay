package com.masterweb.japripay.chat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.hardware.Camera;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bistri.api.Conference;
import com.bistri.api.DataStream;
import com.bistri.api.MediaStream;
import com.bistri.api.PeerStream;
import com.masterweb.japripay.R;
import com.masterweb.japripay.chat.function.MediaStreamLayout;

import java.nio.ByteBuffer;
import java.util.ArrayList;

public class RoomActivity extends AppCompatActivity implements View.OnClickListener, Conference.Listener, PeerStream.Handler {
    private static final String TAG = "MainActivity";
    private static final String DEFAULT_ROOM_NAME = "androidroom";
    private static final int PERMISSIONS_CAMERA_AND_MICROPHONE = 1;

    // Members
    private EditText room_edit;
    private String  room_name;
    private Button join_button;
    private TextView status;
    private ImageView loader_spinner;
    private MediaStreamLayout call_layout;
    private LinearLayout room_layout;
    private boolean orientation_landscape;
    private Spinner select_camera;
    private boolean in_call;

    private boolean permissions;
    private Conference conference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_room);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        in_call = false;
        room_edit = ( EditText )findViewById( R.id.room_name );
        join_button = ( Button )findViewById( R.id.join_button );
        status = ( TextView )findViewById( R.id.status );
        loader_spinner = ( ImageView )findViewById( R.id.loader_spinner );
        call_layout = ( MediaStreamLayout )findViewById( R.id.call_layout );
        room_edit.setText(DEFAULT_ROOM_NAME);
        room_layout = ( LinearLayout )findViewById( R.id.room_layout );
        orientation_landscape = (getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE);
        if ( orientation_landscape ) {
            room_layout.setOrientation(LinearLayout.HORIZONTAL);
        } else {
            room_layout.setOrientation(LinearLayout.VERTICAL);
        }
        select_camera = ( Spinner )findViewById( R.id.select_camera );


        // Force to use only Alphanumeric characters.
        InputFilter filter = new InputFilter() {
            public CharSequence filter(CharSequence source, int start, int end,
                                       Spanned dest, int dstart, int dend) {
                if ( !source.toString().matches("[a-zA-Z0-9_-]*") ) {
                    return "";
                }
                return null;
            }
        };
        room_edit.setFilters(new InputFilter[]{filter});
        // Set keyboard action
        room_edit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND) {
                    join_button.performClick();
                    return true;
                }
                return false;
            }
        });

        // Set button listener
        join_button.setOnClickListener(this);


        conference = null;
        permissions = false;

        initConference();
    }
    private boolean checkPermission() {

        permissions = (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) &&
                (ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED);

        if ( !permissions ) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA, Manifest.permission.RECORD_AUDIO}, PERMISSIONS_CAMERA_AND_MICROPHONE);
        }

        return permissions;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSIONS_CAMERA_AND_MICROPHONE) {

            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                initConference();

            } else {

                // permission denied
                Toast.makeText(this, R.string.permissions_error, Toast.LENGTH_LONG).show();
                finish();
            }
            return;
        }
    }

    protected void initCameraList() {


        if ( conference.getCameraNb() == 0 ) {
            select_camera.setVisibility( View.GONE );
        } else {

            class CameraAdapter extends ArrayAdapter<Camera.CameraInfo> {
                public CameraAdapter(Context context, ArrayList<Camera.CameraInfo> infos) {
                    super(context, android.R.layout.simple_spinner_dropdown_item, infos);
                }

                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    if(convertView == null)
                        convertView = View.inflate(getContext(),
                                android.R.layout.simple_spinner_dropdown_item,
                                null);

                    TextView tvText1 = (TextView)convertView.findViewById(android.R.id.text1);
                    Camera.CameraInfo info = getItem( position );
                    String content = "Camera "  + position + " " + ( info.facing == Camera.CameraInfo.CAMERA_FACING_BACK ? "(back)" : "(front)" );
                    tvText1.setText( content );

                    return convertView;
                }

                @Override
                public View getDropDownView(int position, View convertView, ViewGroup parent) {
                    return getView(position, convertView, parent);
                }
            }


            select_camera.setAdapter( new CameraAdapter ( this, conference.getCameraInfos() ) );
            select_camera.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                    conference.setCameraId( position );
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {}
            });
            select_camera.setSelection( conference.getCameraId() );
        }
    }

    private void initConference()
    {
        if ( !checkPermission() ) {
            return;
        }

        //
        onResumeConference();

        initCameraList();

        // Conference
        conference.setInfo( "487fb755", "7b2d0a68d4a7e45e59568d59b22121ab" );

        conference.setVideoOption( Conference.VideoOption.MAX_WIDTH, 320 );
        conference.setVideoOption( Conference.VideoOption.MAX_HEIGHT, 240 );
        conference.setVideoOption( Conference.VideoOption.MAX_FRAME_RATE, 30 );

        conference.setAudioOption( Conference.AudioOption.PREFERRED_CODEC, Conference.AudioCodec.ISAC );
        conference.setAudioOption( Conference.AudioOption.PREFERRED_CODEC_CLOCKRATE, 16000 );

        conference.setLoudspeaker( true );

        conference.connect();
    }

    void showLoaderSpinner( boolean visible )
    {
        int visibility = visible ? View.VISIBLE : View.GONE;

        if ( loader_spinner.getVisibility() == visibility ) return; // Nothing to do

        if ( visible ) {
            // Initialize loading spinner animation
            Animation rotation = AnimationUtils.loadAnimation(this, R.anim.rotate);
            if (rotation != null) loader_spinner.startAnimation(rotation);
        } else {
            loader_spinner.clearAnimation();
        }
        loader_spinner.setVisibility( visibility );
    }


    private void onResumeConference()
    {
        if ( permissions ) {
            conference = Conference.getInstance( getApplicationContext() );
            conference.addListener( this );
            onConnectionEvent(conference.getStatus());
        }
    }

    @Override
    protected void onResume()
    {
        super.onResume();

        onResumeConference();
    }

    @Override
    protected void onPause()
    {
        Log.d(TAG, "onPause");

        if (conference != null) {
            conference.removeListener( this );
        }

        super.onPause();
    }

    @Override
    protected void onDestroy()
    {
        Log.d(TAG, "onDestroy");

        conference.disconnect();
        super.onDestroy();
    }

    private void updateViewAccordingState(Conference.Status state) {

        if ( state == Conference.Status.DISCONNECTED ) {
            status.setText( R.string.disconnected );
        } else if ( ( state == Conference.Status.CONNECTING ) || ( state == Conference.Status.CONNECTING_SENDREQUEST ) ) {
            status.setText( R.string.connecting );
        } else if ( state == Conference.Status.CONNECTED ) {
            status.setText( R.string.connected );
        }

        boolean showLoader = ( state == Conference.Status.CONNECTING ) || ( state == Conference.Status.CONNECTING_SENDREQUEST );
        showLoaderSpinner( showLoader );

        room_edit.setEnabled(state == Conference.Status.CONNECTED);
        join_button.setEnabled(state == Conference.Status.CONNECTED);
    }

    private void setInCall( boolean inCall ){
        room_layout.setVisibility( inCall ? View.GONE : View.VISIBLE );
        call_layout.setVisibility( inCall ? View.VISIBLE : View.GONE );
        if ( inCall ) {
            hideKeyboard();
        } else {
            call_layout.removeAllMediaStream();
        }
        in_call = inCall;
    }

    @Override
    public void onBackPressed() {
        Log.w(TAG, "onBackPressed");

        if ( in_call ) {
            conference.leave( room_name );
            return;
        }
        super.onBackPressed();
    }

    @Override
    public void onClick( View view )
    {
        Log.d(TAG, "onClick");

        switch( view.getId() ) {
            case R.id.join_button:

                room_name = room_edit.getText().toString();

                if ( room_name.length() == 0 ) {
                    Toast.makeText(this, R.string.create_input_error, Toast.LENGTH_SHORT).show();
                    return;
                }

                if ( conference.getStatus() == Conference.Status.CONNECTED ) {
                    conference.join( room_name );
                    setInCall( true );
                    return;
                } else {
                    Log.w( TAG, "Cannot join room : not connected");
                }
                break;
        }
    }

    /*
     *       Listener implementation
     */

    @Override
    public void onConnectionEvent(Conference.Status state) {

        updateViewAccordingState(state);
    }

    @Override
    public void onError(Conference.ErrorEvent error) {
        if ( error == Conference.ErrorEvent.CONNECTION_ERROR ) {
            Toast.makeText(getApplicationContext(), "Connection error", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onRoomJoined(String roomName) {
        setInCall( true );
        conference.getMembers( roomName );
    }

    @Override
    public void onRoomQuitted(String roomName) {
        setInCall( false );
    }

    @Override
    public void onNewPeer( PeerStream peerStream ) {
        peerStream.setHandler( this );
        /*

        // Open dataChannel example
        if ( !peerStream.isLocal() ) {
            // Open dataChannel on remote peer stream
            peerStream.openDataChannel( "testDataChannelAndroid" );
        }
        */
    }

    @Override
    public void onRemovedPeer(PeerStream peerStream) {
        if ( !peerStream.hasMedia() )
            return;

        MediaStream mediaStream = peerStream.getMedia();
        call_layout.removeMediaStream(mediaStream);
    }

    @Override
    public void onMediaStream(String peerId, MediaStream mediaStream) {
        call_layout.addMediaStream(mediaStream);
    }

    @Override
    public void onDataStream(String peer_id, DataStream dataStream) {

        Log.d( TAG, "onDataStream peer_id:" + peer_id + " label:" + dataStream.getLabel() );
        dataStream.setHandler( new DataStream.Handler() {
            @Override
            public void onOpen(DataStream myself) {
                Log.d( TAG, "DataStream.Handler.onOpen" );
            }

            @Override
            public void onMessage(DataStream myself, ByteBuffer message, boolean binary) {
                Log.d( TAG, "DataStream.Handler.onMessage length:" + message.capacity() );
                // Testing : Do an echo for text message
                if ( !binary ) {
                    byte[] b = new byte[message.remaining()];
                    message.get(b);

                    myself.send( "(android echo) " +  new String(b));
                }
            }

            @Override
            public void onClose(DataStream myself) {
                Log.d( TAG, "DataStream.Handler.onClose" );
            }

            @Override
            public void onError(DataStream myself, String error) {
                Log.d( TAG, "DataStream.Handler.onError : " + error );
            }
        });
    }

    @Override
    public void onPresence(String peerId, Conference.Presence presence) {}

    @Override
    public void onIncomingRequest(String peerId, String peerName, String room, String event) {
        Log.v( TAG, "peerId:" + peerId + " peerName:" + peerName + " room:" + room + " event:" + event );
    }

    @Override
    public void onRoomMembers(String roomName, ArrayList<Conference.Member> members) {
        for (int i = 0; i < members.size(); i++) {
            Conference.Member member =  members.get(i);
            Log.v( TAG, "member id:" + member.id() + " name:" + member.name() );
        }
    }

    @Override
    public void onPeerJoinedRoom(String roomName, String peerId, String peerName) {
        Log.w( TAG, "A member has join the room " + roomName + " : " +peerName + " (" + peerId  + ")" );
    }

    @Override
    public void onPeerQuittedRoom(String roomName, String peerId) {
        Log.w( TAG, "A member has left the room " + roomName + " (" + peerId  + ")" );
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig)
    {
        super.onConfigurationChanged(newConfig);

        orientation_landscape = (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE);
        room_layout.setOrientation(orientation_landscape ? LinearLayout.HORIZONTAL : LinearLayout.VERTICAL);
        call_layout.onOrientationChange();
    }

    public void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(room_edit.getWindowToken(), 0);
    }
}