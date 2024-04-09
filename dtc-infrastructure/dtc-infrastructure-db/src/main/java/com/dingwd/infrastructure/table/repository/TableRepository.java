package com.dingwd.infrastructure.table.repository;

import com.dingwd.infrastructure.table.entity.FieldEntity;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
public interface TableRepository {

    @Select("""
            SELECT 
                COLUMN_NAME,
                COLUMN_TYPE,
                COLLATION_NAME,
                IS_NULLABLE,
                COLUMN_KEY,
                COLUMN_DEFAULT,
                EXTRA,
                COLUMN_COMMENT
            FROM information_schema.columns
            WHERE
                table_schema = (SELECT DATABASE()) AND table_name = #{tableName}
            """)
    List<FieldEntity> getColumns(String tableName);

    @Select("""
            SELECT 
                table_comment 
            FROM information_schema.tables
            WHERE 
                table_schema = (SELECT DATABASE()) AND table_name = #{tableName}
            """)
    String getTableComment(String tableName);

}
