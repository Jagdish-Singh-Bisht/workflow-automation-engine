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


            // Sheet 1: Fresh Updates
            Sheet sheet = workbook.createSheet("Fresh Updates");

            createHeader(sheet);

            int rowNum = 1;
            for (Shipment s : shipments) {
                Row row = sheet.createRow(rowNum++);
                fillRow(row, s);
            }

            // Sheet 2: Pending
//            Sheet pendingSheet = workbook.createSheet("Pending Shipments");
//
//            createHeader(pendingSheet);
//
//            int pendingRowNum = 1;
//            for (Shipment s : allShipments) {
//
//                if (!"DELIVERED".equalsIgnoreCase(s.getStatus())) {
//
//                    Row row = pendingSheet.createRow(pendingRowNum++);
//                    fillRow(row, s);
//                }
//            }

            // Auto size columns
            for (int i = 0; i < 6; i++) {
                sheet.autoSizeColumn(i);
            }

            ByteArrayOutputStream out = new ByteArrayOutputStream();
            workbook.write(out);

            return out.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generating Excel", e);
        }
    }

    // Common Header
    private void createHeader(Sheet sheet) {
        Row header = sheet.createRow(0);

        header.createCell(0).setCellValue("Shipment ID");
        header.createCell(1).setCellValue("Client");
        header.createCell(2).setCellValue("Status");
        header.createCell(3).setCellValue("Priority");
        header.createCell(4).setCellValue("Quantity");
        header.createCell(5).setCellValue("Last Updated");
    }

    // Fill Row
    private void fillRow(Row row, Shipment s) {
        row.createCell(0).setCellValue(s.getShipmentId());
        row.createCell(1).setCellValue(s.getClientName());
        row.createCell(2).setCellValue(s.getStatus());
        row.createCell(3).setCellValue(s.getPriority());
        row.createCell(4).setCellValue(s.getQuantity());
        row.createCell(5).setCellValue(
                s.getLastUpdated() != null ? s.getLastUpdated().toString() : ""
        );
    }
}