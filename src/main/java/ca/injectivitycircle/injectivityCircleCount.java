package ca.injectivitycircle;

import java.util.*;
import ca.catools.Tools;

public final class injectivityCircleCount {         // d <= 5

    // 统计环（规则，左半径，右半径，细胞数，移位相同是否视为同一配置）
    public static void showCircleList(String r, int lr, int rr, int n, boolean shiftingAsOne) {
        int d = lr + rr + 1;
        int rule = Tools.getRule(r, 1 << d);
        int ruleMask = (1 << d) - 1;
        int lBoundaryMask = (1 << lr) - 1;
        int rBoundaryMask = (1 << rr) - 1;
        int maxConfig = (1 << n) - 1;
        Map<Integer, Integer> loopCountMap = new TreeMap<>();
        boolean[] visited = new boolean[1 << n];
        for (int i = 0; i <= maxConfig; i++) {
            if (visited[i]) {
                continue;
            }
            int loop = 0;
            int curConfig = i;
            while (!visited[curConfig]) {
                if (shiftingAsOne) {
                    int periodicConfig = curConfig;
                    for (int k = 0; k < n; k++) {
                        if (visited[periodicConfig]) {
                            break;
                        }
                        visited[periodicConfig] = true;
                        periodicConfig = ((periodicConfig << 1) & maxConfig) | ((periodicConfig >> (n - 1)) & 1);
                    }
                } else {
                    visited[curConfig] = true;
                }
                int preimage = ((curConfig & lBoundaryMask) << (n + rr)) | (curConfig << rr) | ((curConfig >> (n - rr)) & rBoundaryMask);
                int image = 0;
                for (int j = 0; j < n; j++) {
                    image <<= 1;
                    image |= (rule >> ((preimage >> (n - 1 - j)) & ruleMask)) & 1;
                }
                curConfig = image;
                loop++;
            }
            loopCountMap.put(loop, loopCountMap.getOrDefault(loop, 0) + 1);
        }
        System.out.println("rule:\t\t" + r);
        System.out.println("n:   \t\t" + n);
        System.out.println("length\tcount");
        System.out.println("--------------");
        for (Map.Entry<Integer, Integer> entry : loopCountMap.entrySet()) {
            System.out.println(entry.getKey() + "\t\t" + entry.getValue());
        }
    }

    // 打印特定长度的环（规则，左半径，右半径，细胞数，移位相同是否视为同一配置，环长度，起始序号，末尾序号）
    public static void showCircleWithLength(String r, int lr, int rr, int n, boolean shiftingAsOne, int lengthForPrint, int startNum, int endNum) {
        int d = lr + rr + 1;
        int rule = Tools.getRule(r, 1 << d);
        int ruleMask = (1 << d) - 1;
        int lBoundaryMask = (1 << lr) - 1;
        int rBoundaryMask = (1 << rr) - 1;
        int maxConfig = (1 << n) - 1;
        Map<Integer, Integer> loopCountMap = new TreeMap<>();
        boolean[] visited = new boolean[1 << n];
        int circleCnt = 0;
        String arrow = getArrow(n);
        System.out.println("rule:\t\t\t" + r);
        System.out.println("n:   \t\t\t" + n);
        System.out.println("-------------------------------");
        System.out.println("circle length:\t" + lengthForPrint);
        System.out.println("-------------------------------");
        for (int i = 0; i <= maxConfig; i++) {
            if (visited[i]) {
                continue;
            }
            List<Integer> evolution = new ArrayList<>();
            int loop = 0;
            int curConfig = i;
            while (!visited[curConfig]) {
                evolution.add(curConfig);
                if (shiftingAsOne) {
                    int periodicConfig = curConfig;
                    for (int k = 0; k < n; k++) {
                        if (visited[periodicConfig]) {
                            break;
                        }
                        visited[periodicConfig] = true;
                        periodicConfig = ((periodicConfig << 1) & maxConfig) | ((periodicConfig >> (n - 1)) & 1);
                    }
                } else {
                    visited[curConfig] = true;
                }
                int preimage = ((curConfig & lBoundaryMask) << (n + rr)) | (curConfig << rr) | ((curConfig >> (n - rr)) & rBoundaryMask);
                int image = 0;
                for (int j = 0; j < n; j++) {
                    image <<= 1;
                    image |= (rule >> ((preimage >> (n - 1 - j)) & ruleMask)) & 1;
                }
                curConfig = image;
                loop++;
            }
            evolution.add(curConfig);
            if (loop == lengthForPrint) {
                circleCnt++;
                if (circleCnt >= startNum && circleCnt <= endNum) {
                    System.out.println("Circle No." + circleCnt + ":");
                    for (int idx = 0; idx <= lengthForPrint; idx++) {
                        System.out.println(Tools.toNBitString(evolution.get(idx), n));
                        if (idx < lengthForPrint) {
                            System.out.println(arrow);
                        }
                    }
                    System.out.println("-------------------------------");
                }
            }
            loopCountMap.put(loop, loopCountMap.getOrDefault(loop, 0) + 1);
        }

    }

    private static String getArrow(int len) {
        StringBuffer buffer = new StringBuffer();
        buffer.append(" ".repeat(Math.max(0, len / 2)));
        buffer.append("↓");
        return  buffer.toString();
    }

}
