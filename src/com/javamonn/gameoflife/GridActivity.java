package com.javamonn.gameoflife;

import android.app.*;
import android.os.*;
import android.view.*;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.*;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.content.Intent;
import android.view.View.*;

public class GridActivity extends Activity implements OnClickListener, OnSeekBarChangeListener {

	private Grid _gridview;
	private Button startButton;
	private SeekBar animationSpeed;
	
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.grid);
		_gridview = (Grid) findViewById(R.id.grid_view);
		
		animationSpeed = (SeekBar) findViewById(R.id.animation_speed);
		animationSpeed.setOnSeekBarChangeListener(this);
		
		startButton = (Button) findViewById(R.id.start_button);
		startButton.setOnClickListener(this);
		
		Spinner spinner = (Spinner) findViewById(R.id.underpopulation_spinner);
		// Create an ArrayAdapter using the string array and a default spinner layout
		ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
		        R.array.underpopulation_options, android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner
		spinner.setAdapter(adapter);
		
		_gridview.getViewTreeObserver().addOnGlobalLayoutListener( 
			new OnGlobalLayoutListener(){
				@SuppressWarnings("deprecation")
				@Override
				public void onGlobalLayout() {
					// gets called after layout has been done but before display
					// so we can get the height then hide the view

					_gridview.buildGrid(_gridview.getWidth(), _gridview.getHeight(), false);

					_gridview.getViewTreeObserver().removeGlobalOnLayoutListener(this);
				}

			});
		
		/* Since we need the size of the view in pixels in order to create a grid
		 * that properly fills the screen, we intercept after the view has been 
		 * created but not displayed
		 */
		_gridview.getViewTreeObserver().addOnGlobalLayoutListener( 
			    new OnGlobalLayoutListener(){
			        @SuppressWarnings("deprecation")
					@Override
			        public void onGlobalLayout() {
			            // gets called after layout has been done but before display
			            // so we can get the height then hide the view

			        	_gridview.buildGrid(_gridview.getWidth(), _gridview.getHeight(), false);
			        	
			            _gridview.getViewTreeObserver().removeGlobalOnLayoutListener( this );
			        }

			});
    }
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.start_button:
				if (_gridview.getState() == Grid.PAUSE) {
					_gridview.setMode(Grid.RUNNING);
					startButton.setText("Pause Game");
				}
				else {
					_gridview.setMode(Grid.PAUSE);
					startButton.setText("Play Game");
				}
				break;
				
		}
	}
	
	@Override
	public void onProgressChanged(SeekBar bar, int progress, boolean isUser) {
		_gridview.setDelay(progress);
	}
	
	@Override
	public void onStartTrackingTouch(SeekBar seekBar) {
		//so the compiler doesnt yell at us
	}

	@Override
	public void onStopTrackingTouch(SeekBar seekBar) {
		//so the compiler doesnt yell at us
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case R.id.settings:
				startActivity(new Intent(this, PreferencesActivity.class));
				return true;
		}
		return false;
	}
	
	@Override
	protected void onPause() {
		super.onPause();
		_gridview.setMode(Grid.PAUSE);
	}
}
