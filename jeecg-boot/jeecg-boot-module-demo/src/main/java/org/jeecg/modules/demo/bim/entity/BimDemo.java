package org.jeecg.modules.demo.bim.entity;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.math.BigDecimal;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;
import org.jeecgframework.poi.excel.annotation.Excel;
import org.jeecg.common.aspect.annotation.Dict;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @Description: 建筑信息模型
 * @Author: jeecg-boot
 * @Date:   2022-04-20
 * @Version: V1.0
 */
@Data
@TableName("bim_demo")
@Accessors(chain = true)
@EqualsAndHashCode(callSuper = false)
@ApiModel(value="bim_demo对象", description="建筑信息模型")
public class BimDemo implements Serializable {
    private static final long serialVersionUID = 1L;

	/**id*/
	@TableId(type = IdType.ASSIGN_ID)
    @ApiModelProperty(value = "id")
    private java.lang.String id;
	/**创建人*/
    @ApiModelProperty(value = "创建人")
    private java.lang.String createBy;
	/**创建日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "创建日期")
    private java.util.Date createTime;
	/**更新人*/
    @ApiModelProperty(value = "更新人")
    private java.lang.String updateBy;
	/**更新日期*/
	@JsonFormat(timezone = "GMT+8",pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern="yyyy-MM-dd")
    @ApiModelProperty(value = "更新日期")
    private java.util.Date updateTime;
	/**模型编号*/
	@Excel(name = "模型编号", width = 15)
    @ApiModelProperty(value = "模型编号")
    private java.lang.String modelCode;
	/**模型名称*/
	@Excel(name = "模型名称", width = 15)
    @ApiModelProperty(value = "模型名称")
    private java.lang.String modelName;
	/**模型类型*/
	@Excel(name = "模型类型", width = 15)
    @ApiModelProperty(value = "模型类型")
    private java.lang.String modelType;
	/**模型版本*/
	@Excel(name = "模型版本", width = 15)
    @ApiModelProperty(value = "模型版本")
    private java.lang.String modelVersion;
	/**模型大小*/
	@Excel(name = "模型大小", width = 15)
    @ApiModelProperty(value = "模型大小")
    private java.lang.String modelSize;
	/**转换状态*/
	@Excel(name = "转换状态", width = 15)
    @ApiModelProperty(value = "转换状态")
    private java.lang.String modelStatus;
}
