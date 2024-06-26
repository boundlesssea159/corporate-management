package com.application.corporatemanagement.domain.projectmng;

// 工时粒度
public enum EffortGranularity {
    PROJECT, // only assigned to Project
    SUBPROJECT, // only assigned to SubProject
    ALL // both Project and SubProject
}
