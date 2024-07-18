package top.cutexingluo.tools.utils.se.file.space;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.jetbrains.annotations.NotNull;

import java.util.Map;

/**
 * 空间元
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/6 14:05
 */
@Data
@AllArgsConstructor
public class SpaceEntry implements Map.Entry<SpaceEnums, Long>, Comparable<SpaceEntry> {

    protected SpaceEnums unit;
    protected Long value;

    @Override
    public SpaceEnums getKey() {
        return unit;
    }

    @Override
    public Long getValue() {
        return value;
    }

    @Override
    public Long setValue(Long value) {
        long oldValue = this.value;
        this.value = value;
        return oldValue;
    }

    public SpaceEntry() {
        this.unit = SpaceEnums.B;
        this.value = 0L;
    }


    /**
     * @param size 大小, 单位B
     */
    public SpaceEntry(long size) {
        this.unit = SpaceEnums.parse(size, SpaceEnums.B);
        this.value = this.unit.divideValue(size);
    }


    public String getString() {
        return this.value + "" + this.unit.unit;
    }

    @Override
    public int compareTo(@NotNull SpaceEntry o) {
        int compare = this.unit.compareTo(o.unit);
        return compare == 0 ? this.value.compareTo(o.value) : compare;
    }

}
