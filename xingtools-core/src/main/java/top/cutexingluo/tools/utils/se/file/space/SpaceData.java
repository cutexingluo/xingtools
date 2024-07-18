package top.cutexingluo.tools.utils.se.file.space;


import lombok.Data;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 空间转化数据集合
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2024/3/6 13:48
 */
@Data
public class SpaceData {

    Map<SpaceEnums, Long> map;

    public SpaceData(Map<SpaceEnums, Long> map) {
        this.map = map;
    }

    public SpaceData() {
        this.map = new HashMap<>();
    }

    /**
     * @param size       当前大小, 单位spaceEnums
     * @param spaceEnums 当前单位
     */
    @NotNull
    @Contract("_, _ -> new")
    public static SpaceData of(long size, @NotNull SpaceEnums spaceEnums) {
        return new SpaceData(size, spaceEnums);
    }

    /**
     * @param size 当前大小, 单位 B
     */
    @NotNull
    @Contract("_ -> new")
    public static SpaceData of(long size) {
        return new SpaceData(size);
    }


    /**
     * @param size       当前大小, 单位spaceEnums
     * @param spaceEnums 当前单位
     */
    public SpaceData(long size, @NotNull SpaceEnums spaceEnums) {
        this.map = getMap(size, spaceEnums);
    }

    /**
     * @param size 当前大小, 单位 B
     */
    public SpaceData(long size) {
        this.map = getMap(size, SpaceEnums.B);
    }

    /**
     * 获取转化后的 map
     *
     * @param size       当前大小, 单位spaceEnums
     * @param spaceEnums 当前单位
     */
    @NotNull
    public static HashMap<SpaceEnums, Long> getMap(long size, @NotNull SpaceEnums spaceEnums) {
        HashMap<SpaceEnums, Long> map = new HashMap<>(7);
        SpaceEnums[] enums = SpaceEnums.values();
        long num = size;
        long res;
        for (int i = enums.length - 1; i >= 1; i--) {
            if (enums[i].bytes <= spaceEnums.bytes) {
                break;
            }
            SpaceEnums target = enums[i].divideByUnitCheck(spaceEnums);
            res = target.modBy(num);
            long quotient = target.divideValue(num);
            map.put(enums[i], quotient);
            num = res;
        }
        if (num >= 0) {
            map.put(enums[0], num);
        }
        return map;
    }

    /**
     * 转为 list 集合
     */
    public List<SpaceEntry> toList() {
        Stream<SpaceEntry> stream = this.map.entrySet().stream().map(entry -> new SpaceEntry(entry.getKey(), entry.getValue()));
        return stream.collect(Collectors.toList());
    }

    /**
     * 转为 list 集合, 0 不转换
     */
    public ArrayList<SpaceEntry> toCheckList() {
        ArrayList<SpaceEntry> list = new ArrayList<>(this.map.size());
        map.forEach((e, v) -> {
            if (v != 0) {
                list.add(new SpaceEntry(e, v));
            }
        });
        return list;
    }

    /**
     * 转为 list 集合, 0 不转换, 并且升序排序
     */
    public ArrayList<SpaceEntry> toCheckListAsc() {
        ArrayList<SpaceEntry> arrayList = toCheckList();
        arrayList.sort(SpaceEntry::compareTo);
        return arrayList;
    }

    /**
     * 转为 list 集合, 0 不转换, 并且降序排序
     */
    public ArrayList<SpaceEntry> toCheckListDesc() {
        ArrayList<SpaceEntry> arrayList = toCheckList();
        arrayList.sort(Comparator.reverseOrder());
        return arrayList;
    }
}
