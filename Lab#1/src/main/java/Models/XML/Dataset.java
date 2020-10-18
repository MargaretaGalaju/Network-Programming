package Models.XML;

import Models.Person;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.List;

public class Dataset {
    @JsonProperty("record")
    @JacksonXmlProperty(localName = "record")
    @JacksonXmlElementWrapper(useWrapping = false)
    private List<Person> record;

    public List<Person> getRecord() {
        return record;
    }

    public void setRecord(List<Person> record) {
        this.record = record;
    }
}
