package top.cutexingluo.tools.designtools.juc.stream;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.concurrent.Callable;
import java.util.concurrent.RecursiveTask;

/**
 * ForkJoin 不建议使用
 */
@EqualsAndHashCode(callSuper = true)
@Data
@AllArgsConstructor
public class XTForkJoin extends RecursiveTask<Long> {
    private Long start;
    private Long end;
    private Long criticalValue;

    public Long run(Callable<Long> callable) throws Exception {
        if (end - start < criticalValue) {
            Long sum = 0L;
            sum += callable.call();
            return sum;
        } else {
            Long mid = start + end >> 1;
            XTForkJoin task1 = new XTForkJoin(start, mid, criticalValue);
            task1.join();
            XTForkJoin task2 = new XTForkJoin(mid, end, criticalValue);
            task2.join();
            return task1.join() + task2.join();
        }
    }

    public Long doRun() throws Exception {
        return run(() -> {
            long sum = 0L;
            for (Long i = start; i <= end; i++) {
                sum += i;
            }
            return sum;
        });
    }

    @Override
    protected Long compute() {
        Long result = 0L;
        try {
            result = doRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
