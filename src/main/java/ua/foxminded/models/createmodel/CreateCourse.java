package ua.foxminded.models.createmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateCourse {
    private String name;
    private String description;
}
