package com.mskjp.NamedCapture;

import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class NamedPattern implements Cloneable {

	/**
	 * Patternのラッピング
	 */
	private Pattern pattern;
	/**
	 * nameの保存
	 */
	private ArrayList<String> names;

	/**
	 * nameに存在する有効な名前の数
	 */
	private int namesCount = 0;

	/**
	 * 名前なし検索文字列
	 */
	private String unnamedPattern;

	
	/**
	 * 正規表現パターンをコンパイルする Pattern.compileに相当
	 * 
	 * @param regex
	 */
	public NamedPattern(CharSequence regex) {
		this(regex, 0, "P");
	}

	/**
	 * 正規表現パターンをコンパイルする Pattern.compileに相当
	 * 
	 * @param regex
	 * @param flags
	 */
	public NamedPattern(CharSequence regex, int flags) {
		this(regex, flags, "P");
	}

	/**
	 * 必要になったので追加。
	 * 名前付きキャプチャの(?P<name>)の"P"の部分を指定できる
	 * 
	 * @param regex
	 * @param flags
	 * @param nameString
	 */
	public NamedPattern(CharSequence regex, int flags, String nameString) {
		/**
		 * キャプチャ名を検索、キャプチャするための正規表現文字列
		 */
		final Pattern namePattern = Pattern
				.compile("(?<!\\\\)\\(((?=[^?])|(\\?" + nameString
						+ "<(.+?)>))");

		// 正規表現文字列の分析
		// "("単位に?P<name>を取得し、後で割り当てられるようにnameを保存しておく
		Matcher nameMatcher = namePattern.matcher(regex);

		namesCount = 0;
		names = new ArrayList<String>();
		// 全体を表す、index=0は名前が無いので空文字列
		names.add("");

		while (nameMatcher.find()) {
			// group(2)に、nameに対応する文字列が取得できている
			String name = nameMatcher.group(3);
			if (name == null) {
				// 名前が指定されていないグループの場合は、数を合わせるために空文字列とする
				names.add("");
			} else {
				namesCount++;
				names.add(name);
			}
		}

		// ?P<name>を削除した文字列を生成し、それを正規表現の文字列とする
		// nameMatcherが"(?P<name>"または、"("に該当するはずなので、resetしてそのままreplaceAllで"("に置換
		// "("の数は変わらないため、groupはnamesと同じ数になるはず。
		nameMatcher.reset();
		unnamedPattern = nameMatcher.replaceAll("(");

		pattern = Pattern.compile(unnamedPattern, flags);
	}

	@Override
	public Object clone() {
		NamedPattern clone = null;
		try {
			clone = (NamedPattern) super.clone();
			clone.pattern = Pattern.compile(this.pattern.pattern());
			clone.names = new ArrayList<>(this.names);
		} catch (CloneNotSupportedException e) {
			e.printStackTrace();
		}

		return clone;
	}

	/**
	 * matcherを取得する
	 * 
	 * @param text
	 * @return
	 */
	public NamedMatcher matcher(CharSequence text) {
		Matcher matcher = pattern.matcher(text);

		return new NamedMatcher(this, matcher);
	}

	/**
	 * 認識している名前の数を取得する 名前付けされていないグループはカウントしない
	 * 
	 * @return
	 */
	public int getNamesCount() {
		return namesCount;
	}

	/**
	 * 名前に対するgroupを返却する nameがnullまたは、空文字列、存在んしない名前の場合は、-1を返却する
	 * 
	 * @param name
	 * 
	 * @return
	 */
	public int getGroup(String name) {
		if (name == null || name.isEmpty()) {
			return -1;
		}
		return names.indexOf(name);
	}

	/**
	 * 認識している名前を取得する 名前付けされていないグループは、空文字列
	 * 
	 * @return
	 */
	public String getName(int index) {
		return names.get(index);
	}

	/**
	 * 名前付きキャプチャの名前部分を排除した正規表現文字列を取得する
	 * 
	 * @return
	 */
	public String getUnNamedPattern() {
		return unnamedPattern;
	}

}
