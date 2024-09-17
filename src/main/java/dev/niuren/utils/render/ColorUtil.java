package dev.niuren.utils.render;

import java.awt.*;

public class ColorUtil {

    public static int toRGBA(int r, int g, int b, int a) {
        return (r << 16) + (g << 8) + b + (a << 24);
    }

    public static Color injectAlpha(Color color, int alpha) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), alpha);
    }

    public static int injectAlpha(int color, int alpha) {
        return toRGBA(new Color(color).getRed(), new Color(color).getGreen(), new Color(color).getBlue(), alpha);
    }
    public static Color interpolateColorsBackAndForth(int speed, int index, Color start, Color end, boolean trueColor) {
        int angle = (int) (((System.currentTimeMillis()) / speed + index) % 360);
        angle = (angle >= 180 ? 360 - angle : angle) * 2;
        return trueColor ? interpolateColorHue(start, end, angle / 360f) : interpolateColorC(start, end, angle / 360f);
    }

    public static Color interpolateColorC(Color color1, Color color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));
        return new Color(interpolateInt(color1.getRed(), color2.getRed(), amount), interpolateInt(color1.getGreen(), color2.getGreen(), amount), interpolateInt(color1.getBlue(), color2.getBlue(), amount), interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }

    public static Color interpolateColorHue(Color color1, Color color2, float amount) {
        amount = Math.min(1, Math.max(0, amount));

        float[] color1HSB = Color.RGBtoHSB(color1.getRed(), color1.getGreen(), color1.getBlue(), null);
        float[] color2HSB = Color.RGBtoHSB(color2.getRed(), color2.getGreen(), color2.getBlue(), null);

        Color resultColor = Color.getHSBColor(interpolateFloat(color1HSB[0], color2HSB[0], amount), interpolateFloat(color1HSB[1], color2HSB[1], amount), interpolateFloat(color1HSB[2], color2HSB[2], amount));

        return new Color(resultColor.getRed(), resultColor.getGreen(), resultColor.getBlue(), interpolateInt(color1.getAlpha(), color2.getAlpha(), amount));
    }
    public static double interpolate(double oldValue, double newValue, double interpolationValue) {
        return (oldValue + (newValue - oldValue) * interpolationValue);
    }

    public static float interpolateFloat(float oldValue, float newValue, double interpolationValue) {
        return (float) interpolate(oldValue, newValue, (float) interpolationValue);
    }

    public static int interpolateInt(int oldValue, int newValue, double interpolationValue) {
        return (int) interpolate(oldValue, newValue, (float) interpolationValue);
    }

    public static Color pulseColor(Color color, int index, int count) {
        float[] hsb = new float[3];
        Color.RGBtoHSB(color.getRed(), color.getGreen(), color.getBlue(), hsb);
        float brightness = Math.abs((System.currentTimeMillis() % ((long) 1230675006 ^ 0x495A9BEEL) / Float.intBitsToFloat(Float.floatToIntBits(0.0013786979f) ^ 0x7ECEB56D) + index / (float) count * Float.intBitsToFloat(Float.floatToIntBits(0.09192204f) ^ 0x7DBC419F)) % Float.intBitsToFloat(Float.floatToIntBits(0.7858098f) ^ 0x7F492AD5) - Float.intBitsToFloat(Float.floatToIntBits(6.46708f) ^ 0x7F4EF252));
        brightness = Float.intBitsToFloat(Float.floatToIntBits(18.996923f) ^ 0x7E97F9B3) + Float.intBitsToFloat(Float.floatToIntBits(2.7958195f) ^ 0x7F32EEB5) * brightness;
        hsb[2] = brightness % Float.intBitsToFloat(Float.floatToIntBits(0.8992331f) ^ 0x7F663424);
        return new Color(Color.HSBtoRGB(hsb[0], hsb[1], hsb[2]));
    }

    public static Color pulseColor(Color startColor, Color endColor, int index, int count, double speed) {
        double brightness = Math.abs((System.currentTimeMillis() * speed % ((long) 1230675006 ^ 0x495A9BEEL) / Float.intBitsToFloat(Float.floatToIntBits(0.0013786979f) ^ 0x7ECEB56D) + (double) index / count * Float.intBitsToFloat(Float.floatToIntBits(0.09192204f) ^ 0x7DBC419F)) % Float.intBitsToFloat(Float.floatToIntBits(0.7858098f) ^ 0x7F492AD5) - Float.intBitsToFloat(Float.floatToIntBits(6.46708f) ^ 0x7F4EF252));
        //brightness = Float.intBitsToFloat(Float.floatToIntBits(18.996923f) ^ 0x7E97F9B3) + Float.intBitsToFloat(Float.floatToIntBits(2.7958195f) ^ 0x7F32EEB5) * brightness;
        double quad = brightness % Float.intBitsToFloat(Float.floatToIntBits(0.8992331f) ^ 0x7F663424);
        return fadeColor(startColor, endColor, quad);
    }

    public static Color fadeColor(Color startColor, Color endColor, double quad) {
        int sR = startColor.getRed();
        int sG = startColor.getGreen();
        int sB = startColor.getBlue();
        int sA = startColor.getAlpha();

        int eR = endColor.getRed();
        int eG = endColor.getGreen();
        int eB = endColor.getBlue();
        int eA = endColor.getAlpha();
        return new Color((int) (sR + (eR - sR) * quad),
                (int) (sG + (eG - sG) * quad),
                (int) (sB + (eB - sB) * quad),
                (int) (sA + (eA - sA) * quad));
    }
}

