package com.example.amank.lifeconnect;

/**
 * Created by Yogesh on 11/13/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class TabFragment2 extends Fragment {
    private String strPatientName, strDoctorName;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.tab_fragment_2, container, false);
        strPatientName = getArguments().getString("Patient Name");
        strDoctorName = getArguments().getString("doctorName");

        loginPanel = (LinearLayout) v.findViewById(R.id.loginpanel);
        chatPanel = (LinearLayout) v.findViewById(R.id.chatpanel);

        editTextUserName = (EditText) v.findViewById(R.id.username);
//        editTextAddress = (EditText) findViewById(R.id.address);
        textPort = (TextView) v.findViewById(R.id.port);
        textPort.setText("port: " + SocketServerPORT);
        buttonConnect = (Button) v.findViewById(R.id.connect);
        buttonDisconnect = (Button) v.findViewById(R.id.disconnect);
        chatMsg = (TextView) v.findViewById(R.id.chatmsg);

        final String IPAddress = "192.168.0.17";

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textUserName = editTextUserName.getText().toString();
                if (textUserName.equals("")) {
                    Toast.makeText(getActivity(), "Enter User Name",
                            Toast.LENGTH_LONG).show();
                    return;
                }
//                String textAddress = editTextAddress.getText().toString();
//                if (textAddress.equals("")) {
//                    Toast.makeText(MainActivity.this, "Enter Addresse",
//                            Toast.LENGTH_LONG).show();
//                    return;
//                }
                msgLog = "";
                chatMsg.setText(msgLog);
                loginPanel.setVisibility(View.GONE);
                chatPanel.setVisibility(View.VISIBLE);

                chatClientThread = new ChatClientThread(
                        textUserName, IPAddress, SocketServerPORT);
                chatClientThread.start();
            }
        });

        buttonDisconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chatClientThread == null) {
                    return;
                }
                chatClientThread.disconnect();
            }
        });


        editTextSay = (EditText) v.findViewById(R.id.say);
        buttonSend = (Button) v.findViewById(R.id.send);

        buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextSay.getText().toString().equals("")) {
                    return;
                }

                if (chatClientThread == null) {
                    return;
                }

                chatClientThread.sendMsg(editTextSay.getText().toString() + "\n");

            }
        });

        return v;
    }
}