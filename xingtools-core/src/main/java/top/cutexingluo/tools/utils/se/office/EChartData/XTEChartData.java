package top.cutexingluo.tools.utils.se.office.EChartData;

import cn.hutool.core.collection.CollUtil;
import lombok.Data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 填数据包，横坐标和值
 * @author XingTian
 * @date 2023/2/2 21:37
 * @version 1.0.0
 */
@Data
public class XTEChartData {
    private Map<String, List<Object>> map;

    public XTEChartData(Map<String, List<Object>> map) {
        this.map = map;
    }

    public XTEChartData() {
        this(new HashMap<>());
    }

    public Map<String, List<Object>> getResults() { //获取map
        return map;
    }

    public List<Object> getValuesResults() { //只获取值
        return getValues();
    }

    public void setXAxis(List<Object> values) {
        map.put("x", values);
    }

    public void setXAxis(Object... values) {
        setXAxis(CollUtil.newArrayList(values));
    }

    public List<Object> getXAxis() {
        return map.get("x");
    }

    public void setYAxis(List<Object> values) {
        map.put("y", values);
    }

    public void setYAxis(Object... values) {
        setYAxis(CollUtil.newArrayList(values));
    }

    public List<Object> getYAxis() {
        return map.get("y");
    }

    public void setValues(List<Object> values) {
        map.put("values", values);
    }

    public void setValues(Object... values) {
        setValues(CollUtil.newArrayList(values));
    }

    public List<Object> getValues() {
        return map.get("values");
    }
}
