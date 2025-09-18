package com.example.mori.global.jackson;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import java.io.IOException;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.util.List;

/** "2025-09-17", "2025/09/17", "2025.09.17", ISO datetime, epoch(ms) 모두 허용 */
public class LocalDateFlexDeserializer extends JsonDeserializer<LocalDate> {

	private static final List<DateTimeFormatter> F = List.of(
		DateTimeFormatter.ISO_LOCAL_DATE,                 // 2025-09-17
		DateTimeFormatter.ofPattern("yyyy/MM/dd"),
		DateTimeFormatter.ofPattern("yyyy.MM.dd")
	);

	@Override
	public LocalDate deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
		JsonNode node = p.getCodec().readTree(p);   // ★ TreeNode -> JsonNode
		if (node == null || node.isNull()) return null;

		if (node.isNumber()) {
			long epoch = node.asLong(); // epoch millis 가정
			return Instant.ofEpochMilli(epoch).atZone(ZoneId.of("Asia/Seoul")).toLocalDate();
		}

		String s = node.asText().trim();
		if (s.isEmpty()) return null;

		if (s.contains("T")) {
			try { return OffsetDateTime.parse(s).atZoneSameInstant(ZoneId.of("Asia/Seoul")).toLocalDate(); } catch (Exception ignore) {}
			try { return LocalDateTime.parse(s).toLocalDate(); } catch (Exception ignore) {}
		}
		for (var fmt : F) {
			try { return LocalDate.parse(s, fmt); } catch (Exception ignore) {}
		}
		return null;
	}
}