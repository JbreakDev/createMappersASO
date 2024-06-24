package com.jesus.testapp.utils;

import com.jesus.testapp.model.Mapper;

import java.util.*;

public class CreateMapper {

    private static final String TYPE_FILTER = "STRING";

    public static void generateMappingCommands(List<Mapper> mappings) {
        StringBuilder output = new StringBuilder();

        for (Mapper entry : mappings) {
            // Generar el comando de mapeo
            final String command = generateCommand(entry);
            output.append(command).append("\n");
        }

        // Imprimir los comandos en consola
        System.out.println(output.toString());
    }

    private static String generateCommand(Mapper mapper) {
        final String[] sourceParts = mapper.getFrom().split("\\.");
        final String[] targetParts = mapper.getTo().split("\\.");

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
        if (TYPE_FILTER.equals(mapper.getType())) {
            sourceMethodCall.append(".filter(StringUtils::isNotBlank)");
        }
        sourceMethodCall.append(".orElse(null)");

        return String.format("%s(%s);", targetMethodCall.toString(), sourceMethodCall.toString());
    }

    private static String capitalize(String str) {
        if (str == null || str.isEmpty()) {
            return str;
        }
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
    
}
