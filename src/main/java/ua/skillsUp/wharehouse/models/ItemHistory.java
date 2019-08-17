package ua.skillsUp.wharehouse.models;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Data
public class ItemHistory {

    private long id;
    @JsonFormat(pattern = "dd/MM/yyyy HH:mm")
    @DateTimeFormat(pattern = "dd/MM/yyyy HH:mm")
    private LocalDateTime date;
    private int count;
    private String status;
}


