package com.sqisland.android.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

public class GridLayoutVariableSpanSizeActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    s
    recyclerView.setLayoutManager(manager);

    recyclerView.setAdapter(new NumberedAdapter(30));
  }
}