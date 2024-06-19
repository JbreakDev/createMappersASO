package com.jesus.testapp;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException {
        final Sheet sheet = readFile();
        final List<String> keys = new ArrayList<>();
        final List<String> values = new ArrayList<>();

        for (Row row : sheet) {
            for (Cell cell : row) {
                if (cell.getColumnIndex() == 0) {
                    keys.add(cell.getStringCellValue());
                } else {
                    values.add(cell.getStringCellValue());
                }
            }
        }

        final Map<String, String> mappers = getMappers(keys, values);
        printMenu(mappers);
    }

    public static void printMenu(Map<String, String> mappers) {
        boolean exit = true;
        Scanner scanner = new Scanner (System.in);
        int option = 0;

        while (exit) {
            System.out.println("Elija la opcion deseada");
            System.out.println("1. Imprimir mappers");
            System.out.println("2. Imprimir pruebas unitarias");
            System.out.println("3. Salir");
            option = scanner.nextInt();
            switch (option) {
                case 1:
                    System.out.println("***************** Mappers *******************");
                    CreateMapper.generateMappingCommands(mappers);
                    break;
                case 2:
                    System.out.println("******************** Junit Test ********************");
                    CreateMapperTests.generateMappingTestCommands(mappers);
                    break;
                case 3:
                    exit = false;
                    break;
            }
        }
    }

    public static Map<String, String> getMappers(List<String> keys, List<String> values) {
        final Map<String, String> mappers = new LinkedHashMap<>();
        for (int i = 0; i < keys.size(); i++) {
            mappers.put(keys.get(i), values.get(i));
        }
        return mappers;
    }

    public static Sheet readFile() throws IOException {
        final InputStream inp = new FileInputStream("Mapping.xlsx");
        final Workbook wb = WorkbookFactory.create(inp);
        return wb.getSheetAt(0);
    }
}
