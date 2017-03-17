package pl.com.knopers.tomaszewcoins;

/**
 * Created by Knopers on 17.03.2017.
 */

public class MxYor
{
	public static String get(String v, String k)
	{
		StringBuilder sb = new StringBuilder();
		for(int i = 0; i < v.length(); i++)
			sb.append((char)(v.charAt(i) ^ k.charAt(i % k.length())));
		return sb.toString();
	}
}
