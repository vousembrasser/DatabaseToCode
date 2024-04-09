package com.dingwd.infrastructure.table.service;

import com.dingwd.infrastructure.table.entity.FieldEntity;
import com.dingwd.infrastructure.table.repository.TableRepository;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class TableService {

    private final TableRepository tableRepository;

    public TableService(TableRepository tableRepository) {
        this.tableRepository = tableRepository;
    }

    public List<FieldEntity> getColumns(String tableName) {
        return tableRepository.getColumns(tableName);
    }

    public String getTableComment(String tableName) {
        return tableRepository.getTableComment(tableName);
    }

}
