package top.cutexingluo.tools.utils.generator.java;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.OutputFile;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;
import top.cutexingluo.tools.common.database.DBProp;
import top.cutexingluo.tools.utils.se.file.XTPath;

import java.util.Collections;

/**
 * 代码生成器，<br>
 * 不推荐使用，建议自己使用 EasyCode 自行配置
 *
 * mp CodeGenerator <br>
 * <p> 需要导入 com.baomidou:mybatis-plus-generator 包</p>
 *
 * @version v1.0.0
 * @since 2022-10-13
 */

@Data
@NoArgsConstructor
@AllArgsConstructor
//@Builder
public class OldVmCodeGenerator {

    //    springboot
    private String author = "";
//    // 数据库名字
//    private static String dataBaseName = "xing";

    // 数据库信息
    @NotNull
    private DBProp dbProp;
    // 表名
    @NotNull
    private String tableName;

    // 项目绝对路径地址  spring
    @NotNull
    private String basePath = "";
    // 生成的父包名
    @NotNull
    private String packageName = "com.example";

//    // vue
//    //类名
//    private static final String ClassName = "Course";
//
//    //模板名
//    private static final String templateSrcFile = "menu.vue.vm";
//    //***********************
//    //目标文件名
//    private static final String vueDestFile = ClassName + ".vue";
    // 实体类路径
//    private static final String entityPath = "/src/main/java/com/xing/springboot/entity/"+ClassName+".java";
    //包名（含类名）
//    private static final String packageName = "com.xing.springboot.entity." + ClassName;

    /**
     * 根据信息生成对应的代码，初始化
     *
     * @param dbProp      数据库信息（url 账号密码）
     * @param tableName   要生成的表名
     * @param packageName 包名（生成多个类的父包名）
     */
    public OldVmCodeGenerator buildBy(DBProp dbProp, String tableName, String packageName) {
        return buildBy(dbProp, tableName, XTPath.getProjectPath(), packageName, "");
    }

    /**
     * 根据信息生成对应的代码，初始化
     *
     * @param dbProp      数据库信息（url 账号密码）
     * @param tableName   要生成的表名
     * @param basePath    项目路径（项目的位置，不含包）
     * @param packageName 包名（生成多个类的父包名）
     */
    public OldVmCodeGenerator buildBy(DBProp dbProp, String tableName, String basePath, String packageName) {
        return buildBy(dbProp, tableName, basePath, packageName, "");
    }

    /**
     * 根据信息生成对应的代码，初始化
     *
     * @param dbProp      数据库信息（url 账号密码）
     * @param tableName   要生成的表名
     * @param basePath    项目路径（项目的位置，不含包）
     * @param packageName 包名（生成多个类的父包名）
     * @param author      作者名
     */
    public OldVmCodeGenerator buildBy(DBProp dbProp, String tableName, String basePath, String packageName, String author) {
        this.dbProp = dbProp;
        this.tableName = tableName;
        this.basePath = basePath;
        this.packageName = packageName;
        this.author = author;
        return this;
    }

    public void create() {
        generate();
    }

    private void generate() {
//        FastAutoGenerator.create("jdbc:mysql://localhost:3306/" + dataBaseName + "?serverTimezone=GMT%2b8", "root", "root")
        FastAutoGenerator.create(dbProp.getUrl(), dbProp.getUsername(), dbProp.getPassword())
                .globalConfig(builder -> {
                    builder.author(author) // 设置作者
                            .enableSwagger() // 开启 swagger 模式
                            .fileOverride() // 覆盖已生成文件
                            .outputDir(basePath + "\\src\\main\\java");
//                            .outputDir("../"); // 指定输出目录
                })
                .packageConfig(builder -> {
                    builder.parent(packageName) // 设置父包名
//                            .moduleName("") // 设置父包模块名
                            .moduleName(null) // 设置父包模块名
                            .pathInfo(Collections.singletonMap(OutputFile.xml,
                                    basePath + "\\src\\main\\resources\\mapper")); // 设置mapperXml生成路径
                })
                .strategyConfig(builder -> {
                    builder.entityBuilder().enableLombok();
//                    builder.mapperBuilder().enableMapperAnnotation().build();
                    builder.controllerBuilder().enableHyphenStyle()  // 开启驼峰转连字符
                            .enableRestStyle();  // 开启生成@RestController 控制器
                    builder.addInclude(tableName) // 设置需要生成的表名
                            .addTablePrefix("t_", "sys_"); // 设置过滤表前缀
                })
//                .templateEngine(new FreemarkerTemplateEngine()) // 使用Freemarker引擎模板，默认的是Velocity引擎模板
                .execute();
    }

}
