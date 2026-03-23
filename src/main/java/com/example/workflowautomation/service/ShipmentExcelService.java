package com.example.workflowautomation.service;

import com.example.workflowautomation.entity.Shipment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
public class ShipmentExcelService {

    public byte[] generateExcel(List<Shipment> shipments) {

        try (Workbook workbook = new XSSFWorkbook()) {

            Sheet sheet = workbook.createSheet("Shipment Report");

            // Header
            Row header = sheet.createRow(0);
            header.createCell(0).setCellValue("Shipment ID");
            header.createCell(1).setCellValue("Client");
            header.createCell(2).setCellValue("Status");
            header.createCell(3).setCellValue("Priority");
            header.createCell(4).setCellValue("Quantity");
            header.createCell(5).setCellValue("Last Updated");

            // Data
            int rowNum = 1;

            for (Shipment s : shipments) {
                Row row = sheet.createRow(rowNum++);

                row.createCell(0).setCellValue(s.getShipmentId());
                row.createCell(1).setCellValue(s.getClientName());
                row.createCell(2).setCellValue(s.getStatus());
                row.createCell(3).setCellValue(s.getPriority());
                row.createCell(4).setCellValue(s.getQuantity());
                row.createCell(5).setCellValue(s.getLastUpdated().toString());
            }

            // Convert to byte[]
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel", e);
        }
    }
}