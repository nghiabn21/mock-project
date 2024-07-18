package com.example.mockproject.utils.common;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class CustomPageUtils {

    public static final String ASC = "asc";
    public static final String DESC = "DESC";

    /**
     * This method use to get custom pageable
     * @param pageable used to get pageNumber and pageSize passed from front-end
     * @param sortBy used to identify what attribute be sorted by
     * @param order used to identify order to be sorted
     * @return Pageable
     */
    public static Pageable getPageable(Pageable pageable, String sortBy, String order) {
        if (order.equalsIgnoreCase(DESC)) {
            return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortBy).descending());
        }
        return PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(sortBy));
    }
}
