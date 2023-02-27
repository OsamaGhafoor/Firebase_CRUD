package com.example.firebasecrud;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class Adapter extends RecyclerView.Adapter<FoodViewHolder> {

    private Context context;

    private ArrayList<Food>  arrayList;

    private int lastPostion = -1;




    public Adapter(Context context, ArrayList<Food> arrayList) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public FoodViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.itemview,parent,false);



        return new FoodViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FoodViewHolder holder, int postion) {


        Glide.with(context)
                .load(arrayList.get(postion).getItemImage()).into(holder.imageview);

       // holder.imageview.setImageResource(Integer.parseInt(arrayList.get(postion).getItemImage()));

        holder.tvtitle.setText(arrayList.get(postion).getItemName());

        holder.tvdescription.setText(arrayList.get(postion).getItemDescription());

        holder.tvprice.setText(arrayList.get(postion).getItemPrice());

        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent(context,DetailActivity.class);

                intent.putExtra("Image",arrayList.get(holder.getAdapterPosition()).getItemImage());
                intent.putExtra("Description",arrayList.get(holder.getAdapterPosition()).getItemDescription());
                intent.putExtra("KeyValue",arrayList.get(holder.getAdapterPosition()).getKey());
                intent.putExtra("Name",arrayList.get(holder.getAdapterPosition()).getItemName());
                intent.putExtra("Price",arrayList.get(holder.getAdapterPosition()).getItemPrice());

                context.startActivity(intent);
            }
        });

        setAnimation(holder.itemView,postion);

    }

    public void  setAnimation(View view, int position){

        if (position > lastPostion){

            ScaleAnimation scaleAnimation = new ScaleAnimation(0.0f,1.0f,0.0f,1.0f,
                    Animation.RELATIVE_TO_SELF,0.5f,
                    Animation.RELATIVE_TO_SELF,0.5f);

            scaleAnimation.setDuration(1500);
            view.startAnimation(scaleAnimation);
            lastPostion = position;
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public void filteredList(ArrayList<Food> filterList) {

        arrayList = filterList;

        notifyDataSetChanged();
    }
}

class FoodViewHolder extends RecyclerView.ViewHolder{


    ImageView imageview;

    TextView tvtitle,tvdescription,tvprice;

    CardView cardView;

    public FoodViewHolder( View itemView) {
        super(itemView);

        imageview =(ImageView) itemView.findViewById(R.id.imageview);

        tvtitle = (TextView) itemView.findViewById(R.id.tvtitle);

        tvdescription = (TextView) itemView.findViewById(R.id.tvdesciption);

        tvprice = (TextView) itemView.findViewById(R.id.tvprice);

        cardView = (CardView) itemView.findViewById(R.id.cardview);



    }
}
