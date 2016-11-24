package com.example.amank.lifeconnect;

import android.app.NotificationManager;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.NotificationCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Created by Aman K on 11/23/2016.
 */

public class Patient_Broadcast extends Fragment {

    private static String email;
    private static String strPatientName;

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
    NotificationCompat.Builder mBuilder;



    public static Patient_Broadcast newInstance(String s, String name) {
        Patient_Broadcast fragment = new Patient_Broadcast();
        email = s;
        strPatientName = name;
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.patient_broadcast, container, false);
        loginPanel = (LinearLayout) rootView.findViewById(R.id.loginpanel);
        chatPanel = (LinearLayout) rootView.findViewById(R.id.chatpanel);

        editTextUserName = (EditText) rootView.findViewById(R.id.username);
        editTextUserName.setText(strPatientName);
//        editTextAddress = (EditText) findViewById(R.id.address);
        //textPort = (TextView) v.findViewById(R.id.port);
        //textPort.setText("port: " + SocketServerPORT);
        buttonConnect = (Button) rootView.findViewById(R.id.connect);
        buttonDisconnect = (Button) rootView.findViewById(R.id.disconnect);
        chatMsg = (TextView) rootView.findViewById(R.id.chatmsg);

        final String IPAddress = "192.168.0.17";

        mBuilder =
                new NotificationCompat.Builder(getContext())
                        .setSmallIcon(R.drawable.notif)
                        .setContentTitle("LifeConnect")
                        .setContentText("You have a new message");

        buttonConnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String textUserName = editTextUserName.getText().toString();
                if (textUserName.equals("")) {
                    Toast.makeText(getActivity(), "Enter User Name",
                            Toast.LENGTH_LONG).show();
                    return;
                }
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
        return rootView;


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
                                NotificationManager mNotificationManager =
                                        (NotificationManager)getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                                mNotificationManager.notify(0,mBuilder.build());
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