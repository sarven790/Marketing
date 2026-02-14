package com.example.stockservice.api.dto.response;

import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class GetCategoryResponseList {

    private List<CategoryResponse> categoryResponses;

}
