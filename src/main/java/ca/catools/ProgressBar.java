package ca.catools;

public class ProgressBar {
    private int step;   // 每次增长百分之几
    private int len;    // 每个=代表百分之几
    private String text = "Progress:|";    // 进度条文本
    private int lastUpdateLen = 0; // 上次进度条显示的长度
    private int cur = 0;    // 当前百分比

    public ProgressBar() {
        this.step = 1;
        this.len = 1;
        update();
    }

    public ProgressBar(int step) {
        if (step <= 0) {
            throw new IllegalArgumentException("invalid argument.");
        }
        this.step = step;
        this.len = 1;
        update();
    }

    public ProgressBar(int step, int len) {
        if (step <= 0 || len <= 0) {
            throw new IllegalArgumentException("invalid argument.");
        }
        this.step = step;
        this.len = len;
        update();
    }

    /**
     * 调用此方法使进度条增长step%
     */
    public void increase() {
        if (cur == 100) {
            return;
        }
        cur += step;
        if (cur > 100) {
            cur = 100;
        }
        update();
        if (cur == 100) {
            System.out.println();
        }
    }

    private void update() {
        System.out.print("\b".repeat(lastUpdateLen));
        int eqCnt = cur / len;
        String progressBar = text + "=".repeat(eqCnt) + cur + "%";
        lastUpdateLen = progressBar.length();
        System.out.print(text + "=".repeat(eqCnt) + cur + "%");
    }
}
