package com.application.corporatemanagement.adapter.driving.restful.orgmng;

import com.application.corporatemanagement.application.orgmng.OrgDto;
import com.application.corporatemanagement.application.orgmng.OrgService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/org")
public class OrgController {

    private final OrgService orgService;

    @Autowired
    public OrgController(OrgService orgService) {
        this.orgService = orgService;
    }

    @PostMapping
    public ResponseEntity<Boolean> add(@RequestBody OrgDto orgDto) {
        orgService.add(orgDto, 1L);
        return ResponseEntity.ok(true);
    }
}
