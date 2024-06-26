package com.application.corporatemanagement.application.effortmng;

import lombok.Builder;

import java.util.Objects;

@Builder
public class EffortItemDTO {
    private Long id;
    private String name;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EffortItemDTO that = (EffortItemDTO) o;
        return Objects.equals(id, that.id) && Objects.equals(name, that.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
