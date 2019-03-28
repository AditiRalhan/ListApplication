package com.example.listapplication;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    EditText userInp;
    RecyclerView recyclerView;
    RecyclerView recyclerView2;
    List<ResponseMessage> responseMessages;
    ArrayList<ButtonsMsg> buttnmsgs;
    MessageAdapter messageAdapter;
    ButtonAdapter buttonAdapter;
    String json_url ="http://9706ea21.ngrok.io/webhooks/rest/webhook";
    String s2="",s1="",s3="",bttn="";


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        userInp = (EditText) findViewById(R.id.userInput);

        recyclerView = (RecyclerView) findViewById(R.id.conversation);
        responseMessages = new ArrayList<>();
        messageAdapter= new MessageAdapter(responseMessages,getApplicationContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
        recyclerView.setAdapter(messageAdapter);

        recyclerView2 = (RecyclerView) findViewById(R.id.lv);
        buttnmsgs = new ArrayList<>();
        buttonAdapter= new ButtonAdapter(buttnmsgs);


        final String[] test ={"Aditi","deepanshu","shreysh"};

        userInp.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event)
            {
                if(actionId == EditorInfo.IME_ACTION_SEND)
                {
                    final ResponseMessage message = new ResponseMessage(userInp.getText().toString(),true);
                    responseMessages.add(message);

                    JSONObject jsonBodyObj = new JSONObject();
                    try{
                        jsonBodyObj.put("sender", "user");
                        jsonBodyObj.put("message", userInp.getText().toString());
                    }catch (JSONException e){
                        e.printStackTrace();
                    }
                    final String requestBody = jsonBodyObj.toString();


                    StringRequest stringRequest = new StringRequest(Request.Method.POST, json_url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    try {
                                        s2="";
                                        JSONArray jsonArray = new JSONArray(response);
                                        JSONObject jsonObject = jsonArray.getJSONObject(0);

                                        Iterator i = jsonObject.keys();
                                        List<String> keysList = new ArrayList<String>();
                                        while(i.hasNext()) {
                                            String key = (String) i.next();
                                            keysList.add(key);
                                        }

                                        bttn = keysList.get(0);


                                        if(bttn.equals("buttons"))
                                        {
                                            Toast.makeText(getApplicationContext(), "Hellozzz", Toast.LENGTH_SHORT).show();
                                            recyclerView2.setLayoutManager(new LinearLayoutManager(getApplicationContext(),LinearLayoutManager.VERTICAL,false));
                                            recyclerView2.setAdapter(buttonAdapter);
                                            JSONArray k = jsonObject.getJSONArray("buttons");
                                            JSONObject inte = k.getJSONObject(0);

                                            Iterator i1 = inte.keys();
                                            List<String> keysList11 = new ArrayList<String>();
                                            while(i.hasNext()) {
                                                String key = (String) i.next();
                                                ButtonsMsg msg3 = new ButtonsMsg(key);
                                                buttnmsgs.add(msg3);
                                            }



                                            /*JSONArray keys = jsonObject.getJSONArray("buttons");
                                            JSONObject inte = keys.getJSONObject(0);
                                            s3=inte.getString("payload");
                                            try
                                            {
                                                Toast.makeText(getApplicationContext(), "Hellozzz", Toast.LENGTH_SHORT).show();
                                                Intent mo = new Intent(MainActivity.this, Class.forName(s3));
                                                startActivity(mo);
                                            }catch (ClassNotFoundException e) {
                                                e.printStackTrace();
                                            }*/

                                        }

                                        s1= jsonObject.getString("recipient_id");
                                        s2 = jsonObject.getString("text");

                                        ResponseMessage message2 = new ResponseMessage(s2, false);
                                        responseMessages.add(message2);

                                        messageAdapter.notifyDataSetChanged();
                                        if (!isVisible()) {
                                            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                                        }


                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            },
                            new Response.ErrorListener() {
                                @Override
                                public void onErrorResponse(VolleyError error) {
                                    if (error.networkResponse == null)
                                    {
                                        Toast.makeText(getApplicationContext(),"network error",Toast.LENGTH_LONG).show();
                                        error.printStackTrace();
                                    }
                                    else {
                                        Toast.makeText(getApplicationContext(), "error....", Toast.LENGTH_LONG).show();
                                        error.printStackTrace();
                                    }
                                }
                            } )

                    {
                        @Override
                        public String getBodyContentType() {
                            return "application/json; charset=utf-8";
                        }

                        @Override
                        public byte[] getBody() throws AuthFailureError {
                            try {
                                return requestBody == null ? null : requestBody.getBytes("utf-8");
                            } catch (UnsupportedEncodingException uee) {
                                VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                                return null;
                            }
                        }

                    };

                    MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);



                    messageAdapter.notifyDataSetChanged();
                    if(!isVisible()) {
                        recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                    }
                    userInp.getText().clear();
                }
                return true;
            }
        });

    }


    public boolean isVisible()
    {
        LinearLayoutManager linearLayoutManager= (LinearLayoutManager)recyclerView.getLayoutManager();
        int posOfLastVisibleItem = linearLayoutManager.findLastCompletelyVisibleItemPosition();
        int itemcount = recyclerView.getAdapter().getItemCount();
        return (posOfLastVisibleItem==itemcount);
    }


    public void Send(View view)
    {
        final ResponseMessage message = new ResponseMessage(userInp.getText().toString(),true);
        responseMessages.add(message);

        JSONObject jsonBodyObj = new JSONObject();
        try{
            jsonBodyObj.put("sender", "user");
            jsonBodyObj.put("message", userInp.getText().toString());
        }catch (JSONException e){
            e.printStackTrace();
        }
        final String requestBody = jsonBodyObj.toString();


        StringRequest stringRequest = new StringRequest(Request.Method.POST, json_url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            s2="";
                            JSONArray jsonArray = new JSONArray(response);

                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            s1= jsonObject.getString("recipient_id");
                            s2 = jsonObject.getString("text");


                            ResponseMessage message2 = new ResponseMessage(s2, false);
                            responseMessages.add(message2);
                            messageAdapter.notifyDataSetChanged();
                            if (!isVisible()) {
                                recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        if (error.networkResponse == null)
                        {
                            Toast.makeText(getApplicationContext(),"network error",Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                        else {
                            Toast.makeText(getApplicationContext(), "error....", Toast.LENGTH_LONG).show();
                            error.printStackTrace();
                        }
                    }
                } )

        {
            @Override
            public String getBodyContentType() {
                return "application/json; charset=utf-8";
            }

            @Override
            public byte[] getBody() throws AuthFailureError {
                try {
                    return requestBody == null ? null : requestBody.getBytes("utf-8");
                } catch (UnsupportedEncodingException uee) {
                    VolleyLog.wtf("Unsupported Encoding while trying to get the bytes of %s using %s", requestBody, "utf-8");
                    return null;
                }
            }

        };

        MySingleton.getInstance(MainActivity.this).addToRequestQueue(stringRequest);



        messageAdapter.notifyDataSetChanged();
        if(!isVisible()) {
            recyclerView.smoothScrollToPosition(messageAdapter.getItemCount() - 1);
        }
        userInp.getText().clear();
    }


}
