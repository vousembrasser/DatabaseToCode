package com.dingwd.service;

import com.dingwd.mappers.TableMapper;
import com.dingwd.service.file.FileGetSavePathService;
import com.dingwd.utils.MyStringUtils;
import com.dingwd.var.Field;
import com.dingwd.var.TableInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Slf4j
@Service
public class TableService {

    public TableService(TableMapper tableMapper, FileGetSavePathService fileGetSavePathService) {
        this.tableMapper = tableMapper;
        this.fileGetSavePathService = fileGetSavePathService;
    }

    private final TableMapper tableMapper;
    private final FileGetSavePathService fileGetSavePathService;

    public List<Field> getColumns(String tableName) {
        return tableMapper.getColumns(tableName);
    }


    public List<TableInfo> getTablesInfo(List<String> tablesName, String removePrefix, String fileSuffix, boolean priNumberToString) {
        List<TableInfo> tables = Collections.synchronizedList(new ArrayList<>());
        List<CompletableFuture<Void>> futureList = new ArrayList<>();
        try (ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor()) {
            for (String tableName : tablesName) {
                String tableTemp = StringUtils.hasText(removePrefix) ?
                        tableName.replaceFirst(removePrefix, "") : tableName;
                futureList.add(CompletableFuture.runAsync(() -> {
                    try {
                        TableInfo table = new TableInfo();
                        List<Field> columns = tableMapper.getColumns(tableName);
                        columns.stream().forEach((each) -> {
                            if ("PRI".equals(each.getKey())) {
                                table.setPrimaryKey(each.getField());
                                table.setPrimaryKeyCamel(MyStringUtils.underlineToHump(each.getField()));
                            }
                            each.setFieldClass(getFieldClass(fileSuffix,
                                    priNumberToString, each.getType(),
                                    each.getKey(), each.getExtra()));
                        });

                        table.setTableName(tableName);
                        table.setTableNameCamel(MyStringUtils.underlineToHump(tableTemp));
                        table.setTableNameFirstUpperCase(MyStringUtils.upperFirstWord(table.getTableNameCamel()));
                        table.setTableComment(tableMapper.getTableComment(tableName));
                        table.setFields(columns);
                        tables.add(table);
                    } catch (Exception e) {
                        log.error("获取表信息失败", e);
                    }
                }, executor));
            }
        }
        CompletableFuture<Void> completableFuture = CompletableFuture.allOf(futureList.toArray(CompletableFuture[]::new));
        try {
            completableFuture.get();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return tables;
    }

    public String getFieldClass(String fileSuffix, boolean priNumberToString, String type, String key, String extra) {
        String tempField = type.split("\\(")[0];
        String fieldPrecision;
        if (type.contains("(")) {
            fieldPrecision = type.substring(type.indexOf("(") + 1, type.indexOf(")"));
        } else {
            fieldPrecision = "0";
        }
        String fieldClass;
        if ("PRI".equals(key) && "auto_increment".equals(extra)) {
            fieldClass = fileGetSavePathService.columnTypeToClassType(
                    fileSuffix,
                    priNumberToString ? "VARCHAR" : "BIGINT",
                    fieldPrecision);

        } else {
            fieldClass = fileGetSavePathService.columnTypeToClassType(fileSuffix, tempField, fieldPrecision);
        }
        return fieldClass;
    }
}
