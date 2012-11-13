package com.example.gametest;
/*
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class ScrollMap extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scroll_map);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_scroll_map, menu);
        return true;
    }
}
*/ 


import android.app.Activity;
import android.os.Bundle;
import android.view.Window;

public class ScrollMap extends Activity {
   private static final String LOG_TAG = ScrollMap.class.getSimpleName();
   private CellMap _map;

   /** Called when the activity is first created. */
   @Override
   public void onCreate(Bundle savedInstanceState) {
       super.onCreate(savedInstanceState);
       requestWindowFeature(Window.FEATURE_NO_TITLE);
       _map = new CellMap(this);
       setContentView(_map);
   }
}
