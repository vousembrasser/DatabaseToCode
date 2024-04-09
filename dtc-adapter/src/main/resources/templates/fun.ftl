&{utilFun.lowerFirstWord(Test)}
&{utilFun.lowerFirstWordAndHumpTo(testAbc,/)}

//嵌套
&{utilFun.lowerFirstWord(utilFun.lowerFirstWordAndHumpTo(TestAbc,/))}

//含变量
&{utilFun.lowerFirstWord(${classParam.dtoName})}