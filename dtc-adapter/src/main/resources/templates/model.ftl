package ${classParam.packagePath};

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

/**
 * <p>Title: ${tableInfo.tableName}</p>
 * <p>Description: ${tableInfo.tableComment}</p>
 *
 * @author : ${author}
 * @date : ${time}
 * @version 1.0
 */

@Setter
@Getter
@Entity
@Table(name = "${tableInfo.tableName}")
@Schema(name = "${tableInfo.tableName}模型")
public class ${classParam.className} implements Serializable {
<#if tableInfo.fields??>
    <#list tableInfo.fields as field>

        <#if (tableInfo.primaryKey)??>
        <#if tableInfo.primaryKeyCamel == field.fieldNameCamel>
        @Id
        <#if (field.extra)??>
        <#if "auto_increment" == field.extra>
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        </#if>
        </#if>
        </#if>
        </#if>
        <#if field.comment == "">
        @Column(name = "${field.field}", columnDefinition= "${field.type}")
        @Schema(title = "${field.fieldNameCamel}")
        </#if>
        <#if field.comment != "">
        @Column(name = "${field.field}", columnDefinition= "${field.type} comment '${field.comment}'")
        @Schema(title = "${field.comment}")
        </#if>
        private ${field.fieldClass} ${field.fieldNameCamel};

    </#list>
</#if>

}