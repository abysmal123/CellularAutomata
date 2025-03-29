package ca.catools;

public class ProgressBar2 {
    private String text = "Progress:|";    // 进度条文本
    private int lastUpdateLen = 0; // 上次进度条显示的长度

    public ProgressBar2() {
    }

    public void update(double a, double b) {
        System.out.print("\b".repeat(lastUpdateLen));
        double cur = a / b * 100;
        String progressBar = text + String.format("%.2f", cur) + "%";
        lastUpdateLen = progressBar.length();
        System.out.print(progressBar);
    }
}
