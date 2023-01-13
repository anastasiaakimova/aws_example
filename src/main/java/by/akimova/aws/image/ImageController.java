package by.akimova.aws.image;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

import static by.akimova.aws.image.ImageUtil.getAllImagesResponseEntity;
import static by.akimova.aws.image.ImageUtil.getImageResponseEntity;

@RestController
@RequiredArgsConstructor
@RequestMapping("/images")
public class ImageController {

    private final ImageService imageService;


    @PostMapping("image/upload")
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile imgFile) throws IOException {

        var imageEntity = getImageDto(imgFile);


         imageService.saveImage(imageEntity);

        return new ResponseEntity<>(imageEntity, HttpStatus.OK);
    }

    @GetMapping(value = "/image/{id}")
    public ResponseEntity<byte[]> getImage(@PathVariable Long id) {

        var image = imageService.getImageById(id);
        return getImageResponseEntity(image);

    }

    @GetMapping()
    public ResponseEntity<List<ResponseEntity<byte[]>>> getAllImages() {

        var images = imageService.getAllImages();
        return getAllImagesResponseEntity(images);
    }


    private ImageEntity getImageDto(final MultipartFile file) throws IOException {
        final ImageEntity imageEntity = new ImageEntity();
        imageEntity.setImage(ImageUtil.getImageBytesArr(file));
        imageEntity.setName(file.getOriginalFilename());
        var contentType = ImageUtil.getContentType(file);
        imageEntity.setContentType(contentType);
        imageEntity.setSize(file.getSize());
        return imageEntity;
    }

    @DeleteMapping(value = "image/{id}")
    public ResponseEntity<Void> deleteImage(@PathVariable Long id) {
        imageService.deleteImage(id);
        return ResponseEntity.ok().build();
    }
}
