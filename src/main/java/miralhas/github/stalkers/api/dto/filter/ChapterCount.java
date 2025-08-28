package miralhas.github.stalkers.api.dto.filter;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChapterCount {

	public enum Operator {
		GTE, LTE
	}

	private Operator operator;
	private Long count;
}
