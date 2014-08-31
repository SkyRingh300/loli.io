package io.loli.sc.server.action;

import io.loli.util.string.ShortUrl;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.Test;

public class ShortUrlTest {

    @Test
    public void testNoDup() {
        List<String> urls = new ArrayList<>();
        Set<Long> longs = new HashSet<>();
        for (int i = 0; i < 1000000; i++) {
            longs.add(System.nanoTime());
        }
        System.out.println(longs.size());
        for (Long l : longs) {
            String[] s = ShortUrl.shortText(String.valueOf(l), 7);
            urls.add(s[0]);
        }
        Set<String> result = new HashSet<>();
        result.addAll(urls);
        System.out.println(result.size());
    }
}
