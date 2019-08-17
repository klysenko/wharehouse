package ua.skillsUp.wharehouse.models;

import lombok.Data;

import java.util.List;

@Data
public class Owner {
    private Long id;
    private String login;
    private String firstName;
    private String lastName;
    private String companyName;
    private List<OwnerContact> contactsList;
    private List<Item> itemList;
}
