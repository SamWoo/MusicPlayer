package sam.musicplayer.Activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import sam.musicplayer.Constant;
import sam.musicplayer.R;
import sam.musicplayer.Util.SPUtil;

/**
 * Created by Administrator on 2017/6/29.
 */

public class ChangeNameActivity extends AppCompatActivity {

    private EditText mEdit;
    private Button mBtnConfirm;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        mBtnConfirm= (Button) findViewById(R.id.btn_confim);
        mEdit= (EditText) findViewById(R.id.edit_name);
        mBtnConfirm.setOnClickListener((view -> changeName()));
        overridePendingTransition(R.anim.fade_in_left, R.anim.fade_out_left);
    }

    //修改用户名
    private void changeName() {
        String newName = mEdit.getText().toString().trim();
        if (!TextUtils.isEmpty(newName)) {
            SPUtil.put(this, Constant.USER_NAME, newName);
            Toast.makeText(this, "昵称修改成功", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "昵称不能为空", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
