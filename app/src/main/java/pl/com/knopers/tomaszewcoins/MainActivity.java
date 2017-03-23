package pl.com.knopers.tomaszewcoins;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import pl.com.knopers.tomaszewcoins.Game.CoinsActivity;
import pl.com.knopers.tomaszewcoins.Tutorial.TutorialActivity;

public class MainActivity extends Activity
{

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	public void play(View view)
	{
		Intent playIntent = new Intent(this, CoinsActivity.class);
		startActivity(playIntent);
	}

	public void tutorial(View view)
	{
		Intent playIntent = new Intent(this, TutorialActivity.class);
		startActivity(playIntent);
	}

	public void exit(View view)
	{
		finish();
	}
}
