package com.mskjp.namedcapture;

import java.util.regex.Matcher;

/**
 * 名前付きキャプチャを可能とするMatcher Patternの生成は、コンストラクタで行なう
 * 
 * 必要なMethodの簡易実装のため、Interfaceの継承は行わない
 * なお、Matcherはfinal宣言であるため継承できないので、ラッピングで実装する
 */
// public class NamedMatcher implements MatchResult{
public class NamedMatcher {

	/**
	 * Matcherのラッピング
	 */
	private Matcher matcher;
	private NamedPattern pattern;

	/**
	 * 
	 * @param regexString
	 *            正規表現
	 */
	protected NamedMatcher(NamedPattern pattern, Matcher matcher) {
		this.pattern = pattern;
		this.matcher = matcher;
	}

	/**
	 * find
	 * 
	 * @return
	 */
	public boolean find() {
		return matcher.find();
	}

	/**
	 * find(start)
	 * 
	 * @param start
	 * 
	 * @return
	 */
	public boolean find(int start) {
		return matcher.find(start);
	}

	/**
	 * nameからgroupを取得する
	 * 
	 * @param name
	 * @return
	 */
	public String group(String name) {

		// グループの番号を取得する
		int index = pattern.getGroup(name);

		if (index < 0) {
			// indexがマイナスの場合は、不正や、存在しないnameなので、nullを返却する
			return null;
		}

		return group(index);
	}

	/**
	 * 指定のgroupを取得する
	 * 
	 * @param group
	 * @return
	 */
	public String group(int group) {

		if (group < 0 || matcher.groupCount() < group) {
			throw new IndexOutOfBoundsException();
		} else {
			return matcher.group(group);
		}
	}

	/**
	 * groupの数を取得する
	 * 
	 * @return
	 */
	public int groupCount() {
		return matcher.groupCount();
	}

	/**
	 * Matcher.region(start, end)
	 * 
	 * @param start
	 * @param end
	 */
	public void region(int start, int end) {
		matcher.region(start, end);
	}

	/**
	 * Matcher.start(group)
	 * 
	 * @param group
	 * @return
	 */
	public int start(int group) {
		return matcher.start(group);
	}

	/**
	 * Matcher.end(group)
	 * 
	 * @param group
	 * @return
	 */
	public int end(int group) {
		return matcher.end(group);
	}

	/**
	 * 最初にマッチした名前付きのGroupを返却する Groupがない場合は、0を返却する
	 * 
	 * @return groupのインデックス
	 */
	public int getMatchNamedGroup() {
		for (int i = 1; i <= matcher.groupCount(); i++) {
			if (matcher.group(i) != null) {
				if (!pattern.getName(i).isEmpty()) {
					return i;
				}
			}
		}

		return 0;
	}

	/**
	 * groupに対応するnameを返却する
	 * 
	 * @param group
	 * @return
	 */
	public String getName(int group) {
		return pattern.getName(group);
	}
}
