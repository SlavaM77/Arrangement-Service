package com.iprody.lms.arrangement.service.repository.query.sorting;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SortingTest {

    @Test
    void shouldCreateSorting_withAscDirection_successfully() {
        Sorting sorting = new Sorting(SortingType.MENTOR_TYPE, "asc");

        assertThat(sorting.getSortingClause()).isNotBlank();
        assertThat(sorting.getSortDirection()).isEqualTo("ASC");
    }

    @Test
    void shouldCreateSorting_withDscDirection_successfully() {
        Sorting sorting = new Sorting(SortingType.START_DAY_SORTING, "desc");

        assertThat(sorting.getSortingClause()).isNotBlank();
        assertThat(sorting.getSortDirection()).isEqualTo("DESC");
    }

    @Test
    void shouldCreateSorting_withInvalidDirection_successfully() {
        Sorting sorting = new Sorting(SortingType.MENTOR_TYPE, "invalid");

        assertThat(sorting.getSortingClause()).isNotBlank();
        assertThat(sorting.getSortDirection()).isEqualTo("ASC");
    }
}
