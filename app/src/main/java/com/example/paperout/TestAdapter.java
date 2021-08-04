package com.example.paperout;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.List;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.ViewHolder>{

    private Context context;
    private List<Test> list;
    private onClickListener monclicklistener;


    public TestAdapter(Context context, List<Test> list, onClickListener monclicklistener) {
        this.context = context;
        this.list = list;
        this.monclicklistener = monclicklistener;
    }

    @NonNull
    @Override
    public TestAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.testlayout,parent,false);
        return new ViewHolder(v,monclicklistener);
    }

    @Override
    public void onBindViewHolder(@NonNull TestAdapter.ViewHolder holder, int position) {
        Test test = list.get(position);
        holder.testname.setText(String.valueOf(test.getTestname()));
        holder.testdate.setText(String.valueOf(test.getTest_date()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        onClickListener onClickListener;
        TextView testname,testdate;
        ImageView start_test;

        public ViewHolder(@NonNull View itemView,onClickListener onClickListener) {
            super(itemView);

            this.onClickListener = onClickListener;
            testname = itemView.findViewById(R.id.test_name);
            testdate = itemView.findViewById(R.id.test_date);
            start_test = itemView.findViewById(R.id.start_test1);

            start_test.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onClickListener.onClicked(getAdapterPosition());
        }
    }

    public interface onClickListener{
        void onClicked(int position);
        void onDownload(int position);
    }
}
