package org.jeecg.modules.demo.bim.controller;

import com.bimface.api.bean.response.FileTranslateBean;
import com.bimface.exception.BimfaceException;
import com.bimface.file.bean.FileBean;
import com.bimface.sdk.BimfaceClient;
import com.bimface.sdk.bean.request.FileUploadRequest;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.demo.bim.entity.BimDemo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;


/**
 * @Description: 建筑信息模型
 * @Author: jeecg-boot
 * @Date: 2022-04-20
 * @Version: V1.0
 */
@Api(tags = "建筑信息模型")
@RestController
@RequestMapping("/bim/model")
@Slf4j
public class BimUploadController {
    @Autowired
    private BimfaceClient bimfaceClient;

    /**
     * 获取view token
     *
     * @param fileId
     * @return
     */
    @ApiOperation(value = "建筑信息模型-获取view token", notes = "建筑信息模型-获取view token")
    @GetMapping(value = "/getViewToken")
    public Result<BimDemo> getViewToken(@RequestParam(name = "fileId", required = true) String fileId) {
        // 获取view token
        String viewToken = null;
        try {
            viewToken = bimfaceClient.getViewTokenByFileId(Long.valueOf(fileId));
        } catch (NumberFormatException e) {
        } catch (BimfaceException e) {
        }
        return Result.OK(viewToken);
    }

    /**
     * 上传模型
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/uploadUrl", method = RequestMethod.POST)
    public Result<?> uploadUrl(HttpServletRequest request, HttpServletResponse response) {

        //上传文件
        FileBean fileBean = null;
        try {
            MultipartHttpServletRequest multipartRequest = (MultipartHttpServletRequest) request;
            Map<String, MultipartFile> fileMap = multipartRequest.getFileMap();
//            FileItemFactory factory = new DiskFileItemFactory();
//            ServletFileUpload upload = new ServletFileUpload(factory);
//            List<FileItem> fileItems = upload.parseRequest(request);
            FileUploadRequest fileUploadRequest = new FileUploadRequest();
            for (Map.Entry<String, MultipartFile> entity : fileMap.entrySet()) {
                MultipartFile fileItem = entity.getValue();
                fileUploadRequest.setContentLength(fileItem.getSize());
                fileUploadRequest.setName(fileItem.getOriginalFilename());
                fileUploadRequest.setInputStream(fileItem.getInputStream());
            }
            fileBean = bimfaceClient.upload(fileUploadRequest);
        } catch (Exception e) {
        }

        // 获取fileId
        Long fileId = fileBean.getFileId();
        Map result = new HashMap();
        result.put("modelCode", fileBean.getFileId());
        result.put("modelName", fileBean.getName());
        DecimalFormat df = new DecimalFormat("#.00");
        result.put("modelType", fileBean.getSuffix());
        result.put("modelSize", df.format(fileBean.getLength() / 1000 / 1024) + "MB");
        // 发起文件转换
        try {
            FileTranslateBean translateBean = bimfaceClient.translate(fileId);
            if ("success".equals(translateBean.getStatus())) {
                result.put("modelStatus", "转换成功");
            }
        } catch (BimfaceException e) {
        }
        return Result.OK(result);
    }

    /**
     * 删除文件
     *
     * @param fileId
     * @return
     */
    @ApiOperation(value = "建筑信息模型-删除文件", notes = "建筑信息模型-删除文件")
    @GetMapping(value = "/deleteFile")
    public Result<BimDemo> deleteFile(@RequestParam(name = "fileId", required = true) String fileId) {
        try {
            bimfaceClient.deleteFile(Long.valueOf(fileId));
            return Result.OK("success");
        } catch (BimfaceException e) {
            return Result.OK("failure");
        }

    }
}
