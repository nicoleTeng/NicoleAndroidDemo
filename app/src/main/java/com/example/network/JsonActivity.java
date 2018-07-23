package com.example.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.util.JsonTools;

public class JsonActivity extends Activity {
    private static final String TAG = "JsonActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        TextView textView = new TextView(this);
        textView.setText("JsonActivity");
        setContentView(textView);

        test();
        testPersonsJson();
        testMapJson();
    }
    
    private void test() {
        Person person = new Person(1, "xiaoluo", "shenzhen");
        String personString = JsonTools.getJsonString("person", person);
        Log.v(TAG, "txh personString = " + personString.toString());
    }
    
    public void testPersonsJson() {
        List<Person> persons = new ArrayList<Person>();
        Person person = new Person(1, "xiaoluo", "����");
        Person person2 = new Person(2, "android", "�Ϻ�");
        persons.add(person);
        persons.add(person2);
        String personsString = JsonTools.getJsonString("persons", persons);
        Log.v(TAG, "txh personString = " + personsString);

    }
    
    public void testMapJson() {
        List<Map<String, String>> list = new ArrayList<Map<String, String>>();
        Map<String, String> map1 = new HashMap<String, String>();
        map1.put("id", "001");
        map1.put("name", "xiaoluo");
        map1.put("age", "20");
        Map<String, String> map2 = new HashMap<String, String>();
        map2.put("id", "002");
        map2.put("name", "android");
        map2.put("age", "33");
        list.add(map1);
        list.add(map2);
        String listString = JsonTools.getJsonString("list", list);
        Log.v(TAG, "txh listString = " + listString);
        
//        JSONObject jsonObject = JsonTools.getJsonObject("list", list);
//        JSONArray listArray = jsonObject.getJSONArray("list");
//        List<Map<String, String>> list2 = (List<Map<String, String>>) listArray.toCollection(listArray, Map.class);
//        System.out.println(list2);
    }
}
