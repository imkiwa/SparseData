package com.rom.ide.sparsedata;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() throws Exception {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void test1() throws IOException {
        SparseData data = new SparseData("/Users/kiva/ss/1.dat", "/Users/kiva/ss/1.list");
        data.convertToImage("/Users/kiva/ss/1.img");
    }
}