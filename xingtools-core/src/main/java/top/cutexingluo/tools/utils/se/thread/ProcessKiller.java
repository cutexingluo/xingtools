package top.cutexingluo.tools.utils.se.thread;

/**
 * 线程 killer
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/8 17:45
 */
public class ProcessKiller extends Thread {
    protected Process process;

    public ProcessKiller(Process process) {
        this.process = process;
    }

    @Override
    public void run() {
        this.process.destroy();
    }
}
