package ca.valuebasedlinear;

import ca.catools.Tools;

import java.io.IOException;

public class Script20230408 {
    public static void main(String[] args) throws IOException {
        int[] zeroFunctions = {1, 4, 5}; // 001, 100, 101
        int[] oneFunctions = {1, 4, 5, 3, 6, 7}; // 001, 100, 101, 011, 110, 111
        ShowProcedureTreeECA.setPath("graph/2023.4.8/");
        for (int z : zeroFunctions) {
            for (int o : oneFunctions) {
                String r = toWolfram(z, o);
                String label = "[0:" + Tools.toNBitString(z, 3) + ", 1:" + Tools.toNBitString(o, 3) + "] " + r;
                ShowProcedureTreeECA.storeImage(r, label);
            }
        }
    }

    static String toWolfram(int zeroFunction, int oneFunction) {
        StringBuffer buffer = new StringBuffer();
        for (int i = 7; i >= 0; i--) {
            buffer.append(Integer.bitCount(i & (((i >> 1) & 1) == 0 ? zeroFunction : oneFunction)) % 2);
        }
        return buffer.toString();
    }
}
