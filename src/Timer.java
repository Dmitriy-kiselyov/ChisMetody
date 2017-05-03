import static java.lang.System.out;
class Timer {

    private Long start, finish;
    private Long time;
    private String message;

    private boolean print = true;

    public void start() {
        start("", true);
    }

    public void start(String message) {
        start(message, true);
    }

    public void start(boolean print) {
        start("", print);
    }

    public void start(String message, boolean print) {
        if (start != null && finish == null)
            stop();

        if (message.trim().isEmpty())
            this.message = "";
        else
            this.message = " <" + message + ">";

        if (print)
            out.println("Start" + this.message);
        start = System.nanoTime() / 1000000;
    }

    public long stop() {
        finish = System.nanoTime() / 1000000;
        time = finish - start;

        if (print)
            out.println("Time" + message + " = " + time + " ms");

        start = null;
        finish = null;

        return time;
    }

    public long time() {
        finish = System.nanoTime() / 1000000;
        time = finish - start;

        if (print)
            out.println("Time" + message + " = " + time + " ms");

        return time;
    }

    public void setPrint(boolean print) {
        this.print = print;
    }
}