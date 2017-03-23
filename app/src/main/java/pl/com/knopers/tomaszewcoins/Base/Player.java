package pl.com.knopers.tomaszewcoins.Base;


/**
 * Created by Knopers on 17.03.2017.
 */

public abstract class Player
{
	protected volatile int iItn;
	protected String name;
	public Player()
	{
	}

	public int getItnCount()
	{
		return iItn;
	}

	public void addItn(int num)
	{
		iItn += num;
	}

	public void reduceItn(int num)
	{
		iItn -= num;
	}

	public void reset()
	{
		iItn = 0;
	}
}
