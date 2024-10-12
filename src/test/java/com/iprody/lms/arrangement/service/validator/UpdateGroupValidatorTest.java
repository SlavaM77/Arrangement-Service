package com.iprody.lms.arrangement.service.validator;

import com.iprody.lms.arrangement.service.dto.request.MeetRequestDto;
import com.iprody.lms.arrangement.service.dto.request.UpdateGroupRequestDto;
import com.iprody.lms.arrangement.service.validation.validator.UpdateGroupValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

class UpdateGroupValidatorTest {

    private UpdateGroupValidator validator;

    @Mock
    private ConstraintValidatorContext context;

    @Mock
    private UpdateGroupRequestDto requestDto;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        validator = new UpdateGroupValidator();
    }

    @Test
    void shouldReturnTrue_withOnlyNameParam_successfully() {
        when(requestDto.name()).thenReturn("name");

        boolean isValid = validator.isValid(requestDto, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnTrue_withOnlyScheduledForParam_successfully() {
        when(requestDto.scheduledFor()).thenReturn(Instant.now());

        boolean isValid = validator.isValid(requestDto, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnTrue_withOnlyMeetsParam_successfully() {
        List<MeetRequestDto> meets = Instancio.createList(MeetRequestDto.class);

        when(requestDto.meets()).thenReturn(meets);

        boolean isValid = validator.isValid(requestDto, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnTrue_withOnlyMemberGuidsParam_successfully() {
        when(requestDto.memberGuids()).thenReturn(List.of("guid"));

        boolean isValid = validator.isValid(requestDto, context);

        assertThat(isValid).isTrue();
    }

    @Test
    void shouldReturnFalse_withAllNullParams_successfully() {
        boolean isValid = validator.isValid(requestDto, context);

        assertThat(isValid).isFalse();
    }

    @Test
    void shouldReturnFalse_withAllNullAndEmptyListsParams_successfully() {
        when(requestDto.meets()).thenReturn(List.of());
        when(requestDto.memberGuids()).thenReturn(List.of());

        boolean isValid = validator.isValid(requestDto, context);

        assertThat(isValid).isFalse();
    }
}
