package top.cutexingluo.tools.designtools.juc.lock;

import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * 原子类，获取原子引用
 * <p>CAS -> AtomicReference 封装</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2023/5/6 13:14
 */
public class XTCas {
    public static <T> AtomicReference<T> getAtomicRef() {
        return new AtomicReference<>();
    }

    public static <T> AtomicReference<T> getAtomicRef(T object) {
        return new AtomicReference<>(object);
    }

    public static <T> AtomicStampedReference<T> getAtomicSRef(T object) {
        return new AtomicStampedReference<T>(object, 1);
    }

    public static <T> void compareAndSet(AtomicStampedReference<T> atomicStampedReference,
                                         T expectedRef,
                                         T newRef,
                                         int stamp, int newStamp) {
        atomicStampedReference.compareAndSet(expectedRef, newRef, stamp, newStamp);
    }

    public static <T> void compareAndSet(AtomicStampedReference<T> atomicStampedReference,
                                         T expectedRef, T newRef) {
        compareAndSet(atomicStampedReference, expectedRef, newRef,
                atomicStampedReference.getStamp(),
                atomicStampedReference.getStamp() + 1);
    }
}
