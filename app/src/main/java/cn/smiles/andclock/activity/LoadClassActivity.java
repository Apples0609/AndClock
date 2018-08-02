package cn.smiles.andclock.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.method.ScrollingMovementMethod;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileReader;
import java.lang.reflect.Method;
import java.util.Properties;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import cn.smiles.andclock.R;
import cn.smiles.andclock.tools.Dexter;

public class LoadClassActivity extends AppCompatActivity {

    @BindView(R.id.class_path)
    TextView classPath;
    @BindView(R.id.choice_load_class)
    Button choiceLoadClass;
    @BindView(R.id.execute_method)
    Button executeMethod;
    @BindView(R.id.text_result)
    TextView textResult;
    private File configF;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load_class);
        ButterKnife.bind(this);
        textResult.setMovementMethod(ScrollingMovementMethod.getInstance());
    }

    @OnClick({R.id.choice_load_class, R.id.execute_method})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.choice_load_class:
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("file/*");
                startActivityForResult(intent, 101);
                break;
            case R.id.execute_method:
                if (configF == null) {
                    Toast.makeText(this, "还没选择配置文件", Toast.LENGTH_LONG).show();
                    break;
                }

                try {
                    String[] DEX_FILES = {"clazz.dex"};
                    for (String dex : DEX_FILES)
                        Dexter.loadFromAssets(this, dex);
                } catch (Exception e) {
                    throw new RuntimeException("Unable to load DEX files", e);
                }
                try {
                    Class<?> clazz = Class.forName("Clazz");
                    if (clazz == null) {
                        System.out.println("不存在clazz");
                    } else {
                        System.out.println(clazz);
                        executeClazz(clazz);
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void executeClazz(Class<?> clazz) {
        try {
            Properties properties = new Properties();
            properties.load(new FileReader(configF));
            String smethod = properties.getProperty("smethod");
            String sparame = properties.getProperty("sparame");

            Method method = clazz.getDeclaredMethod(smethod, Class.forName(sparame));
            Object o = method.invoke(null, "/");
            String rst = (String) o;
            textResult.setText(rst);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && requestCode == 101) {
            Uri uri = data.getData();
            if (uri != null) {
                String path = uri.getPath();
                File file = new File(path);
                if (file.getName().equals("config.properties")) {
                    configF = file;
                    classPath.setText(path);
                } else {
                    new AlertDialog.Builder(this)
                            .setTitle("提示")
                            .setMessage("必须选择 config.properties 名字的文件")
                            .setPositiveButton("确定", null)
                            .create().show();
                }
            }
        }
    }
}
