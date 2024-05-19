package com.application.corporatemanagement.adapter.driven.persistence.orgmng;

import com.application.corporatemanagement.adapter.driven.persistence.exceptions.QueryException;
import com.application.corporatemanagement.adapter.driven.persistence.exceptions.ReflectException;
import com.application.corporatemanagement.domain.orgmng.emp.*;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
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
    public void save(Emp emp, long userId) {
        Number createdId = empInsert.executeAndReturnKey(Map.of(
                "tenant_id", emp.getTenant()
                , "name", emp.getName()
                , "org_id", emp.getOrgId()
                , "status", emp.getStatus().getValue()
                , "post_codes", emp.getPostCodes().stream().map(Object::toString).collect(Collectors.joining(","))
                , "created_at", LocalDateTime.now()
                , "created_by", userId
                , "last_updated_at", LocalDateTime.now()
                , "last_updated_by", userId
        ));
        setId(emp, createdId);
        insertSkills(emp, userId);
        insertWorkExperiences(emp, userId);
    }

    private void insertSkills(Emp emp, Long userId) {
        emp.getSkills().forEach(skill -> {
            Number id = skillsInsert.executeAndReturnKey(Map.of(
                    "tenant_id", emp.getTenant()
                    , "emp_id", emp.getId()
                    , "skill_type", skill.getSkillType()
                    , "skill_level", skill.getSkillLevel().getValue()
                    , "duration", skill.getDuration()
                    , "created_at", LocalDateTime.now()
                    , "created_by", userId
                    , "last_updated_at", LocalDateTime.now()
                    , "last_updated_by", userId
            ));
            setId(skill, id);
        });
    }

    private void insertWorkExperiences(Emp emp, long userId) {
        emp.getWorkExperiences().forEach(workExperience -> {
            Number id = workExperiencesInsert.executeAndReturnKey(Map.of(
                    "tenant_id", emp.getTenant()
                    , "emp_id", emp.getId()
                    , "start_date", workExperience.getStartDate().toString()
                    , "end_date", workExperience.getEndDate().toString()
                    , "company", workExperience.getCompany()
                    , "created_at", LocalDateTime.now()
                    , "created_by", userId
                    , "last_updated_at", LocalDateTime.now()
                    , "last_updated_by", userId
            ));
            setId(workExperience, id);
        });
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

    // todo the code of building emp can be reused
    public Optional<Emp> findById(Long id) {
        try {
            Emp emp = jdbcTemplate.queryForObject("select * from emp where id=?", (rs, rowNum) ->
                            Emp.builder()
                                    .tenant(rs.getLong("tenant_id"))
                                    .name(rs.getString("name"))
                                    .orgId(rs.getLong("org"))
                                    .status(EmpStatus.getBy(rs.getString("status")).get())
                                    .build()
                    , id);
            return Optional.ofNullable(emp);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new QueryException(e);
        }
    }

    public Optional<List<Emp>> findAll() {
        try {
            List<Emp> emps = new ArrayList<>();
            jdbcTemplate.query("select * from emp", (rs) -> {
                EmpForRebuilding emp = EmpForRebuilding.builder()
                        .id(rs.getLong("id"))
                        .tenant(rs.getLong("tenant_id"))
                        .name(rs.getString("name"))
                        .orgId(rs.getLong("org_id"))
                        .status(EmpStatus.getBy(rs.getString("status")).get())
                        .postCodes(Arrays.stream(rs.getString("post_codes").split(",")).map(Long::parseLong).toList())
                        .build();

                jdbcTemplate.query("select * from skills where emp_id = ?", (skillRow) -> {
                    Skill skill = Skill.builder()
                            .id(skillRow.getLong("id"))
                            .tenant(skillRow.getLong("tenant_id"))
                            .skillType(skillRow.getLong("skill_type"))
                            .skillLevel(SkillLevel.valueOf(skillRow.getLong("skill_level")).get())
                            .duration(skillRow.getLong("duration"))
                            .build();
                    emp.addSkill(skill);
                }, emp.getId());

                jdbcTemplate.query("select * from work_experiences where emp_id = ?", (workExperienceRow) -> {
                    WorkExperience workExperience = WorkExperience.builder()
                            .id(workExperienceRow.getLong("id"))
                            .tenant(workExperienceRow.getLong("tenant_id"))
                            .startDate(LocalDate.parse(workExperienceRow.getString("start_date")))
                            .endDate(LocalDate.parse(workExperienceRow.getString("end_date")))
                            .company(workExperienceRow.getString("company"))
                            .build();
                    emp.addWorkExperience(workExperience);
                }, emp.getId());
                emps.add(emp);
            });
            return Optional.of(emps);
        } catch (DataAccessException e) {
            return Optional.empty();
        } catch (Exception e) {
            throw new QueryException(e);
        }
    }
}
