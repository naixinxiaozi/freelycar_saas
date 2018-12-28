package com.freelycar.saas.project.controller;

import com.freelycar.saas.project.service.AutoPartsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author tangwei - Toby
 * @date 2018-12-28
 * @email toby911115@gmail.com
 */
@RestController
@RequestMapping("/autoParts")
public class AutoPartsController {
    @Autowired
    private AutoPartsService autoPartsService;

}
