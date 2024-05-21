package com.application.corporatemanagement.adapter.driven.persistence.orgmng;

import com.application.corporatemanagement.adapter.driven.persistence.exceptions.QueryException;
import com.application.corporatemanagement.adapter.driven.persistence.exceptions.ReflectException;
import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import com.application.corporatemanagement.domain.common.valueobjs.Duration;
import com.application.corporatemanagement.domain.orgmng.emp.EmpRepository;
import com.application.corporatemanagement.common.framework.ChangingStatus;
import com.application.corporatemanagement.domain.orgmng.emp.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Repository
public class EmpJdbc implements EmpRepository {

    private final JdbcTemplate jdbcTemplate;

    private final SimpleJdbcInsert empInsert;

    private final SimpleJdbcInsert skillsInsert;

    private final SimpleJdbcInsert workExperiencesInsert;

    public EmpJdbc(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.empInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("emp")
                .usingGeneratedKeyColumns("id");

        this.skillsInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("skills")
                .usingGeneratedKeyColumns("id");

        this.workExperiencesInsert = new SimpleJdbcInsert(jdbcTemplate)
                .withTableName("work_experiences")
                .usingGeneratedKeyColumns("id");


    }

    @Override
    public boolean existsByIdAndStatus(Long tenant, Long id, EmpStatus... status) {
        return false;
    }

    @Override
    public Optional<List<Emp>> findOrgEmps(Long tenant, Long orgId) {
        return Optional.empty();
    }

    @Override
    public void create(Emp emp) {
        Number createdId = empInsert.executeAndReturnKey(Map.of(
                "tenant_id", emp.getTenant()
                , "name", emp.getName()
                , "org_id", emp.getOrgId()
                , "status", emp.getStatus().getValue()
                , "post_codes", convertPostCodesFromListToString(emp)
                , "created_at", LocalDateTime.now()
                , "created_by", emp.getCreatedBy()
                , "last_updated_at", LocalDateTime.now()
                , "last_updated_by", emp.getLastUpdatedBy()
                , "version", emp.getVersion()
        ));
        setId(emp, createdId);
        insertSkills(emp.getSkills(), emp.getTenant(), emp.getId());
        insertWorkExperiences(emp.getWorkExperiences(), emp.getTenant(), emp.getId());
    }

    private void insertSkills(List<Skill> skills, Long tenant, Long empId) {
        skills.forEach(skill -> insertSkill(tenant, empId, skill));
    }

    private void insertSkill(Long tenant, Long empId, Skill skill) {
        Number id = skillsInsert.executeAndReturnKey(Map.of(
                "tenant_id", tenant
                , "emp_id", empId
                , "skill_type", skill.getSkillType()
                , "skill_level", skill.getSkillLevel().getValue()
                , "duration", skill.getDuration()
                , "created_at", LocalDateTime.now()
                , "created_by", skill.getCreatedBy()
                , "last_updated_at", LocalDateTime.now()
                , "last_updated_by", skill.getLastUpdatedBy()
        ));
        setId(skill, id);
    }

    private void insertWorkExperiences(List<WorkExperience> workExperiences, Long tenant, Long empId) {
        workExperiences.forEach(workExperience -> insertWorkExperience(tenant, empId, workExperience));
    }

    private void insertWorkExperience(Long tenant, Long empId, WorkExperience workExperience) {
        Number id = workExperiencesInsert.executeAndReturnKey(Map.of(
                "tenant_id", tenant
                , "emp_id", empId
                , "start_date", workExperience.getDuration().startDate().toString()
                , "end_date", workExperience.getDuration().endDate().toString()
                , "company", workExperience.getCompany()
                , "created_at", LocalDateTime.now()
                , "created_by", workExperience.getCreatedBy()
                , "last_updated_at", LocalDateTime.now()
                , "last_updated_by", workExperience.getLastUpdatedBy()
        ));
        setId(workExperience, id);
    }

    private void setId(Object obj, Number createdId) {
        try {
            Field id = obj.getClass().getDeclaredField("id");
            id.setAccessible(true);
            id.set(obj, createdId.longValue());
        } catch (Exception e) {
            throw new ReflectException(e);
        }
    }

    public Optional<List<Emp>> findAll() {
        try {
            List<Emp> emps = new ArrayList<>();
            jdbcTemplate.query("select * from emp", (rs) -> {emps.add(buildEmpAggregator(rs));});
            return Optional.of(emps);
        } catch (DataAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new QueryException(e);
        }
    }

    private List<Long> convertPostCodesFromStringToList(ResultSet rs) throws SQLException {
        return Arrays.stream(rs.getString("post_codes").split(",")).map(Long::parseLong).toList();
    }

    @Override
    public Optional<Emp> findById(Long tenant, Long empId) {
        try {
            return Optional.ofNullable(jdbcTemplate.queryForObject("select * from emp where tenant_id = ? and id=?", (rs, rowNum) -> buildEmpAggregator(rs), tenant, empId));
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new QueryException(e);
        }
    }

    private EmpForRebuilding buildEmpAggregator(ResultSet rs) throws SQLException {
        EmpForRebuilding empForRebuilding = EmpForRebuilding.builder()
                .id(rs.getLong("id"))
                .tenant(rs.getLong("tenant_id"))
                .name(rs.getString("name"))
                .orgId(rs.getLong("org_id"))
                .status(EmpStatus.getBy(rs.getString("status")).get())
                .postCodes(convertPostCodesFromStringToList(rs))
                .version(rs.getLong("version"))
                .build();

        jdbcTemplate.query("select * from skills where emp_id = ?", (skillRow) -> {
            Skill skill = Skill.builder()
                    .id(skillRow.getLong("id"))
                    .tenant(skillRow.getLong("tenant_id"))
                    .skillType(skillRow.getLong("skill_type"))
                    .skillLevel(SkillLevel.valueOf(skillRow.getLong("skill_level")).get())
                    .duration(skillRow.getLong("duration"))
                    .build();
            empForRebuilding.addSkill(skill);
        }, empForRebuilding.getId());

        jdbcTemplate.query("select * from work_experiences where emp_id = ?", (workExperienceRow) -> {
            WorkExperience workExperience = WorkExperience.builder()
                    .id(workExperienceRow.getLong("id"))
                    .tenant(workExperienceRow.getLong("tenant_id"))
                    .duration(new Duration(LocalDate.parse(workExperienceRow.getString("start_date")), LocalDate.parse(workExperienceRow.getString("end_date"))))
                    .company(workExperienceRow.getString("company"))
                    .build();
            empForRebuilding.addWorkExperience(workExperience);
        }, empForRebuilding.getId());
        return empForRebuilding;
    }

    @Override
    public void update(Emp emp) {
        updateEmp(emp);
        updateSkills(emp.getSkills(), emp.getId());
        updateWorkExperiences(emp.getWorkExperiences(), emp.getId());
    }

    private void updateEmp(Emp emp) {
        if (Objects.requireNonNull(emp.getChangingStatus()) == ChangingStatus.UPDATED) {
            int update = this.jdbcTemplate.update("update emp "
                            + " set org_id = ?"
                            + ", version = version+1"
                            + ", status = ?"
                            + ", post_codes =? "
                            + ", name = ?"
                            + ", last_updated_at =?"
                            + ", last_updated_by =? "
                            + " where tenant_id = ? and id = ? and version = ?",
                    emp.getOrgId()
                    , emp.getStatus().getValue()
                    , convertPostCodesFromListToString(emp)
                    , emp.getName()
                    , emp.getLastUpdatedAt()
                    , emp.getLastUpdatedBy()
                    , emp.getTenant()
                    , emp.getId()
                    , emp.getVersion()
            );
            if (update == 0) {
                throw new BusinessException("不能同时刻修改员工信息");
            }
        }
    }

    private String convertPostCodesFromListToString(Emp emp) {
        return emp.getPostCodes().stream().map(Object::toString).collect(Collectors.joining(","));
    }

    private void updateSkills(List<Skill> skills, Long empId) {
        skills.forEach(skill -> {
            switch (skill.getChangingStatus()) {
                case UPDATED -> this.jdbcTemplate.update("update skills "
                                + "set skill_level = ?"
                                + ",duration = ?"
                                + ",last_updated_by = ?"
                                + " where tenant_id = ? and emp_id = ? and skill_type = ?"
                        , skill.getSkillLevel().getValue()
                        , skill.getDuration()
                        , skill.getLastUpdatedBy()
                        , skill.getTenant()
                        , empId
                        , skill.getSkillType());
                case NEW -> this.insertSkill(skill.getTenant(), empId, skill);
                case DELETED ->
                        this.jdbcTemplate.execute(String.format("delete from skills where tenant_id = '%s' and emp_id = '%s' and skill_type='%s'", skill.getTenant(), empId, skill.getSkillType().toString()));
            }
        });
    }

    private void updateWorkExperiences(List<WorkExperience> workExperiences, Long empId) {
        workExperiences.forEach(workExperience -> {
            switch (workExperience.getChangingStatus()) {
                case UPDATED -> this.jdbcTemplate.update("update work_experiences "
                                + "set start_date=?"
                                + ",end_date=?"
                                + " where tenant_id=? and emp_id=? and company=?"
                        , workExperience.getStartDate()
                        , workExperience.getEndDate()
                        , workExperience.getTenant()
                        , empId
                        , workExperience.getCompany());
                case NEW -> insertWorkExperience(workExperience.getTenant(), empId, workExperience);
                case DELETED ->
                        this.jdbcTemplate.execute(String.format("delete from work_experiences where tenant_id = '%s' and emp_id = '%s' and company= '%s'", workExperience.getTenant(), empId, workExperience.getCompany()));
            }
        });
    }
}
