package com.application.corporatemanagement.domain.orgmng.emp;

import com.application.corporatemanagement.common.framework.AuditableEntity;
import com.application.corporatemanagement.common.framework.ChangingStatus;
import com.application.corporatemanagement.domain.common.exceptions.BusinessException;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Getter
@SuperBuilder
public class Emp extends AuditableEntity {

    protected Long id;

    protected String name;

    protected Long tenant;

    protected Long orgId; // associate external object by id

    protected List<Long> postCodes; // any emp can work on several posts

    protected List<Skill> skills; // associate inner object with real object

    protected List<WorkExperience> workExperiences;

    protected EmpStatus status;

    public void becomeRegular() {
        status = EmpStatus.REGULAR;
    }

    public void terminate() {
        status = EmpStatus.TERMINATED;
    }

    public void addSkill(Long skillType, Long skillLevel, Long duration, Long userId) {
        Skill skill = createSkill(skillType, skillLevel, duration, userId);
        if (this.skills == null) {
            this.skills = new ArrayList<>();
        }
        skillShouldNotBeDuplicated(skill);
        this.skills.add(skill);
    }

    protected Skill createSkill(Long skillType, Long skillLevel, Long duration, Long userId) {
        Optional<Skill> skill = SkillLevel.valueOf(skillLevel).map(skl -> {
            Skill newSkill = new Skill(tenant, skillType, LocalDateTime.now(), userId);
            newSkill.setDuration(duration);
            newSkill.setLevel(skl);
            return newSkill;
        });
        if (skill.isEmpty()) {
            throw new BusinessException("技能类型不存在");
        }
        return skill.get();
    }

    private void skillShouldNotBeDuplicated(Skill skill) {
        if (this.skills.stream().anyMatch(skl -> skl.getSkillType().equals(skill.getSkillType()))) {
            throw new BusinessException("同一个技能不能录入两次");
        }
    }

    public void addWorkExperience(LocalDate startDate, LocalDate endDate, String company, Long userId) {
        if (this.workExperiences == null) {
            this.workExperiences = new ArrayList<>();
        }
        WorkExperience workExperience = new WorkExperience(tenant, startDate, endDate, company, LocalDateTime.now(), userId);
        workExperienceTimeShouldNotOverlap(workExperience);
        this.workExperiences.add(workExperience);
    }

    private void workExperienceTimeShouldNotOverlap(WorkExperience workExperience) {
        this.workExperiences.forEach(we -> {
            if (we.getStartDate().isBefore(workExperience.getEndDate()) ||
                    workExperience.getStartDate().isBefore(we.getEndDate())) {
                throw new BusinessException("工作经验时间不能重叠");
            }
        });
    }

    public void addPostCode(Long postCode) {
        this.postCodes.add(postCode);
    }

    public void updateSkill(Long skillType, Long skillLevel, Long duration) {
        Skill skill = findSkill(skillType);
        if (!needBeChanged(skill, skillLevel, duration)) return;
        toUnChange();
        skill.setLevel(mapSkillLevel(skillLevel));
        skill.setDuration(duration);
        skill.toUpdate();
    }

    private Skill findSkill(Long skillType) {
        return this.skills.stream()
                .filter(skl -> skl.getSkillType().equals(skillType))
                .findFirst()
                .orElseThrow(() -> new BusinessException("员工不存在" + skillType.toString() + "技能"));
    }

    private boolean needBeChanged(Skill skill, Long skillLevel, Long duration) {
        return !skill.getSkillLevel().getValue().equals(skillLevel) ||
                !skill.getDuration().equals(duration);
    }

    private SkillLevel mapSkillLevel(Long skillLevel) {
        return SkillLevel.valueOf(skillLevel).orElseThrow(() -> new BusinessException("技能等级" + skillLevel.toString() + "未被定义过"));
    }
}
