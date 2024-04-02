package com.dingwd.mappers;

import com.dingwd.var.Field;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface TableMapper {

    @Select("SHOW FULL COLUMNS FROM ${tableName}")
    List<Field> getColumns(String tableName);

    @Select("SELECT table_comment FROM information_schema.tables WHERE table_schema = (SELECT DATABASE()) AND table_name = #{tableName}")
    String getTableComment(String tableName);
}
