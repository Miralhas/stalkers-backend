package miralhas.github.stalkers.infrastructure.service;

import lombok.RequiredArgsConstructor;
import miralhas.github.stalkers.domain.service.interfaces.ImageStorageService;
import miralhas.github.stalkers.domain.utils.ErrorMessages;
import miralhas.github.stalkers.infrastructure.exception.StorageException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
@RequiredArgsConstructor
public class ImageLocalStorageService implements ImageStorageService {

	@Value("${stalkers.storage.local.dir.images}")
	private Path storagePath;
	private final ErrorMessages errorMessages;

	@Override
	public InputStream retrieve(String fileName) {
		try {
			return Files.newInputStream(getFilePath(fileName));
		} catch (IOException e) {
			throw new StorageException(errorMessages.get("imageStorage.retrieve", fileName), e);
		}
	}

	@Override
	public void save(NewImage newImage) {
		Path filePath = getFilePath(newImage.getFileName());
		try {
			FileCopyUtils.copy(newImage.getInputStream(), Files.newOutputStream(filePath));
		} catch (IOException e) {
			throw new StorageException(errorMessages.get("imageStorage.store", newImage.getFileName()), e);
		}
	}

	@Override
	public void remove(String fileName) {
		Path filePath = getFilePath(fileName);
		try {
			Files.deleteIfExists(filePath);
		} catch (IOException e) {
			throw new StorageException(errorMessages.get("imageStorage.delete", fileName), e);
		}
	}

	private Path getFilePath(String fileName) {
		return storagePath.resolve(fileName);
	}
}
