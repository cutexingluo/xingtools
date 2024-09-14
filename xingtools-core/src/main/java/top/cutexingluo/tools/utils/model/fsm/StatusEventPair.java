package top.cutexingluo.tools.utils.model.fsm;


import top.cutexingluo.tools.common.data.PairEntry;
import top.cutexingluo.tools.utils.model.fsm.base.BaseEvent;
import top.cutexingluo.tools.utils.model.fsm.base.BaseStatus;

import java.util.Arrays;

/**
 * StatusEventPair。status, event 键值对
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/6/24 16:18
 * @since 1.0.5
 */
public class StatusEventPair<S extends BaseStatus, E extends BaseEvent> implements PairEntry<S, E> {

    /**
     * 指定的状态
     */
    protected final S status;
    /**
     * 可接受的事件
     */
    protected final E event;

    public StatusEventPair(S status, E event) {
        this.status = status;
        this.event = event;
    }


    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj instanceof StatusEventPair) {
            StatusEventPair<S, E> other = (StatusEventPair<S, E>) obj;
            return this.status.equals(other.status) && this.event.equals(other.event);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return Arrays.hashCode(new Object[]{status, event});
    }

    @Override
    public S getKey() {
        return status;
    }

    @Override
    public E getValue() {
        return event;
    }
}
