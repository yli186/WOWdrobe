package com.example.baiwen.vcloset;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;


public class OutfitChoice extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_outfit_choice);

        ViewPager viewPagertop = (ViewPager) findViewById(R.id.pagertop);
        viewPagertop.setAdapter(new CustomPagerAdapter(this));

        ViewPager viewPagerBottom = (ViewPager) findViewById(R.id.pagerbottom);
        viewPagerBottom.setAdapter(new CustomPagerAdapter(this));

        ViewPager viewPagerShoes = (ViewPager) findViewById(R.id.pagershoes);
        viewPagerShoes.setAdapter(new CustomPagerAdapter(this));

        
    }


}
