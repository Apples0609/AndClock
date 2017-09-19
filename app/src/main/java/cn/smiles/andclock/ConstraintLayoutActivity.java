package cn.smiles.andclock;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ConstraintLayoutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_constraint_layout);

        ListView lv_list = (ListView) findViewById(R.id.lv_list);

        List<String> haHas = new ArrayList<>();
        for (int i = 0; i < 20; i++) {
            haHas.add(UUID.randomUUID().toString());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, android.R.id.text1, haHas);
        lv_list.setAdapter(adapter);
    }

}
