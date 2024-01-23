package ua.foxminded.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class Course {
    private int id;
    private String name;
    private String description;
}
