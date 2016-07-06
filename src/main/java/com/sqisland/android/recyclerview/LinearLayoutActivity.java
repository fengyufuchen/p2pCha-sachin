package com.sqisland.android.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class LinearLayoutActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    superw LinearLayoutManager(this));
    recyclerView.setAdapter(new NumberedAdapter(30));
  }
}