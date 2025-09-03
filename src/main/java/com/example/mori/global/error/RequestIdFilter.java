package com.example.mori.global.error;


import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.UUID;

/** 요청 단위 추적용 Request-Id 부여 및 응답 헤더 반영 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class RequestIdFilter implements Filter {
	public static final String HEADER = "X-Request-Id";
	public static final String MDC_KEY = "requestId";

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
		throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		String rid = request.getHeader(HEADER);
		if (rid == null || rid.isBlank()) rid = UUID.randomUUID().toString();

		MDC.put(MDC_KEY, rid);
		response.setHeader(HEADER, rid);
		try {
			chain.doFilter(req, res);
		} finally {
			MDC.remove(MDC_KEY);
		}
	}
}
