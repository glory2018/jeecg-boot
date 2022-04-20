package org.jeecg.modules.demo.bim.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.bimface.api.bean.response.FileTranslateBean;
import com.bimface.exception.BimfaceException;
import com.bimface.file.bean.FileBean;
import com.bimface.sdk.BimfaceClient;
import com.bimface.sdk.bean.request.FileUploadRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.http.fileupload.FileUploadException;
import org.jeecg.common.api.vo.Result;
import org.jeecg.common.aspect.annotation.AutoLog;
import org.jeecg.common.system.base.controller.JeecgController;
import org.jeecg.common.system.query.QueryGenerator;
import org.jeecg.modules.demo.bim.entity.BimDemo;
import org.jeecg.modules.demo.bim.service.IBimDemoService;
import org.jeecgframework.poi.excel.entity.ImportParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.Map;

/**
 * @Description: 建筑信息模型
 * @Author: jeecg-boot
 * @Date: 2022-04-20
 * @Version: V1.0
 */
@Api(tags = "建筑信息模型")
@RestController
@RequestMapping("/bim/bimDemo")
@Slf4j
public class BimDemoController extends JeecgController<BimDemo, IBimDemoService> {

    @Autowired
    private IBimDemoService bimDemoService;

    /**
     * 分页列表查询
     *
     * @param bimDemo
     * @param pageNo
     * @param pageSize
     * @param req
     * @return
     */
    //@AutoLog(value = "建筑信息模型-分页列表查询")
    @ApiOperation(value = "建筑信息模型-分页列表查询", notes = "建筑信息模型-分页列表查询")
    @GetMapping(value = "/list")
    public Result<IPage<BimDemo>> queryPageList(BimDemo bimDemo,
                                                @RequestParam(name = "pageNo", defaultValue = "1") Integer pageNo,
                                                @RequestParam(name = "pageSize", defaultValue = "10") Integer pageSize,
                                                HttpServletRequest req) {
        QueryWrapper<BimDemo> queryWrapper = QueryGenerator.initQueryWrapper(bimDemo, req.getParameterMap());
        Page<BimDemo> page = new Page<BimDemo>(pageNo, pageSize);
        IPage<BimDemo> pageList = bimDemoService.page(page, queryWrapper);
        return Result.OK(pageList);
    }

    /**
     * 添加
     *
     * @param bimDemo
     * @return
     */
    @AutoLog(value = "建筑信息模型-添加")
    @ApiOperation(value = "建筑信息模型-添加", notes = "建筑信息模型-添加")
    @PostMapping(value = "/add")
    public Result<String> add(@RequestBody BimDemo bimDemo) {
        bimDemoService.save(bimDemo);
        return Result.OK("添加成功！");
    }

    /**
     * 编辑
     *
     * @param bimDemo
     * @return
     */
    @AutoLog(value = "建筑信息模型-编辑")
    @ApiOperation(value = "建筑信息模型-编辑", notes = "建筑信息模型-编辑")
    @RequestMapping(value = "/edit", method = {RequestMethod.PUT, RequestMethod.POST})
    public Result<String> edit(@RequestBody BimDemo bimDemo) {
        bimDemoService.updateById(bimDemo);
        return Result.OK("编辑成功!");
    }

    /**
     * 通过id删除
     *
     * @param id
     * @return
     */
    @AutoLog(value = "建筑信息模型-通过id删除")
    @ApiOperation(value = "建筑信息模型-通过id删除", notes = "建筑信息模型-通过id删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@RequestParam(name = "id", required = true) String id) {
        bimDemoService.removeById(id);
        return Result.OK("删除成功!");
    }

    /**
     * 批量删除
     *
     * @param ids
     * @return
     */
    @AutoLog(value = "建筑信息模型-批量删除")
    @ApiOperation(value = "建筑信息模型-批量删除", notes = "建筑信息模型-批量删除")
    @DeleteMapping(value = "/deleteBatch")
    public Result<String> deleteBatch(@RequestParam(name = "ids", required = true) String ids) {
        this.bimDemoService.removeByIds(Arrays.asList(ids.split(",")));
        return Result.OK("批量删除成功!");
    }

    /**
     * 通过id查询
     *
     * @param id
     * @return
     */
    //@AutoLog(value = "建筑信息模型-通过id查询")
    @ApiOperation(value = "建筑信息模型-通过id查询", notes = "建筑信息模型-通过id查询")
    @GetMapping(value = "/queryById")
    public Result<BimDemo> queryById(@RequestParam(name = "id", required = true) String id) {
        BimDemo bimDemo = bimDemoService.getById(id);
        if (bimDemo == null) {
            return Result.error("未找到对应数据");
        }
        return Result.OK(bimDemo);
    }

    /**
     * 导出excel
     *
     * @param request
     * @param bimDemo
     */
    @RequestMapping(value = "/exportXls")
    public ModelAndView exportXls(HttpServletRequest request, BimDemo bimDemo) {
        return super.exportXls(request, bimDemo, BimDemo.class, "建筑信息模型");
    }

    /**
     * 通过excel导入数据
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/importExcel", method = RequestMethod.POST)
    public Result<?> importExcel(HttpServletRequest request, HttpServletResponse response) {
        return super.importExcel(request, response, BimDemo.class);
    }

}
