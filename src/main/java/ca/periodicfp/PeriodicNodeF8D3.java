package ca.periodicfp;

/**
 * 用于判断循环边界F8直径3可逆性的Amoroso图节点
 * @author mjc
 */
public final class PeriodicNodeF8D3 {

    private static final int INT_SIZE = 32; // int类型的位数
    private static final int LEN = 4096; // 元组的种数
    private static final int P = 8; // 状态集
    private static final int SHIFT_ELE = 3; // 移位参数（单个元素）
    private static final int SHIFT_HALF = 6; // 移位参数（半元组）
    private static final int RULE_SIZE = 512;   // 规则长度
    private static final int FIRST_MASK = 4032; // 前半元组掩码
    private static final int SECOND_MASK = 63; // 后半元组掩码
    private static final byte[] rule;  // 规则
    private static final int[] periodicTuples; // 循环元组


    static {
        rule = new byte[RULE_SIZE];

        periodicTuples = new int[SECOND_MASK + 1];
        for (int i = 0; i <= SECOND_MASK; i++) {
            periodicTuples[i] = (i << SHIFT_HALF) + i;
        }
    }

    private boolean[] tuples;    // 记录每种元组是否出现

    private PeriodicNodeF8D3() {
        tuples = new boolean[LEN];
    }

    /**
     * 设置局部规则
     * @param r 局部规则的Wolfram数的字符串表示
     * @throws IllegalArgumentException 如果输入规则有误
     */
    public static void setRule(String r) throws IllegalArgumentException {
        if (r.length() != RULE_SIZE) {
            throw new IllegalArgumentException("Rule size incorrect.");
        }
        int i = RULE_SIZE - 1;
        for (char ch : r.toCharArray()) {
            if (ch < '0' || ch > '7') {
                throw new IllegalArgumentException("Rule format incorrect.");
            }
            rule[i--] = (byte) (ch - '0');
        }
    }

    /**
     * 返回根节点
     * @return 根节点
     */
    public static PeriodicNodeF8D3 getRoot() {
        PeriodicNodeF8D3 root = new PeriodicNodeF8D3();
        for (int tuple : periodicTuples) {
            root.tuples[tuple] = true;
        }
        return root;
    }

    /**
     * 判断节点是否为GOE
     * @return 是否为GOE
     */
    public boolean isGOE() {
        int periodicTupleCount = 0;
        for (int tuple : periodicTuples) {
            if (tuples[tuple])
                periodicTupleCount++;
        }
        return periodicTupleCount == 1;
    }

    /**
     * 求指定规则下节点的8个孩子
     * @return 节点的8个孩子节点
     */
    public PeriodicNodeF8D3[] getChildren() {
        PeriodicNodeF8D3[] children = new PeriodicNodeF8D3[8];
        for (int i = 0; i < 8; i++) {
            children[i] = new PeriodicNodeF8D3();
        }
        for (int i = 0; i < LEN; i++) {
            if (!tuples[i])
                continue;
            int first = i & FIRST_MASK;
            int secondFormer = (i & SECOND_MASK) << SHIFT_ELE;
            for (int j = 0; j < 8; j++) {
                int idx = rule[secondFormer + j];
                children[idx].tuples[first + ((secondFormer + j) & SECOND_MASK)] = true;
            }
        }
        return children;
    }

    /**
     * 相等当且仅当所有元组一样
     * @param o 比较对象
     * @return 是否相等
     */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof PeriodicNodeF8D3 that)) {
            return false;
        }
        for (int i = 0; i < LEN; i++) {
            if (tuples[i] != that.tuples[i]) {
                return false;
            }
        }
        return true;
    }

    /**
     * 每32种元组的出现情况编为一个int，所有int取异或作为哈希值
     * @return 节点哈希值
     */
    @Override
    public int hashCode() {
        int hash = 0, cur = 0;
        for (int i = 0; i < LEN; i++) {
            if (i % INT_SIZE == 0) {
                hash ^= cur;
            }
            cur <<= 1;
            cur += (tuples[i] ? 1 : 0);
        }
        hash ^= cur;
        return hash;
    }
}
