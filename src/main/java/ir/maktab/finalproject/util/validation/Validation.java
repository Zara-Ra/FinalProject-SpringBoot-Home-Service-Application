package ir.maktab.finalproject.util.validation;

import ir.maktab.finalproject.configuration.MessageSourceConfiguration;
import ir.maktab.finalproject.util.exception.PhotoValidationException;
import ir.maktab.finalproject.util.exception.ValidationException;
import lombok.experimental.UtilityClass;
import net.sf.jmimemagic.*;

@UtilityClass
public class Validation {

    private MessageSourceConfiguration messageSource;
    private static final int ONE_KILOBYTE = 1024;
    private static final int PHOTO_SIZE = 300;

    private final TriConsumer validate = (s, r, m) -> {
        if (s == null || s.equals("") || !s.matches(r))
            throw new ValidationException(m);
    };
    // Phase 2 Photo Validation with the path of a file
    /*public void validatePhoto(String photoPath) throws IOException {
        File file = new File(photoPath);
        int length = (int) file.length() / ONE_KILOBYTE;
        if (length > PHOTO_SIZE)
            throw new PhotoValidationException("Photo Size Should Be Less Than 300 KiloBytes");
        InputStream is = null;
        try {
            is = new BufferedInputStream(new FileInputStream(file));
        } catch (FileNotFoundException e) {
            throw new PhotoValidationException("Photo Not Found");
        }
        String mimeType;
        mimeType = URLConnection.guessContentTypeFromStream(is);
        if (!mimeType.equals("image/jpeg"))
            throw new PhotoValidationException("Invalid Photo Type Only 'jpeg' Accepted");
    }*/

    public void validatePhoto(byte[] photo) {
        MagicMatch match = null;
        int length = photo.length / ONE_KILOBYTE;
        if (length > PHOTO_SIZE)
            throw new PhotoValidationException(messageSource.getMessage("errors.message.invalid_photo_size"));
        try {
            match = Magic.getMagicMatch(photo);
        } catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
            throw new PhotoValidationException(messageSource.getMessage("errors.message.invalid_photo"));
        }
        String mimeType = match.getMimeType();
        if (!mimeType.equals("image/jpeg"))
            throw new PhotoValidationException(messageSource.getMessage("errors.message.invalid_photo_type"));
    }
}
