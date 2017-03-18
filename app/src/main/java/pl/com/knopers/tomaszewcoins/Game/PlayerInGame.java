package pl.com.knopers.tomaszewcoins.Game;

import android.app.Activity;

import pl.com.knopers.tomaszewcoins.Base.Player;

/**
 * Created by Knopers on 17.03.2017.
 */

public class PlayerInGame extends Player
{
	private CoinsActivity coinsActivity;

	public PlayerInGame(CoinsActivity coinsActivity)
	{
		this.coinsActivity = coinsActivity;
	}

	@Override
	public void addItn(int num)
	{
		super.addItn(num);
		coinsActivity.updateHud();
	}

	@Override
	public void reduceItn(int num)
	{
		super.reduceItn(num);
		coinsActivity.updateHud();
	}

	public void reset()
	{
		iItn = 0;
	}
}
