package com.sqisland.android.recyclerview;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

public class GridLayoutAutoFitActivity extends Activity {
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    sedSize(true);
    recyclerView.setAdapter(new NumberedAdapter(30));
  }
}