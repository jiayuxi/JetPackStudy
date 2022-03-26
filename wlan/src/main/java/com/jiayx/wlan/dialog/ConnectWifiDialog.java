package com.jiayx.wlan.dialog;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.jiayx.wlan.R;

/**
 * 连接wifi监听
 **/

public abstract class ConnectWifiDialog extends Dialog implements View.OnClickListener {

    private EditText mEditTextPassword;

    public abstract void connect(String password);

    private TextView mTextViewSsid;
    private Button mButtonConnect;


    public ConnectWifiDialog(Context context) {
        super(context);
        initView();
        initAction();
    }

    private void initView() {
        // 布局这里考虑只有分享到微信还有和朋友圈 所以没有用RecyclerView
        View view = View.inflate(getContext().getApplicationContext(), R.layout.lib_wifi_dialog_connect_wifi, null);
        mTextViewSsid = (TextView) view.findViewById(R.id.tv_ssid);
        mEditTextPassword = (EditText) view.findViewById(R.id.et_pwd);
        Button mButtonCancel = (Button) view.findViewById(R.id.btn_cancel);
        mButtonCancel.setOnClickListener(this);
        mButtonConnect = (Button) view.findViewById(R.id.btn_connect);
        mButtonConnect.setOnClickListener(this);
        // 加载布局
        setContentView(view);
        // 设置Dialog参数
        Window window = getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.WRAP_CONTENT;
        params.gravity = Gravity.CENTER;
        window.setAttributes(params);
    }

    private void initAction() {
        if (mEditTextPassword != null) {
            if (TextUtils.isEmpty(mEditTextPassword.getText())) {
                mButtonConnect.setEnabled(false);
            }
            mEditTextPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mButtonConnect.setEnabled(s.length() >= 8);
                }
            });
        }
    }

    public ConnectWifiDialog setSsid(String ssid) {
        mTextViewSsid.setText(ssid);
        return this;
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == R.id.btn_cancel) {
            dismiss();
        } else if (id == R.id.btn_connect) {
            String pwd = mEditTextPassword.getText().toString();
            connect(pwd);
            dismiss();
        }
    }
}
