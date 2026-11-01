package miralhas.github.stalkers.domain.metrics.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.api.metrics.dto.UserRatingDTO;
import miralhas.github.stalkers.api.metrics.dto.input.RatingInput;
import miralhas.github.stalkers.domain.metrics.exception.RatingNotFoundException;
import miralhas.github.stalkers.domain.metrics.model.Rating;
import miralhas.github.stalkers.domain.novel.model.Novel;
import miralhas.github.stalkers.domain.novel.repository.NovelRepository;
import miralhas.github.stalkers.domain.metrics.repository.RatingRepository;
import miralhas.github.stalkers.shared.utils.ErrorMessages;
import miralhas.github.stalkers.shared.utils.ValidateAuthorization;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MetricsService {

	private final ValidateAuthorization validateAuthorization;
	private final NovelRepository novelRepository;
	private final RatingRepository ratingRepository;
	private final ErrorMessages errorMessages;

	public UserRatingDTO getUserRatingOnNovel(Long userId, Long novelId) {
		return ratingRepository.getUserRatingOnNovel(userId, novelId)
				.map(r -> new UserRatingDTO(r.getNovel().getId(), r.getUser().getId(), r.getRatingValue()))
				.orElseThrow(() -> new RatingNotFoundException(
						errorMessages.get("rating.notFound", userId, novelId)));
	}

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
		novel.viewsPlusRandom();
		novelRepository.save(novel);
	}
}
