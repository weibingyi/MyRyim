package com.janabo.myim;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.janabo.myim.entity.GuestInfo;
import com.janabo.myim.entity.Message;
import com.janabo.myim.http.HttpClientUtil;
import com.janabo.myim.http.Manager;
import com.janabo.myim.util.EmojiUtil;
import com.janabo.myim.util.Global;

import org.xutils.common.Callback;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.Event;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.HashMap;
import java.util.Map;

/**
 * 作者：janabo on 2016/12/5 14:19
 */
@ContentView(R.layout.activity_login)
public class LoginActivity extends AppCompatActivity {
    Context mContext = this;
    @ViewInject(R.id.title)
    TextView title;
//    @ViewInject(R.id.uid)
//    MaterialEditText uid;
//    @ViewInject(R.id.psw)
//    MaterialEditText psw;
    @ViewInject(R.id.ok)
    Button ok;

    CoreSharedPreferencesHelper helper;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        x.view().inject(this);
        title.setText("登录");
        helper = new CoreSharedPreferencesHelper(this);

        Global.EMOJIS = EmojiUtil.parseEmoji(this);
        Global.EMOJISCODE = EmojiUtil.parseEmojiCode(this);
        Global.EMOJISCODE2 = EmojiUtil.parseEmojiCode2(this);
    }
    @Event(value={R.id.ok})
    private void getEvent(View view){
        switch(view.getId()){
            case R.id.ok:
                login();
                break;
        }
    }

    private void login(){
        Map<String,String> map = new HashMap<>();
        map.put("Ip","123456789012");
        map.put("guest_name","system");
        map.put("urlref","");
        HttpClientUtil.doPost("http://srv.huaruntong.cn/chat/hprongyun.asmx/Init_Guest_Info", map, new Callback.CommonCallback<String>() {
            @Override
            public void onSuccess(String result) {
                GuestInfo guestInfo = Manager.getObj(result, GuestInfo.class);
                helper.setValue("mtoken",guestInfo.getToken());
                helper.setValue("mguestid",guestInfo.getGuest_id());
                Toast.makeText(mContext,"登录成功",Toast.LENGTH_SHORT).show();

                Global.MESSAGES.clear();
                Message m = new Message();
                m.setDate("星期三");
                m.setInfo("客服");
                m.setNickName("客服一");
                m.setOldPosition(0);
                Global.MESSAGES.add(m);

                Intent intent = new Intent(mContext,MessageActivity.class);
                startActivity(intent);
            }
            @Override
            public void onError(Throwable ex, boolean isOnCallback) {
//                Toast.makeText(mContext,"登录失败",Toast.LENGTH_SHORT).show();
            }
            @Override
            public void onCancelled(CancelledException cex) {

            }
            @Override
            public void onFinished() {

            }
        });

    }
}
