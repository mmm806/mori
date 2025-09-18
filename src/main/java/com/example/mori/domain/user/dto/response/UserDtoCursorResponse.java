package com.example.mori.domain.user.dto.response;

import java.util.List;
import java.util.UUID;

public record UserDtoCursorResponse(
	List<UserResponseWithStats> date,
	String nextCursor,
	UUID nextIdAfter,
	boolean hasNext,
	long totalCount,
	String sortBy,
	String sortDirection

) {
}
