package cn.edu.nju.candleflame.weixingood;

import org.junit.Test;

import static org.junit.Assert.*;

public class PeopleUtilTest {
    @Test
    public void test(){
        int[] result=PeopleUtil.getNumbers(3);
        System.out.printf(result.length+"");
        assertEquals(3,result.length);
    }
}