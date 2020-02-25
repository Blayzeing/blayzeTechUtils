package blayzeTechUtils.math;
/**
 * A collection of hash mixing algorithms, as described by Thomas Wang in his
 * paper on integer hash functions: https://web.archive.org/web/20121102023700/http://www.concentric.net/~Ttwang/tech/inthash.htm
 * Implementation by Blayze Millward.
 */

public class Hashing {
	public static int hash32shift(int key)
	{
		key = ~key + (key << 15); // key = (key << 15) - key - 1;
		key = key ^ (key >>> 12);
		key = key + (key << 2);
		key = key ^ (key >>> 4);
		key = key * 2057; // key = (key + (key << 3)) + (key << 11);
		key = key ^ (key >>> 16);
		return (key);
	}
	public static long hash64shift(long key)
	{
		key = (~key) + (key << 21); // key = (key << 21) - key - 1;
		key = key ^ (key >>> 24);
		key = (key + (key << 3)) + (key << 8); // key * 265
		key = key ^ (key >>> 14);
		key = (key + (key << 2)) + (key << 4); // key * 21
		key = key ^ (key >>> 28);
		key = key + (key << 31);
		return (key);
	}
	public static int hash6432shift(long key)
	{
		key = (~key) + (key << 18); // key = (key << 18) - key - 1;
		key = key ^ (key >>> 31);
		key = key * 21; // key = (key + (key << 2)) + (key << 4);
		key = key ^ (key >>> 11);
		key = key + (key << 6);
		key = key ^ (key >>> 22);
		return ((int) key);
	}
}
