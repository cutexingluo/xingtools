package top.cutexingluo.tools.common.database;

import lombok.Builder;
import lombok.Data;

/**
 * @author XingTian
 * @version 1.0.0
 * @date 2023/2/4 23:02
 */
@Data
@Builder
public class DBProp {
    private String url;
    private String username;
    private String password;
}
