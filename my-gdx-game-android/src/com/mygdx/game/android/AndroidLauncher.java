package com.mygdx.game.android;
import android.os.Bundle;
import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.mygdx.game.MyGdxGame;
////////////////////////////////////////////
import android.app.Dialog;
import android.graphics.Color;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;

public class AndroidLauncher extends AndroidApplication {
	private static final String AD_UNIT_ID_BANNER = "put in your admob id here david";
	protected AdView adView;
	protected View gameView;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		AndroidApplicationConfiguration cfg = new AndroidApplicationConfiguration();
		// cfg.useGL20 = false;
		cfg.useAccelerometer = false;
		cfg.useCompass = false;

		//set a new window
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
		getWindow().clearFlags(WindowManager.LayoutParams.FLAG_FORCE_NOT_FULLSCREEN);

		//set to relative layout
		RelativeLayout layout = new RelativeLayout(this);
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT);
		layout.setLayoutParams(params);

		//create a view for the banner
		AdView admobView = createAdView();
		layout.addView(admobView);
		
		//create the view for the game
		View gameView = createGameView(cfg);
		layout.addView(gameView);

		setContentView(layout);
		
		//start the adds!
		startAdvertising(admobView);
	}

	/*
	 * create the view for the add
	 */
	private AdView createAdView() {
		adView = new AdView(this);
		adView.setAdSize(AdSize.SMART_BANNER);
		adView.setAdUnitId(AD_UNIT_ID_BANNER);
		adView.setId(12345); // this is an arbitrary id, allows for relative positioning in createGameView()
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		adView.setLayoutParams(params);
		adView.setBackgroundColor(Color.BLACK);
		return adView;
	}
	
	/*
	 * create the view for the game, calls your game class
	 */
	private View createGameView(AndroidApplicationConfiguration cfg) {
		gameView = initializeForView(new MyGdxGame(), cfg); //put in your game class here David
		RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.WRAP_CONTENT);
		params.addRule(RelativeLayout.ALIGN_PARENT_TOP, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.CENTER_HORIZONTAL, RelativeLayout.TRUE);
		params.addRule(RelativeLayout.ABOVE, adView.getId());
		gameView.setLayoutParams(params);
		return gameView;
	}

	private void startAdvertising(AdView adView) {
		AdRequest adRequest = new AdRequest.Builder().build();
		adView.loadAd(adRequest);
	}

	@Override
	public void onResume() {
		super.onResume();
		if (adView != null) adView.resume();
	}

	@Override
	public void onPause() {
		if (adView != null) adView.pause();
		super.onPause();
	}

	@Override
	public void onDestroy() {
		if (adView != null) adView.destroy();
		super.onDestroy();
	}

	//menu for the back button
	@Override
	public void onBackPressed() {
		final Dialog dialog = new Dialog(this);
		dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

		LinearLayout ll = new LinearLayout(this);
		ll.setOrientation(LinearLayout.VERTICAL);

		//only teh quit button
		Button b1 = new Button(this);
		b1.setText("Quit");
		b1.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				finish();
			}
		});
		//you can create more buttons if you want
		ll.addView(b1);

		dialog.setContentView(ll);
		dialog.show();
	}
}
