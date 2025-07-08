package pl.kurs.peopleapp.commands.person;

import com.fasterxml.jackson.annotation.JsonTypeInfo;

@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.EXISTING_PROPERTY, property = "type", visible = true)
public abstract class PersonRequestCommand {

    private String type;

    public abstract String getType();
    public abstract String getPesel();

    public void setType(String type) {
        this.type = type;
    }
}
