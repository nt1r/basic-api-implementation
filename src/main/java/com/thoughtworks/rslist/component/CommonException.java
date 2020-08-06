package com.thoughtworks.rslist.component;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class CommonException {
    private String error;

    public CommonException(String error) {
        this.error = error;
    }
}
