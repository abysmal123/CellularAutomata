package ca.injectivitycircle;

import java.util.*;
import ca.catools.Tools;

<<<<<<< Updated upstream:src/main/java/ca/injectivitycircle/InjectivityCircleCount.java
public final class InjectivityCircleCount {     // d >= 3
=======
public final class injectivityCircleCount {     // d >= 3
>>>>>>> Stashed changes:src/main/java/ca/injectivitycircle/injectivityCircleCount.java

    // 统计环（规则，左半径，右半径，细胞数，移位相同是否视为同一配置，是否打印到控制台）,打印环长分布列表，返回环长分布的位图（bitmap）
    public static long showCircleList(String r, int lr, int rr, int n, boolean shiftingAsOne, boolean show) {
        int d = lr + rr + 1;
        boolean[] rule = Tools.getRuleAsBooleanArray(r);
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
                    image |= rule[(preimage >> (n - 1 - j)) & ruleMask] ? 1 : 0;
                }
                curConfig = image;
                loop++;
            }
            loopCountMap.put(loop, loopCountMap.getOrDefault(loop, 0) + 1);
        }
        long loopBitMap = 0;
        if (show) {
            System.out.println("rule:\t\t" + r);
            System.out.println("n:   \t\t" + n);
            System.out.println("length\tcount");
            System.out.println("--------------");
        }
        for (Map.Entry<Integer, Integer> entry : loopCountMap.entrySet()) {
            loopBitMap |= (1L << (entry.getKey() - 1));
            if (show) {
                System.out.println(entry.getKey() + "\t\t" + entry.getValue());
            }
        }
        return loopBitMap;
    }

    // 打印特定长度的环（规则，左半径，右半径，细胞数，移位相同是否视为同一配置，环长度，起始序号，末尾序号）
    public static void showCircleWithLength(String r, int lr, int rr, int n, boolean shiftingAsOne, int lengthForPrint, int startNum, int endNum) {
        int d = lr + rr + 1;
        boolean[] rule = Tools.getRuleAsBooleanArray(r);
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
                    image |= rule[(preimage >> (n - 1 - j)) & ruleMask] ? 1 : 0;
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

    // 打印特定配置所处的环（规则，左半径，右半径，配置，移位相同是否视为同一配置）
    public static void showConfigInCircle(String r, int lr, int rr, String c, boolean shiftingAsOne) {
        int d = lr + rr + 1;
        int n = c.length();
        boolean[] rule = Tools.getRuleAsBooleanArray(r);
        int config = Tools.toInteger(c);
        int ruleMask = (1 << d) - 1;
        int lBoundaryMask = (1 << lr) - 1;
        int rBoundaryMask = (1 << rr) - 1;
        int maxConfig = (1 << n) - 1;
        Set<Integer> visited = new HashSet<>();
        String arrow = getArrow(n);
        System.out.println("rule:\t\t\t" + r);
        System.out.println("left radius:   \t\t" + lr);
        System.out.println("right radius:\t\t" + rr);
        System.out.println("configuration:\t" + c);
        System.out.println("-------------------------------");
        List<Integer> evolution = new ArrayList<>();
        int loop = 0;
        int curConfig = config;
        while (!visited.contains(curConfig)) {
            evolution.add(curConfig);
            if (shiftingAsOne) {
                int periodicConfig = curConfig;
                for (int k = 0; k < n; k++) {
                    if (visited.contains(periodicConfig)) {
                        break;
                    }
                    visited.add(periodicConfig);
                    periodicConfig = ((periodicConfig << 1) & maxConfig) | ((periodicConfig >> (n - 1)) & 1);
                }
            } else {
                visited.add(curConfig);
            }
            int preimage = ((curConfig & lBoundaryMask) << (n + rr)) | (curConfig << rr) | ((curConfig >> (n - rr)) & rBoundaryMask);
            int image = 0;
            for (int j = 0; j < n; j++) {
                image <<= 1;
                image |= rule[(preimage >> (n - 1 - j)) & ruleMask] ? 1 : 0;
            }
            curConfig = image;
            loop++;
        }
        evolution.add(curConfig);
        System.out.println("circle length: \t\t" + loop);
        for (int idx = 0; idx <= loop; idx++) {
            System.out.println(Tools.toNBitString(evolution.get(idx), n));
            if (idx < loop) {
                System.out.println(arrow);
            }
        }
        System.out.println("-------------------------------");
    }

    // 转换环长分布位图为元组的字符串表示
    public static String circleBitMapToTupleString(long circleBitMap) {
        StringBuffer buffer = new StringBuffer("(");
        int i = 1;
        while (circleBitMap > 0) {
            if ((circleBitMap & 1) == 1) {
                buffer.append(i + ", ");
            }
            i++;
            circleBitMap >>= 1;
        }
        buffer.delete(buffer.length() - 2, buffer.length());
        buffer.append(")");
        return buffer.toString();
    }

    // 找到多对一的配置（规则，左半径，右半径，细胞个数，打印起始编号，打印终止编号）
    public static Map<Integer, List<Integer>> findManyForOne(String r, int lr, int rr, int n, int startNum, int endNum) {
        int d = lr + rr + 1;
        boolean[] rule = Tools.getRuleAsBooleanArray(r);
        int ruleMask = (1 << d) - 1;
        int lBoundaryMask = (1 << lr) - 1;
        int rBoundaryMask = (1 << rr) - 1;
        int maxConfig = (1 << n) - 1;
        Map<Integer, List<Integer>> evolutionMap = new TreeMap<>();
        boolean[] visited = new boolean[maxConfig + 1];
        String arrow = getArrow(n);
        System.out.println("rule:\t\t\t" + r);
        System.out.println("n:   \t\t\t" + n);
        System.out.println("-------------------------------");
        for (int i = 0; i <= maxConfig; i++) {
            if (visited[i]) continue;
            int periodicConfig = i;
            for (int k = 0; k < n; k++) {
                if (visited[periodicConfig]) {
                    break;
                }
                visited[periodicConfig] = true;
                periodicConfig = ((periodicConfig << 1) & maxConfig) | ((periodicConfig >> (n - 1)) & 1);
            }
            int preimage = ((i & lBoundaryMask) << (n + rr)) | (i << rr) | ((i >> (n - rr)) & rBoundaryMask);
            int image = 0;
            for (int j = 0; j < n; j++) {
                image <<= 1;
                image |= rule[(preimage >> (n - 1 - j)) & ruleMask] ? 1 : 0;
            }
            int periodicImage = image;
            for (int k = 0; k < n; k++) {
                if (evolutionMap.containsKey(periodicImage)) {
                    break;
                }
                periodicImage = ((periodicImage << 1) & maxConfig) | ((periodicImage >> (n - 1)) & 1);
            }
            evolutionMap.putIfAbsent(periodicImage, new ArrayList<>());
            evolutionMap.get(periodicImage).add(i);
        }
        int cnt = 0;
        for (Map.Entry<Integer, List<Integer>> entry : evolutionMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                cnt++;
            }
        }
        System.out.println("Number of Many-For-One:\t\t" + cnt);
        System.out.println("-------------------------------");
        int num = 0;
        for (Map.Entry<Integer, List<Integer>> entry : evolutionMap.entrySet()) {
            if (entry.getValue().size() > 1) {
                num++;
                if (num < startNum || num > endNum) {
                    continue;
                }
                StringBuilder sb = new StringBuilder("[");
                for (int c : entry.getValue()) {
                    sb.append(Tools.toNBitString(c, n)).append(',');
                }
                sb.setCharAt(sb.length() - 1, ']');
                System.out.println(sb.toString());
                System.out.println(arrow);
                System.out.println(Tools.toNBitString(entry.getKey(), n));
                System.out.println("-------------------------------");
            }
        }
        return evolutionMap;
    }

    public static String getArrow(int len) {
        return " ".repeat(Math.max(0, len / 2)) + "↓";
    }

}
