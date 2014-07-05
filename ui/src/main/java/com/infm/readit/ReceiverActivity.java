package com.infm.readit;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;

public class ReceiverActivity extends Activity {

	private static final String LOGTAG = "ReceiverActivity";
	private static final String READER_FRAGMENT_TAG = "ReaSq!d99erFra{{1239gm..1ent1923";

	public static void startReceiverActivity(Context context, Integer intentType, String intentPath){
		Intent intent = new Intent(context, ReceiverActivity.class);

		Bundle bundle = new Bundle();
		bundle.putInt(Constants.EXTRA_TYPE, intentType);
		bundle.putString(Constants.EXTRA_PATH, intentPath);
		intent.putExtras(bundle);

		context.startActivity(intent);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_receiver);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

		Bundle bundle = bundleReceivedData();
		startReaderFragment(bundle);
	}

	private Bundle bundleReceivedData(){
		Bundle bundle = getIntent().getExtras();
		if (bundle != null){
			//bundle.putLong(Constants.EXTRA_UNIQUE_ID, System.currentTimeMillis());
			Log.w(LOGTAG, "bundle: " + bundle.toString());
		} else {
			Log.i(LOGTAG, "bundle: " + null);
		}
		return bundle;
	}

	private void startReaderFragment(Bundle bundle){
		FragmentManager fragmentManager = getFragmentManager();
		ReaderFragment readerFragment = (ReaderFragment) fragmentManager.findFragmentByTag(READER_FRAGMENT_TAG);
		if (readerFragment == null){
			readerFragment = new ReaderFragment();
			if (bundle != null)
				readerFragment.setArguments(bundle);
			else
				Log.w(LOGTAG, "startReaderFragment(): bundle is null");
			getFragmentManager().beginTransaction().
					add(R.id.fragment_container, readerFragment, READER_FRAGMENT_TAG).
					addToBackStack(null).
					commit();
		}
	}
}
