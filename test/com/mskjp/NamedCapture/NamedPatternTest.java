package com.mskjp.NamedCapture;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.assertThat;

import java.util.regex.Pattern;

import org.junit.Before;
import org.junit.Test;

public class NamedPatternTest {

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNamedPatternCharSequenceInt() {
		NamedPattern namedPattern = new NamedPattern(
				"(?P<name>[^\t]+)\t(?P<file>[^\t]+\\.xls)(\t(?P<address>[^\t]+))?",
				Pattern.CASE_INSENSITIVE);

		assertThat(namedPattern, isA(NamedPattern.class));
		NamedMatcher matcher = namedPattern
				.matcher("name\tfilename.XLS\taddress");

		// flagが有効であれば、XLSとxlsが合致して、trueになるはず。
		assertThat(matcher.find(), is(true));

		NamedPattern namedPattern2 = new NamedPattern(
				"(?P<name>[^\t]+)\t(?P<file>[^\t]+\\.xls)(\t(?P<address>[^\t]+))?",
				0);

		assertThat(namedPattern2, isA(NamedPattern.class));
		NamedMatcher matcher2 = namedPattern2
				.matcher("name\tfilename.XLS\taddress");

		// flagが有効であれば、XLSとxlsが合致しないので、falseになるはず。
		assertThat(matcher2.find(), is(false));

	}

	@Test
	public void testNamedPatternCharSequenceIntString() {

		// 名前付きキャプチャをZで指定する
		NamedPattern namedPattern = new NamedPattern(
				"(?Z<name>[^\t]+)\t(?Z<file>[^\t]+\\.xls)(\t(?Z<address>[^\t]+))?",
				Pattern.CASE_INSENSITIVE, "Z");

		// インスタンスが作成できていること
		assertThat(namedPattern, isA(NamedPattern.class));

		// 名前が取得できること
		assertThat(namedPattern.getGroup("name"), is(1));
		assertThat(namedPattern.getGroup("file"), is(2));
		assertThat(namedPattern.getGroup("address"), is(4));
		assertThat(namedPattern.getGroup(""), is(-1));
		assertThat(namedPattern.getGroup(null), is(-1));

		// flagが有効であること
		NamedMatcher matcher = namedPattern
				.matcher("name\tfilename.XLS\taddress");
		// flagが有効であれば、XLSとxlsが合致して、trueになるはず。
		assertThat(matcher.find(), is(true));
	}

	@Test
	public void testClone() {
		NamedPattern namedPattern = new NamedPattern(
				"(?P<name>[^\t]+)\t(?P<file>[^\t]+)(\t(?P<address>[^\t]+))?");

		Object clone = namedPattern.clone();

		NamedPattern namedPattern2 = (NamedPattern) clone;
		
		assertThat(namedPattern2, isA(NamedPattern.class));
		assertThat(namedPattern2, not(is(namedPattern)));
		assertThat(namedPattern2.getNamesCount(), is(namedPattern.getNamesCount()));

		for (int i = 0; i < namedPattern2.getNamesCount(); i++) {
			assertThat(namedPattern2.getName(i), is(namedPattern.getName(i)));
		}

		assertThat(namedPattern2.getUnNamedPattern(), is(namedPattern.getUnNamedPattern()));
	}

	@Test
	public void testMatcher() {
		NamedPattern namedPattern = new NamedPattern(
				"(?P<name>[^\t]+)\t(?P<file>[^\t]+)(\t(?P<address>[^\t]+))?");

		assertThat(namedPattern.matcher("testtext"), isA(NamedMatcher.class));
	}

	@Test
	public void testGetNamesCount0() {
		NamedPattern namedPattern = new NamedPattern(
				"([^\t]+)\t([^\t]+)(\t([^\t]+))?");

		assertThat(namedPattern.getNamesCount(), is(0));

	}

	@Test
	public void testGetNamesCount3() {
		NamedPattern namedPattern = new NamedPattern(
				"(?P<name>[^\t]+)\t(?P<file>[^\t]+)(\t(?P<address>[^\t]+))?");

		assertThat(namedPattern.getNamesCount(), is(3));

	}

	@Test
	public void testGetGroup() {
		NamedPattern namedPattern = new NamedPattern(
				"(?P<name>[^\t]+)\t(?P<file>[^\t]+)(\t(?P<address>[^\t]+))?");

		assertThat(namedPattern.getGroup("name"), is(1));
		assertThat(namedPattern.getGroup("file"), is(2));
		assertThat(namedPattern.getGroup("address"), is(4));
		assertThat(namedPattern.getGroup(""), is(-1));
		assertThat(namedPattern.getGroup(null), is(-1));
	}

	@Test
	public void testGetName() {
		NamedPattern namedPattern = new NamedPattern(
				"(?P<name>[^\t]+)\t(?P<file>[^\t]+)(\t(?P<address>[^\t]+))?");

		assertThat(namedPattern.getName(0), is(""));
		assertThat(namedPattern.getName(1), is("name"));
		assertThat(namedPattern.getName(2), is("file"));
		assertThat(namedPattern.getName(3), is("")); // 4番目は名前がないので空文字列
		assertThat(namedPattern.getName(4), is("address"));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGetNameExcept1() {
		NamedPattern namedPattern = new NamedPattern(
				"(?P<name>[^\t]+)\t(?P<file>[^\t]+)(\t(?P<address>[^\t]+))?");

		namedPattern.getName(5);
	}

	@Test
	public void testGetUnNamedPattern() {
		NamedPattern namedPattern = new NamedPattern(
				"(?P<name>[^\t]+)\t(?P<file>[^\t]+)(\t(?P<address>[^\t]+))?");

		assertThat(namedPattern.getUnNamedPattern(),
				is("([^\t]+)\t([^\t]+)(\t([^\t]+))?"));

	}

}
