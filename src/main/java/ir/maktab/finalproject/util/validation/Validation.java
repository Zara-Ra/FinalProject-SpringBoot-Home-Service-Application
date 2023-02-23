package ir.maktab.finalproject.util.validation;

import ir.maktab.finalproject.util.exception.PhotoValidationException;
import ir.maktab.finalproject.util.exception.ValidationException;
import lombok.experimental.UtilityClass;
import net.sf.jmimemagic.*;

@UtilityClass
public class Validation {
    private static final int ONE_KILOBYTE = 1024;
    private static final int PHOTO_SIZE = 300;

    private final TriConsumer validate = (s, r, m) -> {
        if (s == null || s.equals("") || !s.matches(r))
            throw new ValidationException(m);
    };

    public void validateName(String name) {
        validate.accept(name, "^[a-zA-Z ]{2,}", "Invalid Name Only Alphabetic Characters Accepted");
    }

    public void validateEmail(String email) {
        validate.accept(email, "^[a-zA-Z0-9+_.-]+@[a-zA-Z0-9.-]+$", "Invalid Email");
    }

    public void validatePassword(String password) {
        validate.accept(password, "^([0-9a-zA-Z]){8,}$", "Invalid Password should be 8 characters including " +
                "alphanumeric values");
    }

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
            throw new PhotoValidationException("Photo Size Should Be Less Than 300 KiloBytes");
        try {
            match = Magic.getMagicMatch(photo);
        } catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
            throw new PhotoValidationException("Invalid Photo");
        }
        String mimeType = match.getMimeType();
        if (!mimeType.equals("image/jpeg"))
            throw new PhotoValidationException("Invalid Photo Type Only 'jpeg' Accepted");
    }
}
