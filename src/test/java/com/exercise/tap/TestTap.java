package com.exercise.tap;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

import com.exercise.tap.Tap;

public class TestTap {

	@Test
	public void testGetPrice() {
		Tap t = new Tap();
		assertEquals(3.25, t.GetPrice("Stop1Stop2"));
		assertEquals(7.30, t.GetPrice("Stop3"));
	}

}
