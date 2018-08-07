//package com.link.cloud.view;
//
//import android.app.AlertDialog;
//import android.content.Context;
//import android.content.DialogInterface;
//import android.os.Bundle;
//import android.view.Gravity;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.Window;
//import android.view.WindowManager;
//import android.webkit.WebView;
//import android.webkit.WebViewClient;
//import android.widget.TextView;
//
//import com.link.cloud.R;
//
//public class SyncdataDialog extends AlertDialog {
//
//        private View rootview;
//
//        WebContentView mWebView;
//
//        private TextView mTitleClose;
//
//        private TextView midTitle;
//
//        NormalProgressDialog loadingDialog;
//
//        public interface QuestionDialogInterface {
//            public void dismisss();
//
//            public void onItemClick(int pos);
//        }
//
//        public QuestionDialogInterface mListener;
//
//        // public void setListener(QuestionDialogInterface istener){
//        // mListener = istener;
//        // }
//
//        public QuestionDialog1(Context context) {
//            // super(context, R.style.DialogStyle);
//            super(context);
//            rootview = LayoutInflater.from(context).inflate(R.layout.question_dialog1, null, false);
//
//            loadingDialog = new NormalProgressDialog(context);
//
//            midTitle = (TextView) rootview.findViewById(R.id.midTitle);
//
//            mWebView = (WebContentView) rootview.findViewById(R.id.webContentView1);
//
//            mWebView.setWebViewClient(new WebViewClient() {
//                @Override
//                public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
//                }
//
//                @Override
//                public void onPageFinished(WebView view, String url) {
//                    super.onPageFinished(view, url);
////                    loadingDialog.dismiss();
//                }
//            });
//
////            mTitleClose = (TextView) rootview.findViewById(R.id.title_close_text);
//
//            mTitleClose.setOnClickListener(new View.OnClickListener() {
//
//                @Override
//                public void onClick(View arg0) {
//                    // mListener.dismisss();
//                    dismiss();
//                }
//            });
//
//            setOnDismissListener(new OnDismissListener() {
//                @Override
//                public void onDismiss(DialogInterface dialog) {
//                    if (loadingDialog != null) {
////                        loadingDialog.dismiss();
//                    }
//
//                }
//            });
//        }
//
//        public void setTitle(String title) {
//            midTitle.setText(title);
//        }
//
//        @Override
//        protected void onCreate(Bundle savedInstanceState) {
//            super.onCreate(savedInstanceState);
//            this.setContentView(rootview);
//            // this.getWindow().setBackgroundDrawable(new
//            // PaintDrawable(Color.TRANSPARENT));
//            setCancelable(true);
//            setCanceledOnTouchOutside(false);
//        }
//        /**
//         * 显示本dialog
//         * 外部对象调用
//         *
//         * @param url 需要现实的网页
//         */
//        public void ShowDialog(String url) {
//            if (this.isShowing()) return;
//            if (mWebView != null) {
////                mWebView.loadUrl(url);
//            }
//            Window window = this.getWindow();
//            window.setGravity(Gravity.BOTTOM); // 此处可以设置dialog显示的位置
//            window.setWindowAnimations(R.style.mystyle); // 添加动画
//
//            // 两句的顺序不能调换
//            this.show();
//            WindowManager.LayoutParams lp = window.getAttributes();
//            lp.width = WindowManager.LayoutParams.FILL_PARENT;
//            lp.height = WindowManager.LayoutParams.FILL_PARENT;
//            window.setAttributes(lp);
//            window.getDecorView().setPadding(0, 0, 0, 0);
//        }
//  }
//
