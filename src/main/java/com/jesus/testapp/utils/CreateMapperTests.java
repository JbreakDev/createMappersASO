package com.jesus.testapp.utils;

import com.jesus.testapp.model.Mapper;

import java.util.List;
import java.util.Map;

public class CreateMapperTests {

    public static void generateMappingTestCommands(List<Mapper> mappings) {
        StringBuilder output = new StringBuilder();

        for (Mapper entry : mappings) {
            String originalField = entry.getFrom();
            String mappedField = entry.getTo();

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

        StringBuilder sourceMethodCall = new StringBuilder("source");
        StringBuilder targetMethodCall = new StringBuilder("target");

        // Construir la cadena de métodos usando map() con referencias de métodos para la fuente
        for (int i = 0; i < sourceParts.length; i++) {
            sourceMethodCall.append(".get").append(capitalize(sourceParts[i])).append("()");
        }

        // Construir la cadena de métodos para el setter de destino
        for (int i = 0; i < targetParts.length; i++) {
            targetMethodCall.append(".get").append(capitalize(targetParts[i])).append("()");
        }

        return String.format("Assert.assertEquals(%s, %s);", sourceMethodCall.toString(), targetMethodCall.toString());
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
