package com.application.corporatemanagement.domain.orgmng.emp;

import com.application.corporatemanagement.domain.common.exceptions.BusinessException;

import java.util.Arrays;
import java.util.Optional;

public enum EmpStatus {
    PROBATION("1", "试用期员工"),
    REGULAR("2", "正式员工"),
    TERMINATED("3", "终止");

    private final String value;

    private final String text;

    EmpStatus(String value, String text) {
        this.value = value;
        this.text = text;
    }

    public String getValue() {
        return value;
    }


    public static Optional<EmpStatus> getBy(String value) {
        return Arrays.stream(EmpStatus.values()).filter(empStatus -> empStatus.value.equals(value)).findFirst();
    }

    public EmpStatus becomeRegular() {
        if (!this.equals(EmpStatus.PROBATION)) {
            throw new BusinessException("只有试用期的员工才能转正");
        }
        return REGULAR;
    }

    public EmpStatus terminate() {
        if (this.equals(EmpStatus.TERMINATED)) {
            throw new BusinessException("已终止的员工不能再次终止");
        }
        return TERMINATED;
    }
}

