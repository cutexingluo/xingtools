package top.cutexingluo.tools.utils.orm.mybatisplus;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.ibatis.type.BaseTypeHandler;
import org.apache.ibatis.type.JdbcType;
import org.postgresql.util.PGobject;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *JsonNode 2 Jsonb 类型处理器 (PG 版)
 *
 * @author XingTian
 * @version 1.0.0
 * @date 2025/12/2 16:53
 * @since  1.2.1
 */
public class JsonNode2JsonbTypeHandler   extends BaseTypeHandler<JsonNode> {

    protected final ObjectMapper OM;

    public JsonNode2JsonbTypeHandler() {
        OM = new ObjectMapper();
    }

    public JsonNode2JsonbTypeHandler(ObjectMapper OM) {
        this.OM = OM;
    }

    @Override
    public void setNonNullParameter(PreparedStatement ps, int i, JsonNode node, JdbcType jdbcType) throws SQLException {
        String json = node.toString();
        String dbProduct = ps.getConnection().getMetaData().getDatabaseProductName();
        if ("PostgreSQL".equalsIgnoreCase(dbProduct)) {
            PGobject pg = new PGobject();
            pg.setType("jsonb");   // 若列是 json 改成 "json"
            pg.setValue(json);
            ps.setObject(i, pg);
        } else {
            // MySQL、H2、SQL Server、SQLite 等直接 setString
            ps.setString(i, json);
        }
    }

    @Override
    public JsonNode getNullableResult(ResultSet rs, String columnName) throws SQLException {
        return parse(rs.getString(columnName));
    }

    @Override
    public JsonNode getNullableResult(ResultSet rs, int columnIndex) throws SQLException {
        return parse(rs.getString(columnIndex));
    }

    @Override
    public JsonNode getNullableResult(CallableStatement cs, int columnIndex) throws SQLException {
        return parse(cs.getString(columnIndex));
    }

    protected JsonNode parse(String str) throws SQLException {
        if (str == null || str.isEmpty()) return null;
        try {
            return OM.readTree(str);
        } catch (IOException e) {
            throw new SQLException("Error parsing JSON string", e);
        }
    }


}
