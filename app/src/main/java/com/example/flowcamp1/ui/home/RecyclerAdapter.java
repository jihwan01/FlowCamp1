package com.example.flowcamp1.ui.home;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.recyclerview.widget.RecyclerView;

import com.example.flowcamp1.R;

import java.util.ArrayList;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.ViewHolder> {
    private ArrayList<RecyclerItem> mData = null;
    // 생성자에서 데이터 리스트 객체를 전달받음.
    RecyclerAdapter(ArrayList<RecyclerItem> list) {
        mData = list;
    }


    public interface OnItemClickListener {
        void onItemClick(int position);
    }
    private OnItemClickListener onItemClickListener;
    public void setOnItemClickListener(OnItemClickListener listener) {
        onItemClickListener = listener;
    }
    // 아이템 뷰를 저장하는 뷰홀더 클래스.
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView name;
        TextView phoneNum;
        ImageView face;
        ImageButton infoBtn;
        ViewHolder(View itemView) {
            super(itemView) ;

            // 뷰 객체에 대한 참조. (hold strong reference)
            name = itemView.findViewById(R.id.name);
            phoneNum = itemView.findViewById(R.id.phoneNum);
            face = itemView.findViewById(R.id.face);
            infoBtn = itemView.findViewById(R.id.video_call_btn);
        }
    }

    // onCreateViewHolder() - 아이템 뷰를 위한 뷰홀더 객체 생성하여 리턴.
    @Override
    public RecyclerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext() ;
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) ;

        View view = inflater.inflate(R.layout.recyclerview_item, parent, false) ;
        RecyclerAdapter.ViewHolder vh = new RecyclerAdapter.ViewHolder(view) ;

        return vh ;
    }

    // onBindViewHolder() - position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시.
    @Override
    public void onBindViewHolder(RecyclerAdapter.ViewHolder holder, int position) {
//        RecyclerItem item = mData.get(position);
//        holder.name.setText(item.getName());
//        holder.age.setText(item.getAge());
        holder.infoBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("TAG", "Item in recycler view is clicked!!");
                if(onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }else{
                    Log.d("TAG", "Item click listener is null");
                }
            }
        });
        RecyclerItem item = mData.get(position) ;
        holder.face.setImageDrawable(item.getFace()); ;
        holder.name.setText(item.getName()) ;
        String phoneNum = item.getPhoneNum();
        String digits = phoneNum.replaceAll("\\D", "");
        if(digits.length() >= 8){
            phoneNum = digits.substring(0,3) + "-" + digits.substring(3, 7) + "-" + digits.substring(7);
        }else if(digits.length() >=4){
            phoneNum = digits.substring(0,3) + "-" + digits.substring(3);
        }else {
            phoneNum = digits;
        }
        holder.phoneNum.setText(phoneNum);
    }

    // getItemCount() - 전체 데이터 갯수 리턴.
    @Override
    public int getItemCount() {
        return mData.size() ;
    }
}
