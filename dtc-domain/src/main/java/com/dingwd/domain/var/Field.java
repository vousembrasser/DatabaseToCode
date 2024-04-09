package com.dingwd.domain.var;

import com.dingwd.infrastructure.utils.stringutils.MyStringUtils;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Schema(name = "字段")
@NoArgsConstructor
public class Field {

    @Schema(title = "字段名称")
    private String field;
    @Schema(title = "字段类型")
    private String type;
    @Schema(title = "索引类型 只用来判断是否是主键")
    private String key;
    private String extra;
    private String comment;

    private String fieldNameCamel;
    private String fieldClass;


    public void setField(String field) {
        this.field = field;
        this.fieldNameCamel = MyStringUtils.underlineToHump(field);
    }
}
