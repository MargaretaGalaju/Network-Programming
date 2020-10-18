package Deserializer;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CSVDeserializer {
    public static List<Object> convertCsv(String stringObj, Class cls) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        ArrayList<Object> objects = new ArrayList<>();
        String[] objectData = stringObj.split("\n");
        String[] fields = objectData[0].split(",");
        for(int i = 1; i < objectData.length ; i++){
            Object object = cls.newInstance();

            String[] fieldValues = objectData[i].split(",");

            for(int j = 0; j < fields.length ; j++){
                Field reflectionField = cls.getField(fields[j]);
                String valueForField = fieldValues[j];

                Class type = reflectionField.getType();
                switch (type.getName()){
                    case "int":
                        reflectionField.setInt(object,Integer.parseInt(valueForField));
                        break;
                    case "double":
                        reflectionField.setDouble(object,Double.parseDouble(valueForField));
                        break;
                    case "java.lang.String":
                        reflectionField.set(object,valueForField);
                        break;
                }

            }
            objects.add(object);
        }

        return objects;
    }

    public static List<Field> getAllFields(List<Field> fields, Class<?> type) {
        fields.addAll(Arrays.asList(type.getDeclaredFields()));

        if (type.getSuperclass() != null) {
            getAllFields(fields, type.getSuperclass());
        }

        return fields;
    }
}
