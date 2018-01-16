package com.litte.wms;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import com.litte.wms.manager.HttpManager;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editText_username,editText_password;
    private Button button_login,button_register;
    private CheckBox checkBox;
    private SharedPreferences sharedPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initUI();
    }

    private void initUI() {
        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        //获取组件的实例
        editText_username = (EditText) findViewById(R.id.editText_username);
        editText_password = (EditText) findViewById(R.id.editText_password);
        button_login = (Button) findViewById(R.id.login_button);
        button_register = (Button) findViewById(R.id.register_button);
        checkBox = (CheckBox) findViewById(R.id.checkbox);

        button_login.setOnClickListener(this);
        button_register.setOnClickListener(this);
//        checkBox.setOnCheckedChangeListener(this);

        if (sharedPreferences.getBoolean("checkbox",false)){
            editText_username.setText(sharedPreferences.getString("username",null));
            editText_password.setText(sharedPreferences.getString("password",null));
            checkBox.setChecked(true);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.login_button:
                login();
                break;
            case R.id.register_button:
                //注册按钮 的功能部分
                break;
            default:
                break;
        }
    }

    private void login() {
        String name = editText_username.getText().toString();
        String password = editText_password.getText().toString();
        if (TextUtils.isEmpty(name)||TextUtils.isEmpty(password)){
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
            return;
        }
        //异步处理登录
        asyncLogin(name,password);
        /*if (TextUtils.isEmpty(name)||TextUtils.isEmpty(password)){
            Toast.makeText(this, "用户名或密码不能为空", Toast.LENGTH_SHORT).show();
        }else if (name.equals("admin")&&password.equals("admin")){
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (checkBox.isChecked()){
                editor.putBoolean("checkbox",true);
                editor.putString("username",name);
                editor.putString("password",password);
                editor.commit();
            }else {
                editor.putBoolean("checkbox",true);
                editor.putString("username",null);
                editor.putString("password",null);
                editor.commit();
            }
            startActivity(new Intent(LoginActivity.this,MainActivity.class));
            finish();
        }else {
            Toast.makeText(this, "用户名或密码不能为空！", Toast.LENGTH_SHORT).show();
        }*/
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            Boolean flag = (Boolean) msg.obj;
            if (flag){
                Toast.makeText(LoginActivity.this, "登陆成功过", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(LoginActivity.this,MainActivity.class));
                finish();
            }else {
                Toast.makeText(LoginActivity.this, "登陆失败", Toast.LENGTH_SHORT).show();
            }
        }
    };

    private void asyncLogin(final String name, final String password) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                boolean flag = HttpManager.loginHttpPost(name,password);
                Message message = handler.obtainMessage();
                message.obj = flag;
                handler.sendMessage(message);

                SharedPreferences.Editor editor = sharedPreferences.edit();
                if (checkBox.isChecked()){
                    editor.putBoolean("checkbox",true);
                    editor.putString("username",name);
                    editor.putString("password",password);
                    editor.commit();
                }else {
                    editor.putBoolean("checkbox",true);
                    editor.putString("username",null);
                    editor.putString("password",null);
                    editor.commit();
                }

            }
        }).start();
    }
}
