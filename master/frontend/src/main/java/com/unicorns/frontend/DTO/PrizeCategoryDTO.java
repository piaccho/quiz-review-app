package com.unicorns.frontend.DTO;

import com.unicorns.frontend.model.Prize;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Data
public class PrizeCategoryDTO {

    private Long id;
    private String name;
    private List<Long> availablePrizes;
}
