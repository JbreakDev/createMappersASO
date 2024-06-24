package com.jesus.testapp;

import com.jesus.testapp.model.Mapper;
import com.jesus.testapp.utils.CreateMapper;
import com.jesus.testapp.utils.CreateMapperTests;
import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class Main {

    private static final String MESSAGE_DEFAULT = "Columna fuera de rango";

    public static void main(String[] args) throws IOException {
        final Sheet sheet = readFile();
        final List<Mapper> mapperList = getMapperList(sheet);
        printMenu(mapperList);
    }

    private static List<Mapper> getMapperList(Sheet sheet) {
        final List<Mapper> mappers = new ArrayList<>();

        for (Row row : sheet) {
            int columnIndex = 0;
            final Mapper mapper = new Mapper();
            for (Cell cell : row) {
                columnIndex = cell.getColumnIndex();
                switch (columnIndex) {
                    case 0:
                        mapper.setFrom(cell.getStringCellValue());
                        break;
                    case 1:
                        mapper.setTo(cell.getStringCellValue());
                        break;
                    case 2:
                        mapper.setType(cell.getStringCellValue());
                        break;
                    default:
                        System.out.println(MESSAGE_DEFAULT);
                }
            }
            mappers.add(mapper);
        }

        return mappers;
    }

    public static void printMenu(List<Mapper> mappers) {
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

    public static Sheet readFile() throws IOException {
        final InputStream inp = new FileInputStream("Mapping.xlsx");
        final Workbook wb = WorkbookFactory.create(inp);
        return wb.getSheetAt(0);
    }
}
