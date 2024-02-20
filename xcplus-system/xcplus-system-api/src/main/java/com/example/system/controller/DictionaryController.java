package com.example.system.controller;

import com.example.xcplussystemmodel.model.po.Dictionary;
import com.example.system.xuecheng.service.DictionaryService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * <p>
 * 数据字典 前端控制器
 * </p>
 *
 * @author itcast
 */


@RestController
public class DictionaryController  {


    private final DictionaryService dictionaryService;

    public DictionaryController(DictionaryService DictionaryService) {
        this.dictionaryService = DictionaryService;
    }

    @GetMapping("/dictionary/all")
    public List<Dictionary> queryAll() {
        return dictionaryService.queryAll();
    }

    @GetMapping("/dictionary/code/{code}")
    public Dictionary getByCode(@PathVariable String code) {
        return dictionaryService.getByCode(code);
    }
}
