package com.jesus.testapp;

import java.util.Map;

public class CreateMapperTests {

    public static void generateMappingTestCommands(Map<String, String> mappings) {
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
