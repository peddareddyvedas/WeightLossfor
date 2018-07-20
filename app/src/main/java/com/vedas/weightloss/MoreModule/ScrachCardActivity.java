package com.vedas.weightloss.MoreModule;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cooltechworks.views.ScratchTextView;
import com.vedas.weightloss.R;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.Random;

import butterknife.ButterKnife;

public class ScrachCardActivity extends AppCompatActivity {

    ArrayList<String> name = new ArrayList<>();
    ArrayList<Integer> image = new ArrayList<>();

    Random myRandom;
    RecyclerView addRecyclerView;
    static int selectedPosition = -1;
    ScrachCard adapter;
    Button button;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scrach_card);
        ButterKnife.bind(this);
        init();
    }


    private void init() {


        name.add("Scratch Card");
        name.add("Share & Earn");
        name.add("Scratch Card");
        name.add("Scratch Card");
        name.add("Scratch Card");

        image.add(R.drawable.ic_layout_1);
        image.add(R.drawable.ic_layout_2);
        image.add(R.drawable.ic_layout_3);
        image.add(R.drawable.ic_layout_1);
        image.add(R.drawable.ic_layout_2);

        myRandom = new Random();

        addRecyclerView = (RecyclerView) findViewById(R.id.scrachcard_recyclerview);
        adapter = new ScrachCard(name, getApplicationContext());
        LinearLayoutManager horizontalLayoutManager = new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false);
        addRecyclerView.setLayoutManager(horizontalLayoutManager);
        addRecyclerView.setAdapter(adapter);
        addRecyclerView.setHasFixedSize(true);
        adapter.notifyDataSetChanged();


        button = (Button) findViewById(R.id.back);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }


    // Step 1:-
    public class ScrachCard extends RecyclerView.Adapter<ScrachCard.ViewHolder> {

        // step 3:-
        ArrayList<String> arrayList = new ArrayList<>();
        Context ctx;

        public ScrachCard(ArrayList<String> arrayList, Context ctx) {
            this.ctx = ctx;
            this.arrayList = arrayList;
        }

        // step 5:-
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {


            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.scrachcardviewlist, parent, false);


            ViewHolder myViewHolder = new ViewHolder(view, ctx, arrayList);
            return myViewHolder;


        }

        //step 6:-
        @Override
        public void onBindViewHolder(final ViewHolder holder, final int position) {

            holder.name.setText(name.get(position));

            //Random rnd = new Random();
            //int color = Color.argb(rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            /*int[] androidColors = getResources().getIntArray(R.array.androidcolors);
            int randomAndroidColor = androidColors[new Random().nextInt(androidColors.length)];
            //view.setBackgroundColor(randomAndroidColor);*/
            //holder.relative_one.setBackgroundColor(getRandomColor());
            holder.relative_one.setBackgroundResource(image.get(position));


            if (selectedPosition == position) {
                holder.relative_two.setVisibility(View.VISIBLE);
                holder.textdis.setVisibility(View.VISIBLE);
                // holder.rupees.setVisibility(View.VISIBLE);
                // holder.scrachimage.setVisibility(View.INVISIBLE);
            } else {
                holder.relative_two.setVisibility(View.GONE);
                holder.textdis.setVisibility(View.GONE);
                //holder.rupees.setVisibility(View.GONE);
                // holder.scrachimage.setVisibility(View.VISIBLE);
            }

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (selectedPosition != position) {
                        selectedPosition = position;
                    } else {
                        selectedPosition = -1;
                    }

                    notifyDataSetChanged();
                }
            });


        }

        // step 4:-
        @Override
        public int getItemCount() {

            Log.e("listarray", "" + arrayList.size());
            return arrayList.size();
        }


        // Step 2:-
        class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


            TextView name, textdis, rupees;
            RelativeLayout relative, relative_one, relative_two;
            //ImageView scrachimage;
            ScratchTextView scratchTextView;

            ArrayList<String> arrayList = new ArrayList<String>();
            Context ctx;

            public ViewHolder(View itemView, Context ctx, final ArrayList<String> arrayList) {
                super(itemView);

                this.arrayList = arrayList;
                this.ctx = ctx;
                name = (TextView) itemView.findViewById(R.id.scratchname);

                relative = (RelativeLayout) itemView.findViewById(R.id.relative);
                relative_one = (RelativeLayout) itemView.findViewById(R.id.relative_one);
                relative_two = (RelativeLayout) itemView.findViewById(R.id.relative_two);
                textdis = (TextView) itemView.findViewById(R.id.text);
                // scrachimage = (ImageView) itemView.findViewById(R.id.scrachimage);
                //  rupees = (TextView) itemView.findViewById(R.id.rupees);
                scratchTextView = itemView.findViewById(R.id.rupees);
                itemView.setOnClickListener(this);

            }

            @Override
            public void onClick(View v) {
                assert scratchTextView != null;
                scratchTextView.setRevealListener(new ScratchTextView.IRevealListener() {
                    @Override
                    public void onRevealed(ScratchTextView tv) {
                        Toast.makeText(ScrachCardActivity.this, "Revealed!", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onRevealPercentChangedListener(ScratchTextView scratchTextView, float v) {

                    }
                });

            }


        }
    }

    private int getRandomColor() {
        SecureRandom rgen = new SecureRandom();
        return Color.HSVToColor(150, new float[]{
                rgen.nextInt(359), 1, 1
        });


    }

}
