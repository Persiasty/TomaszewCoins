package pl.com.knopers.tomaszewcoins.Components;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import pl.com.knopers.tomaszewcoins.Base.Playable;
import pl.com.knopers.tomaszewcoins.Game.CoinsActivity;
import pl.com.knopers.tomaszewcoins.Game.SoundFx;
import pl.com.knopers.tomaszewcoins.R;

/**
 * Created by Knopers on 17.03.2017.
 */

public class Coin implements View.OnTouchListener
{
	private static Bitmap mHeadImage, mTailImage;

	private ImageView item;
	private Playable coinsActivity;
	private ObjectAnimator mFlipper, mUnFlipper;
	private boolean mIsHead, mClicked;

	private ValueAnimator.AnimatorUpdateListener flipperListener = new ValueAnimator.AnimatorUpdateListener()
	{
		@Override
		public void onAnimationUpdate(ValueAnimator animation)
		{
			mIsHead = animation.getAnimatedFraction() < 0.25f;
			item.setImageBitmap(mIsHead ? mHeadImage : mTailImage);
		}
	};

	private ValueAnimator.AnimatorUpdateListener unflipperListener = new ValueAnimator.AnimatorUpdateListener()
	{
		@Override
		public void onAnimationUpdate(ValueAnimator animation)
		{
			mIsHead = !(animation.getAnimatedFraction() < 0.25f);
			item.setImageBitmap(mIsHead ? mHeadImage : mTailImage);
		}
	};

	public static Coin initCoin(Playable coinsActivity, ImageView item)
	{
		if(mHeadImage == null)
			mHeadImage = BitmapFactory.decodeResource(coinsActivity.getResources(), R.drawable.head);

		if(mTailImage == null)
			mTailImage = BitmapFactory.decodeResource(coinsActivity.getResources(), R.drawable.tail);

		return new Coin(coinsActivity, item);
	}

	private Coin(Playable coinsActivity, ImageView item)
	{
		this.item = item;
		this.coinsActivity = coinsActivity;

		this.mIsHead = true;

		this.item.setOnTouchListener(this);
		this.item.setImageBitmap(mHeadImage);

		mFlipper = ObjectAnimator.ofFloat(this.item, "rotationY", 0f, 180f);
		mFlipper.setDuration(500);
		mFlipper.addUpdateListener(flipperListener);

		mUnFlipper = ObjectAnimator.ofFloat(this.item, "rotationY", 180f, 360f);
		mUnFlipper.setDuration(500);
		mUnFlipper.addUpdateListener(unflipperListener);
	}

	public ImageView getView()
	{
		return item;
	}

	public void makeFlip()
	{
		mClicked = false;
		if(mIsHead)
		{
			if(mUnFlipper.isRunning())
			{
				mUnFlipper.cancel();
			}

			mFlipper.start();
			coinsActivity.getPlayer().addItn(1);
		}
		else
		{
			if(mFlipper.isRunning())
			{
				mFlipper.cancel();
			}

			mUnFlipper.start();
		}
	}

	@Override
	public boolean onTouch(View v, MotionEvent event)
	{
		if(event.getAction() == MotionEvent.ACTION_DOWN)
		{
			if(mIsHead)
			{
				coinsActivity.getSoundFx().playSound(SoundFx.Fx.Bad);
				coinsActivity.getPlayer().addItn(1);
			}
			else if(!mClicked)
			{
				mClicked = true;
				coinsActivity.getSoundFx().playSound(SoundFx.Fx.Good);

				if(mFlipper.isRunning())
					mFlipper.cancel();

				mUnFlipper.start();
				coinsActivity.getPlayer().reduceItn(1);
			}

			return true;
		}
		return false;
	}

	public void reset()
	{
		if(!mIsHead)
		{
			if(mFlipper.isRunning())
			{
				mFlipper.cancel();
			}

			mUnFlipper.start();
		}
	}
}
