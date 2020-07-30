package com.sammidev.demo.utils;
import java.util.function.Predicate;
public class Kegantengan implements Predicate<String> {
    static enum StartWith{ay, ah, sa, mi, ad, it, da, di}
    @Override
    public boolean test(String isGanteng) {
      return isGanteng.startsWith(StartWith.ay.toString()) ||
              isGanteng.startsWith(StartWith.sa.toString()) ||
              isGanteng.startsWith(StartWith.ad.toString()) ||
              isGanteng.startsWith(StartWith.da.toString()) &&
              isGanteng.endsWith(StartWith.ah.toString()) ||
              isGanteng.endsWith(StartWith.mi.toString()) ||
              isGanteng.endsWith(StartWith.it.toString()) ||
              isGanteng.endsWith(StartWith.di.toString()) &&
              isGanteng.length() == 5 ||
              isGanteng.length() == 9 ||
              isGanteng.length() == 4;
    }
}