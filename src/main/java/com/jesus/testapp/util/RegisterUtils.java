package com.jesus.testapp.util;

import com.jesus.testapp.model.Register;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RegisterUtils {

    public static List<Register> getRegisters(String log) throws IOException {
        final List<Register> registers = new ArrayList<>();
        final List<String> values = getValues(log);

        System.out.println(values.size());

        String typeRegister = values.get(0);

        switch (typeRegister) {
            case "OP": {
                int i = 0;
                for (String field : Constants.opFields){
                    final Register register = new Register();
                    register.setField(field);
                    register.setValue(values.get(i));
                    register.setId("OP" + ++i);

                    registers.add(register);
                }
                break;
            }
            case "PB": {
                int i = 0;
                for (String field : Constants.pbFields){
                    final Register register = new Register();
                    register.setField(field);
                    register.setValue(values.get(i));
                    register.setId("PB" + ++i);

                    registers.add(register);
                }
                break;
            }
            case "NM": {
                int i = 0;
                for (String field : Constants.nmFields){
                    final Register register = new Register();
                    register.setField(field);
                    register.setValue(values.get(i));
                    register.setId("NM" + ++i);

                    registers.add(register);
                }
                break;
            }
        }

        return registers;
    }

    public static List<String> getValues(String register) throws IOException {
        if (register == null) {
            return null;
        }
        final String[] fields = register.split("\\|");
        final List<String> values = new ArrayList<>(Arrays.asList(fields));

        return values;
    }
}
