package in.reqres.models;

import lombok.Data;

@Data
public class CreateResponseModel {
    String name;
    String job;
    Integer id;
    String createdAt;
}