package top.cutexingluo.tools.basepackage.chain.task;

/**
 * Object Task Node
 * <p>对象任务节点</p>
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/6/6 16:21
 * @since 1.1.7
 */
public class ObjTaskNode extends BasicTaskNode<Object> {

    public ObjTaskNode(Object value, boolean complete, boolean change) {
        super(value, complete, change);
    }

    public ObjTaskNode() {
        super();
    }

    public ObjTaskNode(Object value) {
        super(value);
    }
}
