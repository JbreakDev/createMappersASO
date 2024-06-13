package com.jesus.testapp;

import org.apache.poi.ss.usermodel.*;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class CreateMapper {
    public static void main(String[] args) throws IOException {
        final Sheet sheet = readFile();
        final List<String> keys = new ArrayList<>();
        final List<String> values = new ArrayList<>();
        final List<String> mappings = new ArrayList<>();


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
        generateMappingCommands(mappers);
    }

    private static void generateMappingCommands(Map<String, String> mappings) {
        StringBuilder output = new StringBuilder();

        for (Map.Entry<String, String> entry : mappings.entrySet()) {
            String originalField = entry.getKey();
            String mappedField = entry.getValue();

            // Generar el comando de mapeo
            String command = generateCommand(originalField, mappedField);
            output.append(command).append("\n");
        }

        // Imprimir los comandos en consola
        System.out.println(output.toString());
    }

    private static String generateCommand(String originalField, String mappedField) {
        String[] sourceParts = originalField.split("\\.");
        String[] targetParts = mappedField.split("\\.");
        String sourceObject = "";

        StringBuilder sourceMethodCall = new StringBuilder("Optional.ofNullable(cardUpdatePost)");
        StringBuilder targetMethodCall = new StringBuilder("request");

        // Construir la cadena de métodos usando map() con referencias de métodos para la fuente
        for (int i = 0; i < sourceParts.length; i++) {
            if (i == 0) {
                sourceMethodCall.deleteCharAt(sourceMethodCall.length() - 1);
                sourceMethodCall.append(".get").append(capitalize(sourceParts[i])).append(")");
            } else {
                sourceMethodCall.append(".map(").append(capitalize(sourceParts[i - 1])).append("::get").append(capitalize(sourceParts[i])).append(")");
            }
        }

        // Construir la cadena de métodos para el setter de destino
        for (int i = 0; i < targetParts.length - 1; i++) {
            targetMethodCall.append(".get").append(capitalize(targetParts[i])).append("()");
        }

        // El último nivel de destino debe ser un setter
        targetMethodCall.append(".set").append(capitalize(targetParts[targetParts.length - 1]));

        // Agregar .orElse(null) al final de la cadena de la fuente
        sourceMethodCall.append(".filter(StringUtils::isNotBlank).orElse(null)");

        return String.format("%s(%s);", targetMethodCall.toString(), sourceMethodCall.toString());
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
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
