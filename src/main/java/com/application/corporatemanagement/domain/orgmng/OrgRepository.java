package com.application.corporatemanagement.domain.orgmng;

import java.util.Optional;

public interface OrgRepository  {
    Optional<Org> save(Org org, Long userId);
}
