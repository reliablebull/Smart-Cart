package com.example.hwang.smartcart.classModels;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.hwang.smartcart.R;

// slide 이미지 를 보여주기 위한 class
// images[] 에 이미지를 추가해주시면 순서대로 출력됩니다.
public class Adapter extends PagerAdapter{private int[] images = {R.drawable.slider1,R.drawable.slider2,R.drawable.slider3};private LayoutInflater inflater;private Context context;public Adapter(Context context){this.context = context;}
    @Override
    public int getCount() {
        return images.length;
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == ((LinearLayout) object);
    }
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        inflater = (LayoutInflater)context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
        View v = inflater.inflate(R.layout.slider, container, false);
        ImageView imageView = (ImageView)v.findViewById(R.id.imageView);

        imageView.setImageResource(images[position]);

        container.addView(v);
        return v;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object){container.invalidate();}}