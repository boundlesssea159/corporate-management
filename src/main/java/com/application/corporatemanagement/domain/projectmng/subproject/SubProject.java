package com.application.corporatemanagement.domain.projectmng.subproject;

import com.application.corporatemanagement.common.framework.AuditableEntity;
import com.application.corporatemanagement.domain.effortmng.EffortItem;
import lombok.experimental.SuperBuilder;

@SuperBuilder
public class SubProject extends AuditableEntity implements EffortItem {

    @Override
    public String getName() {
        return null;
    }
}
