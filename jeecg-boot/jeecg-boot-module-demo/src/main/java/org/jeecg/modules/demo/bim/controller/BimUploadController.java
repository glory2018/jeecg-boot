package org.jeecg.modules.demo.bim.controller;

import com.bimface.api.bean.response.FileTranslateBean;
import com.bimface.exception.BimfaceException;
import com.bimface.file.bean.FileBean;
import com.bimface.sdk.BimfaceClient;
import com.bimface.sdk.bean.request.FileUploadRequest;
import com.bimface.sdk.config.Endpoint;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.jeecg.common.api.vo.Result;
import org.jeecg.modules.demo.bim.entity.BimDemo;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;


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
    // 通过(bimface.com)创建应用，并拿到自己的appKey和appSecret
    public final static String APP_KEY = "Txyp85RELtjWg69tPniNInowYKjO0MtV";
    public final static String APP_SECRET = "TvDVcI0V12uOMjJEPoKVH1rNP9WPq34l";
    // 初始化BimfaceClient
    Endpoint endPoint = new Endpoint("http://10.32.5.35:8011/", "http://10.32.5.35:8011/file/");
    BimfaceClient bimfaceClient = new BimfaceClient(APP_KEY, APP_SECRET, endPoint);

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
            FileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);
            List<FileItem> fileItems = upload.parseRequest(request);
            FileUploadRequest fileUploadRequest = new FileUploadRequest();
            for (FileItem fileItem : fileItems) {
                fileUploadRequest.setContentLength(fileItem.getSize());
                fileUploadRequest.setName(fileItem.getName());
                fileUploadRequest.setInputStream(fileItem.getInputStream());
            }
            fileBean = bimfaceClient.upload(fileUploadRequest);
        } catch (FileUploadException e) {
        } catch (Exception e) {
        }

        // 获取fileId
        Long fileId = fileBean.getFileId();

        // 发起文件转换
        FileTranslateBean translateBean = null;
        try {
            translateBean = bimfaceClient.translate(fileId);
        } catch (BimfaceException e) {
        }
        return Result.OK(translateBean);
    }
}
