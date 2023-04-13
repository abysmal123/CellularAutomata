package ca.injectivityloop;

public final class Script20230412 {

    public static void main(String[] args) {
        int lr = 1, rr = 3;
        int d = 5;
        String r = "11000110010011101100011011001100";
        int n = 20;
        injectivityLoopCount.showLoop(r, d, n);
        System.out.println();
        injectivityLoopCount.showLoop(r, lr, rr, n);
    }

}
