package pl.com.knopers.tomaszewcoins.Utils;

import android.util.Log;

/**
 * Created by Knopers on 17.03.2017.
 */

public class MxYor
{
	public static int validate(String str)
	{
		try
		{
			int ret = Integer.parseInt(str, 2);
			if(ret >= 0)
				return ret;
			throw new Exception();
		}
		catch(Exception e)
		{
			return -1;
		}
	}

	public static String get(String v, String k)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < v.length(); i++)
			sb.append((char)(v.charAt(i) ^ k.charAt(i % k.length())));
		Log.d("MxYor", sb.toString());
		return sb.toString();
	}
}
