package pl.com.knopers.tomaszewcoins.Game;

import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import pl.com.knopers.tomaszewcoins.R;

/**
 * Created by Knopers on 17.03.2017.
 */

public class SoundFx
{
	private Context ctx;
	private MediaPlayer mediaPlayer;
	private Map<Fx, AssetFileDescriptor> cache;

	public enum Fx
	{
		Good(R.raw.coin),
		Bad(R.raw.fail);

		private int res;
		private Fx(int res)
		{
			this.res = res;
		}
		public int getResourceId()
		{
			return res;
		}
	}

	public SoundFx(Context ctx)
	{
		this.ctx = ctx;
		mediaPlayer = new MediaPlayer();
		cache = new HashMap<>();
	}

	public void playSound(Fx sound)
	{
		AssetFileDescriptor file = cache.get(sound);
		if(file == null)
		{
			cache.put(sound, file = ctx.getResources().openRawResourceFd(sound.getResourceId()));
		}

		try
		{
			mediaPlayer.reset();
			mediaPlayer.setDataSource(file.getFileDescriptor(), file.getStartOffset(), file.getDeclaredLength());
			mediaPlayer.prepare();
			mediaPlayer.start();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
	}
}
