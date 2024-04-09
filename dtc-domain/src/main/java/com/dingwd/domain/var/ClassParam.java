package com.dingwd.domain.var;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ClassParam {

    //类名
    String className;
    //包路径
    String packagePath;
    //do的路径
    String doPath;
    //do的class类名
    String doName;
    //dto的路径
    String dtoPath;
    //dto的class类名
    String dtoName;
    //mapper的路径
    String mapperPath;
    //mapper的文件名
    String mapperName;
    //repository的路径
    String repositoryPath;
    //repository的class类名
    String repositoryName;
    //接口的路径
    String interfacePath;
    //接口的class类名
    String interfaceName;
    //service的路径
    String serviceImplPath;
    //service的class类名
    String serviceImplName;
    //controller的路径
    String controllerPath;
    //controller的class类名
    String controllerName;

}
