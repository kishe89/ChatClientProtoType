package com.example.kjw.chatclientprototype;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


public class BlankFragment extends Fragment implements MsgListener {
    private String mParam1;
    private String mParam2;
    private TextView fr_text;
    private View rootview;
    private MainActivity parent;
    public BlankFragment() {
        // Required empty public constructor
    }
    // TODO: Rename and change types and number of parameters
    public static BlankFragment newInstance(String param1, String param2) {
        BlankFragment fragment = new BlankFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        rootview = inflater.inflate(R.layout.fragment_blank, container, false);
        fr_text = (TextView)rootview.findViewById(R.id.fr_text);
        parent = (MainActivity) getActivity();
        parent.setOnMessageListener(this);
        return rootview;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void OnMessage(final Message message) {
        parent.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Log.e("UITHREAD","on");
                fr_text.setText(message.getMessage());
            }
        });
    }
}
