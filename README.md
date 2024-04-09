# Getting Started

使用JAVA开发的Spring Boot应用程序，需要安装JDK21

使用ftl模板

自定义了一些参数 集中在var包路径下

# 参数

1. [ClassParam.java](dtc-domain%2Fsrc%2Fmain%2Fjava%2Fcom%2Fdingwd%2Fdomain%2Fvar%2FClassParam.java)

| 字段              | 含义                 |
|:----------------|:-------------------|
 className       | 类名                 
 packagePath     | 包路径                
 doPath          | do的路径              
 doName          | do的class类名         
 dtoPath         | dto的路径             
 dtoName         | dto的class类名        
 mapperPath      | mapper的路径          
 mapperName      | mapper的文件名         
 repositoryPath  | repository的路径      
 repositoryName  | repository的class类名 
 interfacePath   | 接口的路径              
 interfaceName   | 接口的class类名         
 serviceImplPath | service的路径         
 serviceImplName | service的class类名    
 controllerPath  | controller的路径      
 controllerName  | controller的class类名 

2. [Field.java](dtc-domain%2Fsrc%2Fmain%2Fjava%2Fcom%2Fdingwd%2Fdomain%2Fvar%2FField.java)

| 字段             | 含义                   |
|:---------------|:---------------------|
 field          | 数据库中字段名称             |
 type           | 数据库中字段类型             
 key            | 数据库中 索引类型 只用来判断是否是主键 
 extra          | 数据库中 判断是不是自增         
 comment        | 数据库中 字段注释            
 fieldNameCamel | 字段驼峰                 
 fieldClass     | 数据库中字段类型 在 程序对应的类型   

3. [TableInfo.java](src%2Fmain%2Fjava%2Fcom%2Fdingwd%2Fvar%2FTableInfo.java)[TableInfo.java](dtc-domain%2Fsrc%2Fmain%2Fjava%2Fcom%2Fdingwd%2Fdomain%2Fvar%2FTableInfo.java)

| 字段                      | 含义                      |
|:------------------------|:------------------------|
 primaryKey              | 表的主键 要在模板中判断是否存在主键      
 primaryKeyCamel         | 表的主键 驼峰格式 要在模板中判断是否存在主键 
 tableName               | 表的名称                    
 tableNameCamel          | 表的名称 驼峰格式               
 tableNameFirstUpperCase | 表的名称 驼峰格式 首字母大写         
 tableComment            | 表的注释                    
 fields                  | 字段列表                    

4. [utilFun.java ftl中可以执行的方法](dtc-domain%2Fsrc%2Fmain%2Fjava%2Fcom%2Fdingwd%2Fdomain%2Fvar%2Futil%2FutilFun.java)

| 方法名称                                                     | 含义                          |
|:---------------------------------------------------------|:----------------------------|
 humpTo(String var, String to)                            | var驼峰转成turnTo字段的参数          
 lowerFirstWord(String var)                               | var首字母小写                    
 subString(String var, String beginIndex)                 | var从beginIndex开始截取          
 subString(String var, String beginIndex,String endIndex) | var从beginIndex开始到endIndex结束 
    
