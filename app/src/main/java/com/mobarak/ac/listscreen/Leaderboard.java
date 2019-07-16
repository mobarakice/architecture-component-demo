/*
 * Copyright 2019, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.mobarak.ac.listscreen;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mobarak.ac.R;

import java.util.ArrayList;
import java.util.List;


/**
 * Shows a register form to showcase UI state persistence. It has a button that goes to [`Registered.java`]
 */
public class Leaderboard extends Fragment {
    private int[] listOfAvatars = new int[]{
            R.drawable.avatar_1_raster,
            R.drawable.avatar_2_raster,
            R.drawable.avatar_3_raster,
            R.drawable.avatar_4_raster,
            R.drawable.avatar_5_raster,
            R.drawable.avatar_6_raster
    };
    private RecyclerView recyclerView;
    private RecyclerView.Adapter viewAdapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_leaderboard, container, false);
//        String name = getArguments() != null?getArguments().getString(USERNAME_KEY): "Ali Connors";
//        ((TextView) view.findViewById(R.id.profile_user_name)).setText(name);

        recyclerView = view.findViewById(R.id.leaderboard_list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        List<String> data = new ArrayList<String>();
        for (int i = 1; i <= 10; i++) {
            data.add("Person " + i);
        }
        viewAdapter = new MyAdapter(data);
        recyclerView.setAdapter(viewAdapter);
        return view;
    }

    class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

        private List<String> myDataset;

        public MyAdapter(List<String> myDataset) {
            this.myDataset = myDataset;
        }

        @NonNull
        @Override
        public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MyViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_view_item, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
            holder.textView.setText(myDataset.get(position));

            holder.imageView.setImageResource(listOfAvatars[position % listOfAvatars.length]);

            holder.item.setOnClickListener(v -> {
                Bundle bundle = new Bundle();
                bundle.putString(UserProfile.USERNAME_KEY, myDataset.get(position));
                Navigation.findNavController(v).navigate(
                        R.id.action_leaderboard_to_userProfile,
                        bundle);
            });
        }

        @Override
        public int getItemCount() {
            return myDataset.size();
        }

        // Provide a reference to the views for each data item
        // Complex data items may need more than one view per item, and
        // you provide access to all the views for a data item in a view holder.
        // Each data item is just a string in this case that is shown in a TextView.
        class MyViewHolder extends RecyclerView.ViewHolder {

            TextView textView;
            ImageView imageView;
            View item;

            public MyViewHolder(@NonNull View itemView) {
                super(itemView);
                this.item = itemView;
                textView = itemView.findViewById(R.id.user_name_text);
                imageView = itemView.findViewById(R.id.user_avatar_image);
            }
        }

    }

}
