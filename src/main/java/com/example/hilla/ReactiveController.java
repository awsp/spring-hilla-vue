package com.example.hilla;

import static org.springframework.http.HttpHeaders.ACCEPT_RANGES;
import static org.springframework.http.HttpHeaders.CONTENT_LENGTH;
import static org.springframework.http.HttpHeaders.CONTENT_RANGE;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;

import com.example.hilla.ReactiveController.AttachmentType;
import java.io.FileNotFoundException;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

@RestController
@RequestMapping("/reactive")
@RequiredArgsConstructor
@Log4j2
public class ReactiveController {

  private final AttachmentService attachmentService;

  @GetMapping("/test")
  public ResponseEntity<StreamingResponseBody> stream(@RequestHeader(value = "Range", required = false) String range) {
    return asStreamResponse(attachmentService.getStreamContent(AttachmentType.VIDEO, range));
  }

  private ResponseEntity<StreamingResponseBody> asStreamResponse(StreamContentDto streamContent) {
    if (streamContent.partial()) {
      return asPartialStreamResponse(streamContent);
    } else {
      return asFullStreamResponse(streamContent);
    }
  }

  private ResponseEntity<StreamingResponseBody> asPartialStreamResponse(StreamContentDto streamingContent) {
    return ResponseEntity
      .status(HttpStatus.PARTIAL_CONTENT)
      .header(CONTENT_TYPE, streamingContent.mediaType())
      .header(CONTENT_LENGTH, Long.toString(streamingContent.contentLength()))
      .header(ACCEPT_RANGES, "bytes")
      .header(CONTENT_RANGE, streamingContent.contentRange())
      .body(streamingContent.streamingResponseBody());
  }

  private ResponseEntity<StreamingResponseBody> asFullStreamResponse(StreamContentDto streamingContent) {
    return ResponseEntity
      .status(HttpStatus.OK)
      .header(CONTENT_TYPE, streamingContent.mediaType())
      .header(CONTENT_LENGTH, Long.toString(streamingContent.contentLength()))
      .body(streamingContent.streamingResponseBody());
  }

  public enum AttachmentType {
    VIDEO,
    IMAGE,
    FILE
  }
}

interface AttachmentService {

  StreamContentDto getStreamContent(AttachmentType attachmentType, String range);
}

interface ObjectStorage {

  StreamContentDto streamContent(AttachmentType attachmentType, String range);
}

@Service
@RequiredArgsConstructor
class AttachmentServiceImpl implements AttachmentService {

  private final ObjectStorage objectStorage;

  @Override
  public StreamContentDto getStreamContent(AttachmentType attachmentType, String range) {
    return objectStorage.streamContent(attachmentType, range);
  }

}

@Service
class ObjectStorageImpl implements ObjectStorage {

  private static final String url = "/Volumes/ClariS/【新海誠】君の名は。/[LoliHouse] Kimi no Na wa [BDRip 1920x1080 HEVC-yuv420p10 FLAC PGS(chs,eng,jpn)].mkv";
  private static final String MEDIA_TYPE_VIDEO_MP4 = "video/mp4";
  private static final String MEDIA_TYPE_IMAGE_PNG = "image/png";
  private static final String MEDIA_TYPE_HTML = "text/html";

  @Override
  public StreamContentDto streamContent(AttachmentType attachmentType, String range) {
    try {
      Path filePath = Paths.get(url);
      long fileSize = Files.size(filePath);
      Pair<Long, Long> ranges = getRange(range, fileSize);

      boolean partial = range != null;
      return new StreamContentDto(partial, getMediaType(attachmentType), getContentLength(ranges),
        getContentRange(ranges, fileSize), getStreamingResponseBody(filePath, ranges));
    } catch (FileNotFoundException e) {
      throw new RuntimeException("File not found", e);
    } catch (Exception e) {
      throw new RuntimeException("Error occurred while streaming content", e);
    }
  }

  private static StreamingResponseBody getStreamingResponseBody(Path filePath, Pair<Long, Long> ranges) {
    byte[] buffer = new byte[640_000];
    return os -> {
      try (RandomAccessFile file = new RandomAccessFile(filePath.toFile(), "r")) {
        long pos = ranges.getFirst();
        file.seek(pos);

        while (pos < ranges.getSecond()) {
          file.read(buffer);
          os.write(buffer);
          pos += buffer.length;
        }

        os.flush();
      } catch (Exception ignored) {
        // do nothing
      }
    };
  }

  private static String getContentRange(Pair<Long, Long> ranges, long fileSize) {
    return "bytes "
      + ranges.getFirst()
      + "-"
      + ranges.getSecond()
      + "/"
      + fileSize;
  }

  private static long getContentLength(Pair<Long, Long> ranges) {
    return (ranges.getSecond() - ranges.getFirst()) + 1;
  }

  private String getMediaType(AttachmentType attachmentType) {
    return switch (attachmentType) {
      case VIDEO -> MEDIA_TYPE_VIDEO_MP4;
      case IMAGE -> MEDIA_TYPE_IMAGE_PNG;
      case FILE -> MEDIA_TYPE_HTML;
    };
  }

  private Pair<Long, Long> getRange(String range, long fileSize) {
    if (range == null) {
      return Pair.of(0L, fileSize - 1);
    }
    String[] ranges = range.split("-");
    Long rangeStart = Long.parseLong(ranges[0].substring(6));
    long rangeEnd = ranges.length > 1 ? Long.parseLong(ranges[1]) : fileSize - 1;

    if (fileSize < rangeEnd) {
      rangeEnd = fileSize - 1;
    }
    return Pair.of(rangeStart, rangeEnd);
  }
}

record StreamContentDto(
  boolean partial,
  String mediaType,
  long contentLength,
  String contentRange,
  StreamingResponseBody streamingResponseBody
) {

}
