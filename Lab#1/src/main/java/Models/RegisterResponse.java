package Models;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty;

public class RegisterResponse {
    @JsonProperty("access_token")
    @JacksonXmlProperty(localName = "access_token")
    private String access_token;
    @JsonProperty("link")
    @JacksonXmlProperty(localName = "link")
    private String link;

    public String getLink() {
        return link;
    }
    public void setLink(String link){
        this.link = link;
    }

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }
}
