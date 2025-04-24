package miralhas.github.stalkers.domain.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.exception.ImageNotFoundException;
import miralhas.github.stalkers.domain.model.Image;
import miralhas.github.stalkers.domain.repository.ImageRepository;
import miralhas.github.stalkers.domain.service.interfaces.ImageStorageService;
import miralhas.github.stalkers.domain.service.interfaces.ImageStorageService.NewImage;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ImageService {

	private final ImageStorageService imageStorageService;
	private final ImageRepository imageRepository;
	private final ErrorMessages errorMessages;

	public Image getImageJsonOrException(Long id) {
		return imageRepository.findById(id).orElseThrow(() -> new ImageNotFoundException(
				errorMessages.get("image.id.notFound", id)
		));
	}

	public Image getImageJsonByEntityOrException(Image imageEntity) {
		var message = errorMessages.get("image.notFound");
		try {
			return imageRepository.findById(imageEntity.getId()).orElseThrow(() -> new ImageNotFoundException(message));
		} catch (NullPointerException e) {
			throw new ImageNotFoundException(message);
		}
	}

	public ResponseEntity<InputStreamResource> getImage(Image imageEntity) {
		Image image = getImageJsonByEntityOrException(imageEntity);
		MediaType imageMediaType = MediaType.parseMediaType(image.getContentType());
		var imageStream = imageStorageService.retrieve(image.getFileName());
		return ResponseEntity.ok().contentType(imageMediaType).body(new InputStreamResource(imageStream));
	}

	@Transactional
	public Image save(Image image, InputStream fileInputStream) {
		String newImageName = imageStorageService.generateFileName(image.getFileName());
		image.setFileName(newImageName);
		image = imageRepository.save(image);
		imageRepository.flush();
		var newImage = NewImage.builder()
				.fileName(image.getFileName())
				.inputStream(fileInputStream)
				.build();
		imageStorageService.save(newImage);
		return image;
	}

	@Transactional
	public Image replace(Image image, String existingImageFileName, InputStream fileInputStream) {
		String newImageName = imageStorageService.generateFileName(image.getFileName());
		image.setFileName(newImageName);
		var existingImage = imageRepository.findImageByFileName(existingImageFileName);
		if (existingImage.isPresent()) {
			existingImageFileName = existingImage.get().getFileName();
			imageRepository.delete(existingImage.get());
			imageRepository.flush();
		}
		image = imageRepository.save(image);
		imageRepository.flush();
		var newImage = NewImage.builder()
				.fileName(image.getFileName())
				.inputStream(fileInputStream)
				.build();
		imageStorageService.replace(newImage, existingImageFileName);
		return image;
	}

	@Transactional
	public void delete(Image image) {
		image = getImageJsonByEntityOrException(image);
		imageRepository.delete(image);
		imageRepository.flush();
		imageStorageService.remove(image.getFileName());
	}
}
