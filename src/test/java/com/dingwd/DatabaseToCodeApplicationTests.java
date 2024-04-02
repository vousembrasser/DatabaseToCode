package com.dingwd;

import com.dingwd.domain.FileNamePrefixAndSuffix;
import com.dingwd.domain.GeneratorInfo;
import com.dingwd.domain.ProjectInfo;
import com.dingwd.enums.PackageTypeEnum;
import com.dingwd.service.GeneratorService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@SpringBootTest
class DatabaseToCodeApplicationTests {

    @Autowired
    GeneratorService generatorService;

    @Test
    void contextLoads() {

        generatorService.build(GeneratorInfo.INSTANCE
                .setTableName("表名")
                .setRemovePrefix("前缀")
                .setNumberToString(true)
                .setAuthor("作者")
                .setProjectInfo(new ProjectInfo("工程名1")
                        .makePackage(
                                PackageTypeEnum.DO, new FileNamePrefixAndSuffix().suffix(""),
                                "复合的包 点路径",
                                "功能名称",
                                "model")
                        .setSub(
                                PackageTypeEnum.DTO, new FileNamePrefixAndSuffix().suffix("Dto"),
                                "dto")
                        .makePackage(
                                PackageTypeEnum.INTERFACE,
                                "复合的包 点路径",
                                "功能名称",
                                "iservice")
                ).saveWith("C:\\Users\\xxxon\\xxx\\xxx\\ats-api\\")

                .setProjectInfo(new ProjectInfo("工程名2")
                        .makePackage(
                                PackageTypeEnum.REPOSITORY,
                                "com.xxx.xxx.component",
                                "功能名称",
                                "repository")
                        .setSub(
                                PackageTypeEnum.MAPPER, new FileNamePrefixAndSuffix().suffix("Mapper").fileSuffix("xml"),
                                "mapper")
                        .makePackage(PackageTypeEnum.SERVICE_IMPL,
                                "com.xxx.xxx.component",
                                "功能名称",
                                "service")
                        .makePackage(PackageTypeEnum.CONTROLLER,
                                "com.xxx.xxx.component",
                                "功能名称",
                                "controller"
                        ))
                .saveWith("C:\\Users\\xxxon\\xxx\\xxx\\ats-component\\")


        ).execute(Map.of("额外的变量", "变量值"));
    }
}
