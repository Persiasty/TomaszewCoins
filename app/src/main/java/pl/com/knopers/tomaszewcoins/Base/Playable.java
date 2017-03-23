package pl.com.knopers.tomaszewcoins.Base;

import android.content.res.Resources;

import pl.com.knopers.tomaszewcoins.Game.PlayerInGame;
import pl.com.knopers.tomaszewcoins.Game.SoundFx;

/**
 * Created by Knopers on 23.03.2017.
 */

public interface Playable
{
	public void updateHud();
	public SoundFx getSoundFx();
	public PlayerInGame getPlayer();
	Resources getResources();
}
