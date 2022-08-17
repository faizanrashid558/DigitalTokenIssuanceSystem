package com.faizanrashid.digitaltokenissuancesystem.Employee.EmployeeComplaint;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.faizanrashid.digitaltokenissuancesystem.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

public class myadapter extends FirebaseRecyclerAdapter<model,myadapter.myviewholder> {

   public myadapter(@NonNull FirebaseRecyclerOptions<model> options) {
      super(options);
   }

   @Override
   protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull model model) {

      holder.uid.setText(model.getUid());
   }

   @NonNull
   @Override
   public myviewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
      View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.singlerow,parent,false);
      return new myviewholder(view);
   }

   class myviewholder extends RecyclerView.ViewHolder{
      TextView  uid;
      public myviewholder(@NonNull View itemView) {
         super(itemView);

         uid = itemView.findViewById(R.id.userid);
      }
   }
}
