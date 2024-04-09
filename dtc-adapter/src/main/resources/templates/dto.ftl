package ${classParam.packagePath};

import ${classParam.doPath};
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * <p>Title: ${tableInfo.tableComment}</p>
 * <p>Description: ${tableInfo.tableComment}</p>
 *
 * @author : ${author}
 * @date : ${time}
 * @version 1.0
 */

@Setter
@Getter
@NoArgsConstructor
@Schema(name = "-Dto模型")
public class ${classParam.className} extends ${classParam.doName} {

}