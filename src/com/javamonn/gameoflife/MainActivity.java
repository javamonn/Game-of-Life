package com.javamonn.gameoflife;

import android.app.*;
import android.os.*;
import android.view.*;
import android.widget.*;
import android.content.Intent;
import android.view.View.OnClickListener;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;


public class MainActivity extends Activity implements OnClickListener
{
	private Grid grid;
	
    /** Called when the app is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
		
		grid = (Grid) findViewById(R.id.grid_main);
		/* Since we need the size of the view in pixels in order to create a grid
		 * that properly fills the screen, we intercept after the view has been 
		 * created but not displayed
		 */
		grid.getViewTreeObserver().addOnGlobalLayoutListener( 
			new OnGlobalLayoutListener(){
				@SuppressWarnings("deprecation")
				@Override
				public void onGlobalLayout() {
					// gets called after layout has been done but before display
					// so we can get the height then hide the view

					grid.buildGrid(grid.getWidth(), grid.getHeight(), true);

					grid.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}

			});
		
		//click listeners
		Button aboutButton = (Button) findViewById(R.id.about_button);
		aboutButton.setOnClickListener(this);
		Button newButton = (Button) findViewById(R.id.new_button);
		newButton.setOnClickListener(this);
    }
	
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.about_button:
				Intent aboutIntent = new Intent(this, AboutActivity.class);
				startActivity(aboutIntent);
				break;
			case R.id.new_button:
				Intent gridIntent = new Intent(this, GridActivity.class);
				startActivity(gridIntent);
		}
	}
}
