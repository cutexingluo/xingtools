package top.cutexingluo.tools.utils.se.obj.ref;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.ref.*;

/**
 * 常规引用类
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/4/8 20:10
 * @since 1.0.5
 */
@Getter
public class Refer<T> {

    /**
     * 引用类型
     */
    public enum RType {
        /**
         * 普通
         */
        Strong,
        /**
         * 软
         */
        Soft,
        /**
         * 弱
         */
        Weak,
        /**
         * 虚
         */
        Phantom,
    }

    protected T refer;

    protected Reference<T> reference;

    protected RType referType;

    public Refer(T refer) {
        this(refer, RType.Strong, null);
    }


    public Refer(T refer, RType referType) {
        this(refer, referType, null);
    }


    public Refer(T refer, RType referType, ReferenceQueue<? super T> referenceQueue) {
        this.refer = refer;
        this.referType = referType;
        this.reference = of(refer, referType, referenceQueue);
    }

    @Nullable
    public static <T> Reference<T> of(T refer, @NotNull RType referType, ReferenceQueue<? super T> referenceQueue) {
        switch (referType) {
            case Soft:
                return new SoftReference<>(refer, referenceQueue);
            case Weak:
                return new WeakReference<>(refer, referenceQueue);
            case Phantom:
                return new PhantomReference<>(refer, referenceQueue);
            default:
                return null;
        }
    }

    public void clear() {
        this.refer = null;
        if (reference != null) {
            reference.clear();
        }
        this.referType = null;
    }

}
