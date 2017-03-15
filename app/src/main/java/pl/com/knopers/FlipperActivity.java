package pl.com.knopers;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.examples.animateproperty.R;

import java.io.IOException;
import java.util.Random;

public class FlipperActivity extends Activity
{

	private boolean mIsHead[];
	private ObjectAnimator mFlipper[], mUnFlipper[];
	private Bitmap mHeadImage, mTailImage;
	private ImageView mFlipImage[];
	private TextView mCounter;
	private int mScore;

	private AssetFileDescriptor mCoinSound, mFailSound;

	private MediaPlayer mediaPlayer;

	private Handler handler;
	private Random rand;

	private Runnable bgTask = new Runnable()
	{
		@Override
		public void run()
		{
			int id = rand.nextInt(8);
			if(mIsHead[id])
				mUnFlipper[id].start();
			else
				mFlipper[id].start();
			handler.postDelayed(this, rand.nextInt(1000));
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		mCounter = (TextView) findViewById(R.id.itn_count);

		mediaPlayer = new MediaPlayer();

		mCoinSound = getResources().openRawResourceFd(R.raw.coin);
		mFailSound = getResources().openRawResourceFd(R.raw.fail);
		mHeadImage = BitmapFactory.decodeResource(getResources(), R.drawable.head);
		mTailImage = BitmapFactory.decodeResource(getResources(), R.drawable.tail);

		mIsHead = new boolean[9];
		mFlipper = new ObjectAnimator[9];
		mUnFlipper = new ObjectAnimator[9];
		mFlipImage = new ImageView[9];

		initCoins();

		rand = new Random();

		handler = new Handler();
		handler.post(bgTask);
	}

	//Dzieci, nie róbcie tego w domu
	private void initCoins()
	{
		for(int i = 1; i < 10; i++)
		{
			int id;
			try
			{
				id = R.id.class.getField("flip_image" + i).getInt(0);
			}
			catch(IllegalAccessException | NoSuchFieldException e)
			{
				e.printStackTrace();
				break;
			}
			final int finalI = i - 1; //Simple but not best

			mFlipImage[finalI] = (ImageView) findViewById(id);
			mFlipImage[finalI].setOnTouchListener(new View.OnTouchListener()
			{
				@Override
				public boolean onTouch(View v, MotionEvent event)
				{
					if(event.getAction() == MotionEvent.ACTION_DOWN)
					{
						if(mIsHead[finalI])
						{
							playSound(mCoinSound);
							mUnFlipper[finalI].start();
							mScore++;
						}
						else
						{
							playSound(mFailSound);
							mScore--;
						}
						mCounter.setText(String.valueOf(mScore));
						return true;
					}
					return false;
				}
			});
			mFlipImage[finalI].setImageBitmap(mHeadImage);

			mFlipper[finalI] = ObjectAnimator.ofFloat(mFlipImage[finalI], "rotationY", 0f, 180f);
			mFlipper[finalI].setDuration(500);
			mFlipper[finalI].addUpdateListener(new AnimatorUpdateListener()
			{
				@Override
				public void onAnimationUpdate(ValueAnimator animation)
				{
					if(animation.getAnimatedFraction() >= 0.25f)
					{
						mFlipImage[finalI].setImageBitmap(mIsHead[finalI] ? mTailImage : mHeadImage);
						mIsHead[finalI] = true;
					}
				}
			});

			mUnFlipper[finalI] = ObjectAnimator.ofFloat(mFlipImage[finalI], "rotationY", 180f, 360f);
			mUnFlipper[finalI].setDuration(500);
			mUnFlipper[finalI].addUpdateListener(new AnimatorUpdateListener()
			{
				@Override
				public void onAnimationUpdate(ValueAnimator animation)
				{
					if(animation.getAnimatedFraction() >= 0.25f)
					{
						mFlipImage[finalI].setImageBitmap(mIsHead[finalI] ? mTailImage : mHeadImage);
						mIsHead[finalI] = false;
					}
				}
			});
		}
	}
	public void playSound(AssetFileDescriptor sound)
	{
		try
		{
			mediaPlayer.reset();
			mediaPlayer.setDataSource(sound.getFileDescriptor(), sound.getStartOffset(), sound.getDeclaredLength());
			mediaPlayer.prepare();
			mediaPlayer.start();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}