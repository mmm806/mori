package com.example.mori.domain.user.repository;

import java.util.ArrayList;
import java.util.List;

import org.flywaydb.core.internal.util.StringUtils;

import com.example.mori.domain.user.dto.request.UserSearchRequest;
import com.example.mori.domain.user.entity.User;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Order;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

public class UserRepositoryImpl implements UserRepositoryCustom {

	@PersistenceContext
	private EntityManager em;

	@Override
	public List<User> search(UserSearchRequest req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<User> cq = cb.createQuery(User.class);
		Root<User> u = cq.from(User.class);

		List<Predicate> predicates = buildPredicates(cb, u, req);

		boolean asc = "ASC".equalsIgnoreCase(req.sortDirection());
		String primarySort = normalizeSortBy(req.sortBy());
		List<Order> orders = new ArrayList<>();

		if (!"id".equals(primarySort)) {
			orders.add(asc ? cb.asc(u.get(primarySort)) : cb.desc(u.get(primarySort)));
		}
		orders.add(asc ? cb.asc(u.get("id")) : cb.desc(u.get("id")));

		cq.select(u).where(predicates.toArray(Predicate[]::new)).orderBy(orders);

		int limit = Math.min(req.limit() == null ? 20 : Math.max(1, req.limit()), 100);
		return em.createQuery(cq).setMaxResults(limit).getResultList();
	}

	@Override
	public long count(UserSearchRequest req) {
		CriteriaBuilder cb = em.getCriteriaBuilder();
		CriteriaQuery<Long> cq = cb.createQuery(Long.class);
		Root<User> u = cq.from(User.class);

		List<Predicate> predicates = buildPredicates(cb, u, req);
		cq.select(cb.count(u)).where(predicates.toArray(Predicate[]::new));

		return em.createQuery(cq).getSingleResult();
	}

	private List<Predicate> buildPredicates(CriteriaBuilder cb, Root<User> u, UserSearchRequest req) {
		List<Predicate> ps = new ArrayList<>();

		if (StringUtils.hasText(req.emailLike())) {
			String like = "%" + req.emailLike().toLowerCase() + "%";
			ps.add(cb.like(cb.lower(u.get("email")), like));
		}
		if (req.roleEqual() != null) {
			ps.add(cb.equal(u.get("role"), req.roleEqual()));
		}
		// 커서(idAfter) 처리: 정렬 방향에 따라 부등호 변경
		if (req.idAfter() != null) {
			boolean asc = "ASC".equalsIgnoreCase(req.sortDirection());
			ps.add(asc ? cb.greaterThan(u.get("id"), req.idAfter())
				: cb.lessThan(u.get("id"), req.idAfter()));
		}
		return ps;
	}

	private String normalizeSortBy(String sortBy) {
		if (sortBy == null) return "createdAt";
		return switch (sortBy) {
			case "createdAt", "updatedAt", "email", "nickname", "id" -> sortBy;
			default -> "createdAt";
		};
	}
}
