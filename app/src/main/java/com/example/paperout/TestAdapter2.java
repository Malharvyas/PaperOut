package com.example.paperout;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class TestAdapter2 extends RecyclerView.Adapter<TestAdapter2.ViewHolder> {

    public static final int MULTI_SELECT = 0;
    public static final int SINGLE_SELECT = 1;

    private Context context;
    private List<Test2> list;
    private String multiselect;
    private onClickListener monClickListener;


    public TestAdapter2(Context context, List<Test2> list,onClickListener onClickListener) {
        this.context = context;
        this.list = list;
        this.monClickListener = onClickListener;
    }

    @NonNull
    @Override
    public TestAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if(viewType == MULTI_SELECT)
        {
            View v = LayoutInflater.from(context).inflate(R.layout.testlayout3,parent,false);
            return new ViewHolder(v,monClickListener);
        }
        else
        {
            View v = LayoutInflater.from(context).inflate(R.layout.testlayout2,parent,false);
            return new ViewHolder(v,monClickListener);
        }

    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter2.ViewHolder holder, int position) {

        Test2 test = list.get(position);
        this.multiselect = test.getMultiselect();
        if(list.size() - 1 == position )
        {
            holder.submit.setVisibility(View.VISIBLE);
        }
        else
        {
            holder.submit.setVisibility(View.GONE);
        }
        if(multiselect.equals("1"))
        {
            holder.question2.setText(test.getQuestion());
            holder.check1.setText(test.getMcq1());
            holder.check2.setText(test.getMcq2());
            holder.check3.setText(test.getMcq3());
            holder.check4.setText(test.getMcq4());
            SharedPreferences answers = context.getSharedPreferences("answers"+position,Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = answers.edit();
            editor.clear();
            holder.check1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    if(checked)
                    {
                        editor.putString("ch1"+position, test.getMcq1());
                        editor.apply();
                    }
                    else {
                        editor.putString("ch1"+position,"null");
                        editor.apply();
                    }
                }
            });
            holder.check2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    if(checked)
                    {
                        editor.putString("ch2"+position, test.getMcq2());
                        editor.apply();
                    }
                    else {
                        editor.putString("ch2"+position,"null");
                        editor.apply();
                    }
                }
            });
            holder.check3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    if(checked)
                    {
                        editor.putString("ch3"+position, test.getMcq3());
                        editor.apply();
                    }
                    else {
                        editor.putString("ch3"+position,"null");
                        editor.apply();
                    }
                }
            });
            holder.check4.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    boolean checked = ((CheckBox) v).isChecked();
                    if(checked)
                    {
                        editor.putString("ch4"+position, test.getMcq4());
                        editor.apply();
                    }
                    else {
                        editor.putString("ch4"+position,"null");
                        editor.apply();
                    }
                }
            });
        }
        else
        {
            holder.question1.setText(test.getQuestion());
            holder.radio1.setText(test.getMcq1());
            holder.radio2.setText(test.getMcq2());
            holder.radio3.setText(test.getMcq3());
            holder.radio4.setText(test.getMcq4());
            holder.group.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(RadioGroup group, int checkedId) {
                    SharedPreferences answers = context.getSharedPreferences("answers"+position,Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = answers.edit();
                    editor.clear();
                    if(holder.radio1.isChecked())
                    {
                        editor.putString(""+position, test.getMcq1());
                        editor.apply();
                    }
                    else if(holder.radio2.isChecked())
                    {
                        editor.putString(""+position, test.getMcq2());
                        editor.apply();
                    }
                    else if(holder.radio3.isChecked())
                    {
                        editor.putString(""+position, test.getMcq3());
                        editor.apply();
                    }
                    else if(holder.radio4.isChecked())
                    {
                        editor.putString(""+position, test.getMcq4());
                        editor.apply();
                    }
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        public TextView question1,question2;
        RadioButton radio1,radio2,radio3,radio4,rb;
        RadioGroup group;
        CheckBox check1,check2,check3,check4;
        Button submit;
        onClickListener onClickListener;

        public ViewHolder(@NonNull View itemView,onClickListener onClickListener) {
            super(itemView);

            question1 = itemView.findViewById(R.id.question1);
            question2 = itemView.findViewById(R.id.question2);
            check1 = itemView.findViewById(R.id.check_option1);
            check2 = itemView.findViewById(R.id.check_option2);
            check3 = itemView.findViewById(R.id.check_option3);
            check4 = itemView.findViewById(R.id.check_option4);
            radio1 = itemView.findViewById(R.id.radio_option1);
            radio2 = itemView.findViewById(R.id.radio_option2);
            radio3 = itemView.findViewById(R.id.radio_option3);
            radio4 = itemView.findViewById(R.id.radio_option4);
            submit = itemView.findViewById(R.id.submit_answers);
            group = itemView.findViewById(R.id.optionsradio);

            this.onClickListener = onClickListener;

            submit.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.submit_answers:
                {
                    onClickListener.onClicked(getAdapterPosition());
                }
                break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        Test2 test = list.get(position);
        this.multiselect = test.getMultiselect();
        if(multiselect.equals("1"))
        {
            return MULTI_SELECT;
        }
        else
        {
            return SINGLE_SELECT;
        }
    }

    public interface onClickListener{
        void onClicked(int position);
//        void onDownload(int position);
    }
}
