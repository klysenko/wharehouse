package ua.skillsUp.wharehouse.models;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
//@Builder
public class Owner {
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String companyName;
    public List<OwnerContact> contactsList;
    public List<Item> itemList;
}
