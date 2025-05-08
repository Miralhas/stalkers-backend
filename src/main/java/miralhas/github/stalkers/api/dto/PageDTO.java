package miralhas.github.stalkers.api.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.io.Serial;
import java.io.Serializable;
import java.util.List;

@Getter
@Setter
public class PageDTO<T> implements Serializable {

	@Serial
	private static final long serialVersionUID = 1L;

	private List<T> results;
	private long totalItems;
	private Integer next;
	private Integer previous;
	private int totalPages;
	private int currentPage;

	public PageDTO(Page<T> page) {
		this.setTotalPages(page.getTotalPages());
		this.setResults(page.getContent());
		this.setCurrentPage(page.getNumber());
		this.setTotalItems(page.getTotalElements());
		this.setNext(page.hasNext() ? page.getNumber() + 1 : null);
		this.setPrevious(page.hasPrevious() ? page.getNumber() - 1 : null);
	}
}
