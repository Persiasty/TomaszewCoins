package pl.com.knopers.tomaszewcoins;

import android.app.Activity;
import android.widget.ProgressBar;

/**
 * Created by Knopers on 16.03.2017.
 */

public class TimerTask extends java.util.TimerTask
{
	private Activity activity;
	private ProgressBar progressbar;
	private int time;

	public TimerTask(Activity activity, int timeLimit)
	{
		super();

		progressbar = (ProgressBar) activity.findViewById(R.id.timeleft);

		this.activity = activity;

		this.time = timeLimit;
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				progressbar.setProgress(time);
				progressbar.setMax(time);
			}
		});
	}

	@Override
	public void run()
	{
		if(time <= 0)
			this.cancel();

		this.time--;
		activity.runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				progressbar.setProgress(time);
			}
		});
	}
}
