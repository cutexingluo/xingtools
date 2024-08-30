package top.cutexingluo.tools.utils.se.file.space;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import top.cutexingluo.tools.common.base.IResultData;

import java.util.Comparator;

/**
 * 空间枚举
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/6 13:48
 */
public enum SpaceEnums implements IResultData<Long> {
    /**
     * Byte 字节
     */
    B("B", SpaceEnums.B_SIZE),
    KB("KB", SpaceEnums.KB_SIZE),
    MB("MB", SpaceEnums.MB_SIZE),
    GB("GB", SpaceEnums.GB_SIZE),
    TB("TB", SpaceEnums.TB_SIZE),
    PB("PB", SpaceEnums.PB_SIZE),
    EB("EB", SpaceEnums.EB_SIZE),
//    ZB("ZB", 1024L* 1024 * 1024 * 1024 * 1024 * 1024 * 1024),
//    YB("YB", 1024L* 1024 * 1024 * 1024 * 1024 * 1024 * 1024 * 1024),
    ;
    public static final long B_SIZE = 1L;
    public static final long KB_SIZE = 1024L;
    public static final long MB_SIZE = KB_SIZE * 1024;
    public static final long GB_SIZE = MB_SIZE * 1024;
    public static final long TB_SIZE = GB_SIZE * 1024;
    public static final long PB_SIZE = TB_SIZE * 1024;
    public static final long EB_SIZE = PB_SIZE * 1024;


    final String unit;
    final long bytes;

    SpaceEnums(String unit, long bytes) {
        this.unit = unit;
        this.bytes = bytes;
    }

    @Nullable
    public static SpaceEnums getByUnit(String unit) {
        for (SpaceEnums spaceEnums : SpaceEnums.values()) {
            if (spaceEnums.unit.equals(unit)) {
                return spaceEnums;
            }
        }
        return null;
    }


    /**
     * @param size 大小, 单位B
     * @return 转化为枚举
     */
    public static SpaceEnums parseBySize(long size) {
        SpaceEnums[] values = SpaceEnums.values();
        for (int i = values.length - 1; i >= 0; i--) {
            if (size >= values[i].bytes) {
                return values[i];
            }
        }
        return B;
    }

    /**
     * @param value 大小, 单位 spaceEnums
     * @return 转化为的数量级
     */
    public SpaceEnums parse(long value) {
        SpaceEnums[] values = SpaceEnums.values();
        for (int i = values.length - 1; i >= 0; i--) {
            if (value >= (double) values[i].bytes / this.bytes) {
                return values[i];
            }
        }
        return B;
    }

    /**
     * @param value 大小, 单位B
     * @return 转化为大小
     */
    public long divideValue(long value) {
        return value / this.bytes;
    }

    /**
     * @param value 大小, 单位B
     * @return 转化为大小
     */
    public long modBy(long value) {
        return value % this.bytes;
    }

    /**
     * @param value      大小, 单位 spaceEnums
     * @param spaceEnums 当前数量级枚举
     * @return 转化为的数量级
     */
    @Contract(pure = true)
    public static SpaceEnums parse(long value, @NotNull SpaceEnums spaceEnums) {
        return spaceEnums.parse(value);
    }


    /**
     * 除以单位
     * <p>当前比目标小 返回null</p>
     */
    public SpaceEnums divideByUnit(@NotNull SpaceEnums spaceEnums) {
        long bytes = this.bytes / spaceEnums.bytes;
        if (bytes == 0) {
            return null;
        }
        return SpaceEnums.parseBySize(bytes);
    }

    /**
     * 除以单位
     * <p>当前比目标小 返回B</p>
     */
    public SpaceEnums divideByUnitCheck(@NotNull SpaceEnums spaceEnums) {
        long bytes = this.bytes / spaceEnums.bytes;
        if (bytes == 0) {
            return B;
        }
        return SpaceEnums.parseBySize(bytes);
    }

    @Override
    public String getMsg() {
        return unit;
    }

    @Override
    public Long getCode() {
        return bytes;
    }


    public static class SpaceComparator implements Comparator<SpaceEnums> {

        @Override
        public int compare(SpaceEnums o1, SpaceEnums o2) {
            long res = o1.bytes - o2.bytes;
            return res < 0 ? -1 : (res == 0 ? 0 : 1);
        }
    }
}
