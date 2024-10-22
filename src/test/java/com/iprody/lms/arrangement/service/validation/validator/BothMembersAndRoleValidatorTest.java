package com.iprody.lms.arrangement.service.validation.validator;

import com.iprody.lms.arrangement.service.dto.request.GroupRequestMeta;
import jakarta.validation.ConstraintValidatorContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class BothMembersAndRoleValidatorTest {

    private BothMembersAndRoleValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private GroupRequestMeta groupRequestMeta;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new BothMembersAndRoleValidator();
    }

    @Test
    void shouldReturnTrue_withAllNull_successfully() {
        when(groupRequestMeta.memberIds()).thenReturn(null);
        when(groupRequestMeta.role()).thenReturn(null);

        boolean isValid = validator.isValid(groupRequestMeta, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnTrue_withEmptyMembersAndNullRole_successfully() {
        when(groupRequestMeta.memberIds()).thenReturn(List.of());
        when(groupRequestMeta.role()).thenReturn(null);

        boolean isValid = validator.isValid(groupRequestMeta, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnTrue_withBothMembersAndRole_successfully() {
        when(groupRequestMeta.memberIds()).thenReturn(List.of("id"));
        when(groupRequestMeta.role()).thenReturn("teacher");

        boolean isValid = validator.isValid(groupRequestMeta, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalse_withOnlyMembers_successfully() {
        when(groupRequestMeta.memberIds()).thenReturn(List.of("id"));
        when(groupRequestMeta.role()).thenReturn(null);

        boolean isValid = validator.isValid(groupRequestMeta, context);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldReturnFalse_withOnlyRole_successfully() {
        when(groupRequestMeta.memberIds()).thenReturn(null);
        when(groupRequestMeta.role()).thenReturn("intern");

        boolean isValid = validator.isValid(groupRequestMeta, context);

        assertThat(isValid).isFalse();
    }
}
