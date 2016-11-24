package com.example.amank.lifeconnect;

/**
 * Created by Yogesh on 11/13/2016.
 */

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

public class TabFragment2 extends Fragment {
    static final int SocketServerPORT = 8080;
    LinearLayout loginPanel, chatPanel;
    EditText editTextUserName;
    Button buttonConnect;
    TextView chatMsg, textPort;
    EditText editTextSay;
    Button buttonSend;
    Button buttonDisconnect;
    String msgLog = "";
    ChatClientThread chatClientThread = null;
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

    private class ChatClientThread extends Thread {

        String name;
        String dstAddress;
        int dstPort;

        String msgToSend = "";
        boolean goOut = false;

        ChatClientThread(String name, String address, int port) {
            this.name = name;
            dstAddress = address;
            dstPort = port;
        }

        @Override
        public void run() {
            Socket socket = null;
            DataOutputStream dataOutputStream = null;
            DataInputStream dataInputStream = null;

            try {
                socket = new Socket(dstAddress, dstPort);
                dataOutputStream = new DataOutputStream(
                        socket.getOutputStream());
                dataInputStream = new DataInputStream(socket.getInputStream());
                dataOutputStream.writeUTF(name);
                dataOutputStream.flush();

                while (!goOut) {
                    if (dataInputStream.available() > 0) {
                        msgLog += dataInputStream.readUTF();

                        getActivity().runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                chatMsg.setText(msgLog);
                            }
                        });
                    }

                    if (!msgToSend.equals("")) {
                        dataOutputStream.writeUTF(msgToSend);
                        dataOutputStream.flush();
                        msgToSend = "";
                    }
                }

            } catch (UnknownHostException e) {
                e.printStackTrace();
                final String eString = e.toString();
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), eString, Toast.LENGTH_LONG).show();
                    }

                });
            } catch (IOException e) {
                e.printStackTrace();
                final String eString = e.toString();
                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), eString, Toast.LENGTH_LONG).show();
                    }

                });
            } finally {
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataOutputStream != null) {
                    try {
                        dataOutputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                if (dataInputStream != null) {
                    try {
                        dataInputStream.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }

                getActivity().runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        loginPanel.setVisibility(View.VISIBLE);
                        chatPanel.setVisibility(View.GONE);
                    }

                });
            }

        }

        private void sendMsg(String msg) {
            msgToSend = msg;
        }

        private void disconnect() {
            goOut = true;
        }
    }
}