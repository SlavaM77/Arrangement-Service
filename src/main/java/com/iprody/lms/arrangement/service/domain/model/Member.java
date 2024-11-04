package com.iprody.lms.arrangement.service.domain.model;

import com.iprody.lms.arrangement.service.domain.enums.MemberRole;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@Data
@EqualsAndHashCode(of = "guid")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Member {

    @NonNull
    private String guid;

    @NonNull
    private String firstName;

    @NonNull
    private String lastName;

    private String avatar;

    @NonNull
    private MemberRole role;

    @Builder.Default
    private boolean enabled = true;
}
