package com.lgs.controller;

import com.lgs.common.Result;
import com.lgs.entity.ModifyRequest;
import com.lgs.service.ModifyRequestService;
import jakarta.annotation.Resource;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/modifyRequest")
public class ModifyRequestController {

    @Resource
    private ModifyRequestService modifyRequestService;

    @PostMapping("/add")
    public Result add(@RequestBody ModifyRequest modifyRequest) {
        modifyRequestService.add(modifyRequest);
        return Result.success();
    }

    @GetMapping("/selectPage")
    public Result selectPage(ModifyRequest modifyRequest, Integer pageNum, Integer pageSize) {
        return Result.success(modifyRequestService.selectPage(modifyRequest, pageNum, pageSize));
    }

    @PutMapping("/approve/{id}")
    public Result approve(@PathVariable Integer id) {
        modifyRequestService.approve(id);
        return Result.success();
    }

    @PutMapping("/reject/{id}")
    public Result reject(@PathVariable Integer id) {
        modifyRequestService.reject(id);
        return Result.success();
    }

    @DeleteMapping("/delete/{id}")
    public Result delete(@PathVariable Integer id) {
        modifyRequestService.deleteById(id);
        return Result.success();
    }

    @GetMapping("/selectById/{id}")
    public Result selectById(@PathVariable Integer id) {
        return Result.success(modifyRequestService.selectById(id));
    }
}