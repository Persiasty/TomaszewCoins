package pl.com.knopers.tomaszewcoins.Game;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Random;
import java.util.Timer;

import pl.com.knopers.tomaszewcoins.Components.AnimatedProgressBar;
import pl.com.knopers.tomaszewcoins.Components.Coin;
import pl.com.knopers.tomaszewcoins.Utils.MxYor;
import pl.com.knopers.tomaszewcoins.R;

public class CoinsActivity extends Activity
{
	private static final int TIMELEFT = 60;

	private PlayerInGame player;
	private SoundFx soundFx;

	private Coin[] coins;
	SharedPreferences prefs;

	String bs = "", ba = "";
	private int bestScore;

	private String bestScoreAuthor;

	private TextView mHighscore;
	private TextView mCounter;
	private AnimatedProgressBar progressbar;

	private Handler handler;

	private Random rand;

	private Runnable bgTask = new Runnable()
	{
		private int prev = -1;
		@Override
		public void run()
		{
			int id = -1;
			do
			{
				id = rand.nextInt(8);
			} while(id == prev);
			prev = id;

			coins[id].makeFlip();
			handler.postDelayed(this, rand.nextInt(700) + 100);
		}
	};

	private java.util.TimerTask summary = new java.util.TimerTask()
	{
		@Override
		public void run()
		{
			handler.removeCallbacks(bgTask);
			if(player.getItnCount() < bestScore || bestScore < 0)
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

		player = new PlayerInGame(this);
		soundFx = new SoundFx(this);
		coins = new Coin[9];

		mCounter = (TextView) findViewById(R.id.itn_count);
		mHighscore = (TextView) findViewById(R.id.higscore_count);

		prefs = getPreferences(MODE_PRIVATE);
		bestScoreAuthor = ba = prefs.getString("best-score-author", getString(R.string.nobody));
		bs = prefs.getString("best-score", "-");
		bestScore = MxYor.validate(MxYor.get(bs, ba));

		initCoins();

		rand = new Random();
		handler = new Handler();

		startup();
	}

	private void startup()
	{
		player.reset();
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				for(Coin coin : coins)
				{
					coin.reset();
				}


				progressbar = new AnimatedProgressBar(CoinsActivity.this,
														(ProgressBar) findViewById(R.id.timeleft),
														60, TIMELEFT);
				handler.postDelayed(summary, TIMELEFT * 1000);

				handler.post(bgTask);
				progressbar.startAnimation();
			}
		});
	}

	private void initCoins()
	{
		for(int i = 0; i < 9; i++)
		{
			int id;
			try
			{
				id = R.id.class.getField("flip_image" + (i + 1)).getInt(0);
			}
			catch(IllegalAccessException e)
			{
				e.printStackTrace();
				continue;
			}
			catch(NoSuchFieldException  e)
			{
				e.printStackTrace();
				continue;
			}
			coins[i] = Coin.initCoin(this, (ImageView) findViewById(id));
		}
	}

	public void updateHud()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{

				mCounter.setText(String.valueOf(player.getItnCount()));
				if(bestScore < 0)
					mHighscore.setText(bestScoreAuthor);
				else
					mHighscore.setText(String.format("%d (%s)", bestScore, bestScoreAuthor));
			}
		});
	}

	public PlayerInGame getPlayer()
	{
		return player;
	}

	public SoundFx getSoundFx()
	{
		return soundFx;
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
								bestScore = player.getItnCount(); bs = Integer.toBinaryString(bestScore);

								prefs.edit().putString("best-score", MxYor.get(bs, ba))
										.putString("best-score-author", bestScoreAuthor)
										.apply();
								updateHud();
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