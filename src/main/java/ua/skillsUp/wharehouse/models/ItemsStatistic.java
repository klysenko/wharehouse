package ua.skillsUp.wharehouse.models;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.math.BigDecimal;

@AllArgsConstructor
@Data
public class ItemsStatistic {
    private Integer storedAmount;
    private Integer withdrawnAmount;
    private BigDecimal storedTotalCost;
    private BigDecimal withdrawnTotalCost;
}
