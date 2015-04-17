package com.publiss.whitelabel;

import android.os.Bundle;
import android.os.PersistableBundle;

import com.bytepoets.bporssreader.EntryListActivity;


public class RSSActivity extends EntryListActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }
}
