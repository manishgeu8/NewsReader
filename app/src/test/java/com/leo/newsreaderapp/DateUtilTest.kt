package com.leo.newsreaderapp

import com.leo.newsreaderapp.util.DateUtil
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Test

class DateUtilTest{

    @Test
    fun `if the date string is null should return empty value`(){
        val result = DateUtil.formatDate("")
        assertThat("",result.isEmpty())
    }

    @Test
    fun `if the date string is empty should return empty value`(){
        val result = DateUtil.formatDate("")
        assertThat("",result.isEmpty())
    }

    @Test
    fun `if the date string is not suitable should return empty value`(){
        val result = DateUtil.formatDate("2022-03-09")
        assertThat("",result.isEmpty())
    }

    //2022-03-09T21:10:00 doesn't returns 09.03.2022`
    @Test
    fun dateConvertTest1(){
        val result = DateUtil.formatDate("2022-03-09T21:10:00")
        assertThat("", result != "09.03.2022")
    }

    //2022-03-09T21:10:00 returns 09.03.2022 21:10:00`
    @Test
    fun dateConvertTest2(){
        val result = DateUtil.formatDate("2022-03-09T21:10:00")
        assertThat("", result =="09.03.2022 21:10:00")
    }
}