package de.grundid.api.utils;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {

    @Test
    public void itShouldCamelCase() throws Exception {
        Assert.assertEquals("testMeNow", Utils.camelCase("TEST_ME_NOW"));
    }
}