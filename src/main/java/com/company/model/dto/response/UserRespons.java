package com.company.model.dto.response;

import com.company.model.dao.entities.User;
import lombok.Data;

import java.util.List;

@Data
public class UserRespons {

    private Long id;
    private String name;
    private String surname;
    private String email;
    private List<TaskRespons> taskList;

    public UserRespons(User user) {
        this.name = user.getFirstName();
        this.surname = user.getLastName();
    }
    public UserRespons(User user, List<TaskRespons> tasks) {
        this.id = user.getId();
        this.name = user.getFirstName();
        this.surname = user.getLastName();
        this.email = user.getUsername();
        this.taskList = tasks;
    }
}
