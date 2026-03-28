package com.example.workflowautomation.service;


import com.example.workflowautomation.entity.Shipment;
import com.example.workflowautomation.repository.ShipmentRepository;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;

import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.FileInputStream;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.Random;





@Service
public class ExcelService {

    private final ShipmentRepository shipmentRepository;

    public ExcelService(ShipmentRepository shipmentRepository) {
        this.shipmentRepository = shipmentRepository;
    }

    public void loadExcel(String filePath) {

        try(FileInputStream fis = new FileInputStream(filePath);
            Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0);

            boolean isHeader = true;

            Random random = new Random();

            int randomHours = random.nextInt(48);  // last 2 days
            for(Row row : sheet) {
                if(isHeader) {
                    isHeader = false;
                    continue;
                }

                Shipment shipment = Shipment.builder()
                        .shipmentId(row.getCell(0).getStringCellValue())
                        .shipName(row.getCell(1).getStringCellValue())
                        .origin(row.getCell(2).getStringCellValue())
                        .destination(row.getCell(3).getStringCellValue())
                        .productType(row.getCell(4).getStringCellValue())
                        .quantity((int) row.getCell(5).getNumericCellValue())
                        .weight((int) row.getCell(6).getNumericCellValue())
                        .status(row.getCell(7).getStringCellValue())
                        .departureDate(row.getCell(8).getLocalDateTimeCellValue().toLocalDate())
                        .arrivalDate(row.getCell(9).getLocalDateTimeCellValue().toLocalDate())
                        .clientName(row.getCell(10).getStringCellValue())
                        .priority(row.getCell(11).getStringCellValue())
                        .lastUpdated(LocalDateTime.now().minusHours(randomHours))
                        .build();

//                shipmentRepository.save(shipment);

                Optional<Shipment> existing = shipmentRepository.findByShipmentId(shipment.getShipmentId());

                if(existing.isPresent()) {

                    Shipment existingShipment = existing.get();

                    existingShipment.setShipName(shipment.getShipName());
                    existingShipment.setOrigin(shipment.getOrigin());

                    existingShipment.setDestination(shipment.getDestination());
                    existingShipment.setProductType(shipment.getProductType());

                    existingShipment.setQuantity(shipment.getQuantity());
                    existingShipment.setWeight(shipment.getWeight());
                    existingShipment.setStatus(shipment.getStatus());

                    existingShipment.setDepartureDate(shipment.getDepartureDate());
                    existingShipment.setArrivalDate(shipment.getArrivalDate());

                    existingShipment.setClientName(shipment.getClientName());
                    existingShipment.setPriority(shipment.getPriority());
                    existingShipment.setLastUpdated(LocalDateTime.now().minusHours(randomHours));

                    shipmentRepository.save(existingShipment);

                } else {
                    shipmentRepository.save(shipment);
                }
            }


            System.out.println("Excel data loaded successfully !");

        } catch(Exception e) {
            e.printStackTrace();
        }
    }

}
