package com.mskjp.NamedCapture;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class NamedMatcherTest {

	NamedPattern namedPattern = new NamedPattern(
			"(?P<name>[^\t]+)\t(?P<file>[^\t]+)(\t(?P<address>[^\t]+))?");

	@Before
	public void setUp() throws Exception {
	}

	@Test
	public void testNamedMatcher() {
		NamedMatcher matcher = namedPattern.matcher("name\tfile\taddress");

		assertThat(matcher, isA(NamedMatcher.class));
	}

	@Test
	public void testFindTrue() {
		NamedMatcher matcher = namedPattern
				.matcher("name\tfile\taddress\tname2\tfile2\taddress2\tname3\tfile3\taddress3");

		assertThat(matcher.find(), is(true));
		assertThat(matcher.group(0), is("name\tfile\taddress"));
		assertThat(matcher.find(), is(true));
		assertThat(matcher.group(0), is("name2\tfile2\taddress2"));
		assertThat(matcher.find(), is(true));
		assertThat(matcher.group(0), is("name3\tfile3\taddress3"));
		assertThat(matcher.find(), is(false));
	}

	@Test
	public void testFindFalse() {
		NamedMatcher matcher = namedPattern.matcher("namefileaddress");

		assertThat(matcher.find(), is(false));
	}

	@Test
	public void testFindInt() {
		NamedMatcher matcher = namedPattern.matcher("n\tf\ta");

		assertThat(matcher.find(0), is(true));

		// 4文字目からの検索では、ヒットしない
		assertThat(matcher.find(3), is(false));
	}

	@Test
	public void testGroupString() {
		NamedMatcher matcher = namedPattern.matcher("NAME\tFILE\tADDRESS");

		matcher.find();
		assertThat(matcher.group("name"), is("NAME"));
		assertThat(matcher.group("file"), is("FILE"));
		assertThat(matcher.group("address"), is("ADDRESS"));
		assertThat(matcher.group("test"), nullValue());
	}

	@Test
	public void testGroupInt() {
		NamedMatcher matcher = namedPattern.matcher("NAME\tFILE\tADDRESS");

		matcher.find();
		assertThat(matcher.group(1), is("NAME"));
		assertThat(matcher.group(2), is("FILE"));
		assertThat(matcher.group(4), is("ADDRESS"));
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGroupIntExcept1() {
		NamedMatcher matcher = namedPattern.matcher("NAME\tFILE\tADDRESS");

		matcher.find();
		matcher.group(5);
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testGroupIntExcept2() {
		NamedMatcher matcher = namedPattern.matcher("NAME\tFILE\tADDRESS");

		matcher.find();
		matcher.group(-1);
	}

	@Test
	public void testGroupCount() {
		NamedMatcher matcher = namedPattern.matcher("NAME\tFILE\tADDRESS");

		matcher.find();
		assertThat(matcher.groupCount(), is(4));
	}

	@Test
	public void testRegion() {
		NamedMatcher matcher = namedPattern.matcher("123N\tF\tA789");
		matcher.region(3, 8);
		assertThat(matcher.find(), is(true));
		assertThat(matcher.group(0), is("N\tF\tA"));
		
		matcher.region(0, 2);
		assertThat(matcher.find(), is(false));
		
		matcher.region(8, 10);
		assertThat(matcher.find(), is(false));
	}

	@Test
	public void testStartEnd() {
		NamedMatcher matcher = namedPattern.matcher("NAME\tFILE\tADDRESS\t");

		matcher.find();
		assertThat(matcher.start(1), is(0));
		assertThat(matcher.end(1), is(4));
		assertThat(matcher.start(2), is(5));
		assertThat(matcher.end(2), is(9));
		assertThat(matcher.start(4), is(10));
		assertThat(matcher.end(4), is(17));
	}

	@Test
	public void testGetMatchNamedGroup() {
		NamedPattern namedPattern = new NamedPattern(
				"([^\t]+)\t(?P<file>[^\t]+)(\t(?P<address>[^\t]+))?");

		NamedMatcher matcher = namedPattern.matcher("NAME\tFILE\tADDRESS\t");

		matcher.find();
		assertThat(matcher.getMatchNamedGroup(), is(2));
	}

	@Test
	public void testGetName() {
		NamedMatcher matcher = namedPattern.matcher("NAME\tFILE\tADDRESS\t");

		matcher.find();
		assertThat(matcher.getName(1), is("name"));
		assertThat(matcher.getName(2), is("file"));
		assertThat(matcher.getName(4), is("address"));
	}

}
