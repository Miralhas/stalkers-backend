package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.dto.input.RatingInput;
import miralhas.github.stalkers.domain.model.metrics.Rating;
import miralhas.github.stalkers.domain.model.novel.Novel;
import miralhas.github.stalkers.domain.repository.NovelRepository;
import miralhas.github.stalkers.domain.repository.RatingRepository;
import miralhas.github.stalkers.domain.utils.ValidateAuthorization;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetricsService {

	private final ValidateAuthorization validateAuthorization;
	private final NovelRepository novelRepository;
	private final RatingRepository ratingRepository;

	@Transactional
	public void createRating(RatingInput ratingInput, Novel novel) {
		var user = validateAuthorization.getCurrentUser();

		var ratingOptional = ratingRepository.getUserRatingOnNovel(user.getId(), novel.getId());

		// user has already rated this novel. Only change rating value
		if (ratingOptional.isPresent()) {
			var userRating = ratingOptional.get();
			userRating.setRatingValue(ratingInput.ratingValue());
			userRating.setNovel(novel);
			ratingRepository.save(userRating);
			return;
		}

		var rating = Rating.builder()
				.ratingValue(ratingInput.ratingValue())
				.user(user)
				.novel(novel)
				.build();

		novel.addRating(rating);
		novelRepository.save(novel);
	}

	@Transactional
	public void updateNovelViewCount(Novel novel) {
		novel.viewsPlusOne();
		novelRepository.save(novel);
	}
}
