package com.church.model;

import com.church.util.FrequencyTypeEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReOccurance {

    private FrequencyTypeEnum reOccuranceFrequency;

    private int numberOfOccurance;

    private Date endDate;

}
