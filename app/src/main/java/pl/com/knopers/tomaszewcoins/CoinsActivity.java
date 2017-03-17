package pl.com.knopers.tomaszewcoins;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;


import java.io.IOException;
import java.util.Random;
import java.util.Timer;

public class CoinsActivity extends Activity
{
	private static final int TIMELEFT = 60;

	private MediaPlayer mediaPlayer;
	SharedPreferences prefs;
	String bs = "", ba = "";

	private ObjectAnimator mFlipper[], mUnFlipper[];
	private ImageView mFlipImage[];
	private TextView mHighscore;
	private TextView mCounter;

	private boolean mIsTail[];

	private volatile int mScore;
	private int bestScore;
	private String bestScoreAuthor;

	private AssetFileDescriptor mCoinSound, mFailSound;

	private Bitmap mHeadImage, mTailImage;
	private Handler handler;
	private Timer timer;

	private Random rand;

	private Runnable bgTask = new Runnable()
	{
		@Override
		public void run()
		{
			int id = rand.nextInt(8);
			if(mIsTail[id])
				mUnFlipper[id].start();
			else
			{
				mFlipper[id].start();
				mScore++;
			}
			updateCoinCounter();
			handler.postDelayed(this, rand.nextInt(1000));
		}
	};
	private java.util.TimerTask summary = new java.util.TimerTask()
	{
		@Override
		public void run()
		{
			handler.removeCallbacks(bgTask);
			if(mScore < bestScore || bestScore < 0)
				win();
			else
				lose();
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.coins_activity);

		mCounter = (TextView) findViewById(R.id.itn_count);
		mHighscore = (TextView) findViewById(R.id.higscore_count);

		mediaPlayer = new MediaPlayer();

		prefs = getPreferences(MODE_PRIVATE);
		bestScoreAuthor = ba = prefs.getString("best-score-author", getString(R.string.nobody));
		bs = prefs.getString("best-score", "-");
		bestScore = bs.equals("-") ? -1 : Integer.parseInt(MxYor.get(bs, ba), 2);

		updateHighscore();

		mCoinSound = getResources().openRawResourceFd(R.raw.coin);
		mFailSound = getResources().openRawResourceFd(R.raw.fail);
		mHeadImage = BitmapFactory.decodeResource(getResources(), R.drawable.head);
		mTailImage = BitmapFactory.decodeResource(getResources(), R.drawable.tail);

		mIsTail = new boolean[9];
		mFlipper = new ObjectAnimator[9];
		mUnFlipper = new ObjectAnimator[9];
		mFlipImage = new ImageView[9];

		initCoins();

		rand = new Random();
		timer = new Timer();

		handler = new Handler();

		startup();
	}

	private void startup()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				mScore = 0;
				updateCoinCounter();
				for(int i = 0; i < 9; i++)
				{
					if(mIsTail[i])
						mUnFlipper[i].start();
				}

				timer.schedule(new TimerTask(CoinsActivity.this, TIMELEFT), 1000, 1000);
				handler.postDelayed(summary, (TIMELEFT + 1) * 1000);

				handler.post(bgTask);
			}
		});
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
			catch(IllegalAccessException e)
			{
				e.printStackTrace();
				break;
			}
			catch(NoSuchFieldException  e)
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
						if(mIsTail[finalI])
						{
							playSound(mCoinSound);
							mUnFlipper[finalI].start();
							mScore--;
						}
						else
						{
							playSound(mFailSound);
							mScore++;
						}
						updateCoinCounter();
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
						mFlipImage[finalI].setImageBitmap(mIsTail[finalI] ? mTailImage : mHeadImage);
						mIsTail[finalI] = true;
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
						mFlipImage[finalI].setImageBitmap(mIsTail[finalI] ? mTailImage : mHeadImage);
						mIsTail[finalI] = false;
					}
				}
			});
		}
	}
	public void updateCoinCounter()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				mCounter.setText(String.valueOf(mScore));
			}
		});
	}
	public void updateHighscore()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				if(bestScore < 0)
					mHighscore.setText(bestScoreAuthor);
				else
					mHighscore.setText(String.format("%d (%s)", bestScore, bestScoreAuthor));
			}
		});
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
	private void win()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				AlertDialog.Builder builder = new AlertDialog.Builder(CoinsActivity.this);
				LayoutInflater inflater = getLayoutInflater();

				View v = inflater.inflate(R.layout.winner_dialog, null);
				final EditText field = (EditText) v.findViewById(R.id.username);

				builder.setView(v)
						.setPositiveButton(R.string.save, new DialogInterface.OnClickListener()
						{
							@Override
							public void onClick(DialogInterface dialog, int id)
							{
								bestScoreAuthor = ba = field.getText().toString();
								bestScore = mScore; bs = Integer.toBinaryString(mScore);

								prefs.edit().putString("best-score", MxYor.get(bs, ba))
										.putString("best-score-author", bestScoreAuthor)
										.apply();
								updateHighscore();
								startup();
							}
						})
						.setCancelable(false);

				builder.create()
						.show();
			}
		});
	}
	private void lose()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				new AlertDialog.Builder(CoinsActivity.this)
					.setMessage(R.string.lose)
					.setPositiveButton(R.string.again, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{
							startup();
						}
					})
					.setNegativeButton(R.string.quit, new DialogInterface.OnClickListener()
					{
						@Override
						public void onClick(DialogInterface dialog, int which)
						{

							CoinsActivity.this.finish();
						}
					})
					.setCancelable(false)
					.create()
					.show();
			}
		});
	}
}