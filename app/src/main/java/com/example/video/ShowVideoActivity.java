package com.example.video;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.example.nicole.R;
/**
 * Created by alanjet on 2016/7/24.
 */
public class ShowVideoActivity extends Activity {
    private GridView gridView;
    private List<Map<String, Object>> items;
    private List<File> videoList ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_video);
        gridView= (GridView) findViewById(R.id.gv_show_video);
        videoList = new ArrayList<File>();
        GetFiles();
        SimpleAdapter adapter = new SimpleAdapter(this,items,R.layout.item_show_video,
                new String[]{"imageItem", "textItem"},new int[]{R.id.image_item, R.id.text_item});
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/VideoRecorderTest/"+FileToStr(videoList)[position]);
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setDataAndType(uri, "video/*");
                startActivity(intent);
            }
        });
    }

    public String[] FileToStr( List<File> f ){
        ArrayList<String> listStr = new ArrayList<String>();
        int i;
        for (i = 0; i < f.size(); i++) {
            String nameString = f.get(i).getName();
            listStr.add(nameString);
        }
        return listStr.toArray(new String[0]);
    }

    public void GetFiles(  ){
        int i=0;
        File filePath=new File(Environment.getExternalStorageDirectory().getPath()+"/VideoRecorderTest");
        if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            if(filePath.listFiles()!=null){
                File[] files = filePath.listFiles();
                for(i=0;i<files.length;i++){
                    if(files[i].getName().toLowerCase().endsWith(".mp4")) {
                        videoList.add(files[i]);
                    }
                }
            }else{
                Toast.makeText(ShowVideoActivity.this,"hello",Toast.LENGTH_SHORT).show();
            }
        }
        items = new ArrayList<Map<String,Object>>();
        for (int j = 0; j <i; j++) {
            Map<String, Object> item = new HashMap<String, Object>();
            item.put("imageItem", R.drawable.folder);
            item.put("textItem",FileToStr(videoList)[j]);
            items.add(item);
        }
    }

}