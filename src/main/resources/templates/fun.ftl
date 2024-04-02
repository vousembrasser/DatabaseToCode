&{utilFun.lowerFirstWord(Test)}
&{utilFun.humpTo(testAbc,/)}

//嵌套
&{utilFun.lowerFirstWord(utilFun.humpTo(TestAbc,/))}

//含变量
&{utilFun.lowerFirstWord(${classParam.dtoName})}