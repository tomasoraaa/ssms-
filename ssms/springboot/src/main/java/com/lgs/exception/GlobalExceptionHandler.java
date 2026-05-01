package com.lgs.exception;

import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.lgs.common.Result;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.IOException;
import java.io.PrintWriter;


@ControllerAdvice(basePackages = "com.lgs.controller")
public class GlobalExceptionHandler {

    private static final Log log = LogFactory.get();


    //统一异常处理@ExceptionHandler,主要用于Exception
    @ExceptionHandler(Exception.class)
    @ResponseBody//返回json串
    public Result error(HttpServletRequest request, HttpServletResponse response, Exception e) {
        log.error("异常信息：", e);
        
        // 检查是否是文件下载请求（通过请求路径判断）
        String requestUri = request.getRequestURI();
        if (requestUri.contains("/scoreExport/")) {
            // 对于文件下载请求，直接输出错误信息，不返回JSON
            try {
                response.reset();
                response.setContentType("text/plain;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write("文件导出失败: " + e.getMessage());
                writer.flush();
                writer.close();
            } catch (IOException ioEx) {
                log.error("写入错误响应失败", ioEx);
            }
            return null; // 返回null，避免重复响应
        }
        
        // 对于RuntimeException，返回具体的错误信息
        if (e instanceof RuntimeException) {
            return Result.error(e.getMessage());
        }
        return Result.error();
    }

    @ExceptionHandler(CustomException.class)
    @ResponseBody//返回json串
    public Result customError(HttpServletRequest request, HttpServletResponse response, CustomException e) {
        // 检查是否是文件下载请求
        String requestUri = request.getRequestURI();
        if (requestUri.contains("/scoreExport/")) {
            try {
                response.reset();
                response.setContentType("text/plain;charset=UTF-8");
                PrintWriter writer = response.getWriter();
                writer.write("文件导出失败: " + e.getMsg());
                writer.flush();
                writer.close();
            } catch (IOException ioEx) {
                log.error("写入错误响应失败", ioEx);
            }
            return null;
        }
        return Result.error(e.getMsg());
    }

}
