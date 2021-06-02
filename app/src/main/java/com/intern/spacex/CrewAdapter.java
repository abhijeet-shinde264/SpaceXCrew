package com.intern.spacex;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import de.hdodenhof.circleimageview.CircleImageView;


public class CrewAdapter extends ListAdapter<CrewMember, CrewAdapter.CrewViewHolder> {
    private final String TAG = CrewMember.class.getName();

    private Context context;
    private ClickListener mListener;


    private static final DiffUtil.ItemCallback<CrewMember> DIFF_UTIL = new DiffUtil.ItemCallback<CrewMember>() {
        @Override
        public boolean areItemsTheSame(@NonNull CrewMember oldItem, @NonNull CrewMember newItem) {
            return oldItem.getId().equals(newItem.getId());
        }

        @Override
        public boolean areContentsTheSame(@NonNull CrewMember oldItem, @NonNull CrewMember newItem) {
            return oldItem.equals(newItem);
        }
    };

    public CrewAdapter(Context context, ClickListener clickListener) {
        super(DIFF_UTIL);
        this.context = context;
        mListener = clickListener;
    }

    @NonNull
    @Override
    public CrewViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        return new CrewViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CrewViewHolder holder, int position) {
        CrewMember member = getItem(position);

        Glide.with(context)
                .load(member.getImage())
                .placeholder(R.drawable.astronaut)
                .error(R.drawable.astronaut)
                .into(holder.imgProfile);

        holder.setData(member);

        holder.tvWiki.setOnClickListener(view -> mListener.openWiki(member.getWikipedia()));
        holder.parentLayout.setOnClickListener(view -> mListener.onItemClick(member));

        //change status color ["active", "inactive", "retired", "unknown"]
        switch (member.getStatus()) {
            case "active":
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.blue1));
                holder.tvStatus.getBackground().setColorFilter(Color.rgb(203, 229, 248), PorterDuff.Mode.SRC_ATOP);
                break ;
            case "inactive":
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.red1));
                holder.tvStatus.getBackground().setColorFilter(Color.rgb(248, 225, 230), PorterDuff.Mode.SRC_ATOP);
                break ;
            case "retired":
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.green1));
                holder.tvStatus.getBackground().setColorFilter(Color.rgb(226, 248, 222), PorterDuff.Mode.SRC_ATOP);
                break ;
            case "unknown":
                holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.gray1));
                holder.tvStatus.getBackground().setColorFilter(Color.rgb(230, 230, 230), PorterDuff.Mode.SRC_ATOP);
                break ;
        }
    }

    public static class CrewViewHolder extends RecyclerView.ViewHolder {

        CardView parentLayout;
        TextView tvName, tvAgency, tvStatus, tvWiki;
        CircleImageView imgProfile;

        public CrewViewHolder(@NonNull View itemView) {
            super(itemView);
            imgProfile = itemView.findViewById(R.id.circleImageView);
            parentLayout = itemView.findViewById(R.id.cvParentView);
            tvName = itemView.findViewById(R.id.tv_Name);
            tvAgency = itemView.findViewById(R.id.tv_Agency);
            tvStatus = itemView.findViewById(R.id.tv_Status);
            tvWiki = itemView.findViewById(R.id.tv_Wiki);
        }

        public void setData(CrewMember crewMember) {
            String agency = crewMember.getAgency() + "  \u00b7";        //ASCII value for middle dot
            String status = crewMember.getStatus();
            status = status.substring(0,1).toUpperCase()+status.substring(1).toLowerCase();     //capitalize first char

            tvName.setText(crewMember.getName());
            tvAgency.setText(agency);
            tvStatus.setText(status);
        }
    }
}
