package com.example.notificationservice.common.base.response;

import com.example.notificationservice.common.base.model.BaseModel;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(callSuper = false)
@Builder
public class ResponseByMessage extends BaseModel {
    private String message;
}
