package com.zxc.pocketsphinx;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.zxc.pocketsphinx.util.AudioRecord.AudioRecordButton;
import com.zxc.pocketsphinx.util.PocketSphinxUtil;
import com.zxc.pocketsphinx.util.Word;
import com.zxc.pocketsphinx.util.wordUtil;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Random;

import edu.cmu.pocketsphinx.demo.RecognitionListener;

public class MainActivity_nnn extends Activity implements RecognitionListener {

    private AudioRecordButton imgbtn_say;
    private Context context;
    private TextView word_textview;
    private String wordname;
    private String folder;
    private Button button;
    private Spinner spinner;
    private ArrayAdapter<String> adapter;
    private String SpinnerFolder;
    public final String [] language ={"机器语言","汇编","c语言","c++语言","java语言"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SpinnerPick();
        getMinimalPairs();
        context = MainActivity_nnn.this;
        PocketSphinxUtil.get(context).setListener(this);
        imgbtn_say = (AudioRecordButton) findViewById(R.id.imgbtn_say);
        imgbtn_say.setAudioRecordFinishListener(new MyAudioRecordFinishListener());
        nextActivity();
        Log.i("DICT2", "onCreate");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onPartialResults(String b) {
//        //一次识别之后，停止语音引擎，下次长按再开启
//        PocketSphinxUtil.get(context).stop();
//        Log.i("result", b);
//        Log.i("result", wordname);
//        if (b.equalsIgnoreCase(wordname)) {
//            Log.i("result", "Correct");
//            Context context = getApplicationContext();
//            CharSequence text = "Yes, correct!";
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
//            go2nextActivity();
//        } else {
//            Log.i("result", "inCorrect");
//            Context context = getApplicationContext();
//            CharSequence text = "Sorry, incorrect";
//            int duration = Toast.LENGTH_SHORT;
//            Toast toast = Toast.makeText(context, text, duration);
//            toast.show();
////            Intent intent = new Intent(context, SettingActivity.class);
////            startActivity(intent);
//        }
    }

    @Override
    public void onResults(String b) {
        //一次识别之后，停止语音引擎，下次长按再开启
        PocketSphinxUtil.get(context).stop();
        Log.i("result", b);
        Log.i("result", wordname);
        if (b.equalsIgnoreCase(wordname)) {
            Log.i("result", "Correct");
            Context context = getApplicationContext();
            CharSequence text = "Yes, correct!";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
            go2nextActivity();
        } else {
            Log.i("result", "inCorrect");
            Context context = getApplicationContext();
            CharSequence text = "Sorry, incorrect";
            int duration = Toast.LENGTH_SHORT;
            Toast toast = Toast.makeText(context, text, duration);
            toast.show();
//            Intent intent = new Intent(context, SettingActivity.class);
//            startActivity(intent);
        }
    }

    @Override
    public void onError(int err) {
    }


    class MyAudioRecordFinishListener implements AudioRecordButton.AudioRecordFinishListener {
        @Override
        public void onFinish() {
            // TODO Auto-generated method stub
        }
    }



    public void getMinimalPairs(){
        String cate = "aa&ee";
        String JSONfileName = "voice/en-us/JP_EN_Minimal_Pairs_oneword_" + cate + ".json";
        Log.i("data2", JSONfileName);
//        String jsonFileString = wordUtil.getJsonFromAssets(getApplicationContext(), "voice/en-us/JP_EN_Minimal_Pairs_record.json");
        String jsonFileString = wordUtil.getJsonFromAssets(getApplicationContext(), JSONfileName);
        Log.i("data", jsonFileString);
        Gson gson = new Gson();
        Type listUserType = new TypeToken<List<Word>>() { }.getType();
        List<Word> users = gson.fromJson(jsonFileString, listUserType);
        final int min = 0;
        final int max = users.size()-1;
        final int random = new Random().nextInt((max - min) + 1) + min;
        String[] temp;
        String str = users.get(random).toString();
        temp = str.split("-");
        wordname = temp[0];
        folder = temp[1];
        Log.i("DICT3", folder);
        word_textview = (TextView)findViewById(R.id.word);
        word_textview.setText(wordname);
        writeData(wordname);
        writeData_Folder(folder);
        Log.i("write", wordname + " " + folder);
//            Log.i("data", "> Item " + i + "\n" + users.get(i));
    }

    //实现setname()方法，设置变量的值
    public void setWordname(String name) {
        this.wordname = name;
    }

    //实现getname()方法，获取变量的值
    public String getWordname() {
        return wordname;
    }

    private void writeData(String wordname) {
        String filePath = "/sdcard/SphinxTest/";
        String fileName = "data.txt";
        writeTxtToFile(wordname, filePath, fileName);
    }
    private void writeData_Folder(String folder) {
        String filePath = "/sdcard/SphinxTest/";
        String fileName = "data_Folder.txt";
        writeTxtToFile(folder, filePath, fileName);
    }

    // 将字符串写入到文本文件中
    private void writeTxtToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        makeFilePath(filePath, fileName);
        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写
        String strContent = strcontent;
        try {
            File file = new File(strFilePath);
            if (!file.exists()) {
                Log.d("TestFile", "Create the file:" + strFilePath);
                file.getParentFile().mkdirs();
                file.createNewFile();
            }
            RandomAccessFile raf = new RandomAccessFile(file, "rwd");
//            raf.seek(file.length());
            raf.setLength(0);
            raf.write(strContent.getBytes());
            raf.close();
        } catch (Exception e) {
            Log.e("TestFile", "Error on write File:" + e);
        }
    }

//生成文件

    private File makeFilePath(String filePath, String fileName) {
        File file = null;
        makeRootDirectory(filePath);
        try {
            file = new File(filePath + fileName);
            if (!file.exists()) {
                file.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

//生成文件夹

    private static void makeRootDirectory(String filePath) {
        File file = null;
        try {
            file = new File(filePath);
            if (!file.exists()) {
                file.mkdir();
            }
        } catch (Exception e) {
            Log.i("error:", e + "");
        }
    }

    public  void go2nextActivity(){
        PocketSphinxUtil.get(context).stop();
        Intent intent = new Intent(MainActivity_nnn.this, MainActivity_nnn.class);
        intent.putExtra("Spinner", SpinnerFolder);
        startActivity(intent);
    }
    public void nextActivity(){
        button = (Button) findViewById(R.id.next_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                // Code here executes on main thread after user presses button
                Log.i("DICT","Click");
                go2nextActivity();
            }
        });

    }

    public void SpinnerPick(){
//        final String [] langurage ={"机器语言","汇编","c语言","c++语言","java语言"};
        spinner = (Spinner)findViewById(R.id.spinner);
        adapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, language);
        //设置下拉列表风格
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        //将适配器添加到spinner中去
        spinner.setAdapter(adapter);
        spinner.setVisibility(View.VISIBLE);//设置默认显示
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1,
                                       int arg2, long arg3) {
                // TODO Auto-generated method stub
                SpinnerFolder = language[arg2];
                Log.i("Spinner", SpinnerFolder);
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
                // TODO Auto-generated method stub

            }
        });
    }

    public int getArrayIndex(String[] arr,String value) {

        int k=0;
        for(int i=0;i<arr.length;i++){

            if(arr[i]==value){
                k=i;
                break;
            }
        }
        return k;
    }

}
