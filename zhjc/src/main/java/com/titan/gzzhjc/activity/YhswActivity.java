package com.titan.gzzhjc.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;

import com.titan.gzzhjc.Echart;
import com.titan.gzzhjc.R;
import com.titan.gzzhjc.customview.PopupWindowCustom;
import com.titan.gzzhjc.impl.OptionImpl;
import com.titan.gzzhjc.impl.PopupWindowImpl;
import com.titan.gzzhjc.impl.VHTableViewDataImpl;
import com.titan.gzzhjc.util.Util;
import com.titan.vhtableview.VHTableView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 林业有害生物
 */
public class YhswActivity extends AppCompatActivity implements View.OnClickListener {

    ImageView mYhsw_back;
    RadioButton rbFsmj;
    RadioButton rbFzmj;
    RadioButton rbFzjf;
    RadioButton rbJymj;
    TextView tvTime;
    TextView tvContent;
    WebView yhswWebview;

    VHTableView yhswTabview;
    Context mContext;
    Echart echart;
    List<Map<String,String>> list = new ArrayList<>();
    String keyType = "";
    PopupWindowCustom popup;
    View childview;
    int timeid = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        childview = getLayoutInflater().inflate(R.layout.activity_yhsw_zhjc,null);
        setContentView(childview);

        mContext = YhswActivity.this;

        initview();

        /** 初始化WebView */
        OptionImpl optionImpl = new OptionImpl();
        String type = getResources().getString(R.string.yhsw);
        echart = optionImpl.initWebView(mContext, yhswWebview, type,handler);
        keyType = getResources().getString(R.string.fsmj);
        echart.setYwType(getResources().getString(R.string.fsmj));
    }

    private void initview() {
        mYhsw_back = (ImageView) childview.findViewById(R.id.yhsw_back);
        rbFsmj = (RadioButton) childview.findViewById(R.id.rb_fsmj);
        rbFzmj = (RadioButton) childview.findViewById(R.id.rb_fzmj);
        rbFzjf = (RadioButton) childview.findViewById(R.id.rb_fzjf);
        rbJymj = (RadioButton) childview.findViewById(R.id.rb_jymj);
        tvTime = (TextView) childview.findViewById(R.id.tv_time);
        tvContent = (TextView) childview.findViewById(R.id.tv_content);
        yhswWebview = (WebView) childview.findViewById(R.id.yhsw_webview);

        mYhsw_back.setOnClickListener(this);
        rbFsmj.setOnClickListener(this);
        rbFzmj.setOnClickListener(this);
        rbFzjf.setOnClickListener(this);
        rbJymj.setOnClickListener(this);
        tvTime.setOnClickListener(this);
        timeid = R.string.time2;
        tvTime.setText(getResources().getString(timeid));
        yhswTabview = (VHTableView) childview.findViewById(R.id.yhsw_tabview);
    }

    @Override
    public void onClick(View view) {
        int i = view.getId();
        if (i == R.id.yhsw_back) {
            this.finish();

        } else if (i == R.id.rb_fsmj) {
            gotoHtml(R.string.fsmj);

        } else if (i == R.id.rb_fzmj) {
            gotoHtml(R.string.fzmj);

        } else if (i == R.id.rb_fzjf) {
            gotoHtml(R.string.fzjf);

        } else if (i == R.id.rb_jymj) {
            gotoHtml(R.string.jymj);

        } else if (i == R.id.tv_time) {
            PopupWindowImpl popupWindow = new PopupWindowImpl();
            popup = popupWindow.showTimeSel(mContext, childview, this);

        } else if (i == R.id.item_time_sewq) {
            reInitWebview((Button) view);

            reInitWebview((Button) view);

        } else if (i == R.id.item_time_elyl) {
            reInitWebview((Button) view);

        } else {
        }
    }

    /**
     * Webview数据更新（根据 底部时间选择弹窗 选择的时间，加载不同数据）
     * @param button
     */
    public void reInitWebview(Button button) {
        String txt = button.getText().toString();
        if(txt.equals(mContext.getResources().getString(R.string.time1))){
            timeid = R.string.time1;
        }else{
            timeid = R.string.time2;
        }
        echart.setTime(timeid);
        tvTime.setText(txt);
        String dbname = echart.getName();
        yhswWebview.loadUrl("javascript:setchart('" + dbname + "');");
        popup.dismiss();
    }

    /**
     * RadioGroup 按钮选择
     * @param type
     */
    public void gotoHtml(int type){
        keyType = mContext.getResources().getString(type);
        echart.setYwType(keyType);
        yhswWebview.loadUrl("javascript:setchart('" + mContext.getResources().getString(R.string.gzsh) + "');");
        changContent();
    }

    /**
     * 数据回调
     */
    public Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if(msg.what == R.string.yhsw){
                list = (List<Map<String, String>>) msg.obj;
                VHTableViewDataImpl viewData = VHTableViewDataImpl.getInstence(mContext);
                /** 表格数据加载 */
                viewData.initYhswTabView(yhswTabview,list);
                /** 顶部描述 */
                changContent();
            }
        }
    };

    /**
     * 顶部描述
     */
    public void changContent(){
        for(Map<String, String> map : list){
            String dqmc = map.get(mContext.getResources().getString(R.string.dqname));
            String gzsh = echart.getName();
            if(gzsh.equals(dqmc.trim())){
                String cfxe = map.get(keyType);
                if(keyType.contains("面积")){
                    tvContent.setText(gzsh+keyType+"："+ Util.round(cfxe)+"万亩");
                }else{
                    tvContent.setText(gzsh+keyType+"："+ Util.round(cfxe)+"万元");
                }
                break;
            }
        }
    }
}
