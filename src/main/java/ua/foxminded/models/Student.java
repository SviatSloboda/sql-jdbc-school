package ua.foxminded.models;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Student {
    private int id;
    private int groupId;
    private String firstName;
    private String secondName;
}
