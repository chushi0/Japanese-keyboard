package online.cszt0.japanesekeyboard;

import static junit.framework.TestCase.fail;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.AndroidJUnit4;

import online.cszt0.japanesekeyboard.util.Dictionary;

/**
 * 字典测试
 */
@RunWith(AndroidJUnit4.class)
public class DictionaryTest {
	private static Context context;

	@BeforeClass
	public static void initialize() {
		// 获取上下文
		context = InstrumentationRegistry.getTargetContext();
		// 初始化字典
		Dictionary.init(context);
	}

	/**
	 * 测试日语解析功能
	 */
	@Test
	public void parseInput() {
		final String[][] testList = {{"kukuasyu", "くくあしゅ"}, {"aaa", "あああ"}, {"aiueo", "あいうえお"}, {"kakiku", "かきく"},
				{"maaosaonimasao", "まあおさおにまさお"},
				// 这里打出的实际上是 “さおだおぃぁみ”，但此处不考虑“ぃぁ”这种，而是照原样打出
				{"saodaoxilami", "さおだおxいlあみ"},
				// 下面的是有意义的句子
				// 欢迎光临
				{"youkoso", "ようこそ"},
				// 只在这里的
				{"kokodakeno", "ここだけの"}};
		StringBuilder builder = new StringBuilder();
		for (String[] test : testList) {
			String result = Dictionary.parseHiragana(test[0]);
			if (!result.equals(test[1])) {
				builder.append(String.format("input = %s, expected '%s', but got '%s'", test[0], test[1], result))
						.append('\n');
			}
		}
		if (builder.length() > 0) {
			fail(builder.toString());
		}
	}
}
