package com.example.program4b;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button b1, b2, b3;
    EditText t1,t2;
    boolean fileCreated=false;
    String createFileName=null;

    String previousFileName=null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        b1 = (Button)findViewById(R.id.btnCreate);
        b1.setOnClickListener(this);
        b2 = (Button)findViewById(R.id.btnOpen);
        b2.setOnClickListener(this);
        b3 = (Button)findViewById(R.id.btnSave);
        b3.setOnClickListener(this);
        t1 = (EditText)findViewById(R.id.txtResult);
        t2 = (EditText)findViewById(R.id.editTextTextPersonName2);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            if (!Environment.isExternalStorageManager()) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION);
                startActivity(intent);
                return;
            }
        }

    }

    public void writeToFile(String filename,String content)
    {
        try {
            FileOutputStream fos= new FileOutputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+filename);
            fos.write(content.getBytes());
            fos.close();
        }
        catch(Exception e){
            Toast.makeText(getBaseContext(),"Write error"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
    }
    public String readFromFile(String filename)
    {
        try {
            FileInputStream fis= new FileInputStream(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+filename);
            byte[] data= new byte[fis.available()];
            fis.read(data);
            t1.setText(data.toString());//textbox set with read data
            fis.close();
            return new String(data);
        }
        catch(Exception e)
        {
            Toast.makeText(getBaseContext(),"Read error"+e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
        }
        return null;
    }
    public void onClick(View v)
    {
        if(v.equals(b1))//v equals create
        {
            createFileName=t2.getText().toString();
            File f = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/"+createFileName);
            if (!f.exists()) {
                try {

                    f.createNewFile();
                    t1.setText(f.getAbsolutePath().toString());//set the textbox with abs path of the file
                } catch (IOException e) {
                    Toast.makeText(getBaseContext(),"Create error", Toast.LENGTH_LONG).show();
                }
            }
            fileCreated=true;t1.setText(f.getAbsolutePath().toString());
        }


        else if(v.equals(b2))//v equals open
        {
            if(previousFileName!=null){
                String data= readFromFile(previousFileName);//then read data from previous fileName
                t1.setText(data);//set textbox with read data
            }
            else
                t1.setText(previousFileName);
        }
        else if(v.equals(b3))//v equals save
        {
            if(fileCreated){
                writeToFile(createFileName,t1.getText().toString());
                t1.setText("");
                previousFileName=createFileName;//previousfile is the now saved file
                createFileName=null;//this means now we are allowed to create a new file
                Toast.makeText(getBaseContext(),"Data saved", Toast.LENGTH_LONG).show();
            }
            else
            {
                Toast.makeText(getBaseContext(),"first create a file", Toast.LENGTH_LONG).show();
            }
        }
    }
}