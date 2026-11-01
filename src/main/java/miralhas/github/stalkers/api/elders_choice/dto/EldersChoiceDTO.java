package miralhas.github.stalkers.api.elders_choice.dto;

import miralhas.github.stalkers.api.novel.dto.NovelSummaryDTO;

import java.io.Serial;
import java.io.Serializable;

public record EldersChoiceDTO(Long id, NovelSummaryDTO novel) implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;
}

