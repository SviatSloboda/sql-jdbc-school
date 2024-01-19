package ua.foxminded.models.createmodel;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateStudent {
    private int groupId;
    private String firstName;
    private String secondName;
}
