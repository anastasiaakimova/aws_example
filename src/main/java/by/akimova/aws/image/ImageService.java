package by.akimova.aws.image;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.Instant;
import java.util.List;


@Service
public class ImageService {
    private final ImageRepository imageRepository;

    public ImageService(ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }


    public void saveImage(ImageEntity file) throws IOException {

        var today = Instant.now();
        file.setCreatedAt(today);


 /*
        ImageEntity image = ImageEntity.builder()
                .name(file.getOriginalFilename())
                .contentType(file.getContentType())
                .image()
                .size(file.getSize())
                .build();

        imageRepository.save(image);*/


        imageRepository.save(file);

    }

    public ImageEntity getImageById(Long imageId) {
        return imageRepository.findById(imageId).orElse(null);
    }


    public void deleteImage(Long imageId) {
        imageRepository.deleteById(imageId);
    }


    public List<ImageEntity> getAllImages() {
        return imageRepository.findAll();
    }
}
