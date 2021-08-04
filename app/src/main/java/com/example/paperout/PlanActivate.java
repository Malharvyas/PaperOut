package com.example.paperout;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.widget.Toast;

import com.razorpay.Checkout;
import com.razorpay.PaymentResultListener;

import org.json.JSONObject;

public class PlanActivate implements PaymentResultListener {

    Context context;
    String splanprice,gplanprice;
    String username,email,mobile;



    public void makePayment(Context context,String plan){
        Activity activity = (Activity) context;
        SharedPreferences plansprice = activity.getSharedPreferences("plans", Context.MODE_PRIVATE);
        splanprice = plansprice.getString("silver","NA");
        gplanprice = plansprice.getString("golden","NA");
        int sprice = Integer.parseInt(splanprice);
        int gprice = Integer.parseInt(gplanprice);

        if(plan.equals("silver"))
        {
            SharedPreferences userpref = context.getSharedPreferences("userpref", Context.MODE_PRIVATE);
            username = userpref.getString("uname","NA");
            email = userpref.getString("email","NA");
            mobile = userpref.getString("mobile","NA");

            Checkout.preload(context);
            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_e2ZrK1Xqu13HIq");

            checkout.setImage(R.drawable.logo3);

            try {
                JSONObject options = new JSONObject();

                options.put("name", username);
                options.put("currency", "INR");
                options.put("amount", sprice);//pass amount in currency subunits
                options.put("prefill.email", email);
                options.put("prefill.contact",mobile);
                JSONObject retryObj = new JSONObject();
                retryObj.put("enabled", true);
                retryObj.put("max_count", 4);
                options.put("retry", retryObj);

                checkout.open((Activity) context, options);

            } catch(Exception e) {
                Log.e("TAG", "Error in starting Razorpay Checkout", e);
            }
        }
        else if(plan.equals("golden"))
        {
            SharedPreferences userpref = context.getSharedPreferences("userpref", Context.MODE_PRIVATE);
            username = userpref.getString("uname","NA");
            email = userpref.getString("email","NA");
            mobile = userpref.getString("mobile","NA");

            Checkout.preload(context);
            Checkout checkout = new Checkout();
            checkout.setKeyID("rzp_test_e2ZrK1Xqu13HIq");

            checkout.setImage(R.drawable.logo3);

            try {
                JSONObject options = new JSONObject();

                options.put("name", username);
                options.put("currency", "INR");
                options.put("amount", gprice);//pass amount in currency subunits
                options.put("prefill.email", email);
                options.put("prefill.contact",mobile);
                JSONObject retryObj = new JSONObject();
                retryObj.put("enabled", true);
                retryObj.put("max_count", 4);
                options.put("retry", retryObj);

                checkout.open((Activity) context, options);

            } catch(Exception e) {
                Log.e("TAG", "Error in starting Razorpay Checkout", e);
            }
        }



    }


    @Override
    public void onPaymentSuccess(String s) {
        Toast.makeText(context,""+s,Toast.LENGTH_LONG).show();
    }

    @Override
    public void onPaymentError(int i, String s) {
        Toast.makeText(context,""+s,Toast.LENGTH_LONG).show();
    }
}
