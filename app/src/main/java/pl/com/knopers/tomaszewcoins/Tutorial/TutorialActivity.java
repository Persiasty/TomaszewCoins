package pl.com.knopers.tomaszewcoins.Tutorial;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.github.amlcurran.showcaseview.ShowcaseView;
import com.github.amlcurran.showcaseview.targets.ViewTarget;

import pl.com.knopers.tomaszewcoins.Base.Playable;
import pl.com.knopers.tomaszewcoins.Components.AnimatedProgressBar;
import pl.com.knopers.tomaszewcoins.Components.Coin;
import pl.com.knopers.tomaszewcoins.Components.TCShowcaseDraver;
import pl.com.knopers.tomaszewcoins.Game.PlayerInGame;
import pl.com.knopers.tomaszewcoins.Game.SoundFx;
import pl.com.knopers.tomaszewcoins.R;

public class TutorialActivity extends Activity implements Playable, View.OnClickListener
{
	private static final int TIMELEFT = 20;

	private PlayerInGame player;
	private SoundFx soundFx;

	private Coin[] coins;

	private TextView mHighscore;
	private TextView mCounter;
	private AnimatedProgressBar progressbar;

	private int[][] tutorialChain = {
			{R.id.flip_image1, R.string.tutorial_tomaszew, R.string.tutorial_tomaszew_desc},
			{-1, R.string.tutorial_itn, R.string.tutorial_itn_desc},
			{-1, R.string.tutorial_tomaszew_again, R.string.tutorial_tomaszew_again_desc},
			{R.id.itn_count, R.string.tutorial_itn_count, R.string.tutorial_itn_count_desc},
			{R.id.higscore_count, R.string.tutorial_highscore, R.string.tutorial_highscore_desc},
			{R.id.timeleft, R.string.tutorial_time, R.string.tutorial_time_desc}
	};

	private int chainIndex = 0;
	private ShowcaseView sv;
	private TCShowcaseDraver dvr;

	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_coins);

		player = new PlayerInGame(this);
		soundFx = new SoundFx(this);
		coins = new Coin[9];

		mCounter = (TextView) findViewById(R.id.itn_count);
		mHighscore = (TextView) findViewById(R.id.higscore_count);
		mHighscore.setText("1 (Knopers)");

		initCoins();

		dvr = new TCShowcaseDraver();

		sv = new ShowcaseView.Builder(this)
				.withMaterialShowcase()
				.setShowcaseDrawer(dvr)
				.setStyle(R.style.TutorialStyle)
				.setContentTitle(R.string.tutorial_start)
				.setContentText(R.string.tutorial_start_desc)
				.setOnClickListener(this)
				.blockAllTouches()
				.build();
	}

	private void startup()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				progressbar = new AnimatedProgressBar(TutorialActivity.this,
														(ProgressBar) findViewById(R.id.timeleft),
														TIMELEFT, TIMELEFT);
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

	@Override
	public void updateHud()
	{
		runOnUiThread(new Runnable()
		{
			@Override
			public void run()
			{
				mCounter.setText(String.valueOf(player.getItnCount()));
			}
		});
	}

	@Override
	public PlayerInGame getPlayer()
	{
		return player;
	}

	@Override
	public SoundFx getSoundFx()
	{
		return soundFx;
	}

	@Override
	public void onClick(View v)
	{
		if(chainIndex == tutorialChain.length)
		{
			finish();
			return;
		}

		sv.setContentTitle(getString(tutorialChain[chainIndex][1]));
		sv.setContentText(getString(tutorialChain[chainIndex][2]));
		if(tutorialChain[chainIndex][0] >= 0)
		{
			View vi = findViewById(tutorialChain[chainIndex][0]);
			dvr.setSize(vi.getWidth(), vi.getHeight());
			sv.setShowcase(new ViewTarget(vi), true);
		}

		switch(chainIndex)
		{
			case 0:
				sv.setButtonText(getString(R.string.next));
				break;
			case 1:
				coins[0].makeFlip();
				break;
			case 2:
				coins[0].makeFlip();
				break;
			case 4:
				startup();
				break;
			case 5:
				sv.setButtonText(getString(R.string.tutorial_close));
				break;
		}
		chainIndex++;
	}
}