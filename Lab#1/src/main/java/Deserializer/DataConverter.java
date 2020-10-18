package Deserializer;
import Models.Person;
import Models.ResultResponse;
import Models.XML.Dataset;
import Models.YAML.YAMLModel;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLMapper;
import java.util.ArrayList;
import java.util.List;

public class DataConverter {
    public static void convertToSpecificFormat(String data, String type) throws JsonProcessingException, IllegalAccessException, NoSuchFieldException, InstantiationException {
        switch (type) {
            case "application/xml": {
                XmlMapper xmlMapper = new XmlMapper();
                ResultResponse.personArrayList.addAll(xmlMapper.readValue(data, Dataset.class).getRecord());
                break;
            }
            case "text/csv": {
                ResultResponse.personArrayList.addAll((List<Person>) (Object) new ArrayList<>(CSVDeserializer.convertCsv(data, Person.class)));
                break;
            }
            case "application/x-yaml": {
                YAMLMapper yamlMapper = new YAMLMapper();
                ResultResponse.personArrayList.addAll(yamlMapper.readValue(data, new TypeReference<ArrayList<YAMLModel>>() { }));
                break;
            }
            case "application/json": {
                ObjectMapper jsonMapper = new ObjectMapper();
                if(data.substring(data.length()-3).contains(",")) {
                    data = data.substring(0, data.lastIndexOf(","))+"]";
                }
                ResultResponse.personArrayList.addAll(jsonMapper.readValue(data, new TypeReference<ArrayList<Person>>() { }));
                break;
            }
        }
    }
}
