package com.dingwd.adapter;

import com.dingwd.application.service.GeneratorService;
import com.dingwd.domain.enums.PackageTypeEnum;
import com.dingwd.domain.model.FileNamePrefixAndSuffix;
import com.dingwd.domain.model.GeneratorInfo;
import com.dingwd.domain.model.ProjectInfo;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Map;

@SpringBootTest
class DatabaseToCodeApplicationTests {

    @Autowired
    GeneratorService generatorService;

    @Test
    void contextLoads() {

        generatorService.build(GeneratorInfo.INSTANCE
                .setTableName("表明")
                .setRemovePrefix("要去除的前缀")
                .setNumberToString(true)
                .setAuthor("作者")
                .setProjectInfo(new ProjectInfo("工程名1")
                        .makePackage(
                                PackageTypeEnum.DO, new FileNamePrefixAndSuffix().suffix(""),
                                "复合的报名",
                                "功能",
                                "模板文件名")
                        .setSub(
                                PackageTypeEnum.DTO, new FileNamePrefixAndSuffix().suffix("Dto"),
                                "zy-dto")
                        .makePackage(
                                PackageTypeEnum.INTERFACE,
                                "复合的报名",
                                "功能",
                                "模板文件名")
                ).saveWith("保存的路径")

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


        ).execute(Map.of("变量key", "变量值"));
    }
}
