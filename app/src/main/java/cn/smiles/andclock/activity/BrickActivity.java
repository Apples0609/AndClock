package cn.smiles.andclock.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import cn.smiles.andclock.R;
import cn.smiles.andclock.view.BrickView;

/**
 * 打砖块 游戏示例
 *
 * @author kaifang
 * @date 2018/3/12 18:14
 */
public class BrickActivity extends AppCompatActivity {

    //declaring gameview
    private BrickView gameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brick);
        gameView = (BrickView) findViewById(R.id.brickView);

        //Initializing game view object
//        gameView = new BrickView(this);
//        setContentView(gameView);

/*        //Getting display object
        Display display = getWindowManager().getDefaultDisplay();

        //Getting the screen resolution into point object
        Point size = new Point();
        display.getSize(size);*/
    }

    //pausing the game when activity is paused
    @Override
    protected void onPause() {
        super.onPause();
        gameView.pause();
    }

    //running the game when activity is resumed
    @Override
    protected void onResume() {
        super.onResume();
        gameView.resume();
    }
}
