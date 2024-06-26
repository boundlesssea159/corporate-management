package com.application.corporatemanagement.application.effortmng;

import java.util.ArrayList;
import java.util.List;

public class AvailableEffortItems {
    List<EffortItemDTO> innerProjects = new ArrayList<>();
    List<EffortItemDTO> clientProjects = new ArrayList<>();
    List<EffortItemDTO> subProjects = new ArrayList<>();
    List<EffortItemDTO> commonEffortItems = new ArrayList<>();

    void addEffortItem(EffortService.EffortItemType type, Long id, String name) {
        switch (type) {
            case INNER_PROJECT -> innerProjects.add(EffortItemDTO.builder().id(id).name(name).build());
            case CLIENT_PROJECT -> clientProjects.add(EffortItemDTO.builder().id(id).name(name).build());
            case SUB_PROJECT -> subProjects.add(EffortItemDTO.builder().id(id).name(name).build());
            case COMMON_EFFORT_ITEM -> commonEffortItems.add(EffortItemDTO.builder().id(id).name(name).build());
        }
    }
}
