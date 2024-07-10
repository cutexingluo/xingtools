package top.cutexingluo.tools.utils.model.fsm;

import lombok.Data;
import top.cutexingluo.tools.utils.model.fsm.base.BaseEvent;
import top.cutexingluo.tools.utils.model.fsm.base.BaseStatus;

import java.util.HashMap;
import java.util.Map;

/**
 * （有限）状态机
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/6/24 16:32
 * @since 1.0.5
 */
@Data
public class StateMachine<S extends BaseStatus, E extends BaseEvent> {
    protected Map<StatusEventPair<S, E>, S> statusEventMap;

    public StateMachine() {
        statusEventMap = new HashMap<>();
    }

    public StateMachine(Map<StatusEventPair<S, E>, S> statusEventMap) {
        this.statusEventMap = statusEventMap;
    }


    /**
     * 接受状态（保存状态转换链）
     * <p>只接受指定的当前状态下，指定的事件触发，可以到达的指定目标状态</p>
     *
     * @param sourceStatus 源状态
     * @param event        事件
     * @param targetStatus 目标状态
     */
    public void accept(S sourceStatus, E event, S targetStatus) {
        statusEventMap.put(new StatusEventPair<>(sourceStatus, event), targetStatus);
    }


    /**
     * 获取目标状态
     *
     * @param sourceStatus 源状态
     * @param event        事件
     * @return {@link S} 目标状态
     */
    public S getTargetStatus(S sourceStatus, E event) {
        return statusEventMap.get(new StatusEventPair<>(sourceStatus, event));
    }
}
