package by.akimova.aws.image;

import org.springframework.http.*;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class ImageUtil {

    public static final int IMAGE_WIDTH = 1024;
    public static final long ONE_YEAR_SEC = 31536000L;
    public static final String DEFAULT_CONTENT_TYPE = "text/plain";


    public static byte[] getImageBytesArr(final MultipartFile file) throws IOException {
        final BufferedImage image = ImageIO.read(file.getInputStream());
        if (image.getWidth() <= IMAGE_WIDTH) {
            return file.getBytes();
        }
        final Image resized = image.getScaledInstance(IMAGE_WIDTH, -1, Image.SCALE_SMOOTH);

        final BufferedImage bufferedImage = new BufferedImage(resized.getWidth(null),
                resized.getHeight(null),
                BufferedImage.TYPE_INT_RGB);
        bufferedImage.getGraphics().drawImage(resized, 0, 0, null);

        final ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "jpg", byteStream);
        byteStream.flush();
        final byte[] imageInByte = byteStream.toByteArray();
        byteStream.close();

        return imageInByte;
    }

    public static String getContentType(final MultipartFile file) throws IOException {
        String contentType = file.getContentType();
        var originalFileNameEncode = URLEncoder.encode(Objects.requireNonNull(file.getOriginalFilename()), StandardCharsets.UTF_8);
        if (!StringUtils.hasText(contentType) && originalFileNameEncode != null) {
            contentType = Files.probeContentType(new File(originalFileNameEncode).toPath());
            if (!StringUtils.hasText(contentType)) {
                int index = originalFileNameEncode.lastIndexOf('.');
                if (index > 0) {
                    contentType = DEFAULT_CONTENT_TYPE;
                }
            }
        }
        return contentType;
    }


    public static ResponseEntity<byte[]> getImageResponseEntity(final ImageEntity image) {
        final MediaType mediaType = image.getContentType() != null
                ? MediaType.valueOf(image.getContentType())
                : MediaType.IMAGE_JPEG;

        CacheControl cacheControl = CacheControl.maxAge(ONE_YEAR_SEC, TimeUnit.SECONDS)
                .noTransform()
                .mustRevalidate();

        final HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.setCacheControl(cacheControl);
        return new ResponseEntity<>(image.getImage(), headers, HttpStatus.OK);
    }

    public static ResponseEntity<List<ResponseEntity<byte[]>>> getAllImagesResponseEntity(final List<ImageEntity> images) {

       List<ResponseEntity<byte[]>> allImages = new ArrayList<>();
        images.forEach(im ->{
            final MediaType mediaType = im.getContentType() != null
                    ? MediaType.valueOf(im.getContentType())
                    : MediaType.IMAGE_JPEG;

            CacheControl cacheControl = CacheControl.maxAge(ONE_YEAR_SEC, TimeUnit.SECONDS)
                    .noTransform()
                    .mustRevalidate();

            final HttpHeaders headers = new HttpHeaders();
            headers.setContentType(mediaType);
            headers.setCacheControl(cacheControl);
            var image = new ResponseEntity<>(im.getImage(), headers, HttpStatus.OK);
            allImages.add(image);

        });
        return new  ResponseEntity<>(allImages, new HttpHeaders(), HttpStatus.OK );
    }
}
