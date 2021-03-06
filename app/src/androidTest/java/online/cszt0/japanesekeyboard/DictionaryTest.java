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
	 * 测试日语解析功能（平假）
	 */
	@Test
	public void parseHiraganaInput() {
		final String[][] testList = {
				// 随意打的可以打出字的用例
				{"kukuasyu", "くくあしゅ"}, {"aaa", "あああ"}, {"aiueo", "あいうえお"}, {"kakiku", "かきく"},
				{"maaosaonimasao", "まあおさおにまさお"},
				// 这里打出的实际上是 “さおだおぃぁみ”，但此处不考虑“ぃぁ”这种，而是照原样打出
				{"saodaoxilami", "さおだおxいlあみ"},
				// 乱码应该照常输出
				{"lmzxwq", "lmzxwq"},
				// 单字符
				{"m", "m"}, {"v", "v"},
				// 未正常结尾
				{"kumam", "くまm"},
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

	/**
	 * 测试日语解析功能（片假）
	 */
	@Test
	public void parseKatakanaInput() {
		final String[][] testList = {
				// 随意打的可以打出字的用例
				{"kukuasyu", "ククアシュ"}, {"aaa", "アアア"}, {"aiueo", "アイウエオ"}, {"kakiku", "カキク"},
				{"maaosaonimasao", "マアオサオニマサオ"},
				// 这里打出的实际上是 “サオダオィァミ”，但此处不考虑“ィァ”这种，而是照原样打出
				{"saodaoxilami", "サオダオxイlアミ"},
				// 乱码应该照常输出
				{"lmzxwq", "lmzxwq"},
				// 单字符
				{"m", "m"}, {"v", "v"},
				// 未正常结尾
				{"kumam", "クマm"},
				// 下面的是有意义的句子
				// 加帕里公园
				{"japaripaku", "ジャパリパク"},
				// 朋友
				{"furenzu", "フレンズ"},
				// 啦啦啦啦
				{"rararara","ララララ"},
				// 当做礼物
				{"purezento","プレゼント"}};
		StringBuilder builder = new StringBuilder();
		for (String[] test : testList) {
			String result = Dictionary.parseKatakana(test[0]);
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
