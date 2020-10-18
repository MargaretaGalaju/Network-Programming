package Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

import java.util.HashMap;

public class HomeResponse {
    @JsonProperty("msg")
    @JacksonXmlProperty(localName = "msg")
    private String msg;
    @JsonProperty("link")
    @JacksonXmlProperty(localName = "link")
    private HashMap<String,String> link;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public HashMap<String, String> getLink() {
        return link;
    }

    public void setLink(HashMap<String, String> link) {
        this.link = link;
    }
}
