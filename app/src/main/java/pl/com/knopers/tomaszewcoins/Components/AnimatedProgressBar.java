package pl.com.knopers.tomaszewcoins.Components;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.animation.LinearInterpolator;
import android.widget.ProgressBar;


public class AnimatedProgressBar
{
	private ObjectAnimator animation;

	public AnimatedProgressBar(Context ctx, ProgressBar progressBar, int from, int duration)
	{
		from *= 1000;
		progressBar.setMax(from);
		progressBar.setProgress(from);

		animation = ObjectAnimator.ofInt(progressBar, "progress", progressBar.getProgress(), 0);
		animation.setDuration(duration * 1000);
		animation.setInterpolator(new LinearInterpolator());
	}

	public void startAnimation()
	{
		animation.start();
	}
}