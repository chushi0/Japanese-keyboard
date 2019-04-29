package online.cszt0.japanesekeyboard.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import online.cszt0.japanesekeyboard.BuildConfig;

public class Dictionary {
	private static Word[] HIRAGANA;

	public static void init(Context context) {
		// TODO: read from assets
		AssetManager assetManager = context.getAssets();
		HIRAGANA = readFromAssets(assetManager, "hiragana");
	}

	private static Word[] readFromAssets(AssetManager assetManager, String name) {
		try (BufferedReader reader = new BufferedReader(
				new InputStreamReader(assetManager.open("dictionary/" + name + ".dic")))) {
			ArrayList<Word> wordArrayList = new ArrayList<>();
			String line;
			while ((line = reader.readLine()) != null) {
				int split = line.indexOf('|');
				if (split == -1) {
					continue;
				}
				String word = line.substring(0, split);
				char[] input = line.substring(split + 1).toCharArray();
				wordArrayList.add(new Word(word, input));
			}
			return wordArrayList.toArray(new Word[0]);
		} catch (IOException e) {
			if (BuildConfig.DEBUG) {
				Log.e("Directory", "Error occurred when read from assets.", e);
			}
			throw new RuntimeException("Error occurred when read from assets.", e);
		}
	}

	public static String parseHiragana(String inputString) {
		char[] input = inputString.toCharArray();
		StringBuilder result = new StringBuilder();
		ArrayList<Word> string = null;
		int len = input.length;
		int offset = 0;
		for (int i = 0; i < len;) {
			char c = input[i];
			if (string == null) {
				string = new ArrayList<>();
				offset = 0;
				// 搜索以字符 c 开头的字
				for (Word word : HIRAGANA) {
					if (word.input[0] == c) {
						string.add(word);
					}
				}
				// 如果为空，则直接放入结果集
				if (string.isEmpty()) {
					result.append(c);
					string = null;
				}
				i++;
			} else {
				offset++;
				// 检查完全匹配
				Word match = null;
				for (Word word : string) {
					if (word.input.length == offset) {
						match = word;
						break;
					}
				}
				// 过滤下一个字符
				Iterator<Word> iterable = string.iterator();
				while (iterable.hasNext()) {
					Word word = iterable.next();
					char[] inputMatch = word.input;
					if (inputMatch.length <= offset || inputMatch[offset] != c) {
						iterable.remove();
					}
				}
				// 如果没有结果了
				if (string.isEmpty()) {
					if (match != null) {
						// 之前已经有完全匹配，则放入完全匹配的字
						result.append(match.word);
					} else {
						// 否则，放入对应的英文
						result.append(inputString.substring(i - offset, i));
					}
					// 清除缓存
					string = null;
				} else {
					i++;
				}
			}
		}
		// 如果还有未完成内容
		if (string != null) {
			// 检查完全匹配
			Word match = null;
			for (Word word : string) {
				if (word.input.length == offset+1) {
					match = word;
					break;
				}
			}
			if (match != null) {
				// 如果存在，则放入文字
				result.append(match.word);
			} else {
				// 否则，放入英文
				result.append(inputString.substring(len - offset - 1));
			}
		}
		return result.toString();
	}

	private static class Word implements Cloneable {
		String word;
		char[] input;
		Word(String word, char... input) {
			this.word = word;
			this.input = input;
		}

		@Override
		public Object clone() {
			try {
				return super.clone();
			} catch (CloneNotSupportedException e) {
				return new Word(word, input.clone());
			}
		}
	}
}
