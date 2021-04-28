package com.zelda.awsimageupload.profile;

import com.zelda.awsimageupload.bucket.BucketName;
import com.zelda.awsimageupload.fileStore.FileStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static org.apache.http.entity.ContentType.*;

@Service
public class UserProfileService {

    private final UserProfileDataAccessService userProfileDataAccessService;
    private final FileStore fileStore;

    @Autowired
    public UserProfileService(UserProfileDataAccessService userProfileDataAccessService, FileStore fileStore) {
        this.userProfileDataAccessService = userProfileDataAccessService;
        this.fileStore = fileStore;
    }

    List<UserProfile> getUserProfiles() {
        return userProfileDataAccessService.getUserProfiles();
    }

    public void uploadUserProfileImage(UUID userProfileId, MultipartFile file) {
        // check if image is not empty
        if (file.isEmpty()) {
            throw new IllegalStateException("File is EMPTY!" + file.getSize());
        }

        // if file is image
        List<String> contentTypes = (List<String>) Arrays.asList(String.valueOf(IMAGE_GIF), String.valueOf(IMAGE_PNG), String.valueOf(IMAGE_JPEG));
        if (!contentTypes.contains(file.getContentType())) {
            throw new IllegalStateException("File must be an IMAGE!" + file.getSize());
        }
        // if user exists
        UserProfile user = getUser(userProfileId);
        // grab metadata if any
        Map<String, String> metadata = new HashMap<>();
        metadata.put("Content-Type", file.getContentType());
        metadata.put("Content-Length", String.valueOf(file.getSize()));
        // store the image in s3 and update database (userProfileImageLink) with s3 image and link
        String path = String.format("%s/%s", BucketName.PROFILE_IMAGE.getBucketName(), user.getUserProfileId());
        String fileName = String.format("%s-%s", file.getOriginalFilename(), UUID.randomUUID());
        try {
            fileStore.save(path, fileName, Optional.of(metadata), file.getInputStream());
            user.setUserProfileImageLink(fileName);
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

    private UserProfile getUser(UUID userProfileId) {
        return userProfileDataAccessService
                .getUserProfiles()
                .stream()
                .filter(userProfile -> userProfile.getUserProfileId().equals(userProfileId))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException(String.format("User with id: %s not found!", userProfileId)));
    }

    public byte[] downloadUserProfileImage(UUID userProfileId) {
        UserProfile user = getUser(userProfileId);
        String path = String.format("%s/%s",
                BucketName.PROFILE_IMAGE.getBucketName(),
                user.getUserProfileId());
        return user.getUserProfileImageLink()
                .map(key -> fileStore.download(path, key))
                .orElse(new byte[0]);
    }
}
