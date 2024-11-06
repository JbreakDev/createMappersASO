package com.jesus.testapp;

import com.jesus.testapp.model.Register;
import com.jesus.testapp.util.RegisterUtils;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.util.*;

public class Main {


    public static void main(String[] args) throws IOException {
        File archivo = new File ("register.txt");
        FileReader fr = new FileReader(archivo);
        BufferedReader br = new BufferedReader(fr);
        String log = br.readLine();
        final List<Register> registers = RegisterUtils.getRegisters(log);
        final String typeRegister = registers.get(0).getValue();

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet(typeRegister.substring(0,1));

            // Crear encabezado
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("ID");
            headerRow.createCell(1).setCellValue("CAMPO");
            headerRow.createCell(2).setCellValue("VALOR");

            int rowNum = 1;
            for (Register register : registers) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(register.getId());
                row.createCell(1).setCellValue(register.getField());
                row.createCell(2).setCellValue(register.getValue());
            }

            // Escribir en archivo
            try (FileOutputStream fileOut = new FileOutputStream("Transaction.xlsx")) {
                workbook.write(fileOut);
            }
        }
    }
}
