package com.example.paperout;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class PDFAdapter extends RecyclerView.Adapter<PDFAdapter.ViewHolder> {

    private Context context;
    private List<PDF> list;
    private onClickListener monclicklistener;


    public PDFAdapter(Context context, List<PDF> list,onClickListener onClickListener) {
        this.context = context;
        this.list = list;
        this.monclicklistener = onClickListener;
    }

    @NonNull
    @Override
    public PDFAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.pdflayout2,parent,false);
        return new ViewHolder(v,monclicklistener);
    }

    @Override
    public void onBindViewHolder(@NonNull PDFAdapter.ViewHolder holder, int position) {

        PDF pdf = list.get(position);
        holder.pdfname.setText(String.valueOf(pdf.getPdf_name()));
        char c = '\0';
        StringBuffer sb1 = new StringBuffer();
        StringBuffer sb2 = new StringBuffer();
        String temp = pdf.getCreated_date();
        for(int i = 0 ; i < 2; i++)
        {
            c = temp.charAt(i);
            sb1 = sb1.append(c);
        }
        for(int i = 2 ; i < temp.length(); i++)
        {
            c = temp.charAt(i);
            sb2 = sb2.append(c);
        }

        holder.pdfdate.setText(sb1.toString()+"\n"+sb2.toString());
        holder.total_ques.setText("Total Questions : "+String.valueOf(pdf.getTotal_question()));

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public TextView pdfname,pdfdate,total_ques;
        public ImageView seepdf,dndpdf;
        onClickListener onClickListener;
        public ViewHolder(View itemView,onClickListener onClickListener)
        {
            super(itemView);

            pdfname = itemView.findViewById(R.id.pdf_name);
            pdfdate = itemView.findViewById(R.id.pdf_date);
            seepdf = itemView.findViewById(R.id.view_pdf);
            total_ques = itemView.findViewById(R.id.pdf_questions);
//            dndpdf = itemView.findViewById(R.id.downloadpdf);

            this.onClickListener = onClickListener;

            seepdf.setOnClickListener(this);
//
//            dndpdf.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            switch (v.getId())
            {
                case R.id.view_pdf:
                {
                    onClickListener.onClicked(getAdapterPosition());
                }
                break;

            }
        }
    }
    public interface onClickListener{
        void onClicked(int position);
        void onDownload(int position);
    }
}
