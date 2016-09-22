package com.zncm.dminter.mmhelper.utils;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ColorGenerator {
    public static ColorGenerator DEFAULT = create(Arrays.asList(new Integer[]{Integer.valueOf(-957596), Integer.valueOf(-686759), Integer.valueOf(-416706), Integer.valueOf(-1784274), Integer.valueOf(-9977996), Integer.valueOf(-10902850), Integer.valueOf(-14642227), Integer.valueOf(-5414233), Integer.valueOf(-8366207)}));
    public static ColorGenerator MATERIAL = create(Arrays.asList(new Integer[]{Integer.valueOf(-1739917), Integer.valueOf(-1023342), Integer.valueOf(-4560696), Integer.valueOf(-6982195), Integer.valueOf(-8812853), Integer.valueOf(-10177034), Integer.valueOf(-11549705), Integer.valueOf(-11677471), Integer.valueOf(-11684180), Integer.valueOf(-8271996), Integer.valueOf(-5319295), Integer.valueOf(-30107), Integer.valueOf(-2825897), Integer.valueOf(-10929), Integer.valueOf(-18611), Integer.valueOf(-6190977), Integer.valueOf(-7297874)}));
    private final List<Integer> mColors;
    private final Random mRandom;
    public static int len;

    public static ColorGenerator create(List<Integer> colorList) {
        return new ColorGenerator(colorList);
    }

    private ColorGenerator(List<Integer> colorList) {
        this.mColors = colorList;
        this.mRandom = new Random(System.currentTimeMillis());
//        this.mRandom = new Random(System.currentTimeMillis());
    }

    public int getRandomColor() {
        return ((Integer) this.mColors.get(this.mRandom.nextInt(this.mColors.size()))).intValue();
    }

    public int getColor(Object key) {
        return ((Integer) this.mColors.get(Math.abs(key.hashCode()) % this.mColors.size())).intValue();
    }
}