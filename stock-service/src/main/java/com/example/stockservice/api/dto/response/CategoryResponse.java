package com.example.stockservice.api.dto.response;

import com.example.stockservice.common.base.model.BaseModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class CategoryResponse extends BaseModel {

    private String key;
    private String value;

}
