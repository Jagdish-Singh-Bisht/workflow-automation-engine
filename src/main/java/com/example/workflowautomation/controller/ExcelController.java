package com.example.workflowautomation.controller;


import com.example.workflowautomation.service.ExcelService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;




@RestController
@RequestMapping("/excel")
public class ExcelController {

    private final ExcelService excelService;

    public ExcelController(ExcelService excelService) {
        this.excelService = excelService;
    }

    @PostMapping("/load")
    public String loadExcel() {
        excelService.loadExcel("C:/Users/bisht/OneDrive/Desktop/shipment_data.xlsx");

        return "Excel Loaded Successfully !";
    }
}
