package in.reqres.models;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
public class CreateResponseModel {
    String name;
    String job;
    @JsonIgnoreProperties(ignoreUnknown = true)
    Integer id;
    @JsonIgnoreProperties(ignoreUnknown = true)
    String createdAt;
}